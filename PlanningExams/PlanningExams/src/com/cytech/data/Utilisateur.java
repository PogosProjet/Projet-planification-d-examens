package com.cytech.data;

public class Utilisateur {
    private String nom;
    private String role;

    public Utilisateur(String nom, String role) {
        this.nom = nom;
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
