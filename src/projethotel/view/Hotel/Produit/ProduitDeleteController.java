package projethotel.view.Hotel.Produit;

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
import projethotel.Model.Produit;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ProduitDeleteController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;
    private Produit row;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setRow(Produit new_produit) {
        row = new_produit;
    }
    
    // Initialisation des objets FXML
    @FXML
    private JFXButton valider = new JFXButton();
    @FXML
    private JFXCheckBox checkbox = new JFXCheckBox();
    @FXML
    private TableView<Produit> list_supprimer = new TableView();

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
        ChangeStage.changeStage("produit", stage, current_user, connection);
    }

    public void valider() {
        try {
            String query = "UPDATE produit SET estActif = 0 WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, row.getId());
            preparedStmt.execute();
            retour();
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void fillTabWithColumnSuppr() throws SQLException {
        TableColumn<Produit, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Produit, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(110);
        TableColumn<Produit, Float> prix = new TableColumn<>();
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prix.setText("prix");
        prix.setMinWidth(75);
        TableColumn<Produit, String> hotel = new TableColumn<>();
        hotel.setCellValueFactory(new PropertyValueFactory<>("hotel_nom"));
        hotel.setText("Hotel");
        hotel.setMinWidth(125);
        list_supprimer.getColumns().clear();
        list_supprimer.getColumns().add(id);
        list_supprimer.getColumns().add(nom);
        list_supprimer.getColumns().add(prix);
        list_supprimer.getColumns().add(hotel);
        ObservableList<Produit> lst = FXCollections.observableArrayList();
        lst.add(row);
        list_supprimer.setItems(lst);
    }
}
