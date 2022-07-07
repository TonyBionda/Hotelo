package projethotel.view.Hotel.Employe;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;
import projethotel.Model.Hotel;
import projethotel.Model.Role;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EmployeController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;
    private Boolean testCheck = false;

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
    private JFXButton hotel, client, chambre, dashboard, accueil, reservation, etage, produit = new JFXButton();
    @FXML
    private TableView<Employe> list = new TableView();
    @FXML
    private StackPane stackpane = new StackPane();
    @FXML
    private Label restaurer = new Label();
    @FXML
    private JFXCheckBox checkbox = new JFXCheckBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkbox.setOnAction(event -> {
            if (checkbox.isSelected()) {
                testCheck = true;
                init();
            } else {
                testCheck = false;
                init();
            }
        });
        java.util.List<JFXButton> lstButton = new ArrayList<>();
        lstButton.add(dashboard);
        lstButton.add(hotel);
        lstButton.add(reservation);
        lstButton.add(accueil);
        lstButton.add(client);
        lstButton.add(etage);
        lstButton.add(chambre);
        lstButton.add(produit);
        lstButton.forEach((value) -> {
            value.setOnAction((ActionEvent event) -> {
                try {
                    ChangeStage.changeStage(value.getId(), stage, current_user, connection);
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
        });
    }

    public void init() {
        try {
            if (testCheck) {
                restaurer.setVisible(true);
            } else {
                restaurer.setVisible(false);
            }
            fill_list();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fill_list() throws SQLException {
        PreparedStatement query;
        if (testCheck) {
            query = connection.prepareStatement("SELECT * FROM employe");
        } else {
            query = connection.prepareStatement("SELECT * FROM employe e JOIN hotel h ON h.id = e.hotel_id WHERE h.estActif = 1 and e.estActif = 1");
        }
        ResultSet rs = query.executeQuery();

        ObservableList<Employe> lst = FXCollections.observableArrayList();
        while (rs.next()) {
            Hotel hotel = new Hotel();
            hotel = hotel.getHotelById(connection, rs.getInt("hotel_id"));
            Role role = new Role();
            role = role.getRoleById(connection, rs.getInt("role_id"));
            lst.add(new Employe(rs.getInt("id"), rs.getString("nom"), rs.getString("motdepasse"), rs.getString("prenom"), rs.getString("adresse"), rs.getString("telephone"), rs.getString("mail"), rs.getBoolean("estActif"), rs.getDate("dateCreation"), rs.getDate("dateModification"), null, rs.getInt("hotel_id"), hotel.getNom(), null, rs.getInt("role_id"), role.getNom()));
        }
        fillTabWithColumn();
        list.setItems(lst);
    }

    public void fillTabWithColumn() {
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
        list.getColumns().clear();
        list.getColumns().add(id);
        list.getColumns().add(nom);
        list.getColumns().add(prenom);
        list.getColumns().add(hotel);
        list.getColumns().add(role);
        list.getColumns().add(adresse);
        list.getColumns().add(telephone);
        list.getColumns().add(mail);
        list.getColumns().add(dateCreation);
        list.getColumns().add(dateModification);
    }

    public void ajouter() throws IOException, SQLException {
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        EmployeAddController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(parent);
        JFXDialog jfxDialog = new JFXDialog(stackpane, jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
        jfxDialogLayout.setStyle(parent.getStyle());
        parent.setVisible(true);
        stackpane.setOnMouseClicked(event -> {
            stackpane.setVisible(false);
            jfxDialog.close();
        });
        jfxDialog.show();
    }

    public void supprimer() throws IOException, SQLException {
        Employe selected_employe = list.getSelectionModel().getSelectedItem();
        if (selected_employe == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        EmployeDeleteController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_employe);
        controller.fillTabWithColumnSuppr();
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(parent);
        JFXDialog jfxDialog = new JFXDialog(stackpane, jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
        jfxDialogLayout.setStyle(parent.getStyle());
        parent.setVisible(true);
        stackpane.setOnMouseClicked(event -> {
            stackpane.setVisible(false);
            jfxDialog.close();
        });
        jfxDialog.show();
    }

    public void modifier() throws IOException, SQLException {
            Employe selected_employe = list.getSelectionModel().getSelectedItem();
            if (selected_employe == null) {
                return;
            }
            stackpane.setVisible(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify.fxml"));
            AnchorPane parent = (AnchorPane) loader.load();
            parent.setVisible(false);
            EmployeModifyController controller = loader.getController();
            controller.setStage(stage);
            controller.setEmploye(current_user);
            controller.setConnection(connection);
            controller.setRow(selected_employe);
            controller.init();
            controller.fillTabWithColumnSuppr();
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            jfxDialogLayout.setBody(parent);
            JFXDialog jfxDialog = new JFXDialog(stackpane, jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
            jfxDialogLayout.setStyle(parent.getStyle());
            parent.setVisible(true);
            stackpane.setOnMouseClicked(event -> {
                stackpane.setVisible(false);
                jfxDialog.close();
            });
            jfxDialog.show();
    }
    
    public void restaurer() throws SQLException {
        Employe selected_employe = list.getSelectionModel().getSelectedItem();
        if (selected_employe == null) {
            return;
        }
        String query = "UPDATE employe SET estActif = 1 WHERE id = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1, selected_employe.getId());
        preparedStmt.execute();
        init();
    }
}
