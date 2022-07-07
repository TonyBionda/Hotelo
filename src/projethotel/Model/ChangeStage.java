package projethotel.Model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projethotel.view.Accueil.AccueilController;
import projethotel.view.Client.Dashboard.clientDashboardController;
import projethotel.view.Client.Profil.clientProfilController;
import projethotel.view.Client.Reservation.clientReservationController;
import projethotel.view.Client.Reserver.clientReserverController;
import projethotel.view.Hotel.Chambre.ChambreController;
import projethotel.view.Hotel.Client.ClientController;
import projethotel.view.Hotel.Dashboard.DashboardController;
import projethotel.view.Hotel.Employe.EmployeController;
import projethotel.view.Hotel.Etage.EtageController;
import projethotel.view.Hotel.Hotel.HotelController;
import projethotel.view.Hotel.Produit.ProduitController;
import projethotel.view.Hotel.Reservation.ReservationController;

public class ChangeStage {

    //CHANGER LE STAGE POUR LE COTE HOTEL
    public static void changeStage(String id, Stage stage, Employe current_user, Connection connection) throws LoadException, IOException {
        FXMLLoader loader;
        AnchorPane mainAnchor;
        URL url;
        switch (id) {
            case "accueil":
                url = new File("src/projethotel/view/Accueil/accueil.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new AccueilController(), loader, mainAnchor, stage, connection);
                break;
            case "dashboard":
                url = new File("src/projethotel/view/Hotel/Dashboard/dashboard.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new DashboardController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "hotel":
                url = new File("src/projethotel/view/Hotel/Hotel/hotel.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new HotelController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "employe":
                url = new File("src/projethotel/view/Hotel/Employe/employe.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new EmployeController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "client":
                url = new File("src/projethotel/view/Hotel/Client/client.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new ClientController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "reservation":
                url = new File("src/projethotel/view/Hotel/Reservation/reservation.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new ReservationController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "etage":
                url = new File("src/projethotel/view/Hotel/Etage/etage.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new EtageController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "chambre":
                url = new File("src/projethotel/view/Hotel/Chambre/chambre.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new ChambreController(), loader, mainAnchor, stage, current_user, connection);
                break;
            case "produit":
                url = new File("src/projethotel/view/Hotel/Produit/produit.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                mainAnchor = (AnchorPane) loader.load();
                changeStage(new ProduitController(), loader, mainAnchor, stage, current_user, connection);
                break;
        }
    }

    //CHANGER LE STAGE POUR LE COTE CLIENT
    public static void changeStageClient(String id, Stage stage, Client current_user, Connection connection) throws LoadException, IOException {
        try {
            FXMLLoader loader;
            AnchorPane mainAnchor;
            URL url;
            switch (id) {
                case "accueil":
                    url = new File("src/projethotel/view/Accueil/accueil.fxml").toURI().toURL();
                    loader = new FXMLLoader(url);
                    mainAnchor = (AnchorPane) loader.load();
                    changeStage(new AccueilController(), loader, mainAnchor, stage, connection);
                    break;
                case "ClientDashboard":
                    url = new File("src/projethotel/view/Client/Dashboard/clientDashboard.fxml").toURI().toURL();
                    loader = new FXMLLoader(url);
                    mainAnchor = (AnchorPane) loader.load();
                    changeStageClient(new clientDashboardController(), loader, mainAnchor, stage, current_user, connection);
                    break;
                case "ClientReserver":
                    url = new File("src/projethotel/view/Client/Reserver/clientReserver.fxml").toURI().toURL();
                    loader = new FXMLLoader(url);
                    mainAnchor = (AnchorPane) loader.load();
                    changeStageClient(new clientReserverController(), loader, mainAnchor, stage, current_user, connection);
                    break;
                case "ClientReservation":
                    url = new File("src/projethotel/view/Client/Reservation/clientReservation.fxml").toURI().toURL();
                    loader = new FXMLLoader(url);
                    mainAnchor = (AnchorPane) loader.load();
                    changeStageClient(new clientReservationController(), loader, mainAnchor, stage, current_user, connection);
                    break;
                case "ClientProfil":
                    url = new File("src/projethotel/view/Client/Profil/clientProfil.fxml").toURI().toURL();
                    loader = new FXMLLoader(url);
                    mainAnchor = (AnchorPane) loader.load();
                    changeStageClient(new clientProfilController(), loader, mainAnchor, stage, current_user, connection);
                    break;
            }
        } catch (LoadException e) {

        }
    }

    //Accueil
    public static void changeStage(AccueilController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        stage.setHeight(624);
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //DashBoard
    public static void changeStage(projethotel.view.Hotel.Dashboard.DashboardController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Hotel
    public static void changeStage(HotelController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Employe
    public static void changeStage(EmployeController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Client
    public static void changeStage(ClientController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Reservation
    public static void changeStage(ReservationController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Etage
    public static void changeStage(EtageController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Chambre
    public static void changeStage(ChambreController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    //Chambre
    public static void changeStage(ProduitController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Employe current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setEmploye(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }
    
    //DashBoard Client
    public static void changeStageClient(clientDashboardController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Client current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setClient(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    public static void changeStageClient(clientReserverController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Client current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setClient(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    public static void changeStageClient(clientReservationController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Client current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setClient(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }

    public static void changeStageClient(clientProfilController controller, FXMLLoader loader, AnchorPane mainAnchor, Stage stage, Client current_user, Connection connection) {
        controller = loader.getController();
        controller.setStage(stage);
        controller.setClient(current_user);
        controller.setConnection(connection);
        controller.init();
        Scene mainScene = new Scene(mainAnchor);

        stage.setScene(mainScene);
        stage.show();
    }
}
