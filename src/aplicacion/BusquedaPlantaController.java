/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Producto;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class BusquedaPlantaController implements Initializable {

    private List<Producto> listaProductos;
    private EntityManager em;
    @FXML
    private TextField tfPlanta;
    @FXML
    private TextField tfSeccion;
    @FXML
    private TextField tfPasillo;
    @FXML
    private TextField tfEspacio;
    @FXML
    private TextField tfCantidad;
    @FXML
    private Button botonBuscar;
    @FXML
    private Button botonCancelar;
    @FXML
    private TextField tfPrecio;

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
            buscarPlanta();
        }
    }

    @FXML
    private void onActionBuscar(ActionEvent event) throws IOException {
        buscarPlanta();
    }

    @FXML
    private void onKeyCancelar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir de la búsqueda?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) botonCancelar.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir de la búsqueda?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        }
    }

    private void buscarPlanta() throws IOException {
        if (tfPlanta.getText().matches("[a-zA-Z]*")) {
            //Consulta el dni
            Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
            listaProductos = queryFindByNombre.getResultList();

            if (listaProductos != null && !listaProductos.isEmpty()) {
                tfSeccion.setText(listaProductos.get(0).getSeccion());
                tfPasillo.setText(listaProductos.get(0).getPasillo());
                tfEspacio.setText(listaProductos.get(0).getEspacio());
                tfCantidad.setText(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                tfPrecio.setText(String.valueOf(listaProductos.get(0).getPrecio()));
            } else {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La planta que busca no existe, ¿desea crearla en la base de datos?");
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage1 = (Stage) botonCancelar.getScene().getWindow();
                    stage1.close();

                    StackPane rootMain = new StackPane();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CrearPlanta.fxml"));

                    Pane root = fxmlLoader.load();
                    rootMain.getChildren().add(root);

                    CrearPlantaController addPlantaController = (CrearPlantaController) fxmlLoader.getController();

                    addPlantaController.setEntityManager(em);

                    Stage stage = new Stage();
                    stage.setTitle("Nueva Planta");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/add_planta.png")));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(rootMain));
                    stage.show();
                }
            }
        }
    }

    @FXML
    private void onKeyBuscarEnter(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            buscarPlanta();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir de la búsqueda?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) botonCancelar.getScene().getWindow();
                stage.close();
            }
        }
    }
}
