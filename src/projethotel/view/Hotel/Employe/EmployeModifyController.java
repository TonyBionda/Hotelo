package projethotel.view.Hotel.Employe;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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
public class EmployeModifyController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Employe current_user;
    private Connection connection;
    private Employe row;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setEmploye(Employe new_employe) {
        current_user = new_employe;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setRow(Employe new_employe) {
        row = new_employe;
    }

    // Initialisation des objets FXML
    @FXML
    private TableView<Employe> list_modifier = new TableView();
    @FXML
    private JFXTextField input_nom, input_adresse, input_telephone, input_prenom, input_mail = new JFXTextField();
    @FXML
    private ComboBox combo_hotel, combo_role = new ComboBox();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        java.util.List<JFXTextField> lstText = new ArrayList<>();
        lstText.add(input_nom);
        lstText.add(input_adresse);
        lstText.add(input_mail);
        lstText.add(input_prenom);
        lstText.add(input_telephone);
        lstText.forEach((value) -> {
            value.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    valider();
                }
            });
        });

    }

    public void init() throws SQLException {
        input_nom.setText(row.getNom());
        input_prenom.setText(row.getPrenom());
        input_adresse.setText(row.getAdresse());
        input_telephone.setText(row.getTelephone());
        input_mail.setText(row.getMail());
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
        combo_hotel.setValue(row.getHotel_id() + " - " + row.getHotel_nom());
        combo_role.setValue(row.getRole_id()+ " - " + row.getRole_nom());
        fillTabWithColumnSuppr();
    }

    public void retour() throws IOException {
        ChangeStage.changeStage("employe", stage, current_user, connection);
    }

    public void valider() {
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
            
            try {
                String query = "UPDATE into employe SET nom = ? and prenom = ? and adresse = ? and telephone = ? and mail = ? and dateModification = ? and hotel_id = ? and role_id = ? WHERE id = ?";
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                Date date = new Date(System.currentTimeMillis());
                preparedStmt.setString(2, nom);
                preparedStmt.setString(3, prenom);
                preparedStmt.setString(4, adresse);
                preparedStmt.setString(5, telephone);
                preparedStmt.setString(6, mail);
                preparedStmt.setDate(7, date);
                preparedStmt.setString(8, hotel_id);
                preparedStmt.setString(9, role_id);
                preparedStmt.setInt(10, row.getId());
                preparedStmt.execute();
                MailSender.SendMailChangement(mail);
                retour(); //C'est juste pour revenir en arrière, à l'onglet employé.
            } catch (SQLException | IOException | MessagingException ex) {
                Logger.getLogger(EmployeModifyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void fillTabWithColumnSuppr() throws SQLException {
        TableColumn<Employe, Integer> id = new TableColumn<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setText("id");
        id.setMinWidth(75);
        TableColumn<Employe, String> nom = new TableColumn<>();
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setText("Nom");
        nom.setMinWidth(100);
        TableColumn<Employe, String> adresse = new TableColumn<>();
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        adresse.setText("Adresse");
        adresse.setMinWidth(200);
        TableColumn<Employe, String> telephone = new TableColumn<>();
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        telephone.setText("Telephone");
        telephone.setMinWidth(90);
        list_modifier.getColumns().add(id);
        list_modifier.getColumns().add(nom);
        list_modifier.getColumns().add(adresse);
        list_modifier.getColumns().add(telephone);
        ObservableList<Employe> lst = FXCollections.observableArrayList();
        lst.add(row);
        list_modifier.setItems(lst);
    }
}
