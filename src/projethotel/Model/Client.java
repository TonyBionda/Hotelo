package projethotel.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    /**
     * Default constructor
     */
    public Client() {
    }

    private int id;

    private String nom;

    private String prenom;

    private String telephone;

    private String mail;

    private java.sql.Date dateCreation;

    private java.sql.Date dateModification;

    public void payer() {
        
    }

    public Client(int id, String nom, String prenom, String telephone, String mail, Date dateCreation, Date dateModification) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.mail = mail;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }
    
    public Client getClientById(Connection connection, int client_id) throws SQLException {
        Client client = new Client();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
        query.setInt(1, client_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            client.setId(rs.getInt("id"));
            client.setNom(rs.getString("nom"));
        }
        return client;
    }
}