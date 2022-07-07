package projethotel.view.Hotel.Employe;

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
import projethotel.Model.Employe;
import projethotel.Model.ChangeStage;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EmployeDeleteController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;
    private Employe row;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setRow(Employe new_employe) {
        row = new_employe;
    }

    // Initialisation des objets FXML
    @FXML
    private JFXButton valider = new JFXButton();
    @FXML
    private JFXCheckBox checkbox = new JFXCheckBox();
    @FXML
    private TableView<Employe> list_supprimer = new TableView();

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
        ChangeStage.changeStage("employe", stage, current_user, connection);
    }

    public void valider() throws IOException, SQLException {
            String query = "UPDATE employe SET estActif = 0 WHERE id = ? and nom = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, row.getId());
            preparedStmt.setString(2, row.getNom());
            preparedStmt.execute();
            retour();
    }

    public void fillTabWithColumnSuppr() throws SQLException {
        TableColumn<Employe, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Employe, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(110);
        TableColumn<Employe, String> prenom = new TableColumn<>();
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        prenom.setText("Prénom");
        prenom.setMinWidth(130);
        TableColumn<Employe, String> adresse = new TableColumn<>();
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        adresse.setText("Adresse");
        adresse.setMinWidth(180);
        TableColumn<Employe, String> telephone = new TableColumn<>();
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        telephone.setText("Téléphone");
        telephone.setMinWidth(150);
        TableColumn<Employe, String> mail = new TableColumn<>();
        mail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        mail.setText("Mail");
        mail.setMinWidth(150);
        TableColumn<Employe, Date> dateCreation = new TableColumn<>();
        dateCreation.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        dateCreation.setText("Date de Création");
        dateCreation.setMinWidth(115);
        TableColumn<Employe, String> dateModification = new TableColumn<>();
        dateModification.setCellValueFactory(new PropertyValueFactory<>("dateModification"));
        dateModification.setText("Date de Modification");
        dateModification.setMinWidth(125);
        TableColumn<Employe, String> role = new TableColumn<>();
        role.setCellValueFactory(new PropertyValueFactory<>("role_nom"));
        role.setText("Role");
        role.setMinWidth(125);
        TableColumn<Employe, String> hotel = new TableColumn<>();
        hotel.setCellValueFactory(new PropertyValueFactory<>("hotel_nom"));
        hotel.setText("Hotel");
        hotel.setMinWidth(125);
        list_supprimer.getColumns().clear();
        list_supprimer.getColumns().add(id);
        list_supprimer.getColumns().add(nom);
        list_supprimer.getColumns().add(prenom);
        list_supprimer.getColumns().add(hotel);
        list_supprimer.getColumns().add(role);
        list_supprimer.getColumns().add(adresse);
        list_supprimer.getColumns().add(telephone);
        list_supprimer.getColumns().add(mail);
        list_supprimer.getColumns().add(dateCreation);
        list_supprimer.getColumns().add(dateModification);
        ObservableList<Employe> lst = FXCollections.observableArrayList();
        lst.add(row);
        list_supprimer.setItems(lst);
    }
}
