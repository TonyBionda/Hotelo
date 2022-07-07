package projethotel.view.Hotel.Etage;

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
import projethotel.Model.Employe;
import projethotel.Model.Etage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Hotel;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EtageController implements Initializable {

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
    private JFXButton hotel, employe, client, chambre, dashboard, accueil, reservation, produit = new JFXButton();
    @FXML
    private TableView<Etage> list = new TableView();
    @FXML
    private Label restaurer = new Label();
    @FXML
    private StackPane stackpane = new StackPane();
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
        lstButton.add(employe);
        lstButton.add(accueil);
        lstButton.add(client);
        lstButton.add(reservation);
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
            query = connection.prepareStatement("SELECT * FROM etage");
        } else {
            query = connection.prepareStatement("SELECT * FROM etage e JOIN hotel h ON h.id = e.hotel_id WHERE h.estActif = 1 and e.estActif = 1");
        }
        ResultSet rs = query.executeQuery();

        ObservableList<Etage> lst = FXCollections.observableArrayList();
        while (rs.next()) {
            Hotel hotel = new Hotel();
            hotel = hotel.getHotelById(connection, rs.getInt("hotel_id"));
            lst.add(new Etage(rs.getInt("id"), rs.getString("nom"), rs.getBoolean("estActif"), rs.getInt("hotel_id"), hotel.getNom()));
        }
        fillTabWithColumn();
        list.setItems(lst);
    }

    public void fillTabWithColumn() {
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
        list.getColumns().clear();
        list.getColumns().add(id);
        list.getColumns().add(nom);
        list.getColumns().add(hotel);
    }

    public void ajouter() throws IOException, SQLException {
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        EtageAddController controller = loader.getController();
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
        Etage selected_etage = list.getSelectionModel().getSelectedItem();
        if (selected_etage == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        EtageDeleteController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_etage);
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
        Etage selected_etage = list.getSelectionModel().getSelectedItem();
        if (selected_etage == null) {
            return;
        }
        String query = "UPDATE etage SET estActif = 1 WHERE id = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1, selected_etage.getId());
        preparedStmt.execute();
        init();
    }

    public void modifier() throws IOException, SQLException {
        Etage selected_etage = list.getSelectionModel().getSelectedItem();
        if (selected_etage == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        EtageModifyController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_etage);
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
}
