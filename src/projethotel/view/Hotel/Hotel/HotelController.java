package projethotel.view.Hotel.Hotel;

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
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;
import projethotel.Model.Hotel;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class HotelController implements Initializable {

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
    private JFXButton employe, client, chambre, dashboard, accueil, reservation, etage, produit = new JFXButton();
    @FXML
    private TableView<Hotel> list = new TableView();
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
        lstButton.add(reservation);
        lstButton.add(employe);
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
            query = connection.prepareStatement("SELECT * FROM hotel");
        } else {
            query = connection.prepareStatement("SELECT * FROM hotel where estActif != 0");
        }
        ResultSet rs = query.executeQuery();
        ObservableList<Hotel> lst = FXCollections.observableArrayList();
        while (rs.next()) {
            lst.add(new Hotel(rs.getInt("id"), rs.getString("nom"), rs.getString("adresse"), rs.getString("telephone"), true));
        }
        fillTabWithColumn();
        list.setItems(lst);
    }

    public void fillTabWithColumn() {
        TableColumn<Hotel, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Hotel, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(100);
        TableColumn<Hotel, String> adresse = new TableColumn<>();
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        adresse.setText("Adresse");
        adresse.setMinWidth(200);
        TableColumn<Hotel, String> telephone = new TableColumn<>();
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        telephone.setText("Telephone");
        telephone.setMinWidth(90);
        list.getColumns().clear();
        list.getColumns().add(id);
        list.getColumns().add(nom);
        list.getColumns().add(adresse);
        list.getColumns().add(telephone);

    }

    public void ajouter() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        HotelAddController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(parent);
        JFXDialog jfxDialog = new JFXDialog(stackpane, jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
        stackpane.setVisible(true);
        jfxDialogLayout.setStyle(parent.getStyle());
        stackpane.setOnMouseClicked(event -> {
            stackpane.setVisible(false);
            jfxDialog.close();
        });
        jfxDialog.show();
    }

    public void supprimer() throws IOException, SQLException {
        Hotel selected_hotel = list.getSelectionModel().getSelectedItem();
        if (selected_hotel == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        HotelDeleteController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_hotel);
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
        Hotel selected_hotel = list.getSelectionModel().getSelectedItem();
        if (selected_hotel == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        HotelModifyController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_hotel);
        controller.init();
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(parent);
        JFXDialog jfxDialog = new JFXDialog(stackpane, jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
        stackpane.setVisible(true);
        jfxDialogLayout.setStyle(parent.getStyle());
        stackpane.setOnMouseClicked(event -> {
            stackpane.setVisible(false);
            jfxDialog.close();
        });
        jfxDialog.show();
    }

    public void restaurer() throws SQLException {
        Hotel selected_hotel = list.getSelectionModel().getSelectedItem();
        if (selected_hotel == null) {
            return;
        }
        String query = "UPDATE hotel SET estActif = 1 WHERE id = ? and nom = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1, selected_hotel.getId());
        preparedStmt.setString(2, selected_hotel.getNom());
        preparedStmt.execute();
        init();
    }
}
