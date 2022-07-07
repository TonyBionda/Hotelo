/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projethotel.view.Client.Reserver;

import projethotel.view.Hotel.Dashboard.*;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projethotel.Model.*;

/**
 * FXML Controller class
 *
 * @author TonyL
 */
public class clientReserverController implements Initializable {
    
    //Les boutons du menu de gauche
    @FXML
    private JFXButton ClientReserver, ClientReservation, ClientProfil, ClientDashboard, accueil = new JFXButton();
    
    private Stage stage;
    private Client current_user;
    private Connection connection;

    public void setStage(Stage new_stage) {
        stage = new_stage;
    }

    public void setClient(Client new_client) {
        current_user = new_client;
    }

    public void setConnection(Connection new_connection) {
        connection = new_connection;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init() {
        java.util.List<JFXButton> lstButton = new ArrayList<>();
        lstButton.add(ClientReserver);
        lstButton.add(ClientReservation);
        lstButton.add(ClientProfil);
        lstButton.add(ClientDashboard);
        lstButton.add(accueil);
        ChangeStage chStage = new ChangeStage();
        for (JFXButton value : lstButton) {
            value.setOnAction(event -> {
                try {
                    System.out.println("EVENT PRIS EN COMPTE");
                    chStage.changeStageClient(value.getId(), stage, current_user, connection);
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
        }
    }
}
