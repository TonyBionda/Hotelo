package projethotel.view.Hotel.Etage;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EtageAddController implements Initializable {

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
    private AnchorPane anchor;
    @FXML
    private JFXTextField input_nom = new JFXTextField();
    @FXML
    private ComboBox combo_hotel = new ComboBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void init() throws SQLException {
        ObservableList<String> listHotel = FXCollections.observableArrayList();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM hotel where estActif != 0");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            String result = rs.getInt("id") + " - " + rs.getString("nom");
            listHotel.add(result);
        }
        combo_hotel.setItems(listHotel);
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("etage", stage, current_user, connection);
    }

    public void valider() {
        try {
            String nom = input_nom.getText();
            String hoteltext = combo_hotel.getSelectionModel().getSelectedItem().toString();
            String hotel_id = new String();
            for (int i = 0; hoteltext.charAt(i) != ' '; i++) {
                hotel_id += hoteltext.charAt(i);
            }
            Integer.parseInt(hotel_id);
            if (nom.length() == 0 || hoteltext.length() == 0) {
                System.out.println("Faut mettre toutes les valeurs");
            } else {
                String query = " insert into etage (nom, estActif, hotel_id)"
                        + " values (?, ?, ?)";
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString(1, nom);
                preparedStmt.setBoolean(2, true);
                preparedStmt.setString(3, hotel_id);
                preparedStmt.execute();
                retour();
            }
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}
