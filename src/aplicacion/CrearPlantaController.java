/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Producto;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class CrearPlantaController implements Initializable {

    private List<Producto> listaPlantas;
    private List<Producto> listaPasillo;
    private EntityManager em;

    @FXML
    private TextField tfPlanta;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TextField tfSeccion;
    private TextField tfLado;
    @FXML
    private TextField tfPasillo;
    @FXML
    private TextField tfEspacio;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonCancelar;
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
        tfPlanta.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
            } else {
                comprobarPlanta(new ActionEvent());
            }
        });
    }

    private void mostrar() {
        tfSeccion.setDisable(false);
        tfLado.setDisable(false);
        tfPasillo.setDisable(false);
        tfEspacio.setDisable(false);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    /*Accion para el boton Guardar*/
    @FXML
    private void onKeyPressedSave(KeyEvent event) {
        if (event.getCode() == ENTER) {
            boolean error = false;
            if (buscarPlanta()) {
                Alert alerta1 = new Alert(Alert.AlertType.ERROR, "La planta ya existe en la base de datos");
                Optional<ButtonType> result = alerta1.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage = (Stage) botonCancelar.getScene().getWindow();
                    stage.close();
                }
            } else {
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
                                if (comprobarLimitePasillo() == false) {
                                    if (comprobarPasillo() == false) {
                                        producto.setPasillo(tfPasillo.getText());
                                    } else {
                                        error = true;
                                        errores += "- Pasillo Ocupado\n";
                                    }
                                } else {
                                    error = true;
                                    errores += "- El pasillo introducido es superior al pasillo límite\n";
                                }

                            } else {
                                error = true;
                                errores += "- Pasillo no válido\n";
                            }

                            if (((tfEspacio.getText() != null) && !tfEspacio.getText().isEmpty()) && tfEspacio.getText().matches("[0-9]*")) {
                                if (comprobarLimiteEspacio() == false) {
                                    if (comprobarEspacio() == false) {
                                        producto.setEspacio(tfEspacio.getText());
                                    } else {
                                        error = true;
                                        errores += "- Espacio Ocupado\n";
                                    }
                                } else {
                                    error = true;
                                    errores += "- El espacio introducido es superior al espacio límite\n";
                                }

                            } else {
                                error = true;
                                errores += "- Espacio no válido\n";
                            }

                            if (((tfCantidad.getText() != null) && !tfCantidad.getText().isEmpty()) && tfCantidad.getText().matches("[0-9]*")) {
                                if (comprobarCantidad() == false) {
                                    producto.setCantidadProducto(Integer.parseInt(tfCantidad.getText()));
                                } else {
                                    error = true;
                                    errores += "- La cantidad supera las 1000 unidades\n";

                                }
                            } else {
                                error = true;
                                errores += "Cantidad no válida\n";
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
                                em.persist(producto);
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
    }

    @FXML
    private void guardarPlanta(ActionEvent event) {
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
                    /*
                    int pas, esp;
                    String posPas, posEsp;
                    boolean añadido = false;

                    for (pas = 1; pas <= 10; pas++) {
                        posPas = String.valueOf(pas);
                        if (añadido) {
                            break;
                        } else {
                            for (esp = 1; esp <= 10; esp++) {
                                posEsp = String.valueOf(esp);
                                Query queryFindByEspacio = em.createNamedQuery("Producto.findByEspacio").setParameter("espacio", posEsp);
                                listaPlantas = queryFindByEspacio.getResultList();

                                if (listaPlantas != null && !listaPlantas.isEmpty()) {

                                    if (!listaPlantas.get(0).getPasillo().equals(posPas)) {
                                        producto.setPasillo(posPas);
                                        producto.setEspacio(posEsp);
                                        añadido = true;
                                        error = false;
                                        break;
                                    } else {
                                        error = true;
                                        errores += "- El hueco ya esta ocupado\n";
                                    }
                                } else {
                                    producto.setPasillo(posPas);
                                    producto.setEspacio(posEsp);
                                    añadido = true;
                                    error = false;
                                    break;
                                }
                            }
                        }
                    }
                     */

                    if (((tfPasillo.getText() != null) && !tfPasillo.getText().isEmpty()) && tfPasillo.getText().matches("[0-9]*")) {
                        if (comprobarLimitePasillo() == false) {
                            if (comprobarPasillo() == false) {
                                producto.setPasillo(tfPasillo.getText());
                            } else {
                                error = true;
                                errores += "- Pasillo Ocupado\n";
                            }
                        } else {
                            error = true;
                            errores += "- El pasillo introducido es superior al pasillo límite\n";
                        }

                    } else {
                        error = true;
                        errores += "- Pasillo no válido\n";
                    }

                    if (((tfEspacio.getText() != null) && !tfEspacio.getText().isEmpty()) && tfEspacio.getText().matches("[0-9]*")) {
                        if (comprobarLimiteEspacio() == false) {
                            if (comprobarEspacio() == false) {
                                producto.setEspacio(tfEspacio.getText());
                            } else {
                                error = true;
                                errores += "- Espacio Ocupado\n";
                            }
                        } else {
                            error = true;
                            errores += "- El espacio introducido es superior al espacio límite\n";
                        }

                    } else {
                        error = true;
                        errores += "- Espacio no válido\n";
                    }

                    if (((tfCantidad.getText() != null) && !tfCantidad.getText().isEmpty()) && tfCantidad.getText().matches("[0-9]*")) {
                        if (comprobarCantidad() == false) {
                            producto.setCantidadProducto(Integer.parseInt(tfCantidad.getText()));
                        } else {
                            error = true;
                            errores += "- La cantidad supera las 1000 unidades\n";

                        }
                    } else {
                        error = true;
                        errores += "Cantidad no válida\n";
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
                        em.persist(producto);
                        em.getTransaction().commit();
                    } else {
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                        alerta2.showAndWait();
                    }

                } catch (RollbackException ex) {
                    Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido guardar la planta");
                    alerta2.showAndWait();
                }

                if (error == false) {
                    Stage stage = (Stage) botonGuardar.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    /*Accion para el boton Cancelar*/
    @FXML
    private void onKeyPressedCancel(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere cancelar el alta del producto?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) botonCancelar.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere cancelar el alta del producto?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        }
    }

    private boolean buscarPlanta() {
        boolean existe = false;
        if (!tfPlanta.getText().isEmpty()) {
            //Consulta el dni
            Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
            listaPlantas = queryFindByNombre.getResultList();

            if (listaPlantas != null && !listaPlantas.isEmpty()) {
                existe = true;
            } else {
                existe = false;
            }
        }
        return existe;
    }

    private boolean comprobarCantidad() {
        boolean error = false;
        if (tfCantidad.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Campo Cantidad: vacio");
            alert.show();
        } else {
            int cant = Integer.parseInt(tfCantidad.getText());
            if (cant > 1000) {
                error = true;
            } else {
                error = false;
            }
        }
        return error;
    }

    @FXML
    private void comprobarPlanta(ActionEvent event) {
        if (tfPlanta.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Campo Nombre: vacio");
            alert.show();
        } else {
            if (buscarPlanta()) {
                Alert alerta1 = new Alert(Alert.AlertType.ERROR, "La planta ya existe en la base de datos");
                Optional<ButtonType> result = alerta1.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage = (Stage) botonCancelar.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    private boolean comprobarEspacio() {
        boolean ocupado = false;
        if (!tfEspacio.getText().isEmpty()) {
            //Consulta el dni
            Query queryFindByEspacio = em.createNamedQuery("Producto.findByEspacio").setParameter("espacio", tfEspacio.getText());
            listaPlantas = queryFindByEspacio.getResultList();

            if (listaPlantas != null && !listaPlantas.isEmpty()) {
                ocupado = true;
            } else {
                ocupado = false;
            }
        }
        return ocupado;
    }

    private boolean comprobarLimiteEspacio() {
        boolean error = false;
        if (tfPasillo.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Campo Espacio: vacio");
            alert.show();
        } else {
            int cant = Integer.parseInt(tfPasillo.getText());
            if (cant > 10) {
                error = true;
            } else {
                error = false;
            }
        }
        return error;
    }

    private boolean comprobarLimitePasillo() {
        boolean error = false;
        if (tfPasillo.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Campo Espacio: vacio");
            alert.show();
        } else {
            int cant = Integer.parseInt(tfPasillo.getText());
            if (cant > 10) {
                error = true;
            } else {
                error = false;
            }
        }
        return error;
    }

    private boolean comprobarPasillo() {
        boolean ocupado = false;
        if (!tfPasillo.getText().isEmpty()) {
            //Consulta el dni
            Query queryFindByPasillo = em.createNamedQuery("Producto.findByPasillo").setParameter("pasillo", tfPasillo.getText());
            listaPlantas = queryFindByPasillo.getResultList();

            if (listaPlantas != null && !listaPlantas.isEmpty()) {
                ocupado = true;
            } else {
                ocupado = false;
            }
        }
        return ocupado;
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
