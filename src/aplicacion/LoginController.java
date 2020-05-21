/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javafx.scene.input.KeyEvent;
import static javafx.scene.input.KeyCode.ENTER;

/**
 * FXML Controller class
 *
 * @author franc
 */
public class LoginController implements Initializable {

    private String conexion;
    private String user;
    private String password;
    private Connection connect;
    private EntityManager em;
    private EntityManagerFactory emf;

    @FXML
    private TextField usuario;
    @FXML
    private PasswordField pass;
    @FXML
    private Text mensaje;
    @FXML
    private Button entrar;
    @FXML
    private Button salir;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void stop() throws Exception {
        try {
            //DriverManager.getConnection("jdbc:derby:BDHotel;shutdown=true");
            conexion = em.getProperties().get("javax.persistence.jdbc.url").toString();
            DriverManager.getConnection(conexion + ";shutdown=true");
            em.close();
        } catch (SQLException ex) {
        }
    }

    public void entrar() throws IOException {
        boolean correcto = false;
        if (usuario.getText().equals("admin")) {
            if (pass.getText().equals("admin")) {
                correcto = true;
            } else {
                mensaje.setText("Contraseña INCORRECTA.\nInténtelo de nuevo.");
            }
            mensaje.setVisible(true);
        } else {
            mensaje.setText("Usuario INCORRECTO.\nInténtelo de nuevo.");
            mensaje.setVisible(true);
        }
        if (correcto) {
            Stage stage = (Stage) entrar.getScene().getWindow();
            stage.close();

            Group rootMain = new Group();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));

            Pane root = fxmlLoader.load();
            rootMain.getChildren().add(root);
            //Carga del EntityManager, etc… 
            Scene scene = new Scene(rootMain);

            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/logo_Empresa_MINI.png")));
            stage.setTitle("Agromarquez");

            MainController mainController = (MainController) fxmlLoader.getController();

            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void onKeyPressedEntrar(KeyEvent event) throws IOException {
        try {
            if (event.getCode() == ENTER) {
                entrar();
            }
        } catch (IOException e) {
        }
    }

    @FXML
    private void onActionEntrar(ActionEvent event) throws IOException {
        try {
            entrar();
        } catch (IOException e) {
        }
    }

    @FXML
    private void onKeyPressedSalir(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere salir?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) salir.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionSalir(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere salir?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) salir.getScene().getWindow();
            stage.close();
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
        try {
            conexion = em.getProperties().get("javax.persistence.jdbc.url").toString();
            user = em.getProperties().get("javax.persistence.jdbc.user").toString();
            password = "";
            connect = DriverManager.getConnection(conexion, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
