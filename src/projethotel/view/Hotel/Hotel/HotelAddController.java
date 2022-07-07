package projethotel.view.Hotel.Hotel;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class HotelAddController implements Initializable {

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
    private JFXTextField input_nom, input_adresse, input_telephone = new JFXTextField();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        input_telephone.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                valider();
            }
        });
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("hotel", stage, current_user, connection);
    }

    public void valider() {
        try {
            String nom = input_nom.getText();
            String adresse = input_adresse.getText();
            String telephone = input_telephone.getText();
            if (nom.length() == 0 || adresse.length() == 0 || telephone.length() == 0) {
                System.out.println("Faut mettre toutes les valeurs");
            } else {
                String query = " insert into hotel (nom, adresse, telephone)"
                        + " values (?, ?, ?)";
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString(1, nom);
                preparedStmt.setString(2, adresse);
                preparedStmt.setString(3, telephone);
                preparedStmt.execute();
                retour();
            }
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}
