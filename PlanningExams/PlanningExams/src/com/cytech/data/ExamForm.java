package com.cytech.data;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ExamForm {

    private AdminDashboard adminDashboard;

    public ExamForm(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
    }

    public void display() {
        Stage window = new Stage();
        window.setTitle("Ajouter un Examen");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label nameLabel = new Label("Nom de l'Examen:");
        GridPane.setConstraints(nameLabel, 0, 0);

        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);

        Label dateLabel = new Label("Date:");
        GridPane.setConstraints(dateLabel, 0, 1);

        TextField dateInput = new TextField();
        GridPane.setConstraints(dateInput, 1, 1);

        Label timeLabel = new Label("Heure de Début:");
        GridPane.setConstraints(timeLabel, 0, 2);

        TextField timeInput = new TextField();
        GridPane.setConstraints(timeInput, 1, 2);

        Label durationLabel = new Label("Durée (minutes):");
        GridPane.setConstraints(durationLabel, 0, 3);

        TextField durationInput = new TextField();
        GridPane.setConstraints(durationInput, 1, 3);

        Label promoLabel = new Label("Promotion:");
        GridPane.setConstraints(promoLabel, 0, 4);

        ComboBox<String> promoInput = new ComboBox<>(FXCollections.observableArrayList("ING1", "ING2", "ING3"));
        GridPane.setConstraints(promoInput, 1, 4);

        Label filiereLabel = new Label("Filière:");
        GridPane.setConstraints(filiereLabel, 0, 5);

        ComboBox<String> filiereInput = new ComboBox<>(FXCollections.observableArrayList("GI", "GM", "MI", "MF", "BTC"));
        GridPane.setConstraints(filiereInput, 1, 5);

        Label amphitheatreLabel = new Label("Amphithéâtre:");
        GridPane.setConstraints(amphitheatreLabel, 0, 6);

        ComboBox<Amphitheatre> amphitheatreInput = new ComboBox<>(FXCollections.observableArrayList(Calendrier.getInstance().getAmphitheatres()));
        GridPane.setConstraints(amphitheatreInput, 1, 6);

        Button addButton = new Button("Ajouter");
        GridPane.setConstraints(addButton, 1, 7);
        addButton.setOnAction(e -> {
            String examName = nameInput.getText();
            String examDate = dateInput.getText();
            String examTime = timeInput.getText();
            int examDuration = Integer.parseInt(durationInput.getText());
            String examPromo = promoInput.getValue();
            String examFiliere = filiereInput.getValue();
            Amphitheatre examAmphi = amphitheatreInput.getValue();

            if (!examName.isEmpty() && !examDate.isEmpty() && !examTime.isEmpty() && examDuration > 0 && examAmphi != null && examPromo != null && examFiliere != null) {
                Examen newExamen = new Examen(examName, examDate, examTime, examDuration, examPromo, examFiliere, examAmphi);
                Calendrier.getInstance().addExamen(newExamen);
                adminDashboard.refreshTable();
                window.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Tous les champs doivent être remplis");
                alert.showAndWait();
            }
        });

        grid.getChildren().addAll(nameLabel, nameInput, dateLabel, dateInput, timeLabel, timeInput, durationLabel, durationInput, promoLabel, promoInput, filiereLabel, filiereInput, amphitheatreLabel, amphitheatreInput, addButton);

        Scene scene = new Scene(grid, 400, 300);
        window.setScene(scene);
        window.show();
    }
}
