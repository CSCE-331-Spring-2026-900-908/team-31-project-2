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
import org.kordamp.bootstrapfx.BootstrapFX;

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

/**
 * Controller class for the Reports dashboard.
 * Generates and displays various analytical reports for the business.
 * Supports visualization through charts and tabular data for business metrics.
 * 
 * @author Team 31
 */
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

    private final Map<String, String> reportToFileMap = new LinkedHashMap<>();
    private final Set<String> dateRangeReports = Set.of(
            "Most Popular Modifiers",
            "Orders by Day of Week",
            "Revenue Over Time",
            "Sales by Category",
            "Top 5 Products",
            "Orders by Hour",
            "Revenue by Employee",
            "Product Usage Chart",
            "Sales Report");
    private String selectedDateRange = "1 Month";

    /**
     * Initializes the controller class.
     * Sets up the report selection map and initializes UI components.
     */
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
        reportToFileMap.put("Product Usage Chart", "custom_reports/product_usage.sql");
        reportToFileMap.put("Sales Report", "custom_reports/sales_by_item.sql");
        reportToFileMap.put("X-Report", "custom_reports/x_report.sql");
        reportToFileMap.put("Z-Report", "custom_reports/z_report.sql");

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

        if (!reportSelector.getItems().isEmpty()) {
            reportSelector.getSelectionModel().selectFirst();
        }
    }

    /**
     * Sets the current user for the session.
     * 
     * @param user The Employee object representing the current user.
     */
    public void setUser(Employee user) {
        this.currentUser = user;
    }

    /**
     * Loads the specified report.
     * Validates the report name, prepares the SQL query, and executing it.
     * 
     * @param reportName The name of the report to load.
     */
    private void loadReport(String reportName) {
        if (reportName == null || !reportToFileMap.containsKey(reportName))
            return;

        if ("Z-Report".equals(reportName)) {
            try (Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet checkRs = stmt.executeQuery(
                            "SELECT 1 FROM \"order\" WHERE z_report_run = TRUE AND DATE(created_at) = CURRENT_DATE LIMIT 1")) {
                if (checkRs.next()) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING, "Z-Report has already been generated today.");
                    alert.showAndWait();
                    reportTitle.setText(reportName);
                    reportTable.getColumns().clear();
                    reportTable.getItems().clear();
                    chartContainer.getChildren().clear();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
    
    /**
     * Reads the content of an SQL file.
     * 
     * @param relativePath The relative path to the SQL file.
     * @return The content of the file as a string.
     */
    private String readSqlFile(String relativePath) {
        Path path = Paths.get("queries", relativePath);
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.err.println("Error reading SQL file at: " + path.toAbsolutePath());
            return null;
        }
    }
    
    /**
     * Resolves the SQL query by replacing placeholders with actual values.
     * 
     * @param reportName The name of the report.
     * @param relativePath The path to the SQL file.
     * @return The resolved SQL query string.
     */
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
            
    /**
     * Gets the selected time interval for the report.
     * 
     * @return The interval string (e.g., "7 days", "1 month").
     */
    private String getSelectedInterval() {
        if ("1 Week".equals(selectedDateRange)) {
            return "7 days";
        }
        if ("1 Year".equals(selectedDateRange)) {
            return "1 year";
        }
        return "1 month";
    }
    
    /**
     * Determines the time bucket for revenue reports based on the selected range.
     * 
     * @return "month" for yearly reports, "day" otherwise.
     */
    private String getRevenueTimeBucket() {
        if ("1 Year".equals(selectedDateRange)) {
            return "month";
        }
        return "day";
    }
    
    /**
     * Determines the label format for revenue reports.
     * 
     * @return The date format string (e.g., "YYYY-MM").
     */
    private String getRevenueLabelFormat() {
        if ("1 Year".equals(selectedDateRange)) {
            return "YYYY-MM";
        }
        return "YYYY-MM-DD";
    }
    
    /**
     * Handles the selection of a date range.
     * Updates the UI and reloads the current report if applicable.
     * 
     * @param range The selected date range.
     */
    private void handleDateRangeSelect(String range) {
        selectedDateRange = range;
        updateDateRangeButtonStyles();
        
        String selectedReport = reportSelector.getSelectionModel().getSelectedItem();
        if (selectedReport != null && dateRangeReports.contains(selectedReport)) {
            loadReport(selectedReport);
        }
    }
    
    /**
     * Updates the visual style of date range buttons to indicate the active selection.
     */
    private void updateDateRangeButtonStyles() {
        String selectedStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #333333; -fx-text-fill: white; "
        + "-fx-border-color: #333333; -fx-border-radius: 6px; -fx-background-radius: 6px;";
        String normalStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: white; "
        + "-fx-border-color: #bbbbbb; -fx-border-radius: 6px; -fx-background-radius: 6px;";
        
        weekRangeButton.setStyle("1 Week".equals(selectedDateRange) ? selectedStyle : normalStyle);
        monthRangeButton.setStyle("1 Month".equals(selectedDateRange) ? selectedStyle : normalStyle);
        yearRangeButton.setStyle("1 Year".equals(selectedDateRange) ? selectedStyle : normalStyle);
    }
    
    /**
     * Executes the SQL query and displays the results in the table and chart.
     * 
     * @param sql The SQL query to execute.
     */
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

            if ("Z-Report".equals(reportSelector.getSelectionModel().getSelectedItem()) && !data.isEmpty()) {
                try (Statement updateStmt = conn.createStatement()) {
                    updateStmt.executeUpdate("UPDATE \"order\" SET z_report_run = TRUE WHERE z_report_run = FALSE;");
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.INFORMATION,
                            "Z-Report generated successfully and the totals have been reset.");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a chart visualization from the report data.
     * Supports BarChart, LineChart, and PieChart depending on the report type.
     * 
     * @param xAxisName The label for the X-axis.
     * @param yAxisName The label for the Y-axis.
     * @param data The data to visualize.
     */
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
                || currentReport.equals("Revenue by Employee") || currentReport.equals("Product Usage Chart")
                || currentReport.equals("Sales Report") || currentReport.equals("X-Report")
                || currentReport.equals("Z-Report")) {
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
    
    /**
     * Navigates to the ordering screen.
     */
    @FXML
    private void handleNavigateOrdering() {
        navigateTo("ordering-view.fxml");
    }
    
    /**
     * Navigates to the menu edit screen.
     */
    @FXML
    private void handleNavigateMenuEdit() {
        navigateTo("menu-edit-view.fxml");
    }
    
    /**
     * Navigates to the employee management screen.
     */
    @FXML
    private void handleNavigateEmployees() {
        navigateTo("employee-list-view.fxml");
    }
    
    /**
     * Navigates to the inventory management screen.
     */
    @FXML
    private void handleNavigateInventory() {
        navigateTo("inventory-view.fxml");
    }
    
    /**
     * Navigates to the reports dashboard (refreshes current view).
     */
    @FXML
    private void handleNavigateReports() {
        navigateTo("reports-view.fxml");
    }
    
    /**
     * Handles sign-out and returns to login screen.
     */
    @FXML
    private void handleSignOut() {
        UserSession.clear();
        navigateTo("login-view.fxml");
    }
    
    /**
     * Helper method to load a new scene.
     * 
     * @param fxmlFile The name of the FXML file to load.
     */
    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if ("ordering-view.fxml".equals(fxmlFile)) {
                OrderController controller = loader.getController();
                controller.setUser(currentUser != null ? currentUser : UserSession.getCurrentUser());
            }

            Stage stage = (Stage) reportTable.getScene().getWindow();
            double width = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_WIDTH : SceneConfig.APP_WIDTH;
            double height = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_HEIGHT : SceneConfig.APP_HEIGHT;
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
