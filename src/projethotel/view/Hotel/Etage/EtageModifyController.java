package projethotel.view.Hotel.Etage;

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
import projethotel.Model.Etage;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EtageModifyController implements Initializable {

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

    public void setRow(Etage new_etage) {
        row = new_etage;
    }

    // Initialisation des objets FXML
    @FXML
    private JFXTextField input_nom = new JFXTextField();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        input_nom.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                valider();
            }
        });
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("etage", stage, current_user, connection);
    }

    public void valider() {
        try {
            String query = "UPDATE hotel SET nom = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            String nom_modif = input_nom.getText();
            preparedStmt.setString(1, nom_modif);
            preparedStmt.setInt(2, row.getId());
            preparedStmt.execute();
            retour();
        } catch (IOException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void init() throws SQLException {
        input_nom.setText(row.getNom());
    }
}
