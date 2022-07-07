package projethotel.view.Hotel.Chambre;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import projethotel.Model.Chambre;
import projethotel.Model.Employe;
import projethotel.Model.Etage;
import projethotel.Model.ChangeStage;
import projethotel.Model.Hotel;
import projethotel.Model.Type;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ChambreController implements Initializable {

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
    private JFXButton hotel, employe, client, produit, dashboard, accueil, reservation, etage = new JFXButton();
    @FXML
    private TableView<Chambre> list = new TableView();
    @FXML
    private StackPane stackpane = new StackPane();

    @FXML

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
        lstButton.add(client);
        lstButton.add(reservation);
        lstButton.add(etage);
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
            fill_list();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fill_list() throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM chambre c join etage e join hotel h ON c.etage_id = e.id and e.hotel_id = h.id where e.estActif != 0 and h.estActif != 0");
        ResultSet rs = query.executeQuery();

        ObservableList<Chambre> lst = FXCollections.observableArrayList();
        Etage etage = new Etage();
        Hotel hotel = new Hotel();
        Type type = new Type();
        while (rs.next()) {
            lst.add(new Chambre(rs.getInt("id"), rs.getString("nom"), rs.getInt("type_id"), rs.getInt("etage_id"), etage.getEtageById(connection, rs.getInt("etage_id")).getHotel_id(), type.getTypeById(connection, rs.getInt("type_id")).getNom(), etage.getEtageById(connection, rs.getInt("etage_id")).getNom(), hotel.getHotelById(connection, etage.getEtageById(connection, rs.getInt("etage_id")).getHotel_id()).getNom()));
        }
        fillTabWithColumn();
        list.setItems(lst);
    }

    public void fillTabWithColumn() {
        TableColumn<Chambre, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Chambre, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(110);
        TableColumn<Chambre, String> etage = new TableColumn<>();
        etage.setCellValueFactory(new PropertyValueFactory<>("etage_nom"));
        etage.setText("Etage");
        etage.setMinWidth(110);
        TableColumn<Chambre, String> hotel = new TableColumn<>();
        hotel.setCellValueFactory(new PropertyValueFactory<>("hotel_nom"));
        hotel.setText("Hotel");
        hotel.setMinWidth(110);
        TableColumn<Chambre, String> type = new TableColumn<>();
        type.setCellValueFactory(new PropertyValueFactory<>("type_nom"));
        type.setText("Type");
        type.setMinWidth(140);
        list.getColumns().add(id);
        list.getColumns().add(nom);
        list.getColumns().add(etage);
        list.getColumns().add(hotel);
        list.getColumns().add(type);
    }

    public void ajouter() throws IOException, SQLException {
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        ChambreAddController controller = loader.getController();
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
        Chambre selected_chambre = list.getSelectionModel().getSelectedItem();
        if (selected_chambre == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        ChambreDeleteController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_chambre);
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

    public void modifier() throws SQLException, IOException {
        Chambre selected_chambre = list.getSelectionModel().getSelectedItem();
        if (selected_chambre == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        ChambreModifyController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_chambre);
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
