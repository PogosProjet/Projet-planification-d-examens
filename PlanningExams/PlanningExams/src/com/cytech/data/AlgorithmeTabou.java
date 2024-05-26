package com.cytech.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgorithmeTabou {

    private List<Examen> examens;
    private List<Examen> bestSolution;
    private List<List<Examen>> tabouList;
    private int tabouSize = 10; // Taille de la liste tabou
    private int maxIterations = 100;
    private String[] timeSlots = {"08:00", "10:00", "12:00", "14:00", "16:00"};

    public AlgorithmeTabou() {
        this.examens = Calendrier.getInstance().getAllExamens();
        this.bestSolution = new ArrayList<>(examens);
        this.tabouList = new ArrayList<>();
    }

    public void creerPlanning() {
        initialiserDates();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<Examen> voisin = genererVoisin();

            if (!tabouList.contains(voisin) && evaluerSolution(voisin) < evaluerSolution(bestSolution)) {
                bestSolution = new ArrayList<>(voisin);
                tabouList.add(voisin);

                if (tabouList.size() > tabouSize) {
                    tabouList.remove(0);
                }
            }
        }

        // Mise à jour des examens dans le calendrier avec la meilleure solution trouvée
        Calendrier.getInstance().getAllExamens().clear();
        Calendrier.getInstance().getAllExamens().addAll(bestSolution);
    }

    private void initialiserDates() {
        Random random = new Random();
        for (Examen examen : examens) {
            String date = "2024-06-" + (random.nextInt(20) + 1);
            String heureDebut = timeSlots[random.nextInt(timeSlots.length)];
            examen.setDate(date);
            examen.setHeure(heureDebut);
        }
    }

    private List<Examen> genererVoisin() {
        Random random = new Random();
        List<Examen> voisin = new ArrayList<>(bestSolution);
        int index = random.nextInt(voisin.size());

        // Modifie la date et l'heure d'un examen de manière aléatoire
        String date = "2024-06-" + (random.nextInt(20) + 1);
        String heureDebut = timeSlots[random.nextInt(timeSlots.length)];
        voisin.get(index).setDate(date);
        voisin.get(index).setHeure(heureDebut);

        return voisin;
    }

    private int evaluerSolution(List<Examen> solution) {
        int conflicts = 0;
        for (int i = 0; i < solution.size(); i++) {
            for (int j = i + 1; j < solution.size(); j++) {
                if (solution.get(i).getDate().equals(solution.get(j).getDate()) &&
                    solution.get(i).getHeure().equals(solution.get(j).getHeure())) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }
}
