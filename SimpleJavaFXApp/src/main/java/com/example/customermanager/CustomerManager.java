package com.example.customermanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerManager extends Application {

    private final ObservableList<Customer> customerData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // --- 1. Form Inputs ---
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");

        // Input restriction: Prevent typing numbers or special symbols in the name field
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {
                nameField.setText(oldValue);
            }
        });

        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll("Central", "Copperbelt", "Eastern", "Luapula", "Lusaka", "Muchinga", "Northern", "North-Western", "Southern", "Western");
        provinceBox.setPromptText("Select Province");
        provinceBox.setMaxWidth(Double.MAX_VALUE);

        // --- 2. Grid Layout for Form ---
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10, 0, 10, 0));

        formGrid.add(new Label("Customer Name:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Province:"), 0, 1);
        formGrid.add(provinceBox, 1, 1);

        // Let the inputs expand to fill available horizontal space
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(provinceBox, Priority.ALWAYS);

        // --- 3. Action Buttons & Search ---
        Button saveButton = new Button("Save Customer");
        saveButton.setDefaultButton(true);
        saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Filter by name or province...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox actionRow = new HBox(10, saveButton, deleteButton, searchField);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        // --- 4. Table Setup ---
        TableView<Customer> table = new TableView<>();

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        table.getColumns().addAll(nameCol, provinceCol);

        // Responsive Table Columns: Auto-stretch to fill the table width evenly
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- 5. Filtering/Search Logic ---
        FilteredList<Customer> filteredData = new FilteredList<>(customerData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase().trim();
                if (customer.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return customer.getProvince().toLowerCase().contains(lowerCaseFilter);
            });
        });
        table.setItems(filteredData);

        // --- 6. Feedback Status Bar ---
        Label statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #757575; -fx-font-style: italic;");

        // --- 7. Event Handlers ---
        saveButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                statusLabel.setText("⚠️ Validation Error: Name cannot be empty.");
                statusLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");
                nameField.requestFocus();
                return;
            }

            String province = provinceBox.getValue();
            if (province == null) {
                statusLabel.setText("⚠️ Validation Error: Please select a province.");
                statusLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");
                provinceBox.requestFocus();
                return;
            }

            customerData.add(new Customer(name, province));
            statusLabel.setText("✅ Customer successfully saved.");
            statusLabel.setStyle("-fx-text-fill: #388E3C; -fx-font-weight: bold;");

            // UI Reset
            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        deleteButton.setOnAction(event -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("⚠️ Action Error: Select a customer from the table first.");
                statusLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");
                return;
            }

            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to permanently delete " + selected.getName() + "?",
                    new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE),
                    ButtonType.CANCEL
            );
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText(null);

            confirmation.showAndWait().ifPresent(response -> {
                if (response.getText().equals("Delete")) {
                    customerData.remove(selected);
                    statusLabel.setText("🗑️ Customer removed successfully.");
                    statusLabel.setStyle("-fx-text-fill: #388E3C; -fx-font-weight: bold;");
                }
            });
        });

        // --- 8. Window Main Assembly ---
        VBox mainLayout = new VBox(15, formGrid, actionRow, table, statusLabel);
        mainLayout.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS); // Table expands dynamically if window is resized

        Scene scene = new Scene(mainLayout, 700, 500);
        stage.setTitle("Customer Relationship Manager");
        stage.setScene(scene);
        stage.setMinWidth(550);
        stage.setMinHeight(400);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
