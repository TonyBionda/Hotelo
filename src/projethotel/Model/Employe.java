
package projethotel.Model;

/**
 **
 */
public class Employe {

    /**
     * Default constructor
     */
    public Employe() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String nom;

    
    private String motdepasse;
    
    /**
     * 
     */
    private String prenom;

    /**
     * 
     */
    private String adresse;

    /**
     * 
     */
    private String telephone;

    /**
     * 
     */
    private String mail;

    /**
     * 
     */
    private Boolean estActif;

    private Role role;
    /**
     * 
     */
    private java.sql.Date dateCreation;

    private int role_id;
    
    private int hotel_id;
    
    private Hotel hotel;
    
    private String hotel_nom;
    
    private String role_nom;
    
    public Employe(int id, String nom, String motdepasse, String prenom, String adresse, String telephone, String mail, Boolean estActif, java.sql.Date dateCreation, java.sql.Date dateModification, Hotel hotel, int hotel_id, String hotel_nom, Role role, int role_id, String role_nom) {
        this.id = id;
        this.nom = nom;
        this.motdepasse = motdepasse;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.mail = mail;
        this.estActif = estActif;
        this.dateCreation = dateCreation;
        this.hotel_id = hotel_id;
        this.hotel = hotel;
        this.hotel_nom = hotel_nom;
        this.dateModification = dateModification;
        this.role_id = role_id;
        this.role = role;
        this.role_nom = role_nom;
    }

    

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    /**
     * 
     */
    private java.sql.Date dateModification;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMail() {
        return mail;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public Boolean getEstActif() {
        return estActif;
    }

    public java.sql.Date getDateCreation() {
        return dateCreation;
    }

    public java.sql.Date getDateModification() {
        return dateModification;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public void setDateCreation(java.sql.Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateModification(java.sql.Date dateModification) {
        this.dateModification = dateModification;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getHotel_nom() {
        return hotel_nom;
    }

    public void setHotel_nom(String hotel_name) {
        this.hotel_nom = hotel_name;
    }

    public String getRole_nom() {
        return role_nom;
    }

    public void setRole_nom(String role_name) {
        this.role_nom = role_name;
    }
    
    
}