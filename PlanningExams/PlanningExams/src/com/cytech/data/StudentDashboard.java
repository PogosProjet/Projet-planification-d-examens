package com.cytech.data;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StudentDashboard extends Application {

    private String studentClass;
    private Calendrier calendrier;

    public StudentDashboard(String studentClass) {
        this.studentClass = studentClass;
        this.calendrier = Calendrier.getInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Dashboard - " + studentClass);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        TableView<Examen> tableView = new TableView<>();
        ObservableList<Examen> examensList = FXCollections.observableArrayList(calendrier.getExamensByClasse(studentClass));
        tableView.setItems(examensList);

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
            if (data.getValue().getAmphitheatre() != null) {
                return new SimpleStringProperty(data.getValue().getAmphitheatre().getNom());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        tableView.getColumns().addAll(subjectCol, durationCol, dateCol, timeCol, amphitheatreCol);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        ToolBar toolBar = new ToolBar(logoutButton);
        root.setTop(toolBar);
        root.setCenter(tableView);

        primaryStage.setScene(scene);
        primaryStage.show();
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
