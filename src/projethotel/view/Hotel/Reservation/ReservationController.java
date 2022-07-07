package projethotel.view.Hotel.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import projethotel.Model.Employe;
import projethotel.Model.Chambre;
import projethotel.Model.ChangeStage;
import projethotel.Model.Hotel;
import projethotel.Model.Reservation;
import projethotel.Model.Client;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ReservationController implements Initializable {

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
    private JFXButton hotel, employe, client, chambre, dashboard, accueil, etage, produit = new JFXButton();
    @FXML
    private JFXSpinner spinner = new JFXSpinner();
    @FXML
    private TableView<Reservation> list = new TableView();
    @FXML
    private StackPane stackpane = new StackPane();
    @FXML
    private JFXCheckBox checkbox = new JFXCheckBox();
    @FXML
    VBox buttons = new VBox();

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
            fill_list();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        switch (current_user.getRole_id()) {
            case 2:
                buttons.getChildren().removeAll(hotel, employe, etage, chambre);
                break;
        }
    }

    public void fill_list() throws SQLException {
        PreparedStatement query;
        if (testCheck) {
            query = connection.prepareStatement("SELECT * FROM reservation");
        } else {
            query = connection.prepareStatement("SELECT * FROM reservation where etat = ?");
            query.setString(1, "reservation");
        }
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
        list.setItems(lst);
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
        list.getColumns().clear();
        list.getColumns().add(id);
        list.getColumns().add(dateDebut);
        list.getColumns().add(dateFin);
        list.getColumns().add(client);
        list.getColumns().add(chambre);
        list.getColumns().add(prix);
        list.getColumns().add(nbPersonne);
        list.getColumns().add(dateCreation);
    }

    //Reservation Add
    public void ajouter() throws IOException, SQLException {
        URL url = new File("src/projethotel/view/Hotel/Reservation/add.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        AnchorPane mainAnchor = (AnchorPane) loader.load();
        ReservationAddController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        spinner.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), ev -> {
            stage.setScene(mainScene);
            stage.show();
        }));
        timeline.play();
    }

    public void supprimer() throws IOException, SQLException {
        Reservation selected_reservation = list.getSelectionModel().getSelectedItem();
        if (selected_reservation == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        ReservationDeleteController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_reservation);
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

    public void reservationToSejour() throws IOException, SQLException {
        Reservation selected_reservation = list.getSelectionModel().getSelectedItem();
        if (selected_reservation == null) {
            return;
        }
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("restosejour.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        ReservationToSejourController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setRow(selected_reservation);
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
    
    public void sejour() throws IOException, SQLException {
        URL url = new File("src/projethotel/view/Hotel/Reservation/sejour.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        AnchorPane mainAnchor = (AnchorPane) loader.load();
        SejourController controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        spinner.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), ev -> {
            stage.setScene(mainScene);
            stage.show();
        }));
        timeline.play();
    }
}
