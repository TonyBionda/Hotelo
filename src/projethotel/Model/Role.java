package projethotel.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 */
public class Role {

    public Role() {
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

    public Role(int id, String nom, String adresse, String telephone) {
        this.id = id;
        this.nom = nom;
    }

    public Role getRoleById(Connection connection, int role_id) throws SQLException {
        Role role = new Role();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM role WHERE id = ?");
        query.setInt(1, role_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            role.setId(rs.getInt("id"));
            role.setNom(rs.getString("nom"));
        }
        return role;
    }

}
