package com.example.spring_restcontroller_banuqe.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Compte {
    private Long idLong;
    private double solde;
    private Date dateCreation;

    @SerializedName("typeCompte") // Associer le champ JSON "typeCompte" avec le champ Java "type"
    private String type;

    public Long getIdLong() {
        return idLong;
    }

    public void setIdLong(Long idLong) {
        this.idLong = idLong;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "id=" + idLong +
                ", solde=" + solde +
                ", dateCreation=" + dateCreation +
                ", type='" + type + '\'' +
                '}';
    }
}


