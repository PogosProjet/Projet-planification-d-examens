package com.cytech.data;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Calendrier implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Examen> examens;
    private List<Amphitheatre> amphitheatres;

    private static Calendrier instance;

    public static Calendrier getInstance() {
        if (instance == null) {
            instance = new Calendrier();
        }
        return instance;
    }

    private Calendrier() {
        examens = new ArrayList<>();
        amphitheatres = new ArrayList<>();
        amphitheatres.add(new Amphitheatre("Amphi 1", 50));
        amphitheatres.add(new Amphitheatre("Amphi 2", 75));
        amphitheatres.add(new Amphitheatre("Amphi 3", 100));
        amphitheatres.add(new Amphitheatre("Amphi 4", 125));
        amphitheatres.add(new Amphitheatre("Amphi 5", 150));
        amphitheatres.add(new Amphitheatre("Amphi 6", 200));
        loadExamens();
    }

    private void loadExamens() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("examens.dat"))) {
            examens = (List<Examen>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            prepopulateData();
        }
    }

    private void saveExamens() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("examens.dat"))) {
            oos.writeObject(examens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepopulateData() {
        addExamen("ING1 MI1", "Algorithmes", 120, "2023-06-01", "09:00", amphitheatres.get(0));
        addExamen("ING1 MI1", "Mathématiques", 90, "2023-06-02", "11:00", amphitheatres.get(1));
        addExamen("ING1 MI2", "Physique", 90, "2023-06-03", "08:00", amphitheatres.get(2));
        addExamen("ING2 MI1", "Chimie", 60, "2023-06-04", "10:00", amphitheatres.get(3));
        addExamen("ING2 MI2", "Biologie", 120, "2023-06-05", "13:00", amphitheatres.get(4));
        addExamen("ING3 MI1", "Informatique", 180, "2023-06-06", "14:00", amphitheatres.get(5));
    }

    public List<Examen> getAllExamens() {
        return examens;
    }

    public List<Examen> getExamensByClasse(String classe) {
        return examens.stream()
                      .filter(examen -> examen.getClasse().equals(classe))
                      .collect(Collectors.toList());
    }

    public void addExamen(String classe, String nom, int duree, String date, String heure, Amphitheatre amphitheatre) {
        examens.add(new Examen(classe, nom, duree, date, heure, amphitheatre));
        saveExamens();
    }

    public void modifierExamen(Examen examen, String nouveauNom, int nouvelleDuree, String nouvelleDate, String nouvelleHeure, Amphitheatre nouveauAmphitheatre) {
        examen.setNom(nouveauNom);
        examen.setDuree(nouvelleDuree);
        examen.setDate(nouvelleDate);
        examen.setHeure(nouvelleHeure);
        examen.setAmphitheatre(nouveauAmphitheatre);
        saveExamens();
    }

    public void supprimerExamen(String nom, String classe) {
        examens.removeIf(ex -> ex.getNom().equals(nom) && ex.getClasse().equals(classe));
        saveExamens();
    }

    public Amphitheatre getAmphitheatreByName(String name) {
        for (Amphitheatre amphitheatre : amphitheatres) {
            if (amphitheatre.getNom().equals(name)) {
                return amphitheatre;
            }
        }
        return null;
    }

    public void generatePlanning(LocalDate startDate, LocalDate endDate) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Random random = new Random();
        int workingHoursPerDay = 8; // 8 hours per day for exams (08:00 - 16:00)
        int totalDays = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int totalSlots = totalDays * workingHoursPerDay;

        if (totalSlots < examens.size()) {
            throw new Exception("La période donnée est insuffisante pour planifier tous les examens.");
        }

        for (Examen examen : examens) {
            boolean conflict;
            do {
                conflict = false;
                long randomDay = random.nextInt(totalDays);
                LocalDate examDate = startDate.plusDays(randomDay);
                int randomHour = random.nextInt(workingHoursPerDay);
                String examTime = String.format("%02d:00", 8 + randomHour); // Entre 08:00 et 15:00
                LocalTime startTime = LocalTime.parse(examTime);
                LocalTime endTime = startTime.plusMinutes(examen.getDuree());

                for (Examen existingExam : examens) {
                    if (existingExam.getDate() != null && !existingExam.getDate().isEmpty()
                            && existingExam.getHeure() != null && !existingExam.getHeure().isEmpty()) {
                        LocalTime existingStartTime = LocalTime.parse(existingExam.getHeure());
                        LocalTime existingEndTime = existingStartTime.plusMinutes(existingExam.getDuree());
                        if (existingExam.getDate().equals(examDate.format(formatter))
                                && (existingExam.getAmphitheatre().equals(examen.getAmphitheatre()) 
                                    || existingExam.getClasse().equals(examen.getClasse()))
                                && (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime))) {
                            conflict = true;
                            break;
                        }
                    }
                }

                if (!conflict) {
                    examen.setDate(examDate.format(formatter));
                    examen.setHeure(examTime);
                    Amphitheatre amphitheatre = amphitheatres.get(random.nextInt(amphitheatres.size()));
                    examen.setAmphitheatre(amphitheatre);
                }

            } while (conflict);
        }

        saveExamens();
    }
}
