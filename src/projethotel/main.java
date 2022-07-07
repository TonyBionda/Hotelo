/* 
   Ce projet a été développé et réalisé par BIONDA Tony et BROCH Alexis du G1 L2 Informatique de l'UEVE
   Il mobilise l'ensemble de nos connaissances et de nos recherches, en autodidacte pour
   répondre au mieux à la problématique du projet numéro 7, celui de l'Hotel.
*/

package projethotel;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projethotel.view.Accueil.AccueilController;



/**
 *
 * @author TonyL
 */
public class main extends Application  {
    private Stage stage;
    
    @Override
    public void start(Stage primaryStage) throws IOException{
        stage = primaryStage;
        
        initMainScene();
    }
    public void initMainScene() throws IOException{
        stage.setTitle("Hotelo");
        stage.getIcons().add(new Image("projethotel/img/hotel.png"));
        System.setProperty( "file.encoding", "UTF-8" );
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Accueil/accueil.fxml"));
        AnchorPane mainAnchor =  (AnchorPane) loader.load();

        Scene mainScene = new Scene(mainAnchor);
        AccueilController controller = loader.getController();
        stage.setResizable(false);
        stage.setHeight(624);
        controller.setStage(stage);
        stage.setScene(mainScene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
