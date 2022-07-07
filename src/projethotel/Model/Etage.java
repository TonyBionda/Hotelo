package projethotel.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Etage {

    public Etage() {
    }

    private int id;

    private String nom;

    private int hotel_id;

    private String hotel_nom;

    private Boolean estActif;

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

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public String getHotel_nom() {
        return hotel_nom;
    }

    public void setHotel_nom(String hotel_nom) {
        this.hotel_nom = hotel_nom;
    }

    public Etage(int id, String nom, Boolean estActif, int hotel_id, String hotel_nom) {
        this.id = id;
        this.nom = nom;
        this.hotel_id = hotel_id;
        this.hotel_nom = hotel_nom;
        this.estActif = estActif;
    }

    public Etage getEtageById(Connection connection, int etage_id) throws SQLException {
        Etage etage = new Etage();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM etage WHERE id = ?");
        query.setInt(1, etage_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            etage.setId(rs.getInt("id"));
            etage.setNom(rs.getString("nom"));
            etage.setHotel_id(rs.getInt("hotel_id"));
        }
        return etage;
    }
}
