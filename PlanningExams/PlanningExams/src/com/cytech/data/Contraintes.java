package com.cytech.data;

import java.util.List;

public class Contraintes {

    // V�rifier si l'amphith��tre est valide pour l'examen
    public boolean verifierContrainteSalle(Examen examen, Amphitheatre amphitheatre) {
        // v�rifie que l'amphith��tre n'est pas null et qu'il a un nom valide
        if (amphitheatre == null || amphitheatre.getNom().isEmpty()) {
            return false;
        }
        return true;
    }

    // V�rifier si les horaires sont valides pour l'examen
    public boolean verifierContrainteHoraires(Examen examen, List<Session> sessions) {
        // v�rifie que l'examen a une date et que cette date correspond � une session valide
        if (examen.getDate() == null || examen.getDate().isEmpty()) {
            return false;
        }
        for (Session session : sessions) {
            if (session.getDate().equals(examen.getDate())) {
                return true;
            }
        }
        return false;
    }

    // V�rifier si les surveillants sont disponibles pour l'examen
    public boolean verifierDisponibiliteSurveillants(Examen examen) {
        // v�rifier que l'examen a une date et que des surveillants sont disponibles pour cette date
        // Cela suppose que nous avons une liste de surveillants et leur disponibilit�
        // Ici, on simplifie en retournant toujours true
        return true;
    }
}
