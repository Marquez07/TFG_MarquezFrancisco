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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.ENTER;
import javax.persistence.EntityManager;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Francisco
 */
public class MainController implements Initializable {

    private EntityManager em;
    private EntityManagerFactory emf;
    private String conexion;
    private String user;
    private String password;
    private Connection connect;
    @FXML
    private Button exitButton;
    @FXML
    private Button botonCrearPlanta;
    @FXML
    private Button botonBuscarPlanta;
    @FXML
    private Button botonEditarPlanta;
    @FXML
    private Button botonCrearCliente;
    @FXML
    private Button botonBuscarCliente;
    @FXML
    private Button botonEditarCliente;
    @FXML
    private Button botonCrearPedido;
    @FXML
    private Button botonBuscarPedido;
    @FXML
    private Button botonEditarPedido;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        emf = Persistence.createEntityManagerFactory("TFG_MarquezFranciscoPU");
        em = emf.createEntityManager();
        this.setEntityManager(em);
    }

    /*Parar la conexión con la Base de datos*/
    public void stop() throws Exception {
        try {
            //DriverManager.getConnection("jdbc:derby:BDHotel;shutdown=true");
            conexion = em.getProperties().get("javax.persistence.jdbc.url").toString();
            DriverManager.getConnection(conexion + ";shutdown=true");
            em.close();
            emf.close();
        } catch (SQLException ex) {
            System.out.println("No se ha podido cerrar la conexión\n");
        }
    }

    /*Configuramos el EntityManager*/
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
        try {
            conexion = em.getProperties().get("javax.persistence.jdbc.url").toString();
            user = em.getProperties().get("javax.persistence.jdbc.user").toString();
            password = "";
            connect = DriverManager.getConnection(conexion, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Acciones para el boton Añadir Planta*/
    @FXML
    private void onKeyCrearPlanta(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewCrearPlanta();
        }
    }

    @FXML
    private void onActionCrearPlanta(ActionEvent event) throws IOException {
        viewCrearPlanta();
    }

    /*Acciones para el boton Buscar Planta*/
    @FXML
    private void onKeyBuscarPlanta(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewBuscarPlanta();
        }
    }

    @FXML
    private void onClickBuscarPlanta(ActionEvent event) throws IOException {
        viewBuscarPlanta();
    }

    /*Acciones para el boton editar una planta*/
    @FXML
    private void onKeyEditarPlanta(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewEditarPlanta();
        }
    }

    @FXML
    private void onClickEditarPlanta(ActionEvent event) throws IOException {
        viewEditarPlanta();
    }

    /*Acciones para el boton Crear Nuevo Cliente*/
    @FXML
    private void onKeyCrearCliente(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewCrearCliente();
        }
    }

    @FXML
    private void onActionCrearCliente(ActionEvent event) throws IOException {
        viewCrearCliente();
    }

    /*Acciones para el boton Buscar Cliente*/
    @FXML
    private void onKeyBuscarCliente(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewBuscarCliente();
        }
    }

    @FXML
    private void onClickBuscarCliente(ActionEvent event) throws IOException {
        viewBuscarCliente();
    }

    /*Acciones para el boton Editar Cliente*/
    @FXML
    private void onKeyEditarCliente(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewEditarCliente();
        }
    }

    @FXML
    private void onClickEditarCliente(ActionEvent event) throws IOException {
        viewEditarCliente();
    }

    /*Acciones para el boton Nuevo Pedido*/
    @FXML
    private void onKeyCrearPedido(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewCrearPedido();
        }
    }

    @FXML
    private void onActionCrearPedido(ActionEvent event) throws IOException {
        viewCrearPedido();
    }

    /*Acciones para el boton Buscar Pedido*/
    @FXML
    private void onKeyBuscarPedido(KeyEvent event) throws IOException, SQLException {
        if (event.getCode() == ENTER) {
            viewBuscarPedido();
        }
    }

    @FXML
    private void onClickBuscarPedido(ActionEvent event) throws IOException, SQLException {
        viewBuscarPedido();
    }

    /*Acciones para el boton Editar Pedido*/
    @FXML
    private void onKeyEditarPedido(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            viewEliminarPedido();
        }
    }

    @FXML
    private void onActionEditarPedido(ActionEvent event) throws IOException {
        viewEliminarPedido();
    }

    /*Crea la interfaz para añadir una planta*/
    private void viewCrearPlanta() throws IOException {
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

    /*Crea la interfaz para buscar una planta*/
    private void viewBuscarPlanta() throws IOException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BusquedaPlanta.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        BusquedaPlantaController busquedaPlantaController = (BusquedaPlantaController) fxmlLoader.getController();

        busquedaPlantaController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Buscar Planta");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/search_planta.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Crea la vista para la edición de una planta*/
    private void viewEditarPlanta() throws IOException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditarPlanta.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        EditarPlantaController addPlantaController = (EditarPlantaController) fxmlLoader.getController();

        addPlantaController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Editar Planta");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/edit_planta.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Crea la vista para añadir un cliente*/
    private void viewCrearCliente() throws IOException {
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

    /*Crea la vista para buscar un cliente*/
    private void viewBuscarCliente() throws IOException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BusquedaCliente.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        BusquedaClienteController searchClienteController = (BusquedaClienteController) fxmlLoader.getController();

        searchClienteController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Buscar Cliente");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/search_cliente.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Crea la vista para editar un cliente*/
    private void viewEditarCliente() throws IOException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditarCliente.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        EditarClienteController addPlantaController = (EditarClienteController) fxmlLoader.getController();

        addPlantaController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Editar Cliente");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/edit_cliente.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Crea la interfaz para añadir un pedido*/
    private void viewCrearPedido() throws IOException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CrearPedido.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        CrearPedidoController addPedidoController = (CrearPedidoController) fxmlLoader.getController();

        addPedidoController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Nuevo Pedido");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/add_pedido.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Crea la interfaz para buscar un pedido*/
    private void viewBuscarPedido() throws IOException, SQLException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BusquedaPedido.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        BusquedaPedidoController searchPedidoController = (BusquedaPedidoController) fxmlLoader.getController();

        searchPedidoController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Buscar Pedido");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/search_pedido.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Crea la interfaz para editar un pedido*/
    private void viewEliminarPedido() throws IOException {
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditarPedido.fxml"));

        Pane root = fxmlLoader.load();
        rootMain.getChildren().add(root);

        EditarPedidoController editPedidoController = (EditarPedidoController) fxmlLoader.getController();

        editPedidoController.setEntityManager(em);

        Stage stage = new Stage();
        stage.setTitle("Editar Pedido");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/delete_pedido.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootMain));
        stage.show();
    }

    /*Acciones para el boton EXIT*/
    @FXML
    private void onKeyExit(KeyEvent event) throws Exception {
        if (event.getCode() == ENTER) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere cerrar la aplicación?");
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                stop();
                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void onActionExit(ActionEvent event) throws Exception {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quiere cerrar la aplicación?");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            stop();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }
}
