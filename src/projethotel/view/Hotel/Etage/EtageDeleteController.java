package projethotel.view.Hotel.Etage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;
import projethotel.Model.Etage;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EtageDeleteController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;
    private Etage row;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setRow(Etage new_hotel) {
        row = new_hotel;
    }
    
    // Initialisation des objets FXML
    @FXML
    private JFXButton valider = new JFXButton();
    @FXML
    private JFXCheckBox checkbox = new JFXCheckBox();
    @FXML
    private TableView<Etage> list_supprimer = new TableView();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkbox.setOnAction(event -> {
            if (checkbox.isSelected()) {
                valider.setDisable(false);
            } else {
                valider.setDisable(true);
            }
        });
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("etage", stage, current_user, connection);
    }

    public void valider() {
        try {
            String query = "UPDATE etage SET estActif = 0 WHERE id = ? and nom = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, row.getId());
            preparedStmt.setString(2, row.getNom());
            preparedStmt.execute();
            retour();
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void fillTabWithColumnSuppr() throws SQLException {
        TableColumn<Etage, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Etage, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(110);
        TableColumn<Etage, String> hotel = new TableColumn<>();
        hotel.setCellValueFactory(new PropertyValueFactory<>("hotel_nom"));
        hotel.setText("Hotel");
        hotel.setMinWidth(125);
        list_supprimer.getColumns().clear();
        list_supprimer.getColumns().add(id);
        list_supprimer.getColumns().add(nom);
        list_supprimer.getColumns().add(hotel);
        ObservableList<Etage> lst = FXCollections.observableArrayList();
        lst.add(row);
        list_supprimer.setItems(lst);
    }
}
