package projethotel.view.Hotel.Client;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projethotel.Model.Employe;
import projethotel.Model.ChangeStage;
import projethotel.Model.Client;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ClientController implements Initializable {

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
    private JFXButton hotel, employe, chambre, dashboard, accueil, reservation, etage, produit = new JFXButton();
    @FXML
    private TableView<Client> list = new TableView();
    @FXML
    VBox buttons = new VBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        java.util.List<JFXButton> lstButton = new ArrayList<>();
        lstButton.add(dashboard);
        lstButton.add(hotel);
        lstButton.add(employe);
        lstButton.add(accueil);
        lstButton.add(reservation);
        lstButton.add(etage);
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
        try {
            fill_list();
        } catch (SQLException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (current_user.getRole_id()) {
            case 2:
                buttons.getChildren().removeAll(hotel, employe, etage, chambre);
                break;
        }
    }

    public void fill_list() throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM client");
        ResultSet rs = query.executeQuery();

        ObservableList<Client> lst = FXCollections.observableArrayList();
        while (rs.next()) {
            lst.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("telephone"), rs.getString("mail"), rs.getDate("dateCreation"), rs.getDate("dateModification")));
        }
        fillTabWithColumn();
        list.setItems(lst);
    }

    public void fillTabWithColumn() {
        TableColumn<Client, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Client, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(100);
        TableColumn<Client, String> prenom = new TableColumn<>();
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        prenom.setText("Prénom");
        prenom.setMinWidth(100);
        TableColumn<Client, String> telephone = new TableColumn<>();
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        telephone.setText("Téléphone");
        telephone.setMinWidth(135);
        TableColumn<Client, String> mail = new TableColumn<>();
        mail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        mail.setText("Mail");
        mail.setMinWidth(150);
        TableColumn<Client, Date> dateCreation = new TableColumn<>();
        dateCreation.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        dateCreation.setText("Date de Création");
        dateCreation.setMinWidth(115);
        TableColumn<Client, String> dateModification = new TableColumn<>();
        dateModification.setCellValueFactory(new PropertyValueFactory<>("dateModification"));
        dateModification.setText("Date de Modification");
        dateModification.setMinWidth(125);
        list.getColumns().clear();
        list.getColumns().add(id);
        list.getColumns().add(nom);
        list.getColumns().add(prenom);
        list.getColumns().add(telephone);
        list.getColumns().add(mail);
        list.getColumns().add(dateCreation);
        list.getColumns().add(dateModification);
    }
}
