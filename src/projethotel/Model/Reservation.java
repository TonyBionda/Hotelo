package projethotel.Model;

import java.util.*;

/**
 * 
 */
public class Reservation {

    /**
     * Default constructor
     */
    public Reservation() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private java.sql.Date dateDebut;

    /**
     * 
     */
    private java.sql.Date dateFin;

    /**
     * 
     */
    private int clientID;

    /**
     * 
     */
    private int nbPersonne;

    /**
     * 
     */
    private String etat;

    
    private int chambreID;
    /**
     * 
     */
    private int creePar;

    /**
     * 
     */
    private java.sql.Date dateCreation;

    /**
     * 
     */
    private java.sql.Date dateModification;

    /**
     * 
     */
    private float prix;

    private String hotelNom;
    
    private String clientNom;
    
    private String chambreNom;

    public Reservation(int id, java.sql.Date dateDebut, java.sql.Date dateFin, int clientID, int chambreID, String clientNom, String chambreNom, String hotelNom, int nbPersonne, String etat, java.sql.Date dateCreation, java.sql.Date dateModification, float prix) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.clientID = clientID;
        this.nbPersonne = nbPersonne;
        this.etat = etat;
        this.creePar = creePar;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.prix = prix;
        this.chambreID = chambreID;
        this.hotelNom = hotelNom;
        this.clientNom = clientNom;
        this.chambreNom = chambreNom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.sql.Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(java.sql.Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public java.sql.Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(java.sql.Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getCreePar() {
        return creePar;
    }

    public void setCreePar(int creePar) {
        this.creePar = creePar;
    }

    public java.sql.Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(java.sql.Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public java.sql.Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(java.sql.Date dateModification) {
        this.dateModification = dateModification;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getChambreID() {
        return chambreID;
    }

    public void setChambreID(int chambreID) {
        this.chambreID = chambreID;
    }

    public String getHotelNom() {
        return hotelNom;
    }

    public void setHotelNom(String hotelNom) {
        this.hotelNom = hotelNom;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getChambreNom() {
        return chambreNom;
    }

    public void setChambreNom(String chambreNom) {
        this.chambreNom = chambreNom;
    }
}