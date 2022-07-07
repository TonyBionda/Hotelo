package projethotel.view.Hotel.Dashboard;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projethotel.Model.Employe;
import projethotel.Model.ChangeStage;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class DashboardController implements Initializable {

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
    private JFXButton hotel, employe, client, etage, chambre, produit, accueil, reservation = new JFXButton();
    @FXML
    Label name, mail, id, label_hotel = new Label();
    @FXML
    VBox buttons = new VBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        java.util.List<JFXButton> lstButton = new ArrayList<>();
        lstButton.add(hotel);
        lstButton.add(employe);
        lstButton.add(accueil);
        lstButton.add(client);
        lstButton.add(etage);
        lstButton.add(reservation);
        lstButton.add(chambre);
        lstButton.add(produit);
        lstButton.forEach((value) -> {
            value.setOnAction((ActionEvent event) -> {
                try {
                    ChangeStage.changeStage(value.getId(), stage, current_user, connection);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        });
    }

    public void init() {
        name.setText(current_user.getPrenom() + " " + current_user.getNom());
        mail.setText(current_user.getMail());
        id.setText("ID : " + current_user.getId());
        switch (current_user.getRole_id()) {
            case 2:
                buttons.getChildren().removeAll(hotel, employe, etage, chambre);
                label_hotel.setText("Hôtel : " + current_user.getHotel().getNom());
                break;
            default:
                label_hotel.setText("");
        }
    }
}
