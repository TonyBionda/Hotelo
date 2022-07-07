package projethotel.Model;

public class Produit {

    /**
     * Default constructor
     */
    public Produit() {
    }

    private int id;
    private String nom;
    private float prix;
    private String hotel_nom;
    private Boolean estActif;
    private int hotel_id;

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

    public String getHotel_nom() {
        return hotel_nom;
    }

    public void setHotel_nom(String hotel_nom) {
        this.hotel_nom = hotel_nom;
    }

    public Boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Produit(int id, String nom, float prix, Boolean estActif, int hotel_id, String hotel_nom) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.hotel_nom = hotel_nom;
        this.estActif = estActif;
        this.hotel_id = hotel_id;
    }
}
