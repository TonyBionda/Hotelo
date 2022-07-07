package projethotel.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Chambre {

    public Chambre() {
    }

    private int id;

    private String nom;

    private int type_id;
    
    private int etage_id;
    
    private int hotel_id;

    private String type_nom;
    
    private String etage_nom;
    
    private String hotel_nom;
    
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

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getEtage_id() {
        return etage_id;
    }

    public void setEtage_id(int etage_id) {
        this.etage_id = etage_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getType_nom() {
        return type_nom;
    }

    public void setType_nom(String type_nom) {
        this.type_nom = type_nom;
    }

    public String getEtage_nom() {
        return etage_nom;
    }

    public void setEtage_nom(String etage_nom) {
        this.etage_nom = etage_nom;
    }

    public String getHotel_nom() {
        return hotel_nom;
    }

    public void setHotel_nom(String hotel_nom) {
        this.hotel_nom = hotel_nom;
    }

    
    
    public Chambre(int id, String nom, int type_id, int etage_id, int hotel_id) {
        this.id = id;
        this.nom = nom;
        this.type_id = type_id;
        this.etage_id = etage_id;
        this.hotel_id = hotel_id;
    }
    
    public Chambre(int id, String nom, int type_id, int etage_id, int hotel_id, String type_nom, String etage_nom, String hotel_nom) {
        this.id = id;
        this.nom = nom;
        this.type_id = type_id;
        this.etage_id = etage_id;
        this.hotel_id = hotel_id;
        this.type_nom = type_nom;
        this.etage_nom = etage_nom;
        this.hotel_nom = hotel_nom;
    }
    
    public Chambre getChambreById(Connection connection, int chambre_id) throws SQLException {
        Chambre chambre = new Chambre();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM chambre WHERE id = ?");
        query.setInt(1, chambre_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            chambre.setId(rs.getInt("id"));
            chambre.setNom(rs.getString("nom"));
            chambre.setType_id(rs.getInt("type_id"));
            Etage etage = new Etage();
            etage = etage.getEtageById(connection, rs.getInt("etage_id"));
            chambre.setHotel_id(etage.getHotel_id());
        }
        return chambre;
    }
}
