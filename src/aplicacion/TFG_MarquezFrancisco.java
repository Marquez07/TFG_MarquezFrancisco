/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Francisco
 */
public class TFG_MarquezFrancisco extends Application {
    /*Atributos*/
    private EntityManager em;
    private EntityManagerFactory emf;
    
    /*Metodo Start*/
    @Override
    public void start(Stage stage) throws Exception {
        /*Cargamos la vista inicial, la vista LOGIN*/
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = fxmlLoader.load();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Acceso");
        stage.getIcons().add(new Image("imagenes/logo_Empresa_MINI.png"));
        
        LoginController controlLogin = (LoginController)fxmlLoader.getController();
        
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
