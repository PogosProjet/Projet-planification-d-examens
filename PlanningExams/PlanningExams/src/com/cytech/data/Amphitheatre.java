package com.cytech.data;

import java.io.Serializable;

public class Amphitheatre implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nom;
    private int capacite;

    public Amphitheatre(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return nom;
    }
}
