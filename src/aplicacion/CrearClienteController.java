/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Cliente;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
public class CrearClienteController implements Initializable {

    private List<Cliente> listaClientes;
    private EntityManager em;
    @FXML
    private TextField nombre;
    @FXML
    private TextField apellido;
    @FXML
    private TextField empresa;
    @FXML
    private TextField direccion;
    @FXML
    private TextField telefono;
    @FXML
    private TextField email;
    @FXML
    private Button saveRecord;
    @FXML
    private Button cancelRecord;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //Cargamos el EntityManager
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    //Acciones para el boton guardar
    @FXML
    private void onKeyPressedSave(KeyEvent event) {
        if (event.getCode() == ENTER) {
            boolean error = false;
            if (buscarEmpresa()) {
                Alert alerta1 = new Alert(Alert.AlertType.ERROR, "El cliente ya existe en la base de datos");
                Optional<ButtonType> result = alerta1.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage = (Stage) cancelRecord.getScene().getWindow();
                    stage.close();
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Has verificado que los datos introducidos para el alta del cliente son correctos?");
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.get() == ButtonType.OK) {

                    if (error == false) {
                        try {
                            String errores = "";
                            Cliente cliente = new Cliente();

                            if ((nombre.getText() != null) && !nombre.getText().isEmpty()) {
                                cliente.setNombre(nombre.getText());
                            } else {
                                error = true;
                                errores += "- Nombre del cliente no válido\n";
                            }

                            if (((apellido.getText() != null) && !apellido.getText().isEmpty())) {
                                cliente.setApellidos(apellido.getText());
                            } else {
                                error = true;
                                errores += "- Apellidos no válido\n";
                            }

                            if (((empresa.getText() != null) && !empresa.getText().isEmpty())) {
                                cliente.setEmpresa(empresa.getText());
                            } else {
                                error = true;
                                errores += "- Empresa no válida\n";
                            }

                            if (((direccion.getText() != null) && !direccion.getText().isEmpty())) {
                                cliente.setDireccion(direccion.getText());
                            } else {
                                error = true;
                                errores += "- Dirección no válida\n";
                            }

                            if (((telefono.getText() != null) && !telefono.getText().isEmpty()) && telefono.getText().matches("[0-9]*")) {
                                cliente.setTelefono(telefono.getText());
                            } else {
                                error = true;
                                errores += "- Teléfono no válido\n";
                            }

                            if (((email.getText() != null) && !email.getText().isEmpty())) {
                                cliente.setCorreo(email.getText());
                            } else {
                                error = true;
                                errores += "- Email no válido\n";
                            }

                            if (error == false) {
                                em.getTransaction().begin();
                                em.persist(cliente);
                                em.getTransaction().commit();
                            } else {
                                Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                                alerta2.showAndWait();
                            }

                        } catch (RollbackException ex) {
                            Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, "No se ha podido guardar el cliente");
                            alerta2.showAndWait();
                        }

                        if (error == false) {
                            Stage stage = (Stage) saveRecord.getScene().getWindow();
                            stage.close();
                        }
                    }
                }
            }
        }
    }

    
    @FXML
    private void onActionSave(ActionEvent event) {
        boolean error = false;
        if (buscarEmpresa()) {
            Alert alerta1 = new Alert(Alert.AlertType.ERROR, "El cliente ya existe en la base de datos");
            Optional<ButtonType> result = alerta1.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) cancelRecord.getScene().getWindow();
                stage.close();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Has verificado que los datos introducidos para el alta del cliente son correctos?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {

                if (error == false) {
                    try {
                        String errores = "";
                        Cliente cliente = new Cliente();

                        if ((nombre.getText() != null) && !nombre.getText().isEmpty()) {
                            cliente.setNombre(nombre.getText());
                        } else {
                            error = true;
                            errores += "- Nombre del cliente no válido\n";
                        }

                        if (((apellido.getText() != null) && !apellido.getText().isEmpty())) {
                            cliente.setApellidos(apellido.getText());
                        } else {
                            error = true;
                            errores += "- Apellidos no válido\n";
                        }

                        if (((empresa.getText() != null) && !empresa.getText().isEmpty())) {
                            cliente.setEmpresa(empresa.getText());
                        } else {
                            error = true;
                            errores += "- Empresa no válida\n";
                        }

                        if (((direccion.getText() != null) && !direccion.getText().isEmpty())) {
                            cliente.setDireccion(direccion.getText());
                        } else {
                            error = true;
                            errores += "- Dirección no válida\n";
                        }

                        if (((telefono.getText() != null) && !telefono.getText().isEmpty()) && telefono.getText().matches("[0-9]*")) {
                            cliente.setTelefono(telefono.getText());
                        } else {
                            error = true;
                            errores += "- Teléfono no válido\n";
                        }

                        if (((email.getText() != null) && !email.getText().isEmpty())) {
                            cliente.setCorreo(email.getText());
                        } else {
                            error = true;
                            errores += "- Email no válido\n";
                        }

                        if (error == false) {
                            em.getTransaction().begin();
                            em.persist(cliente);
                            em.getTransaction().commit();
                        } else {
                            Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                            alerta2.showAndWait();
                        }

                    } catch (RollbackException ex) {
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, "No se ha podido guardar el cliente");
                        alerta2.showAndWait();
                    }

                    if (error == false) {
                        Stage stage = (Stage) saveRecord.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }

    /*Acciones para el boton Cancelar*/
    @FXML
    private void onKeyPressedCancel(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere cancelar el alta del cliente?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) cancelRecord.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere cancelar el alta del cliente?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelRecord.getScene().getWindow();
            stage.close();
        }
    }

    //Busqueda de la empresa
    private boolean buscarEmpresa() {
        boolean existe = false;
        if (empresa.getText().matches("[a-zA-Zá, é, í, ó, ú, Á, É, Í, Ó, Ú]*")) {
            //Consulta el dni
            Query queryFindByDNI = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", empresa.getText());
            listaClientes = queryFindByDNI.getResultList();

            if (listaClientes != null && !listaClientes.isEmpty()) {
                existe = true;
            } else {
                existe = false;
            }
        }
        return existe;
    }
}
