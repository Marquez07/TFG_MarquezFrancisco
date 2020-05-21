/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class BusquedaPedidoController implements Initializable {

    private EntityManager em;
    private Connection connect;
    @FXML
    private TextField tfEmpresa;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private Button botonBuscar;
    @FXML
    private Button botonCancelar;

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

    public void setEntityManager(EntityManager entityManager) throws SQLException {
        this.em = entityManager;
        establecerConexion();
    }

    public Connection establecerConexion() throws SQLException {
        String conexion = em.getProperties().get("javax.persistence.jdbc.url").toString();
        String user = em.getProperties().get("javax.persistence.jdbc.user").toString();
        String password = "";
        connect = DriverManager.getConnection(conexion, user, password);
        return connect;
    }

    @FXML
    private void onKeyBuscar(KeyEvent event) throws SQLException, JRException {
        if (event.getCode() == ENTER) {
            boolean error = false;
            String errores = "";
            Map parametros = new HashMap();
            if (error == false) {
                try {
                    if (tfEmpresa.getText() != null && !tfEmpresa.getText().isEmpty()) {
                        parametros.put("empresa", tfEmpresa.getText());
                    } else {
                        error = true;
                        errores += "- El campo Nombre de la empresa está vacío";
                    }

                    if (dpFecha.getValue() != null) {
                        LocalDate localDate = dpFecha.getValue();
                        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
                        Instant instant = zonedDateTime.toInstant();
                        Date date = Date.from(instant);
                        parametros.put("fecha", date);
                    } else {
                        error = true;
                        errores += "- Fecha de llegada no válida\n";
                    }

                    if (error == false) {
                        JasperReport jr = (JasperReport) JRLoader.loadObject(BusquedaPedidoController.class.getResource("/jasper/pedidos.jasper"));
                        /*Fallo, no se como hacer para que se vea el jasperReport*/

                        JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, connect);
                        JasperViewer.viewReport(jp, false);
                    }
                } catch (RollbackException ex) {
                    Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido encontrar el pedido");
                    alerta2.showAndWait();
                }
            }
        }
    }

    @FXML
    private void onActionBuscar(ActionEvent event) throws JRException, SQLException {
        boolean error = false;
        String errores = "";
        Map parametros = new HashMap();
        if (error == false) {
            try {
                if (tfEmpresa.getText() != null && !tfEmpresa.getText().isEmpty()) {
                    String valorNombreEmpresa = tfEmpresa.getText();
                    parametros.put("Empresa", tfEmpresa.getText());
                } else {
                    error = true;
                    errores += "- El campo Nombre de la empresa está vacío";
                }

                if (dpFecha.getValue() != null) {
                    LocalDate localDate = dpFecha.getValue();
                    ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
                    Instant instant = zonedDateTime.toInstant();
                    Date date = Date.from(instant);
                    parametros.put("Fecha", date);
                } else {
                    error = true;
                    errores += "- Fecha de llegada no válida\n";
                }

                if (error == false) {
                    JasperReport jr = (JasperReport) JRLoader.loadObject(BusquedaPedidoController.class.getResource("/jasper/pedidos.jasper"));
                    /*Fallo, no se como hacer para que se vea el jasperReport*/
                    parametros.put("Logo", "src/imagenes/logo.png");
                    JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, connect);
                    JasperViewer.viewReport(jp, false);
                }
            } catch (RollbackException ex) {
                Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se ha podido encontrar el pedido");
                alerta2.showAndWait();
            }
        }
    }

    @FXML
    private void onKeyCancelar(KeyEvent event) {
        if (event.getCode() == ENTER) {
            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Stage stage = (Stage) botonCancelar.getScene().getWindow();
        stage.close();
    }
}
