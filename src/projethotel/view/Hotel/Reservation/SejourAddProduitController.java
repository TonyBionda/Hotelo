package projethotel.view.Hotel.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projethotel.Model.Chambre;
import projethotel.Model.Employe;
import projethotel.Model.Type;
import projethotel.Model.Reservation;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class SejourAddProduitController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;
    private Reservation row;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setRow(Reservation new_reservation) {
        row = new_reservation;
    }

    // Initialisation des objets FXML
    @FXML
    private JFXButton valider = new JFXButton();
    @FXML
    private JFXComboBox combo_box = new JFXComboBox();
    @FXML
    private TableView<Reservation> list = new TableView();
    @FXML
    private JFXTextField quantite = new JFXTextField();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void init() {
        try {
            ObservableList<String> listProduit = FXCollections.observableArrayList();
            PreparedStatement query = connection.prepareStatement("SELECT * FROM produit");
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                String result = rs.getInt("id") + " - " + rs.getString("nom");
                listProduit.add(result);
            }
            combo_box.getItems().clear();
            combo_box.setItems(listProduit);
            query.close();
            fillTabWithColumn();
        } catch (SQLException ex) {
            Logger.getLogger(SejourAddProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ajouter() throws SQLException {
        String selected_produit = combo_box.getSelectionModel().getSelectedItem().toString();
        if (quantite.getText() == null || quantite.getText() == "Quantité" || selected_produit == null || selected_produit == "Produit" || selected_produit == "") {
            return;
        }
        String regex = "^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(quantite.getText());
        if (!matcher.matches()) {
            return;
        }
        int nbProduit = Integer.parseInt(quantite.getText());
        String produit_id = new String();
        for (int i = 0; selected_produit.charAt(i) != ' '; i++) {
            produit_id += selected_produit.charAt(i);
        }
        PreparedStatement query = connection.prepareStatement("SELECT * FROM produit where id = ?");
        query.setString(1, produit_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            Float prixTotal = rs.getFloat("prix") * nbProduit;
            row.setPrix(row.getPrix() + prixTotal);
            fillTabWithColumn();
        }
    }

    public void rembourser() throws SQLException {
        String selected_produit = combo_box.getSelectionModel().getSelectedItem().toString();
        if (quantite.getText() == null || quantite.getText() == "Quantité" || selected_produit == null || selected_produit == "Produit" || selected_produit == "") {
            return;
        }
        String regex = "^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(quantite.getText());
        if (!matcher.matches()) {
            return;
        }
        int nbProduit = Integer.parseInt(quantite.getText());
        String produit_id = new String();
        for (int i = 0; selected_produit.charAt(i) != ' '; i++) {
            produit_id += selected_produit.charAt(i);
        }
        PreparedStatement query = connection.prepareStatement("SELECT * FROM produit where id = ?");
        query.setString(1, produit_id);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            Float prixTotal = rs.getFloat("prix") * nbProduit;
            Chambre chambre = new Chambre();
            Type type = new Type();
            //On verifie que le prix initial ne peut être abaissé par un contournement du remboursement.
            chambre = chambre.getChambreById(connection, row.getChambreID());
            if (row.getPrix() - prixTotal < type.getTypeById(connection, chambre.getType_id()).getPrix()) {
                return;
            }
            row.setPrix(row.getPrix() - prixTotal);
            fillTabWithColumn();
        }
    }

    public void retour() throws IOException, SQLException {
        URL url = new File("src/projethotel/view/Hotel/Reservation/sejour.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        AnchorPane mainAnchor = (AnchorPane) loader.load();
        SejourController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);
        stage.setScene(mainScene);
        stage.show();
    }

    public void valider() {
        try {
            String query = "UPDATE reservation SET prix = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            Float prix = row.getPrix();
            preparedStmt.setFloat(1, prix);
            preparedStmt.setInt(2, row.getId());
            preparedStmt.execute();
            retour();
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void fillTabWithColumn() throws SQLException {
        TableColumn<Reservation, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Reservation, String> client = new TableColumn<>();
        client.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        client.setText("Client");
        client.setMinWidth(150);
        TableColumn<Reservation, String> chambre = new TableColumn<>();
        chambre.setCellValueFactory(new PropertyValueFactory<>("chambreNom"));
        chambre.setText("Chambre");
        chambre.setMinWidth(100);
        TableColumn<Reservation, Float> prix = new TableColumn<>();
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prix.setText("Prix");
        prix.setMinWidth(125);
        TableColumn<Reservation, Date> nbPersonne = new TableColumn<>();
        nbPersonne.setCellValueFactory(new PropertyValueFactory<>("nbPersonne"));
        nbPersonne.setText("Nb Personnes");
        nbPersonne.setMinWidth(100);
        list.getColumns().clear();
        list.getColumns().add(id);
        list.getColumns().add(client);
        list.getColumns().add(chambre);
        list.getColumns().add(prix);
        list.getColumns().add(nbPersonne);
        ObservableList<Reservation> lst = FXCollections.observableArrayList();
        lst.add(row);
        list.setItems(lst);
    }
}
