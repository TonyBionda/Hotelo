package projethotel.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Type {

    public Type() {
    }

    private int id;

    private String nom;

    private float prix;

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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Type(int id, String nom, float prix) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }
    
    public Type getTypeById(Connection connection, int type_id) throws SQLException {
        Type type = new Type();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM type WHERE id = ?");
        query.setInt(1, type_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            type.setId(rs.getInt("id"));
            type.setNom(rs.getString("nom"));
            System.out.println(rs.getFloat("prix"));
            type.setPrix(rs.getFloat("prix"));
        }
        return type;
    }
}
