package projethotel.view.Hotel.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
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
import projethotel.Model.Reservation;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ReservationToSejourController implements Initializable {

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
    private JFXCheckBox checkbox = new JFXCheckBox();
    @FXML
    private TableView<Reservation> list_supprimer = new TableView();

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
        ChangeStage.changeStage("reservation", stage, current_user, connection);
    }

    public void valider() {
        try {
            String query = "UPDATE reservation SET etat = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, "sejour");
            preparedStmt.setInt(2, row.getId());
            preparedStmt.execute();
            retour();
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void fillTabWithColumnSuppr() throws SQLException {
        TableColumn<Reservation, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Reservation, Date> dateDebut = new TableColumn<>();
        dateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateDebut.setText("Date Début");
        dateDebut.setMinWidth(125);
        TableColumn<Reservation, Date> dateFin = new TableColumn<>();
        dateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        dateFin.setText("Date Fin");
        dateFin.setMinWidth(125);
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
        TableColumn<Reservation, Date> dateCreation = new TableColumn<>();
        dateCreation.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        dateCreation.setText("Date Reservation");
        dateCreation.setMinWidth(125);
        list_supprimer.getColumns().clear();
        list_supprimer.getColumns().add(id);
        list_supprimer.getColumns().add(dateDebut);
        list_supprimer.getColumns().add(dateFin);
        list_supprimer.getColumns().add(client);
        list_supprimer.getColumns().add(chambre);
        list_supprimer.getColumns().add(prix);
        list_supprimer.getColumns().add(nbPersonne);
        list_supprimer.getColumns().add(dateCreation);
        ObservableList<Reservation> lst = FXCollections.observableArrayList();
        lst.add(row);
        list_supprimer.setItems(lst);
    }
}
