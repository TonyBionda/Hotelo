/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projethotel.view.Accueil;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import projethotel.Model.*;
import projethotel.view.Client.Dashboard.clientDashboardController;
import projethotel.view.Hotel.Dashboard.DashboardController;
import projethotel.view.SignUp.SignUpController;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class AccueilController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Connection connection;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }
    
    // Initialisation des objets FXML
    @FXML
    private JFXButton hotel_loginRequest, client_loginRequest, SignUp_loginRequest = new JFXButton();
    @FXML
    private JFXTextField hotel_loginField, client_loginField = new JFXTextField();
    @FXML
    private JFXTextField SignUp_firstnameField, SignUp_lastnameField, SignUp_mailField = new JFXTextField();
    @FXML
    private JFXPasswordField hotel_passwordField, client_passwordField = new JFXPasswordField();
    @FXML
    private JFXProgressBar hotel_progress_bar, SignUp_progress_bar, client_progress_bar = new JFXProgressBar();
    @FXML
    private Label hotel_error, client_error, SignUp_error = new Label();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sql_connect();
            hotel_passwordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    hotel_login();
                }
            });
            client_passwordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    client_login();
                }
            });
            SignUp_mailField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    client_signUp();
                }
            });
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erreur " + e.getMessage());
        }
    }

    public void sql_connect() throws ClassNotFoundException, SQLException {
        String myDriver = "com.mysql.cj.jdbc.Driver";
        String myUrl = "jdbc:mysql://localhost:3306/projet?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName(myDriver);
        connection = DriverManager.getConnection(myUrl, "root", "root");
    }

    public void hotel_login() {
        String login = hotel_loginField.getText();
        String password = hotel_passwordField.getText();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM employe WHERE mail = ? and motdepasse = ?");
            query.setString(1, login);
            query.setString(2, password);
            ResultSet rs = query.executeQuery();
            if (!rs.next()) {
                hotel_error.setText("Adresse mail et/ou mot de passe incorrect");
                hotel_passwordField.clear();
                return;
            } else {
                Boolean isActive = rs.getBoolean("estActif");
                if (!isActive) {
                    hotel_error.setText("Cet employé est licencié");
                    return;
                }
                Employe current_user = new Employe();
                current_user.setId(rs.getInt("id"));
                current_user.setNom(rs.getString("nom"));
                current_user.setPrenom(rs.getString("prenom"));
                current_user.setMail(rs.getString("mail"));
                int hotel_id = rs.getInt("hotel_id");
                Hotel current_userHotel = new Hotel();
                current_user.setHotel_id(hotel_id);
                current_userHotel = current_userHotel.getHotelById(connection, hotel_id);
                int role_id = rs.getInt("role_id");
                Role current_userRole = new Role();
                current_user.setRole_id(role_id);
                current_userRole = current_userRole.getRoleById(connection, role_id);
                if (current_userHotel.getId() == 0 && current_userRole.getId() != 999999) {
                    hotel_error.setText("Cet employé n'est associé à aucun hôtel");
                } else {
                    current_user.setHotel(current_userHotel);
                    current_user.setRole(current_userRole);
                    current_user.setHotel_nom(current_userHotel.getNom());
                    current_user.setRole_nom(current_userRole.getNom());
                    URL url = new File("src/projethotel/view/Hotel/Dashboard/dashboard.fxml").toURI().toURL();
                    FXMLLoader loader = new FXMLLoader(url);
                    AnchorPane mainAnchor = (AnchorPane) loader.load();
                    DashboardController controller = loader.getController();
                    controller.setStage(stage);
                    controller.setEmploye(current_user);
                    controller.setConnection(connection);
                    controller.init();
                    Scene mainScene = new Scene(mainAnchor);
                    hotel_progress_bar.setVisible(true);
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                        hotel_progress_bar.setVisible(false);
                        stage.setHeight(674);
                        stage.setScene(mainScene);
                        stage.show();
                    }));
                    timeline.play();
                }
            }
            query.close();
        } catch (IOException | SQLException e) {
            hotel_error.setText("Erreur interne.");
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void client_login() {
        String login = client_loginField.getText();
        String password = client_passwordField.getText();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM client WHERE mail = ? and motdepasse = ?");
            query.setString(1, login);
            query.setString(2, password);
            ResultSet rs = query.executeQuery();
            if (!rs.next()) {
                client_error.setText("Adresse mail et/ou mot de passe incorrect");
                client_passwordField.clear();
                return;
            } else {
                Client currentClient = new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("telephone"), rs.getString("mail"), rs.getDate("dateCreation"), rs.getDate("dateModification"));
                URL url = new File("src/projethotel/view/Client/Dashboard/clientDashboard.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                AnchorPane mainAnchor = (AnchorPane) loader.load();
                clientDashboardController controller = loader.getController();
                controller.setStage(stage);
                controller.setClient(currentClient);
                controller.setConnection(connection);
                controller.init();
                Scene mainScene = new Scene(mainAnchor);
                client_progress_bar.setVisible(true);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                    client_progress_bar.setVisible(false);
                    stage.setHeight(674);
                    stage.setScene(mainScene);
                    stage.show();
                }));
                timeline.play();
            }
            query.close();
        } catch (IOException | SQLException e) {
            client_error.setText("Erreur interne.");
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void client_signUp() {
        String nom = SignUp_lastnameField.getText();
        String prenom = SignUp_firstnameField.getText();
        String mail = SignUp_mailField.getText();
        if (nom.length() == 0 || prenom.length() == 0 || mail.length() == 0) {
            SignUp_error.setText("Veuillez remplir tous les champs.");
            return;
        }
        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);
        if (!matcher.matches()) {
            SignUp_error.setText("Format de mail invalide.");
            return;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM client where mail = ?");
            query.setString(1, mail);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                SignUp_error.setText("Cette adresse e-Mail est déjà enregistrée.");
                query.close();
                return;
            } else {
                query.close();
            }
            URL url = new File("src/projethotel/view/SignUp/signup.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            AnchorPane mainAnchor = (AnchorPane) loader.load();
            SignUpController controller = loader.getController();
            controller.setStage(stage);
            controller.setRow(nom, prenom, mail);
            controller.setConnection(connection);
            controller.init();
            Scene mainScene = new Scene(mainAnchor);

            SignUp_progress_bar.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                SignUp_progress_bar.setVisible(false);
                stage.setScene(mainScene);
                stage.show();
            }));
            timeline.play();
        } catch (SQLException | IOException e) {
            SignUp_error.setText("Erreur interne.");
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}
