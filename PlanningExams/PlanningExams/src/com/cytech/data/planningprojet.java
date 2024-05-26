package com.cytech.data;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

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
        dialog.setHeaderText("Add a new Examen");

        Label classeLabel = new Label("Classe:");
        TextField classeField = new TextField();
        Label subjectLabel = new Label("Subject:");
        TextField subjectField = new TextField();
        Label durationLabel = new Label("Duration (minutes):");
        TextField durationField = new TextField();
        Label dateLabel = new Label("Date (YYYY-MM-DD):");
        TextField dateField = new TextField();
        Label timeLabel = new Label("Time (HH:MM):");
        TextField timeField = new TextField();
        Label amphitheatreLabel = new Label("Amphitheatre:");
        ComboBox<String> amphitheatreComboBox = new ComboBox<>();
        amphitheatreComboBox.getItems().addAll(
            "Amphi 1 (50)",
            "Amphi 2 (75)",
            "Amphi 3 (100)"
        );

        GridPane grid = new GridPane();
        grid.add(classeLabel, 0, 0);
        grid.add(classeField, 1, 0);
        grid.add(subjectLabel, 0, 1);
        grid.add(subjectField, 1, 1);
        grid.add(durationLabel, 0, 2);
        grid.add(durationField, 1, 2);
        grid.add(dateLabel, 0, 3);
        grid.add(dateField, 1, 3);
        grid.add(timeLabel, 0, 4);
        grid.add(timeField, 1, 4);
        grid.add(amphitheatreLabel, 0, 5);
        grid.add(amphitheatreComboBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String classe = classeField.getText();
                String subject = subjectField.getText();
                int duration = Integer.parseInt(durationField.getText());
                String date = dateField.getText();
                String time = timeField.getText();
                String amphitheatreName = amphitheatreComboBox.getValue();
                Amphitheatre amphitheatre = calendrier.getAmphitheatreByName(amphitheatreName.split(" ")[0]);
                calendrier.addExamen(classe, subject, duration, date, time, amphitheatre);
                return new Examen(classe, subject, duration, date, time, amphitheatre);
            }
            return null;
        });

        Optional<Examen> result = dialog.showAndWait();
        result.ifPresent(examen -> {
            examensList.add(examen);
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
        dialog.setHeaderText("Modify the selected Examen");

        Label classeLabel = new Label("Classe:");
        TextField classeField = new TextField(selectedExamen.getClasse());
        Label subjectLabel = new Label("Subject:");
        TextField subjectField = new TextField(selectedExamen.getNom());
        Label durationLabel = new Label("Duration (minutes):");
        TextField durationField = new TextField(String.valueOf(selectedExamen.getDuree()));
        Label dateLabel = new Label("Date (YYYY-MM-DD):");
        TextField dateField = new TextField(selectedExamen.getDate());
        Label timeLabel = new Label("Time (HH:MM):");
        TextField timeField = new TextField(selectedExamen.getHeure());
        Label amphitheatreLabel = new Label("Amphitheatre:");
        ComboBox<String> amphitheatreComboBox = new ComboBox<>();
        amphitheatreComboBox.getItems().addAll(
            "Amphi 1 (50)",
            "Amphi 2 (75)",
            "Amphi 3 (100)"
        );
        amphitheatreComboBox.setValue(selectedExamen.getAmphitheatre() != null ? selectedExamen.getAmphitheatre().getNom() + " (" + selectedExamen.getAmphitheatre().getCapacite() + ")" : "N/A");

        GridPane grid = new GridPane();
        grid.add(classeLabel, 0, 0);
        grid.add(classeField, 1, 0);
        grid.add(subjectLabel, 0, 1);
        grid.add(subjectField, 1, 1);
        grid.add(durationLabel, 0, 2);
        grid.add(durationField, 1, 2);
        grid.add(dateLabel, 0, 3);
        grid.add(dateField, 1, 3);
        grid.add(timeLabel, 0, 4);
        grid.add(timeField, 1, 4);
        grid.add(amphitheatreLabel, 0, 5);
        grid.add(amphitheatreComboBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                String classe = classeField.getText();
                String subject = subjectField.getText();
                int duration = Integer.parseInt(durationField.getText());
                String date = dateField.getText();
                String time = timeField.getText();
                String amphitheatreName = amphitheatreComboBox.getValue();
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
        alert.setHeaderText("Are you sure you want to delete the selected examen?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            examensList.remove(selectedExamen);
            calendrier.supprimerExamen(selectedExamen.getNom(), selectedExamen.getClasse());
        }
    }

    private void showGeneratePlanningDialog() {
        Dialog<LocalDate[]> dialog = new Dialog<>();
        dialog.setTitle("Generate Planning");
        dialog.setHeaderText("Select the start and end dates for the exam period");

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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                return new LocalDate[]{startDatePicker.getValue(), endDatePicker.getValue()};
            }
            return null;
        });

        Optional<LocalDate[]> result = dialog.showAndWait();
        result.ifPresent(dates -> {
            LocalDate startDate = dates[0];
            LocalDate endDate = dates[1];
            try {
                calendrier.generatePlanning(startDate, endDate);
                examensList.setAll(calendrier.getAllExamens());
            } catch (Exception e) {
                showAlert("Error", "Unable to generate planning", e.getMessage());
            }
        });
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void logout(Stage stage) {
        stage.close();
        Platform.runLater(() -> {
            try {
                new LoginForm().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
