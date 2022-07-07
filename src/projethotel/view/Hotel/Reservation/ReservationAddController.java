package projethotel.view.Hotel.Reservation;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import projethotel.Model.Employe;
import projethotel.Model.Type;
import projethotel.Model.Chambre;
import projethotel.Model.ChangeStage;
import projethotel.Model.Etage;
import projethotel.Model.Hotel;
import projethotel.Model.Reservation;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ReservationAddController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;

    private boolean datePicked = false;
    private String selected_hotel = new String();
    private Date dateA, dateB;
    private Type selected_type;
    private Float nombre;
    private Float days;

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
    private JFXComboBox combo_nombre = new JFXComboBox();
    @FXML
    private JFXComboBox combo_hotel = new JFXComboBox();
    @FXML
    private JFXDatePicker date1, date2;
    @FXML
    private TableView<Type> list_type = new TableView();
    @FXML
    private TableView<Chambre> list_chambre = new TableView();
    @FXML
    private JFXSpinner spinner = new JFXSpinner();
    @FXML
    private StackPane stackpane = new StackPane();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void init() throws SQLException {
        list_type.setOnMouseClicked(value -> {
            if (list_type.getItems().isEmpty() || list_type.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            try {
                fill_listChambre();
            } catch (SQLException e) {
            }
        });
        date1.setOnAction(event -> {
            if (date1.getValue() != null) {
                if (date2.getValue() != null) {
                    if (date1.getValue().compareTo(date2.getValue()) >= 0) {
                        date2.setValue(null);
                    }
                }
                datePicked = true;
                //Ici et en dessous, on est obligé de décaler l'appel récursif du init() car sinon la fenetre du DatePicker ne se ferme pas, car le init() se lance trop rapidement.
                //Du coup ça fait buguer un peu le visuel, rien de grave mais c'est laid. Mettre un tout petit timer de 0.01sec semble faire l'affaire!
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), ev -> {
                    try {
                        init();
                    } catch (SQLException e) {
                    }
                }));
                timeline.play();
            } else {
                datePicked = false;
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), ev -> {
                    try {
                        init();
                    } catch (SQLException e) {
                    }
                }));
                timeline.play();
            }
        });

        if (datePicked) {
            date2.setDisable(false);
            if (date2.getValue() == null) {
                date2.setValue(date1.getValue().plusDays(1));
            }
            date2.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = date1.getValue();
                    setDisable(empty || date.compareTo(today) < 1);
                }
            });
        } else {
            date2.setDisable(true);
        }
        date1.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        if (combo_hotel.getSelectionModel().getSelectedItem() == null) {
            combo_hotel.getItems().clear();
            if (current_user.getRole_id() != 2) {
                ObservableList<String> listHotel = FXCollections.observableArrayList();
                PreparedStatement query = connection.prepareStatement("SELECT * FROM hotel where estActif != 0");
                ResultSet rs = query.executeQuery();
                while (rs.next()) {
                    String result = rs.getInt("id") + " - " + rs.getString("nom");
                    listHotel.add(result);
                }
                combo_hotel.setItems(listHotel);
                query.close();
            } else {
                ObservableList<String> listHotel = FXCollections.observableArrayList();
                String string = String.valueOf(current_user.getHotel_id()) + " - " + current_user.getHotel_nom();
                listHotel.add(string);
                combo_hotel.setItems(listHotel);
                combo_hotel.setValue(string);
                combo_hotel.setDisable(true);
            }
        }
        if (combo_nombre.getSelectionModel().getSelectedItem() == null) {
            combo_nombre.getItems().clear();
            combo_nombre.getItems().addAll(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5"
            );
        }
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("reservation", stage, current_user, connection);
    }

    public void rechercher() throws SQLException {
        nombre = Float.parseFloat(combo_nombre.getSelectionModel().getSelectedItem().toString());
        selected_hotel = combo_hotel.getSelectionModel().getSelectedItem().toString();
        if (date1.getValue() != null && date2.getValue() != null && nombre != null && selected_hotel != null) {
            dateA = Date.valueOf(date1.getValue());
            dateB = Date.valueOf(date2.getValue());
            list_chambre.getItems().clear();
            fill_listType(nombre, date1.getValue(), date2.getValue());
        }
    }

    public void fill_listType(Float nombre, LocalDate date1, LocalDate date2) throws SQLException {
        PreparedStatement query = connection.prepareStatement("SELECT * FROM type where nbPersonneMax >= ?");
        query.setInt(1, Math.round(nombre));
        ResultSet rs = query.executeQuery();
        ObservableList<Type> lst = FXCollections.observableArrayList();
        long longDays = java.time.temporal.ChronoUnit.DAYS.between(date1, date2);
        days = (float) longDays;
        while (rs.next()) {
            lst.add(new Type(rs.getInt("id"), rs.getString("nom"), rs.getFloat("prix") * days));
        }
        query.close();
        fill_listTypeTabWithColumn();
        list_type.setItems(lst);
    }

    public void fill_listTypeTabWithColumn() {
        TableColumn<Type, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Type, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(110);
        TableColumn<Type, Float> prix = new TableColumn<>();
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prix.setText("Prix total");
        prix.setMinWidth(130);
        list_type.getColumns().clear();
        list_type.getColumns().add(id);
        list_type.getColumns().add(nom);
        list_type.getColumns().add(prix);
    }

    public void fill_listChambre() throws SQLException {
        if (selected_type == list_type.getSelectionModel().getSelectedItem()) {
            return;
        }
        selected_type = list_type.getSelectionModel().getSelectedItem();
        if (selected_type == null) {
            return;
        }
        String hotel_id = new String();
        for (int i = 0; selected_hotel.charAt(i) != ' '; i++) {
            hotel_id += selected_hotel.charAt(i);
        }
        PreparedStatement query = connection.prepareStatement("SELECT * FROM chambre c join etage e "
                + "where c.etage_id = e.id and e.hotel_id = ? and c.type_id = ? and c.id not in "
                + "(select chambre_id from reservation r "
                + "where (r.dateDebut <= ? and r.dateFin > ?) or (r.dateDebut < ? and r.dateFin >= ?)  or  (r.dateDebut >= ? and r.dateFin <= ?)) ");
        query.setString(1, hotel_id); //Ca converti le String en Int dans la requete
        query.setInt(2, selected_type.getId());
        query.setDate(3, dateA, java.util.Calendar.getInstance());
        query.setDate(4, dateA, java.util.Calendar.getInstance());
        query.setDate(5, dateB, java.util.Calendar.getInstance());
        query.setDate(6, dateB, java.util.Calendar.getInstance());
        query.setDate(7, dateA, java.util.Calendar.getInstance());
        query.setDate(8, dateB, java.util.Calendar.getInstance());
        ResultSet rs = query.executeQuery();
        ObservableList<Chambre> lst = FXCollections.observableArrayList();
        Etage etage = new Etage();
        Hotel hotel = new Hotel();
        while (rs.next()) {
            lst.add(new Chambre(rs.getInt("id"), rs.getString("nom"), rs.getInt("type_id"), rs.getInt("etage_id"), Integer.parseInt(hotel_id), selected_type.getNom(), etage.getEtageById(connection, rs.getInt("etage_id")).getNom(), hotel.getHotelById(connection, Integer.parseInt(hotel_id)).getNom()));
        }
        spinner.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), ev -> {
            spinner.setVisible(false);
            fill_listChambreTabWithColumn();
            list_chambre.setItems(lst);
        }));
        timeline.play();
    }

    public void fill_listChambreTabWithColumn() {
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
        list_chambre.getColumns().clear();
        list_chambre.getColumns().add(id);
        list_chambre.getColumns().add(nom);
        list_chambre.getColumns().add(etage);
        list_chambre.getColumns().add(hotel);
        list_chambre.getColumns().add(type);
    }

    public void reserver() throws IOException, SQLException {
        Chambre selected_chambre = list_chambre.getSelectionModel().getSelectedItem();
        if (selected_chambre == null) {
            return;
        }
        Reservation new_reservation = new Reservation();
        new_reservation.setDateDebut(dateA);
        new_reservation.setDateFin(dateB);
        int intNombre = Math.round(nombre);
        new_reservation.setNbPersonne(intNombre);
        new_reservation.setEtat("reservation");
        new_reservation.setPrix(selected_type.getPrix());
        new_reservation.setChambreID(selected_chambre.getId());
        new_reservation.setChambreNom(selected_chambre.getNom());
        
        stackpane.setVisible(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add2.fxml"));
        AnchorPane parent = (AnchorPane) loader.load();
        parent.setVisible(false);
        ReservationAdd2Controller controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.setReservation(new_reservation);
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
