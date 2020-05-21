/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Cliente;
import Entidades.Pedido;
import Entidades.Producto;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.TAB;
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
public class CrearPedidoController implements Initializable {

    private EntityManager em;
    private List<Cliente> listaEmpresa;
    private List<Producto> listaProductos;
    private int cantidadStock = 0;
    private int cantidadPedido = 0;
    private int postVenta = 0;
    @FXML
    private TextField tfEmpresa;
    @FXML
    private Button botonBuscarEmpresa;
    @FXML
    private TextField tfPlanta;
    @FXML
    private TextField tfNUnidades;
    @FXML
    private Button botonLimpiar;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonCancelar;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private Button addPlanta;
    @FXML
    private TableView<Producto> tabla;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dpFecha.setValue(LocalDate.now());
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @FXML
    private void onKeyBuscarEmpresaEnter(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER || event.getCode() == TAB) {
            if (tfEmpresa.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
                //Consulta el dni
                Query queryFindByEmpresa = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", tfEmpresa.getText());
                listaEmpresa = queryFindByEmpresa.getResultList();

                if (listaEmpresa != null && !listaEmpresa.isEmpty()) {
                    tfEmpresa.setDisable(true);
                } else {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La empresa no existe en la base de datos, ¿desea registrar a esta empresa?");
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
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/add.png")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(new Scene(rootMain));
                        stage.show();
                    }
                }
            }
        }
    }

    @FXML
    private void onKeyBuscarEmpresa(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            if (tfEmpresa.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
                //Consulta el dni
                Query queryFindByEmpresa = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", tfEmpresa.getText());
                listaEmpresa = queryFindByEmpresa.getResultList();

                if (listaEmpresa != null && !listaEmpresa.isEmpty()) {
                    tfEmpresa.setDisable(true);
                } else {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La empresa no existe en la base de datos, ¿desea registrar a esta empresa?");
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
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/add.png")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(new Scene(rootMain));
                        stage.show();
                    }
                }
            }
        }
    }

    @FXML
    private void onActionBuscarEmpresa(ActionEvent event) throws IOException {
        if (tfEmpresa.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
            //Consulta el dni
            Query queryFindByEmpresa = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", tfEmpresa.getText());
            listaEmpresa = queryFindByEmpresa.getResultList();

            if (listaEmpresa != null && !listaEmpresa.isEmpty()) {
                tfEmpresa.setDisable(true);
            } else {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La empresa no existe en la base de datos, ¿desea registrar a esta empresa?");
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
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/add.png")));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(rootMain));
                    stage.show();
                }
            }
        }
    }

    @FXML
    private void onKeyBuscarPlantaEnter(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER || event.getCode() == TAB) {
            if (tfPlanta.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
                //Consulta el dni
                Query queryFindByPlanta = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
                listaProductos = queryFindByPlanta.getResultList();

                if (listaProductos != null && !listaProductos.isEmpty()) {
                    tfPlanta.setDisable(true);
                    tfNUnidades.setText(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                    tfNUnidades.setDisable(false);
                } else {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La planta no existe en la base de datos, ¿desea registrar a esta planta?");
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
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/plant.png")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(new Scene(rootMain));
                        stage.show();
                    }
                }
            }
        }
    }

    private void onKeyBuscarPlanta(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            if (tfPlanta.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
                //Consulta el dni
                Query queryFindByPlanta = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
                listaProductos = queryFindByPlanta.getResultList();

                if (listaProductos != null && !listaProductos.isEmpty()) {
                    tfPlanta.setDisable(true);
                    tfNUnidades.setText(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                    tfNUnidades.setDisable(false);
                } else {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La planta no existe en la base de datos, ¿desea registrar a esta planta?");
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
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/plant.png")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(new Scene(rootMain));
                        stage.show();
                    }
                }
            }
        }
    }

    private void onActionBuscarPlanta(ActionEvent event) throws IOException {
        if (tfPlanta.getText().matches("[a-zA-Zá,é,í,ó,ú,Á,É,Í,Ó,Ú]*")) {
            //Consulta el dni
            Query queryFindByPlanta = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
            listaProductos = queryFindByPlanta.getResultList();

            if (listaProductos != null && !listaProductos.isEmpty()) {
                tfPlanta.setDisable(true);
                tfNUnidades.setText(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                tfNUnidades.setDisable(false);
            } else {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "La planta no existe en la base de datos, ¿desea registrar a esta planta?");
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.get() == ButtonType.CANCEL) {
                    alerta.close();
                } else if (result.get() == ButtonType.OK) {
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
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/plant.png")));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(rootMain));
                    stage.show();
                }
            }
        }
    }

    @FXML
    private void onKeyLimpiar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres limpiar el pedido?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                limpiar();
            }
        }
    }

    @FXML
    private void onActionLimpiar(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres limpiar el pedido?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            limpiar();
        }
    }

    @FXML
    private void onKeyGuardar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            boolean error = false;
            String errores = "";
            Cliente cliente = new Cliente();
            Producto planta = new Producto();
            Pedido pedido = new Pedido();

            if (error == false) {

                try {
                    if (dpFecha.getValue() != null) {
                        LocalDate localDate = dpFecha.getValue();
                        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
                        Instant instant = zonedDateTime.toInstant();
                        Date date = Date.from(instant);
                        pedido.setFechaPedido(date);
                    } else {
                        error = true;
                        errores += "- Fecha de llegada no válida\n";
                    }

                    if (tfEmpresa.getText() != null && !tfEmpresa.getText().isEmpty()) {
                        cliente.setEmpresa(tfEmpresa.getText());
                        pedido.setEmpresa(cliente);
                    } else {
                        error = true;
                        errores += "- El campo Nombre de la empresa está vacío";
                    }

                    if (tfPlanta.getText() != null && !tfPlanta.getText().isEmpty()) {
                        planta.setNombre(tfPlanta.getText());
                        pedido.setNombrePlanta(planta);
                    } else {
                        error = true;
                        errores += "- El campo Nombre de la planta está vacío";
                    }

                    Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
                    listaProductos = queryFindByNombre.getResultList();

                    if (listaProductos != null && !listaProductos.isEmpty()) {
                        cantidadStock = Integer.parseInt(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                        if (tfNUnidades.getText() != null && !tfNUnidades.getText().isEmpty()) {
                            cantidadPedido = Integer.parseInt(tfNUnidades.getText());
                        } else {
                            cantidadPedido = 0;
                        }

                        if (cantidadPedido > cantidadStock) {
                            error = true;
                            errores += "- La cantidad de plantas que desea vender es superior a la cantidad de plantas que tenemos en STOCK\n";
                        } else {
                            postVenta = cantidadStock - cantidadPedido;
                            if (tfNUnidades.getText() == null && tfNUnidades.getText().isEmpty()) {
                                pedido.setCantidad(0);
                            } else {
                                pedido.setCantidad(cantidadPedido);

                            }
                            planta.setNombre(listaProductos.get(0).getNombre());
                            planta.setSeccion(listaProductos.get(0).getSeccion());
                            planta.setPasillo(listaProductos.get(0).getPasillo());
                            planta.setEspacio(listaProductos.get(0).getEspacio());
                            planta.setPrecio(listaProductos.get(0).getPrecio());
                            planta.setCantidadProducto(postVenta);
                        }
                    }

                    if (error == false) {
                        em.getTransaction().begin();
                        em.persist(pedido);
                        em.merge(planta);
                        em.getTransaction().commit();

                        ObservableList<Producto> data = tabla.getItems();
                        data.add(new Producto(tfPlanta.getText(), planta.getCantidadProducto(), planta.getPrecio()));
                        System.out.println(data.toString());
                        tfPlanta.setText("");
                        tfNUnidades.setText("");
                        tfPlanta.setDisable(false);
                    } else {
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                        alerta2.showAndWait();
                    }

                } catch (RollbackException ex) {
                    Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido guardar el producto");
                    alerta2.showAndWait();
                }
            }
        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {
        boolean error = false;
        String errores = "";
        Cliente cliente = new Cliente();
        Producto planta = new Producto();
        Pedido pedido = new Pedido();

        if (error == false) {

            try {
                if (dpFecha.getValue() != null) {
                    LocalDate localDate = dpFecha.getValue();
                    ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
                    Instant instant = zonedDateTime.toInstant();
                    Date date = Date.from(instant);
                    pedido.setFechaPedido(date);
                } else {
                    error = true;
                    errores += "- Fecha de llegada no válida\n";
                }

                if (tfEmpresa.getText() != null && !tfEmpresa.getText().isEmpty()) {
                    cliente.setEmpresa(tfEmpresa.getText());
                    pedido.setEmpresa(cliente);
                } else {
                    error = true;
                    errores += "- El campo Nombre de la empresa está vacío";
                }

                if (tfPlanta.getText() != null && !tfPlanta.getText().isEmpty()) {
                    planta.setNombre(tfPlanta.getText());
                    pedido.setNombrePlanta(planta);
                } else {
                    error = true;
                    errores += "- El campo Nombre de la planta está vacío";
                }

                Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
                listaProductos = queryFindByNombre.getResultList();

                if (listaProductos != null && !listaProductos.isEmpty()) {
                    cantidadStock = Integer.parseInt(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                    if (tfNUnidades.getText() != null && !tfNUnidades.getText().isEmpty()) {
                        cantidadPedido = Integer.parseInt(tfNUnidades.getText());
                    } else {
                        cantidadPedido = 0;
                    }

                    if (cantidadPedido > cantidadStock) {
                        error = true;
                        errores += "- La cantidad de plantas que desea vender es superior a la cantidad de plantas que tenemos en STOCK\n";
                    } else {
                        postVenta = cantidadStock - cantidadPedido;
                        if (tfNUnidades.getText() == null && tfNUnidades.getText().isEmpty()) {
                            pedido.setCantidad(0);
                        } else {
                            pedido.setCantidad(cantidadPedido);
                        }
                        planta.setNombre(listaProductos.get(0).getNombre());
                        planta.setSeccion(listaProductos.get(0).getSeccion());
                        planta.setPasillo(listaProductos.get(0).getPasillo());
                        planta.setEspacio(listaProductos.get(0).getEspacio());
                        planta.setPrecio(listaProductos.get(0).getPrecio());
                        planta.setCantidadProducto(postVenta);
                    }
                }

                if (error == false) {
                    em.getTransaction().begin();
                    em.persist(pedido);
                    em.merge(planta);
                    em.getTransaction().commit();

                    ObservableList<Producto> data = tabla.getItems();
                    data.add(new Producto(tfPlanta.getText(), planta.getCantidadProducto(), planta.getPrecio()));
                    System.out.println(data.toString());
                    tfPlanta.setText("");
                    tfNUnidades.setText("");
                    tfPlanta.setDisable(false);
                } else {
                    Alert alerta2 = new Alert(Alert.AlertType.INFORMATION, errores);
                    alerta2.showAndWait();
                }

            } catch (RollbackException ex) {
                Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido guardar el producto");
                alerta2.showAndWait();
            }
        }
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

    private void limpiar() {
        tfEmpresa.setText("");
        tfPlanta.setText("");
        tfNUnidades.setText("");
        tfEmpresa.setDisable(false);
        tfPlanta.setDisable(false);
        //tfNUnidades.setDisable(true);
    }

    private boolean buscarEmpresa() throws IOException {
        boolean existe = false;
        if (!tfPlanta.getText().isEmpty()) {
            //Consulta el dni
            Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
            listaEmpresa = queryFindByNombre.getResultList();

            if (listaEmpresa != null && !listaEmpresa.isEmpty()) {
                existe = true;
            } else {
                existe = false;
            }
        }
        return existe;
    }

    private boolean buscarPlanta() throws IOException {
        boolean existe = false;
        if (!tfPlanta.getText().isEmpty()) {
            //Consulta el dni
            Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfPlanta.getText());
            listaProductos = queryFindByNombre.getResultList();

            if (listaProductos != null && !listaProductos.isEmpty()) {
                existe = true;
            } else {
                existe = false;
            }
        }
        return existe;
    }

    @FXML
    private void onKeySalirGuardar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quiere realizar este pedido?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) botonCancelar.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionSalirGuardar(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quiere realizar este pedido?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        }
    }
}
