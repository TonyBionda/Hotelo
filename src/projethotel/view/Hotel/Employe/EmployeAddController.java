package projethotel.view.Hotel.Employe;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import projethotel.Model.ChangeStage;
import projethotel.Model.Employe;
import projethotel.Model.MailSender;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class EmployeAddController implements Initializable {

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
    private JFXTextField input_nom, input_prenom, input_adresse, input_telephone, input_mail = new JFXTextField();
    @FXML
    private ComboBox combo_hotel, combo_role = new ComboBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        input_telephone.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    valider();
                } catch (MessagingException | SQLException | IOException ex) {
                    Logger.getLogger(EmployeAddController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void init() throws SQLException {
        ObservableList<String> listHotel = FXCollections.observableArrayList();
        PreparedStatement query = connection.prepareStatement("SELECT * FROM hotel where estActif != 0");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            String result = rs.getInt("id") + " - " + rs.getString("nom");
            listHotel.add(result);
        }
        combo_hotel.setItems(listHotel);
        ObservableList<String> listRole = FXCollections.observableArrayList();
        PreparedStatement query2 = connection.prepareStatement("SELECT * FROM role");
        ResultSet rs2 = query2.executeQuery();
        while (rs2.next()) {
            String result2 = rs2.getInt("id") + " - " + rs2.getString("nom");
            listRole.add(result2);
        }
        combo_role.setItems(listRole);
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("employe", stage, current_user, connection);
    }

    public void valider() throws MessagingException, SQLException, UnsupportedEncodingException, IOException {
        String nom = input_nom.getText();
        String prenom = input_prenom.getText();
        String adresse = input_adresse.getText();
        String telephone = input_telephone.getText();
        String mail = input_mail.getText();
        String hoteltext = combo_hotel.getSelectionModel().getSelectedItem().toString();
        String roletext = combo_role.getSelectionModel().getSelectedItem().toString();
        String hotel_id = new String();
        for (int i = 0; hoteltext.charAt(i) != ' '; i++) {
            hotel_id += hoteltext.charAt(i);
        }
        Integer.parseInt(hotel_id);
        String role_id = new String();
        for (int j = 0; roletext.charAt(j) != ' '; j++) {
            role_id += roletext.charAt(j);
        }
        Integer.parseInt(role_id);
        System.out.println(hotel_id + " <- / -> " + role_id);
        if (nom.length() == 0 || adresse.length() == 0 || telephone.length() == 0) {
            System.out.println("Faut mettre toutes les valeurs");
        } else {
            String query = " insert into employe (motdepasse, nom, prenom, adresse, telephone, mail, estActif, dateCreation, dateModification, hotel_id, role_id)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            String motdepasse = randomPassword();
            System.out.println(motdepasse);
            Date date = new Date(System.currentTimeMillis());
            preparedStmt.setString(1, motdepasse);
            preparedStmt.setString(2, nom);
            preparedStmt.setString(3, prenom);
            preparedStmt.setString(4, adresse);
            preparedStmt.setString(5, telephone);
            preparedStmt.setString(6, mail);
            preparedStmt.setInt(7, 1);
            preparedStmt.setDate(8, date);
            preparedStmt.setDate(9, date);
            preparedStmt.setString(10, hotel_id);
            preparedStmt.setString(11, role_id);
            preparedStmt.execute();
            MailSender.SendMailEmploye(mail, motdepasse);
            retour(); //C'est juste pour revenir en arrière, à l'onglet employé.
        }
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
