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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    private VBox chartContainer;

    @FXML
    private Button weekRangeButton;

    @FXML
    private Button monthRangeButton;

    @FXML
    private Button yearRangeButton;

    @FXML
    private Button backButton;

    private final Map<String, String> reportToFileMap = new LinkedHashMap<>();
    private final Set<String> dateRangeReports = Set.of(
            "Most Popular Modifiers",
            "Orders by Day of Week",
            "Revenue Over Time",
            "Sales by Category",
            "Top 5 Products",
            "Orders by Hour",
            "Revenue by Employee");
    private String selectedDateRange = "1 Month";

    @FXML
    public void initialize() {
        // Setup the report map
        reportToFileMap.put("Inventory Status", "custom_reports/inventory_status.sql");
        reportToFileMap.put("Most Popular Modifiers", "custom_reports/popular_modifiers.sql");
        reportToFileMap.put("Orders by Day of Week", "custom_reports/orders_by_day.sql");
        reportToFileMap.put("Revenue Over Time", "custom_reports/revenue_by_month.sql");
        reportToFileMap.put("Sales by Category", "custom_reports/sales_by_category.sql");
        reportToFileMap.put("Top 5 Products", "custom_reports/top_5_products.sql");
        reportToFileMap.put("Orders by Hour", "custom_reports/orders_by_hour.sql");
        reportToFileMap.put("Revenue by Employee", "custom_reports/revenue_by_employee.sql");

        weekRangeButton.setOnAction(event -> handleDateRangeSelect("1 Week"));
        monthRangeButton.setOnAction(event -> handleDateRangeSelect("1 Month"));
        yearRangeButton.setOnAction(event -> handleDateRangeSelect("1 Year"));
        updateDateRangeButtonStyles();

        reportSelector.getItems().addAll(reportToFileMap.keySet());

        reportSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadReport(newValue);
            }
        });

        if (backButton != null) {
            backButton.setOnAction(event -> handleBack());
        }

        if (!reportSelector.getItems().isEmpty()) {
            reportSelector.getSelectionModel().selectFirst();
        }
    }

    public void setUser(Employee user) {
        this.currentUser = user;
    }

    private void loadReport(String reportName) {
        if (reportName == null || !reportToFileMap.containsKey(reportName))
            return;

        reportTitle.setText(reportName);
        boolean shouldEnableDateRange = dateRangeReports.contains(reportName);
        weekRangeButton.setDisable(!shouldEnableDateRange);
        monthRangeButton.setDisable(!shouldEnableDateRange);
        yearRangeButton.setDisable(!shouldEnableDateRange);

        String relativePath = reportToFileMap.get(reportName);
        String sql = resolveSql(reportName, relativePath);

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

    private String resolveSql(String reportName, String relativePath) {
        String rawSql = readSqlFile(relativePath);
        if (rawSql == null) {
            return null;
        }

        String dateFilter = "";
        if (dateRangeReports.contains(reportName)) {
            dateFilter = " AND o.created_at BETWEEN NOW() - INTERVAL '" + getSelectedInterval() + "' AND NOW() ";
        }

        String resolvedSql = rawSql.replace("{{DATE_FILTER}}", dateFilter);

        if ("Revenue Over Time".equals(reportName)) {
            resolvedSql = resolvedSql
                    .replace("{{TIME_BUCKET}}", getRevenueTimeBucket())
                    .replace("{{TIME_LABEL_FORMAT}}", getRevenueLabelFormat());
        } else {
            resolvedSql = resolvedSql
                    .replace("{{TIME_BUCKET}}", "day")
                    .replace("{{TIME_LABEL_FORMAT}}", "YYYY-MM-DD");
        }

        return resolvedSql;
    }

    private String getSelectedInterval() {
        if ("1 Week".equals(selectedDateRange)) {
            return "7 days";
        }
        if ("1 Year".equals(selectedDateRange)) {
            return "1 year";
        }
        return "1 month";
    }

    private String getRevenueTimeBucket() {
        if ("1 Year".equals(selectedDateRange)) {
            return "month";
        }
        return "day";
    }

    private String getRevenueLabelFormat() {
        if ("1 Year".equals(selectedDateRange)) {
            return "YYYY-MM";
        }
        return "YYYY-MM-DD";
    }

    private void handleDateRangeSelect(String range) {
        selectedDateRange = range;
        updateDateRangeButtonStyles();

        String selectedReport = reportSelector.getSelectionModel().getSelectedItem();
        if (selectedReport != null && dateRangeReports.contains(selectedReport)) {
            loadReport(selectedReport);
        }
    }

    private void updateDateRangeButtonStyles() {
        String selectedStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #333333; -fx-text-fill: white; "
                + "-fx-border-color: #333333; -fx-border-radius: 6px; -fx-background-radius: 6px;";
        String normalStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: white; "
                + "-fx-border-color: #bbbbbb; -fx-border-radius: 6px; -fx-background-radius: 6px;";

        weekRangeButton.setStyle("1 Week".equals(selectedDateRange) ? selectedStyle : normalStyle);
        monthRangeButton.setStyle("1 Month".equals(selectedDateRange) ? selectedStyle : normalStyle);
        yearRangeButton.setStyle("1 Year".equals(selectedDateRange) ? selectedStyle : normalStyle);
    }

    private void executeAndDisplayReport(String sql) {
        reportTable.getColumns().clear();
        reportTable.getItems().clear();

        chartContainer.getChildren().clear();

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
            || currentReport.equals("Most Popular Modifiers") || currentReport.equals("Orders by Hour")
            || currentReport.equals("Revenue by Employee")) {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel(xAxisName);
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yAxisName);

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(currentReport);

            int maxRows = currentReport.equals("Top 5 Products") ? Math.min(5, data.size()) : data.size();
            for (int i = 0; i < maxRows; i++) {
                ObservableList<String> row = data.get(i);
                try {
                    String xVal = row.get(0);
                    Number yVal = Double.parseDouble(row.get(1));
                    series.getData().add(new XYChart.Data<>(xVal, yVal));
                } catch (NumberFormatException ignored) {
                }
            }
            barChart.getData().add(series);
            chartNode = barChart;

        } else if (currentReport.equals("Revenue Over Time")) {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel(xAxisName);
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yAxisName);

            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(currentReport);

            if ("1 Year".equals(selectedDateRange)) {
                Map<String, Double> revenueByMonth = new LinkedHashMap<>();
                for (ObservableList<String> row : data) {
                    try {
                        revenueByMonth.put(row.get(0), Double.parseDouble(row.get(1)));
                    } catch (NumberFormatException ignored) {
                    }
                }

                YearMonth currentMonth = YearMonth.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                for (int i = 11; i >= 0; i--) {
                    String monthLabel = currentMonth.minusMonths(i).format(formatter);
                    double revenue = revenueByMonth.getOrDefault(monthLabel, 0.0);
                    series.getData().add(new XYChart.Data<>(monthLabel, revenue));
                }
            } else {
                for (ObservableList<String> row : data) {
                    try {
                        String xVal = row.get(0);
                        Number yVal = Double.parseDouble(row.get(1));
                        series.getData().add(new XYChart.Data<>(xVal, yVal));
                    } catch (NumberFormatException ignored) {
                    }
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
            pieChart.setTitle("Sales by Category");
            chartNode = pieChart;
        }

        if (chartNode != null) {
            chartNode.setStyle(
                    "-fx-background-color: white; -fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 5px;");
            chartContainer.getChildren().add(chartNode);
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
