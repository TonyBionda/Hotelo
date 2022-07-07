package projethotel.view.Hotel.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.mail.MessagingException;
import projethotel.Model.Reservation;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;
import projethotel.Model.MailSender;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class ReservationAdd2Controller implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Connection connection;
    private Employe current_user;
    private Reservation reservation;

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setReservation(Reservation new_reservation) {
        reservation = new_reservation;
    }

    // Initialisation des objets FXML
    @FXML
    private JFXTextField field_nom, field_prenom, field_telephone, field_mail = new JFXTextField();
    @FXML
    private JFXProgressBar progress_barLeft, progress_barRight = new JFXProgressBar();
    @FXML
    private Label client_errorLeft, client_errorRight = new Label();
    @FXML
    private JFXButton validerLeft, validerRight = new JFXButton();
    @FXML
    private JFXComboBox combo_box = new JFXComboBox();
    @FXML
    private TableView<Reservation> list = new TableView();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combo_box.setOnAction(event -> {
            if (combo_box.getSelectionModel().getSelectedItem() != null && combo_box.getSelectionModel().getSelectedItem().toString() != "Client") {
                validerLeft.setDisable(false);
            }
        });
    }

    public void init() throws SQLException {
        fillTabWithColumnSuppr();
        ObservableList<String> listClient = FXCollections.observableArrayList();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM client");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            String result = rs.getInt("id") + " - " + rs.getString("nom");
            listClient.add(result);
        }
        combo_box.setItems(listClient);
        query.close();
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("reservation", stage, current_user, connection);
    }

    public void validerLeft() throws SQLException {
        String client = combo_box.getSelectionModel().getSelectedItem().toString();
        if (client == null || client == "Client") {
            return;
        }
        String client_id = new String();
        for (int i = 0; client.charAt(i) != ' '; i++) {
            client_id += client.charAt(i);
        }
        String query = " insert into reservation (dateDebut, dateFin, nbPersonne, etat, dateCreation, dateModification, prix, client_id, chambre_id)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        preparedStmt.setDate(1, reservation.getDateDebut(), java.util.Calendar.getInstance());
        preparedStmt.setDate(2, reservation.getDateFin(), java.util.Calendar.getInstance());
        preparedStmt.setInt(3, reservation.getNbPersonne());
        preparedStmt.setString(4, reservation.getEtat());
        preparedStmt.setDate(5, date, java.util.Calendar.getInstance());
        preparedStmt.setDate(6, date, java.util.Calendar.getInstance());
        preparedStmt.setFloat(7, reservation.getPrix());
        preparedStmt.setString(8, client_id);
        preparedStmt.setInt(9, reservation.getChambreID());
        preparedStmt.execute();
        progress_barLeft.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), ev -> {
            try {
                retour();
            } catch (IOException e) {
            }
        }));
        client_errorLeft.setText("Réservation comptabilisée. Retour à l'écran principal.");
        timeline.play();
    }

    public void validerRight() throws SQLException, MessagingException, UnsupportedEncodingException {
        String nom = field_nom.getText();
        String prenom = field_prenom.getText();
        String mail = field_mail.getText();
        String telephone = field_telephone.getText();
        if (nom.length() == 0 || prenom.length() == 0 || mail.length() == 0 || telephone.length() == 0) {
            client_errorRight.setText("Veuillez remplir tous les champs.");
            return;
        }
        String regex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telephone);
        if (!matcher.matches()) {
            client_errorRight.setText("Format de numéro de téléphone invalide.");
            return;
        }
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        String query = " insert into client (motdepasse, nom, prenom, mail, dateCreation, dateModification)"
                + " values (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        String motdepasse = randomPassword();
        preparedStmt.setString(1, motdepasse);
        preparedStmt.setString(2, nom);
        preparedStmt.setString(3, prenom);
        preparedStmt.setString(4, mail);
        preparedStmt.setDate(5, date);
        preparedStmt.setDate(6, date);
        preparedStmt.execute();
        preparedStmt.close();
        query = "select * from client where motdepasse = ? and nom = ? and prenom = ? and mail = ? and dateCreation = ? and dateModification = ?";
        preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, motdepasse);
        preparedStmt.setString(2, nom);
        preparedStmt.setString(3, prenom);
        preparedStmt.setString(4, mail);
        preparedStmt.setDate(5, date);
        preparedStmt.setDate(6, date);
        MailSender.SendMailClient(mail, motdepasse);
        ResultSet rs = preparedStmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            preparedStmt.close();
            progress_barRight.setVisible(true);
            String query2 = " insert into reservation (dateDebut, dateFin, nbPersonne, etat, dateCreation, dateModification, prix, client_id, chambre_id)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt2 = connection.prepareStatement(query2);
            java.sql.Date date2 = new java.sql.Date(System.currentTimeMillis());
            preparedStmt2.setDate(1, reservation.getDateDebut(), java.util.Calendar.getInstance());
            preparedStmt2.setDate(2, reservation.getDateFin(), java.util.Calendar.getInstance());
            preparedStmt2.setInt(3, reservation.getNbPersonne());
            preparedStmt2.setString(4, reservation.getEtat());
            preparedStmt2.setDate(5, date, java.util.Calendar.getInstance());
            preparedStmt2.setDate(6, date, java.util.Calendar.getInstance());
            preparedStmt2.setFloat(7, reservation.getPrix());
            preparedStmt2.setInt(8, id);
            preparedStmt2.setInt(9, reservation.getChambreID());
            preparedStmt2.execute();
            progress_barRight.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), ev -> {
                try {
                    retour();
                } catch (IOException e) {
                }
            }));
            client_errorRight.setText("Réservation comptabilisée. Retour à l'écran principal.");
            timeline.play();
        }
    }

    public void fillTabWithColumnSuppr() throws SQLException {
        TableColumn<Reservation, Date> dateDebut = new TableColumn<>();
        dateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateDebut.setText("Debut");
        dateDebut.setMinWidth(75);
        TableColumn<Reservation, Date> dateFin = new TableColumn<>();
        dateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        dateFin.setText("Fin");
        dateFin.setMinWidth(75);
        TableColumn<Reservation, String> nbPersonne = new TableColumn<>();
        nbPersonne.setCellValueFactory(new PropertyValueFactory<>("nbPersonne"));
        nbPersonne.setText("Nb");
        nbPersonne.setMinWidth(50);
        TableColumn<Reservation, String> chambre = new TableColumn<>();
        chambre.setCellValueFactory(new PropertyValueFactory<>("chambreNom"));
        chambre.setText("Chambre");
        chambre.setMinWidth(75);
        TableColumn<Reservation, Float> prix = new TableColumn<>();
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prix.setText("Prix");
        prix.setMinWidth(75);
        list.getColumns().add(dateDebut);
        list.getColumns().add(dateFin);
        list.getColumns().add(nbPersonne);
        list.getColumns().add(chambre);
        list.getColumns().add(prix);
        ObservableList<Reservation> lst = FXCollections.observableArrayList();
        lst.add(reservation);
        list.setItems(lst);
    }

    public String randomPassword() {
        int length = 10;
        String symbol = "-/.^&*_!@%=+>)";
        String cap_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String small_letter = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String finalString = cap_letter + small_letter
                + numbers + symbol;

        Random random = new Random();

        char[] password = new char[length];

        for (int i = 0; i < length; i++) {
            password[i]
                    = finalString.charAt(random.nextInt(finalString.length()));

        }
        String pswd = new String(password);
        return (pswd);
    }
}
