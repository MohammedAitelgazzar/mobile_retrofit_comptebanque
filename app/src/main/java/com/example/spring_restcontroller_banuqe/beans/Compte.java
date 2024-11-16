package com.example.spring_restcontroller_banuqe.beans;

import java.util.Date;

public class Compte {
    private Long id;
    private double solde;
    private Date dateCreation;
    private TypeCompte typeCompte;

    // Constructor
    public Compte(Long id, double solde, Date dateCreation, TypeCompte typeCompte) {
        this.id = id;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.typeCompte = typeCompte;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }
}
