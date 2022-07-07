package projethotel.view.Hotel.Chambre;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ChambreAddController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    // Initialisation des objets FXML
    @FXML
    private JFXTextField input_nom = new JFXTextField();
    @FXML
    private ComboBox combo_hotel, combo_type, combo_etage = new ComboBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void init() throws IOException, SQLException {
        combo_hotel.setOnAction(event -> {
            String textHotel = combo_hotel.getSelectionModel().getSelectedItem().toString();
            if (textHotel != null) {
                try {
                    ObservableList<String> listEtage = FXCollections.observableArrayList();
                    String hoteltext = combo_hotel.getSelectionModel().getSelectedItem().toString();
                    String hotel_id = new String();
                    for (int i = 0; hoteltext.charAt(i) != ' '; i++) {
                        hotel_id += hoteltext.charAt(i);
                    }
                    PreparedStatement query = connection.prepareStatement("SELECT * FROM etage e join hotel h ON e.hotel_id = h.id where e.estActif != 0 and h.estActif != 0 and h.id = ?");
                    query.setString(1, hotel_id);
                    ResultSet rs = query.executeQuery();
                    while (rs.next()) {
                        String result = "ID: " + rs.getInt("id") + " - Etage n.  " + rs.getString("nom");
                        listEtage.add(result);
                    }
                    combo_etage.setDisable(false);
                    combo_etage.getItems().clear();
                    combo_etage.setItems(listEtage);
                    query.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ChambreAddController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ObservableList<String> listHotel = FXCollections.observableArrayList();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM hotel where estActif != 0");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            String result = rs.getInt("id") + " - " + rs.getString("nom");
            listHotel.add(result);
        }
        combo_hotel.setItems(listHotel);
        query.close();
        ObservableList<String> listType = FXCollections.observableArrayList();
        query = connection.prepareStatement("SELECT * FROM type");
        rs = query.executeQuery();
        while (rs.next()) {
            String result = rs.getInt("id") + " - " + rs.getString("nom");
            listType.add(result);
        }
        query.close();
        combo_type.setItems(listType);
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("chambre", stage, current_user, connection);
    }

    public void valider() {
        try {
            String nom = input_nom.getText();
            String etagetext = combo_etage.getSelectionModel().getSelectedItem().toString();
            String etage_id = new String();
            String typetext = combo_type.getSelectionModel().getSelectedItem().toString();
            String type_id = new String();
            for (int i = 0; typetext.charAt(i) != ' '; i++) {
                type_id += typetext.charAt(i);
            }
            for (int i = 4; etagetext.charAt(i) != ' '; i++) {
                etage_id += etagetext.charAt(i);
            }
            if (nom.length() == 0 || etagetext.length() == 0 || typetext.length() == 0) {
                return;
            }
            String query = " insert into chambre (nom, type_id, etage_id)"
                    + " values (?, ?, ?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, nom);
            preparedStmt.setString(2, type_id);
            preparedStmt.setString(3, etage_id);
            preparedStmt.execute();
            retour();
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}
