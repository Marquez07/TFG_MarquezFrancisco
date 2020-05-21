/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import Entidades.Cliente;
import Entidades.Pedido;
import Entidades.Producto;
import java.math.BigDecimal;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class EditarPedidoController implements Initializable {

    private EntityManager em;
    private List<Cliente> listaClientes;
    private List<Pedido> listaFechas;
    private List<Producto> listaProductos;

    private int cantidadStock = 0;
    private int cantidadPedido = 0;
    private int postVenta = 0;

    @FXML
    private TableView<Producto> tabla;
    @FXML
    private TextField tfEmpresa;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCantidad;
    @FXML
    private Button botonGuardar;
    @FXML
    private TableColumn<Producto, String> plantaCol;
    @FXML
    private TableColumn<Producto, Integer> cantidadCol;
    @FXML
    private TableColumn<Producto, BigDecimal> precioCol;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonLimpiar;
    @FXML
    private Button addPlanta;

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

    public void buscarEmpresa() {
        Query queryFindByEmpresa = em.createNamedQuery("Cliente.findByEmpresa").setParameter("empresa", tfEmpresa.getText());
        listaClientes = queryFindByEmpresa.getResultList();

        if (listaClientes != null && !listaClientes.isEmpty()) {
            tfEmpresa.setDisable(true);
        } else {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "El cliente no existe, ¿desea salir del apartado de edición?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("SALE DE LA VENTANA");
                /*
                    Stage stage = (Stage) botonCancelar.getScene().getWindow();
                    stage.close();
                 */
            }
        }
    }

    private void buscarFecha() {
        if (dpFecha.getValue() != null) {
            LocalDate localDate = dpFecha.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);

            Query queryFindByFecha = em.createNamedQuery("Pedido.findByFechaPedido").setParameter("fechaPedido", date);
            listaFechas = queryFindByFecha.getResultList();

            if (listaFechas != null && !listaFechas.isEmpty()) {
                dpFecha.setDisable(true);
            } else {

            }
        }
    }

    @FXML
    private void keyBuscarEmpresa(KeyEvent event) {
        if (event.getCode() == ENTER) {
            buscarEmpresa();
        }
    }

    @FXML
    private void keyBuscarFecha(KeyEvent event) {
        if (event.getCode() == ENTER) {
            buscarFecha();
        }
    }

    @FXML
    private void keyGuardarVenta(KeyEvent event) {
    }

    @FXML
    private void guardarVenta(ActionEvent event) {
        boolean error = false;
        int i = 0;
        String errores = "";
        Cliente cliente = new Cliente();
        Producto planta = new Producto();
        Pedido pedido = new Pedido();

        if (error == false) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quiere realizar este pedido?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
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

                    if (tfNombre.getText() != null && !tfNombre.getText().isEmpty()) {
                        planta.setNombre(tfNombre.getText());
                        pedido.setNombrePlanta(planta);
                    } else {
                        error = true;
                        errores += "- El campo Nombre de la planta está vacío";
                    }

                    Query queryFindByNombre = em.createNamedQuery("Producto.findByNombre").setParameter("nombre", tfNombre.getText());
                    listaProductos = queryFindByNombre.getResultList();

                    if (listaProductos != null && !listaProductos.isEmpty()) {
                        cantidadStock = Integer.parseInt(String.valueOf(listaProductos.get(0).getCantidadProducto()));
                        if (tfCantidad.getText() != null && !tfCantidad.getText().isEmpty()) {
                            cantidadPedido = Integer.parseInt(tfCantidad.getText());
                        } else {
                            cantidadPedido = 0;
                        }

                        if (cantidadPedido > cantidadStock) {
                            error = true;
                            errores += "- La cantidad de plantas que desea vender es superior a la cantidad de plantas que tenemos en STOCK\n";
                        } else {
                            postVenta = cantidadStock - cantidadPedido;
                            if (tfCantidad.getText() == null && tfCantidad.getText().isEmpty()) {
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
                        data.add(new Producto(tfNombre.getText(), planta.getCantidadProducto(), planta.getPrecio()));
                        System.out.println(data.toString());
                        tfNombre.setText("");
                        tfCantidad.setText("");

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

    private void limpiar() {
        tfEmpresa.setText("");
        tfNombre.setText("");
        tfCantidad.setText("");
        dpFecha.setValue(LocalDate.now());
        tfEmpresa.setDisable(false);
        tfNombre.setDisable(false);
        //tfCantidad.setDisable(true);
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
