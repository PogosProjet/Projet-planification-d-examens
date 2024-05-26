package com.cytech.data;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class AdminDashboard extends Application {

    private Calendrier calendrier = Calendrier.getInstance();
    private TableView<Examen> tableView;
    private ObservableList<Examen> examensList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        tableView = new TableView<>();
        examensList = FXCollections.observableArrayList(calendrier.getAllExamens());
        tableView.setItems(examensList);

        TableColumn<Examen, String> classeCol = new TableColumn<>("Classe");
        classeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClasse()));

        TableColumn<Examen, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));

        TableColumn<Examen, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDuree()).asObject());

        TableColumn<Examen, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));

        TableColumn<Examen, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHeure()));

        TableColumn<Examen, String> amphitheatreCol = new TableColumn<>("Amphitheatre");
        amphitheatreCol.setCellValueFactory(data -> {
            Amphitheatre amphitheatre = data.getValue().getAmphitheatre();
            return amphitheatre != null ? new SimpleStringProperty(amphitheatre.getNom() + " (" + amphitheatre.getCapacite() + ")") : new SimpleStringProperty("N/A");
        });

        tableView.getColumns().addAll(classeCol, subjectCol, durationCol, dateCol, timeCol, amphitheatreCol);

        Button addButton = new Button("Add Examen");
        addButton.setOnAction(e -> showAddExamenDialog());

        Button modifyButton = new Button("Modify Examen");
        modifyButton.setOnAction(e -> showModifyExamenDialog());

        Button deleteButton = new Button("Delete Examen");
        deleteButton.setOnAction(e -> showDeleteExamenDialog());

        Button generateButton = new Button("Generate Planning");
        generateButton.setOnAction(e -> showGeneratePlanningDialog());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        ToolBar toolBar = new ToolBar(addButton, modifyButton, deleteButton, generateButton, logoutButton);
        root.setTop(toolBar);
        root.setCenter(tableView);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddExamenDialog() {
        Dialog<Examen> dialog = new Dialog<>();
        dialog.setTitle("Add Examen");
        dialog.setHeaderText("Enter the examen details");

        TextField classeField = new TextField();
        TextField subjectField = new TextField();
        TextField durationField = new TextField();
        TextField dateField = new TextField();
        TextField timeField = new TextField();
        ComboBox<String> amphitheatreComboBox = new ComboBox<>();
        amphitheatreComboBox.setItems(FXCollections.observableArrayList("Amphi 1", "Amphi 2", "Amphi 3", "Amphi 4", "Amphi 5", "Amphi 6"));

        GridPane grid = new GridPane();
        grid.add(new Label("Classe:"), 0, 0);
        grid.add(classeField, 1, 0);
        grid.add(new Label("Subject:"), 0, 1);
        grid.add(subjectField, 1, 1);
        grid.add(new Label("Duration (minutes):"), 0, 2);
        grid.add(durationField, 1, 2);
        grid.add(new Label("Date (yyyy-MM-dd):"), 0, 3);
        grid.add(dateField, 1, 3);
        grid.add(new Label("Time (HH:mm):"), 0, 4);
        grid.add(timeField, 1, 4);
        grid.add(new Label("Amphitheatre:"), 0, 5);
        grid.add(amphitheatreComboBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton);

        classeField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton));
        subjectField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton));
        durationField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton));
        dateField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton));
        timeField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton));
        amphitheatreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, addButton));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String classe = classeField.getText();
                String subject = subjectField.getText();
                int duration;
                try {
                    duration = Integer.parseInt(durationField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Duration must be a number.", "");
                    return null;
                }
                String date = dateField.getText();
                String time = timeField.getText();
                String amphitheatreName = amphitheatreComboBox.getValue();
                if (amphitheatreName == null) {
                    showAlert("Invalid Input", "Please select an amphitheatre.", "");
                    return null;
                }
                Amphitheatre amphitheatre = calendrier.getAmphitheatreByName(amphitheatreName.split(" ")[0]);
                Examen examen = new Examen(classe, subject, duration, date, time, amphitheatre);
                calendrier.addExamen(classe, subject, duration, date, time, amphitheatre);
                return examen;
            }
            return null;
        });

        Optional<Examen> result = dialog.showAndWait();
        result.ifPresent(examen -> {
            examensList.setAll(calendrier.getAllExamens());
            tableView.refresh();
        });
    }
    
    private void showModifyExamenDialog() {
        Examen selectedExamen = tableView.getSelectionModel().getSelectedItem();
        if (selectedExamen == null) {
            showAlert("No Selection", "No Examen Selected", "Please select an examen to modify.");
            return;
        }

        Dialog<Examen> dialog = new Dialog<>();
        dialog.setTitle("Modify Examen");
        dialog.setHeaderText("Modify the examen details");

        TextField classeField = new TextField(selectedExamen.getClasse());
        TextField subjectField = new TextField(selectedExamen.getNom());
        TextField durationField = new TextField(String.valueOf(selectedExamen.getDuree()));
        TextField dateField = new TextField(selectedExamen.getDate());
        TextField timeField = new TextField(selectedExamen.getHeure());
        ComboBox<String> amphitheatreComboBox = new ComboBox<>();
        amphitheatreComboBox.setItems(FXCollections.observableArrayList("Amphi 1", "Amphi 2", "Amphi 3", "Amphi 4", "Amphi 5", "Amphi 6"));
        if (selectedExamen.getAmphitheatre() != null) {
            amphitheatreComboBox.setValue(selectedExamen.getAmphitheatre().getNom());
        }

        GridPane grid = new GridPane();
        grid.add(new Label("Classe:"), 0, 0);
        grid.add(classeField, 1, 0);
        grid.add(new Label("Subject:"), 0, 1);
        grid.add(subjectField, 1, 1);
        grid.add(new Label("Duration (minutes):"), 0, 2);
        grid.add(durationField, 1, 2);
        grid.add(new Label("Date (yyyy-MM-dd):"), 0, 3);
        grid.add(dateField, 1, 3);
        grid.add(new Label("Time (HH:mm):"), 0, 4);
        grid.add(timeField, 1, 4);
        grid.add(new Label("Amphitheatre:"), 0, 5);
        grid.add(amphitheatreComboBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        Node modifyButton = dialog.getDialogPane().lookupButton(modifyButtonType);
        validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton);

        classeField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton));
        subjectField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton));
        durationField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton));
        dateField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton));
        timeField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton));
        amphitheatreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateFields(classeField, subjectField, durationField, dateField, timeField, amphitheatreComboBox, modifyButton));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                String classe = classeField.getText();
                String subject = subjectField.getText();
                int duration;
                try {
                    duration = Integer.parseInt(durationField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Duration must be a number.", "");
                    return null;
                }
                String date = dateField.getText();
                String time = timeField.getText();
                String amphitheatreName = amphitheatreComboBox.getValue();
                if (amphitheatreName == null) {
                    showAlert("Invalid Input", "Please select an amphitheatre.", "");
                    return null;
                }
                Amphitheatre amphitheatre = calendrier.getAmphitheatreByName(amphitheatreName.split(" ")[0]);
                calendrier.modifierExamen(selectedExamen, subject, duration, date, time, amphitheatre);
                selectedExamen.setClasse(classe);
                selectedExamen.setNom(subject);
                selectedExamen.setDuree(duration);
                selectedExamen.setDate(date);
                selectedExamen.setHeure(time);
                selectedExamen.setAmphitheatre(amphitheatre);
                return selectedExamen;
            }
            return null;
        });

        Optional<Examen> result = dialog.showAndWait();
        result.ifPresent(examen -> {
            examensList.setAll(calendrier.getAllExamens());
            tableView.refresh();
        });
    }

    private void showDeleteExamenDialog() {
        Examen selectedExamen = tableView.getSelectionModel().getSelectedItem();
        if (selectedExamen == null) {
            showAlert("No Selection", "No Examen Selected", "Please select an examen to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Examen");
        alert.setHeaderText("Are you sure you want to delete this examen?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            calendrier.supprimerExamen(selectedExamen.getNom(), selectedExamen.getClasse());
            examensList.setAll(calendrier.getAllExamens());
            tableView.refresh();
        }
    }

    private void showGeneratePlanningDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Generate Planning");
        dialog.setHeaderText("Enter the planning details");

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        GridPane grid = new GridPane();
        grid.add(new Label("Start Date:"), 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(new Label("End Date:"), 0, 1);
        grid.add(endDatePicker, 1, 1);

        dialog.getDialogPane().setContent(grid);
        ButtonType generateButtonType = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateButtonType, ButtonType.CANCEL);

        Node generateButton = dialog.getDialogPane().lookupButton(generateButtonType);
        generateButton.setDisable(true);

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateDatePickers(startDatePicker, endDatePicker, generateButton));
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateDatePickers(startDatePicker, endDatePicker, generateButton));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                if (startDate != null && endDate != null) {
                    try {
                        calendrier.generatePlanning(startDate, endDate);
                        return dialogButton;
                    } catch (Exception e) {
                        showAlert("Generation Failed", "Unable to generate planning.", e.getMessage());
                    }
                }
            }
            return null;
        });

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            examensList.setAll(calendrier.getAllExamens());
            tableView.refresh();
        });
    }

    private void validateFields(TextField classeField, TextField subjectField, TextField durationField, TextField dateField, TextField timeField, ComboBox<String> amphitheatreComboBox, Node button) {
        button.setDisable(classeField.getText().trim().isEmpty()
                || subjectField.getText().trim().isEmpty()
                || durationField.getText().trim().isEmpty()
                || dateField.getText().trim().isEmpty()
                || timeField.getText().trim().isEmpty()
                || amphitheatreComboBox.getValue() == null);
    }

    private void validateDatePickers(DatePicker startDatePicker, DatePicker endDatePicker, Node button) {
        button.setDisable(startDatePicker.getValue() == null || endDatePicker.getValue() == null);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void logout(Stage primaryStage) {
        Platform.runLater(() -> {
            LoginForm loginForm = new LoginForm();
            try {
                loginForm.start(new Stage());
                primaryStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
