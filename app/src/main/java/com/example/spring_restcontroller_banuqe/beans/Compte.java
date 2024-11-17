package com.example.spring_restcontroller_banuqe.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Compte {
    private Long id;
    private double solde;
    /*@JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date dateCreation;


     */
    @SerializedName("typeCompte") // Associer le champ JSON "typeCompte" avec le champ Java "type"
    private String type;

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

  /*  public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }


   */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "id=" + id +
                ", solde=" + solde +
               // ", dateCreation=" + dateCreation +
                ", type='" + type + '\'' +
                '}';
    }
}


