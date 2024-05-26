package com.cytech.data;

import java.io.Serializable;

public class Examen implements Serializable {
    private static final long serialVersionUID = 1L;
    private String classe;
    private String nom;
    private int duree;
    private String date;
    private String heure;
    private Amphitheatre amphitheatre;

    // Constructeur existant
    public Examen(String classe, String nom, int duree) {
        this.classe = classe;
        this.nom = nom;
        this.duree = duree;
    }

    // Nouveau constructeur
    public Examen(String classe, String nom, int duree, String date, String heure, Amphitheatre amphitheatre) {
        this.classe = classe;
        this.nom = nom;
        this.duree = duree;
        this.date = date;
        this.heure = heure;
        this.amphitheatre = amphitheatre;
    }

    // Getters et setters
    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public Amphitheatre getAmphitheatre() {
        return amphitheatre;
    }

    public void setAmphitheatre(Amphitheatre amphitheatre) {
        this.amphitheatre = amphitheatre;
    }
}
