/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Cliente;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class BusquedaClienteController implements Initializable {

    private List<Cliente> listaCliente;
    private EntityManager em;
    @FXML
    private TextField busquedaText;
    @FXML
    private Button buttonBusqueda;
    @FXML
    private Button buttonCancelar;

    private Text mostrarCliente;
    @FXML
    private TextField nombre_completo;
    @FXML
    private TextField telefono;
    @FXML
    private TextField direccion;
    @FXML
    private TextField email;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @FXML
    private void onKeyBuscar(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            buscarCliente();
        }
    }

    @FXML
    private void onActionBuscar(ActionEvent event) throws IOException {
        buscarCliente();
    }

    @FXML
    private void onKeyCancelar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir de la búsqueda?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) buttonCancelar.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir de la búsqueda?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) buttonCancelar.getScene().getWindow();
            stage.close();
        }
    }

    private void buscarCliente() throws IOException {
        if (busquedaText.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
            //Consulta el dni
            Query queryFindByEmpresa = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", busquedaText.getText());
            listaCliente = queryFindByEmpresa.getResultList();

            if (listaCliente != null && !listaCliente.isEmpty()) {
                String nom_comp = listaCliente.get(0).getApellidos() + ", " + listaCliente.get(0).getNombre();

                nombre_completo.setText(nom_comp);
                telefono.setText(listaCliente.get(0).getTelefono());
                direccion.setText(listaCliente.get(0).getDireccion());
                email.setText(listaCliente.get(0).getCorreo());
            } else {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "El cliente que busca no existe en la base de datos, ¿quiere añadirlo a la base de datos?");
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage1 = (Stage) buttonCancelar.getScene().getWindow();
                    stage1.close();

                    StackPane rootMain = new StackPane();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CrearCliente.fxml"));

                    Pane root = fxmlLoader.load();
                    rootMain.getChildren().add(root);

                    CrearClienteController addClienteController = (CrearClienteController) fxmlLoader.getController();

                    addClienteController.setEntityManager(em);

                    Stage stage = new Stage();
                    stage.setTitle("Nuevo Cliente");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/add_cliente.png")));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(rootMain));
                    stage.show();
                }
            }
        }
    }

    @FXML
    private void onKeyBuscarEnter(KeyEvent event) throws IOException {
        if(event.getCode() == ENTER){
            buscarCliente();
        }
        else if (event.getCode() == KeyCode.ESCAPE) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir de la búsqueda?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) buttonCancelar.getScene().getWindow();
                stage.close();
            }
        }
    }
}
