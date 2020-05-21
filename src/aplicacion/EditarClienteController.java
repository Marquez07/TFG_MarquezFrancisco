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
public class EditarClienteController implements Initializable {

    private List<Cliente> listaCliente;
    private EntityManager em;
    @FXML
    private Button botonLimpiar;
    @FXML
    private TextField tfCliente;
    @FXML
    private Button botonBuscar;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidos;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonCancelar;

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

    private void buscarCliente() throws IOException {
        if (tfCliente.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
            //Consulta el dni
            tfCliente.setDisable(true);
            Query queryFindByEmpresa = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", tfCliente.getText());
            listaCliente = queryFindByEmpresa.getResultList();

            if (listaCliente != null && !listaCliente.isEmpty()) {
                mostrar();
                tfNombre.setText(listaCliente.get(0).getNombre());
                tfApellidos.setText(listaCliente.get(0).getApellidos());
                tfTelefono.setText(listaCliente.get(0).getTelefono());
                tfDireccion.setText(listaCliente.get(0).getDireccion());
                tfEmail.setText(listaCliente.get(0).getCorreo());
            } else {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "El cliente que busca no existe en la base de datos, ¿quiere añadirlo a la base de datos?");
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage1 = (Stage) botonCancelar.getScene().getWindow();
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
    private void onKeyGuardar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            boolean error = false;
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Has verificado que los datos introducidos para el alta del cliente son correctos?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {

                if (error == false) {
                    try {
                        String errores = "";
                        Cliente cliente = new Cliente();

                        if ((tfNombre.getText() != null) && !tfNombre.getText().isEmpty()) {
                            cliente.setNombre(tfNombre.getText());
                        } else {
                            error = true;
                            errores += "- Nombre del cliente no válido\n";
                        }

                        if (((tfApellidos.getText() != null) && !tfApellidos.getText().isEmpty())) {
                            cliente.setApellidos(tfApellidos.getText());
                        } else {
                            error = true;
                            errores += "- Apellidos no válido\n";
                        }

                        if (((tfCliente.getText() != null) && !tfCliente.getText().isEmpty())) {
                            cliente.setEmpresa(tfCliente.getText());
                        } else {
                            error = true;
                            errores += "- Empresa no válida\n";
                        }

                        if (((tfDireccion.getText() != null) && !tfDireccion.getText().isEmpty())) {
                            cliente.setDireccion(tfDireccion.getText());
                        } else {
                            error = true;
                            errores += "- Dirección no válida\n";
                        }

                        if (((tfTelefono.getText() != null) && !tfTelefono.getText().isEmpty()) && tfTelefono.getText().matches("[0-9]*")) {
                            cliente.setTelefono(tfTelefono.getText());
                        } else {
                            error = true;
                            errores += "- Teléfono no válido\n";
                        }

                        if (((tfEmail.getText() != null) && !tfEmail.getText().isEmpty())) {
                            cliente.setCorreo(tfEmail.getText());
                        } else {
                            error = true;
                            errores += "- Email no válido\n";
                        }

                        if (error == false) {
                            em.getTransaction().begin();
                            em.merge(cliente);
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
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Has verificado que los datos introducidos para el alta del cliente son correctos?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (error == false) {
                try {
                    String errores = "";
                    Cliente cliente = new Cliente();

                    if ((tfNombre.getText() != null) && !tfNombre.getText().isEmpty()) {
                        cliente.setNombre(tfNombre.getText());
                    } else {
                        error = true;
                        errores += "- Nombre del cliente no válido\n";
                    }

                    if (((tfApellidos.getText() != null) && !tfApellidos.getText().isEmpty())) {
                        cliente.setApellidos(tfApellidos.getText());
                    } else {
                        error = true;
                        errores += "- Apellidos no válido\n";
                    }

                    if (((tfCliente.getText() != null) && !tfCliente.getText().isEmpty())) {
                        cliente.setEmpresa(tfCliente.getText());
                    } else {
                        error = true;
                        errores += "- Empresa no válida\n";
                    }

                    if (((tfDireccion.getText() != null) && !tfDireccion.getText().isEmpty())) {
                        cliente.setDireccion(tfDireccion.getText());
                    } else {
                        error = true;
                        errores += "- Dirección no válida\n";
                    }

                    if (((tfTelefono.getText() != null) && !tfTelefono.getText().isEmpty()) && tfTelefono.getText().matches("[0-9]*")) {
                        cliente.setTelefono(tfTelefono.getText());
                    } else {
                        error = true;
                        errores += "- Teléfono no válido\n";
                    }

                    if (((tfEmail.getText() != null) && !tfEmail.getText().isEmpty())) {
                        cliente.setCorreo(tfEmail.getText());
                    } else {
                        error = true;
                        errores += "- Email no válido\n";
                    }

                    if (error == false) {
                        em.getTransaction().begin();
                        em.merge(cliente);
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
                    Stage stage = (Stage) botonGuardar.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    private void mostrar() {
        tfNombre.setDisable(false);
        tfApellidos.setDisable(false);
        tfTelefono.setDisable(false);
        tfDireccion.setDisable(false);
        tfEmail.setDisable(false);
    }

    @FXML
    private void onKeyBuscarEnter(KeyEvent event) throws IOException {
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                    buscarCliente();
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
                    buscarCliente();
                    break;
                default:
                    break;
            }
        }
    }
    
    private void limpiar() {
        tfNombre.setText("");
        tfApellidos.setText("");
        tfDireccion.setText("");
        tfEmail.setText("");
        tfCliente.setText("");
        tfTelefono.setText("");
        tfCliente.setDisable(false);
        tfNombre.setDisable(true);
        tfApellidos.setDisable(true);
        tfDireccion.setDisable(true);
        tfEmail.setDisable(true);
        tfTelefono.setDisable(true);

        
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
}
