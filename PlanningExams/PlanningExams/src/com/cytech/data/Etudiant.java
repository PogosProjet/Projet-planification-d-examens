package com.cytech.data;

import java.util.ArrayList;
import java.util.List;

public class Etudiant {
    private String nom;
    private String filiere;
    private List<Examen> examens;

    public Etudiant(String nom, String filiere) {
        this.nom = nom;
        this.filiere = filiere;
        this.examens = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public List<Examen> getExamens() {
        return examens;
    }

    public void setExamens(List<Examen> examens) {
        this.examens = examens;
    }
}
