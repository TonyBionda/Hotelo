package projethotel.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class Hotel {

    /**
     * Default constructor
     */
    public Hotel() {
    }

    /**
     *
     */
    private int id = 0;

    /**
     *
     */
    private String nom;

    /**
     *
     */
    private String adresse;

    private String telephone;
    
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public Hotel(int id, String nom, String adresse, String telephone, Boolean estActif) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.estActif = estActif;
    }
    
    public Hotel getHotelById(Connection connection, int hotel_id) throws SQLException {
        Hotel hotel = new Hotel();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM hotel WHERE id = ?");
        query.setInt(1, hotel_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            hotel.setId(rs.getInt("id"));
            hotel.setNom(rs.getString("nom"));
            hotel.setAdresse(rs.getString("adresse"));
            hotel.setTelephone(rs.getString("telephone"));
        }
        return hotel;
    }
}
