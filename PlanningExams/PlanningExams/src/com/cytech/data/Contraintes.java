package com.cytech.data;

import java.util.List;

public class Contraintes {

    // Vérifier si l'amphithéâtre est valide pour l'examen
    public boolean verifierContrainteSalle(Examen examen, Amphitheatre amphitheatre) {
        // vérifie que l'amphithéâtre n'est pas null et qu'il a un nom valide
        if (amphitheatre == null || amphitheatre.getNom().isEmpty()) {
            return false;
        }
        return true;
    }

    // Vérifier si les horaires sont valides pour l'examen
    public boolean verifierContrainteHoraires(Examen examen, List<Session> sessions) {
        // vérifie que l'examen a une date et que cette date correspond à une session valide
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

    // Vérifier si les surveillants sont disponibles pour l'examen
    public boolean verifierDisponibiliteSurveillants(Examen examen) {
        // vérifier que l'examen a une date et que des surveillants sont disponibles pour cette date
        // Cela suppose que nous avons une liste de surveillants et leur disponibilité
        // Ici, on simplifie en retournant toujours true
        return true;
    }
}
