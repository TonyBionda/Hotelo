package projethotel.view.Hotel.Reservation;

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
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import projethotel.Model.ChangeStage;
import projethotel.Model.Client;
import projethotel.Model.Employe;
import projethotel.Model.Hotel;
import projethotel.Model.Reservation;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class SejourController implements Initializable {

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
    private TableView<Reservation> list_sejour = new TableView();
    @FXML
    private StackPane stackpane = new StackPane();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void retour() throws IOException {
        ChangeStage.changeStage("reservation", stage, current_user, connection);
    }

    public void init() {
        try {
            fill_list();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fill_list() throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM reservation WHERE etat = ?");
        query.setString(1, "sejour");
        ResultSet rs = query.executeQuery();
        ObservableList<Reservation> lst = FXCollections.observableArrayList();
        while (rs.next()) {
            Client client = new Client();
            String clientNom = client.getClientById(connection, rs.getInt("client_id")).getNom();
            Chambre chambre = new Chambre();
            String chambreNom = chambre.getChambreById(connection, rs.getInt("chambre_id")).getNom();
            Hotel hotel = new Hotel();
            String hotelNom = hotel.getHotelById(connection, chambre.getChambreById(connection, rs.getInt("chambre_id")).getHotel_id()).getNom();
            lst.add(new Reservation(rs.getInt("id"), rs.getDate("dateDebut"), rs.getDate("dateFin"), rs.getInt("client_id"), rs.getInt("chambre_id"), clientNom, chambreNom, hotelNom, rs.getInt("nbPersonne"), rs.getString("etat"), rs.getDate("dateCreation"), rs.getDate("dateModification"), rs.getFloat("prix")));
        }
        fillTabWithColumn();
        list_sejour.setItems(lst);
    }

    public void fillTabWithColumn() {
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
        list_sejour.getColumns().clear();
        list_sejour.getColumns().add(id);
        list_sejour.getColumns().add(dateDebut);
        list_sejour.getColumns().add(dateFin);
        list_sejour.getColumns().add(client);
        list_sejour.getColumns().add(chambre);
        list_sejour.getColumns().add(prix);
        list_sejour.getColumns().add(nbPersonne);
        list_sejour.getColumns().add(dateCreation);
    }

    public void payer() {
        try {
            Reservation selected_reservation = list_sejour.getSelectionModel().getSelectedItem();
            if (selected_reservation == null) {
                return;
            }
            String query = "UPDATE reservation SET etat = ? WHERE id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, "finie");
            preparedStmt.setInt(2, selected_reservation.getId());
            preparedStmt.execute();
            init();
        } catch (SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void ajouter() throws IOException, SQLException {
        Reservation selected_reservation = list_sejour.getSelectionModel().getSelectedItem();
        if (selected_reservation == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sejouradd.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        SejourAddProduitController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_reservation);
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
