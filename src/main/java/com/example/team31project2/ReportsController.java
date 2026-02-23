package com.example.team31project2;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class ReportsController {

    private Employee currentUser;

    @FXML
    private ListView<String> reportSelector;

    @FXML
    private TableView<ObservableList<String>> reportTable;

    @FXML
    private Label reportTitle;

    @FXML
    private VBox contentArea;

    @FXML
    private Button backButton;

    private final Map<String, String> reportToFileMap = new TreeMap<>();

    @FXML
    public void initialize() {
        // Setup the report map
        reportToFileMap.put("Inventory Status", "custom_reports/inventory_status.sql");
        reportToFileMap.put("Most Popular Modifiers", "custom_reports/popular_modifiers.sql");
        reportToFileMap.put("Orders by Day of Week", "custom_reports/orders_by_day.sql");
        reportToFileMap.put("Revenue by Month", "custom_reports/revenue_by_month.sql");
        reportToFileMap.put("Sales by Category", "custom_reports/sales_by_category.sql");
        reportToFileMap.put("Top 5 Products", "custom_reports/top_5_products.sql");

        reportSelector.getItems().addAll(reportToFileMap.keySet());

        reportSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadReport(newValue);
            }
        });

        if (backButton != null) {
            backButton.setOnAction(event -> handleBack());
        }
    }

    public void setUser(Employee user) {
        this.currentUser = user;
    }

    private void loadReport(String reportName) {
        if (reportName == null || !reportToFileMap.containsKey(reportName))
            return;

        reportTitle.setText(reportName);
        String relativePath = reportToFileMap.get(reportName);
        String sql = readSqlFile(relativePath);

        if (sql == null || sql.trim().isEmpty()) {
            System.err.println("Could not load SQL for report: " + reportName);
            // Optionally clear the table and show an error row
            reportTable.getColumns().clear();
            reportTable.getItems().clear();
            return;
        }

        executeAndDisplayReport(sql);
    }

    private String readSqlFile(String relativePath) {
        Path path = Paths.get("queries", relativePath);
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.err.println("Error reading SQL file at: " + path.toAbsolutePath());
            return null;
        }
    }

    private void executeAndDisplayReport(String sql) {
        reportTable.getColumns().clear();
        reportTable.getItems().clear();

        // Clear any previous chart from the top of the VBox
        if (contentArea.getChildren().size() > 2) {
            contentArea.getChildren().remove(1); // Assumes Title is 0, Table is last
        }

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Populate columns dynamically
            for (int i = 1; i <= columnCount; i++) {
                final int j = i - 1;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(metaData.getColumnName(i));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                reportTable.getColumns().add(col);
            }

            // Populate data dynamically
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    row.add(value != null ? value : "NULL");
                }
                data.add(row);
            }
            reportTable.setItems(data);

            // Post-process table data into Graphical Chart if applicable based on query
            // metadata rules
            generateChartFromData(metaData.getColumnName(1), metaData.getColumnName(2), data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateChartFromData(String xAxisName, String yAxisName,
            ObservableList<ObservableList<String>> data) {
        String currentReport = reportSelector.getSelectionModel().getSelectedItem();
        if (currentReport == null || data.isEmpty())
            return;

        // Safety boundary to not try charting a table with arbitrary length strings
        // everywhere
        if (data.get(0).size() < 2)
            return;

        javafx.scene.Node chartNode = null;

        // Configuration based on current Report types!
        if (currentReport.equals("Top 5 Products") || currentReport.equals("Orders by Day of Week")
                || currentReport.equals("Most Popular Modifiers")) {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel(xAxisName);
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yAxisName);

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(currentReport);

            for (ObservableList<String> row : data) {
                try {
                    String xVal = row.get(0);
                    Number yVal = Double.parseDouble(row.get(1));
                    series.getData().add(new XYChart.Data<>(xVal, yVal));
                } catch (NumberFormatException ignored) {
                }
            }
            barChart.getData().add(series);
            chartNode = barChart;

        } else if (currentReport.equals("Revenue by Month")) {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel(xAxisName);
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yAxisName);

            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(currentReport);

            for (ObservableList<String> row : data) {
                try {
                    String xVal = row.get(0);
                    Number yVal = Double.parseDouble(row.get(1));
                    series.getData().add(new XYChart.Data<>(xVal, yVal));
                } catch (NumberFormatException ignored) {
                }
            }
            lineChart.getData().add(series);
            chartNode = lineChart;

        } else if (currentReport.equals("Sales by Category")) {
            PieChart pieChart = new PieChart();
            for (ObservableList<String> row : data) {
                try {
                    String xVal = row.get(0);
                    Number yVal = Double.parseDouble(row.get(1));
                    pieChart.getData().add(new PieChart.Data(xVal, yVal.doubleValue()));
                } catch (NumberFormatException ignored) {
                }
            }
            pieChart.setTitle("Sugar Level Preferences");
            chartNode = pieChart;
        }

        if (chartNode != null) {
            chartNode.setStyle(
                    "-fx-background-color: white; -fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 5px;");
            contentArea.getChildren().add(1, chartNode); // Insert between title and table
        }
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ordering-view.fxml"));
            Parent root = loader.load();

            OrderingController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 700)); // Resetting back to ordering page dimensions
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
