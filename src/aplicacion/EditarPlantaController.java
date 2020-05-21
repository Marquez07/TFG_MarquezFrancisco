/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Producto;
import java.io.IOException;
import java.math.BigDecimal;
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
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class EditarPlantaController implements Initializable {

    private List<Producto> listaProductos;
    private EntityManager em;

    @FXML
    private Button botonGuardar;
    @FXML
    private TextField tfPlanta;
    @FXML
    private Button botonBuscar;
    @FXML
    private TextField tfSeccion;
    @FXML
    private TextField tfPasillo;
    @FXML
    private TextField tfEspacio;
    @FXML
    private TextField tfCantidad;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonLimpiar;
    @FXML
    private TextField tfPrecio;

    /**
     * Initializes the controller class.
     *
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
        if (tfPlanta.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
            tfPlanta.setDisable(true);
            //Consulta el dni
            Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
            listaProductos = queryFindByNombre.getResultList();

            if (listaProductos != null && !listaProductos.isEmpty()) {
                mostrar();
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
                } else if (result.get() == ButtonType.CANCEL) {
                    tfPlanta.setDisable(false);
                }
            }
        }
    }

    private void mostrar() {
        tfSeccion.setDisable(false);
        tfPasillo.setDisable(false);
        tfEspacio.setDisable(false);
        tfCantidad.setDisable(false);
        tfPrecio.setDisable(false);
    }

    @FXML
    private void onKeyGuardar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            boolean error = false;
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Has verificado que los datos introducidos para el alta del producto son correctos?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {

                if (error == false) {
                    try {
                        String errores = "";
                        Producto producto = new Producto();

                        if ((tfPlanta.getText() != null) && !tfPlanta.getText().isEmpty()) {
                            producto.setNombre(tfPlanta.getText());
                        } else {
                            error = true;
                            errores += "- Nombre de la planta no válido\n";
                        }

                        if (((tfSeccion.getText() != null) && !tfSeccion.getText().isEmpty()) && tfSeccion.getText().matches("[a-cA-C]")) {
                            producto.setSeccion(tfSeccion.getText());
                        } else {
                            error = true;
                            errores += "- Sección de la planta no válida\n";
                        }

                        if (((tfPasillo.getText() != null) && !tfPasillo.getText().isEmpty()) && tfPasillo.getText().matches("[0-9]*")) {
                            producto.setPasillo(tfPasillo.getText());
                        } else {
                            error = true;
                            errores += "- Pasillo no válido\n";
                        }

                        if (((tfEspacio.getText() != null) && !tfEspacio.getText().isEmpty()) && tfEspacio.getText().matches("[0-9]*")) {
                            producto.setEspacio(tfEspacio.getText());
                        } else {
                            error = true;
                            errores += "- Espacio no válido\n";
                        }

                        if (((tfCantidad.getText() != null) && !tfCantidad.getText().isEmpty()) && tfCantidad.getText().matches("[0-9]*")) {
                            producto.setCantidadProducto(Integer.parseInt(tfCantidad.getText()));
                        } else {
                            error = true;
                            errores += "- Cantidad no válida\n";
                        }

                        if (((tfPrecio.getText() != null) && !tfPrecio.getText().isEmpty()) && tfPrecio.getText().matches("[0-9.]*")) {
                            if (comprobarPrecio() == false) {
                                java.math.BigDecimal precio = BigDecimal.valueOf(Double.parseDouble(tfPrecio.getText()));
                                producto.setPrecio(precio);
                            } else {
                                error = true;
                                errores += "- El precio no puede ser negativo\n";

                            }
                        } else {
                            error = true;
                            errores += "Precio no válida\n";
                        }

                        if (error == false) {
                            em.getTransaction().begin();
                            em.merge(producto);
                            em.getTransaction().commit();
                        } else {
                            Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                            alerta2.showAndWait();
                        }

                    } catch (RollbackException ex) {
                        Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido guardar el producto");
                        alerta2.showAndWait();
                    }

                    if (error == false) {
                        Stage stage = (Stage) botonGuardar.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {
        boolean error = false;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Has verificado que los datos introducidos para el alta del producto son correctos?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (error == false) {
                try {
                    String errores = "";
                    Producto producto = new Producto();

                    if ((tfPlanta.getText() != null) && !tfPlanta.getText().isEmpty()) {
                        producto.setNombre(tfPlanta.getText());
                    } else {
                        error = true;
                        errores += "- Nombre de la planta no válido\n";
                    }

                    if (((tfSeccion.getText() != null) && !tfSeccion.getText().isEmpty()) && tfSeccion.getText().matches("[a-cA-C]")) {
                        producto.setSeccion(tfSeccion.getText());
                    } else {
                        error = true;
                        errores += "- Sección de la planta no válida\n";
                    }

                    if (((tfPasillo.getText() != null) && !tfPasillo.getText().isEmpty()) && tfPasillo.getText().matches("[0-9]*")) {
                        producto.setPasillo(tfPasillo.getText());
                    } else {
                        error = true;
                        errores += "- Pasillo no válido\n";
                    }

                    if (((tfEspacio.getText() != null) && !tfEspacio.getText().isEmpty()) && tfEspacio.getText().matches("[0-9]*")) {
                        producto.setEspacio(tfEspacio.getText());
                    } else {
                        error = true;
                        errores += "- Espacio no válido\n";
                    }

                    if (((tfCantidad.getText() != null) && !tfCantidad.getText().isEmpty()) && tfCantidad.getText().matches("[0-9]*")) {
                        producto.setCantidadProducto(Integer.parseInt(tfCantidad.getText()));
                    } else {
                        error = true;
                        errores += "- Cantidad no válida\n";
                    }

                    if (((tfPrecio.getText() != null) && !tfPrecio.getText().isEmpty()) && tfPrecio.getText().matches("[0-9.]*")) {
                        if (comprobarPrecio() == false) {
                            java.math.BigDecimal precio = BigDecimal.valueOf(Double.parseDouble(tfPrecio.getText()));
                            producto.setPrecio(precio);
                        } else {
                            error = true;
                            errores += "- El precio no puede ser negativo\n";

                        }
                    } else {
                        error = true;
                        errores += "Precio no válida\n";
                    }

                    if (error == false) {
                        em.getTransaction().begin();
                        em.merge(producto);
                        em.getTransaction().commit();
                    } else {
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                        alerta2.showAndWait();
                    }

                } catch (RollbackException ex) {
                    Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido guardar el producto");
                    alerta2.showAndWait();
                }

                if (error == false) {
                    Stage stage = (Stage) botonGuardar.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    @FXML
    private void onKeyBuscarEnter(KeyEvent event) throws IOException {
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                    buscarPlanta();
                    break;
                case ESCAPE:
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres dejar de editar el producto?");
                    Optional<ButtonType> result = alerta.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        Stage stage = (Stage) botonCancelar.getScene().getWindow();
                        stage.close();
                    }
                    break;
                case TAB:
                    buscarPlanta();
                    break;
                default:
                    break;
            }
        }
    }

    private void limpiar() {
        tfPlanta.setText("");
        tfSeccion.setText("");
        tfPasillo.setText("");
        tfEspacio.setText("");
        tfCantidad.setText("");
        tfPrecio.setText("");
        tfPlanta.setDisable(false);
        tfSeccion.setDisable(true);
        tfPasillo.setDisable(true);
        tfEspacio.setDisable(true);
        tfCantidad.setDisable(true);
        tfPrecio.setDisable(true);
    }

    @FXML
    private void onKeyLimpiar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro que quieres limpiar la edición?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                limpiar();
            }
        }
    }

    @FXML
    private void onActionLimpiar(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro que quieres limpiar la edición?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            limpiar();
        }
    }

    private boolean comprobarPrecio() {
        boolean error = false;
        if (tfPrecio.getText() != null && !tfPrecio.getText().isEmpty()) {
            double precio = Double.parseDouble(tfPrecio.getText());
            if (precio < 0) {
                error = true;
            } else {
                error = false;
            }
        }
        return error;
    }
}
