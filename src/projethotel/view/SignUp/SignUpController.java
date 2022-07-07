package projethotel.view.SignUp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import projethotel.Model.ChangeStage;
import projethotel.Model.Client;
import projethotel.Model.Employe;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class SignUpController implements Initializable {

    // Initialisation des paramètres du Stage
    private Stage stage;
    private Connection connection;
    private Client row;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    public void setRow(String nom, String prenom, String mail) {
        row = new Client();
        row.setNom(nom);
        row.setPrenom(prenom);
        row.setMail(mail);
    }

    // Initialisation des objets FXML
    @FXML
    private JFXTextField SignUp_firstnameField, SignUp_lastnameField, SignUp_mailField, SignUp_phoneField = new JFXTextField();
    @FXML
    private JFXPasswordField SignUp_passwordField = new JFXPasswordField();
    @FXML
    private JFXButton accueil;
    @FXML
    private JFXProgressBar progress_bar = new JFXProgressBar();
    @FXML
    private Label SignUp_error = new Label();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accueil.setOnAction(event -> {
            try {
                accueil();
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

    public void init() {
        SignUp_firstnameField.setText(row.getPrenom());
        SignUp_lastnameField.setText(row.getNom());
        SignUp_mailField.setText(row.getMail());
    }

    public void accueil() throws IOException {
        ChangeStage.changeStage("accueil", stage, new Employe(), connection);
    }

    public void signUp() {
        String nom = SignUp_lastnameField.getText();
        String prenom = SignUp_firstnameField.getText();
        String mail = SignUp_mailField.getText();
        String motdepasse = SignUp_passwordField.getText();
        String telephone = SignUp_phoneField.getText();
        if (nom.length() == 0 || prenom.length() == 0 || mail.length() == 0 || motdepasse.length() == 0 || telephone.length() == 0) {
            SignUp_error.setText("Veuillez remplir tous les champs.");
            return;
        }
        if (motdepasse.length() < 5) {
            SignUp_error.setText("Le mot de passe doit être composé au minimum de 5 caractères.");
            SignUp_passwordField.clear();
            return;
        }
        String regex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telephone);
        if (!matcher.matches()) {
            SignUp_error.setText("Format de numéro de téléphone invalide.");
            return;
        }
        try {
            Date date = new Date(System.currentTimeMillis());
            String query = " insert into client (motdepasse, nom, prenom, telephone, mail, dateCreation, dateModification)"
                    + " values (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, motdepasse);
            preparedStmt.setString(2, nom);
            preparedStmt.setString(3, prenom);
            preparedStmt.setString(4, telephone);
            preparedStmt.setString(5, mail);
            preparedStmt.setDate(6, date);
            preparedStmt.setDate(7, date);
            preparedStmt.execute();
            SignUp_error.setText("Inscription réussie. Retour à l'accueil.");
            progress_bar.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                try {
                    accueil();
                } catch (IOException e) {

                }
            }));
            timeline.play();
            preparedStmt.close();
        } catch (SQLException e) {
            SignUp_error.setText("Erreur interne. Contactez l'administrateur.");
            System.out.println(e.getMessage());
        }
    }
}
