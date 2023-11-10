/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

import gestorbd.Conexion;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bryam
 */
public class ViewBasesDatosController implements Initializable {

    @FXML
    private Button BT_UsarBD;
    @FXML
    private Button BT_CrearBD;
    @FXML
    private Button BT_EliminarBD;
    @FXML
    private Button BT_Salir;
    @FXML
    private Button BT_Volver;
    @FXML
    private TableView<String> TV_DB;

    private Connection cx;

    private String baseSelect;
    
    private Stage stage2;
    Stage currentStage = stage2;

    public Connection getCx() {
        return cx;
    }

    public void setCx(Connection cx) {
        this.cx = cx;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<String, String> BC_Bases = new TableColumn<>("Bases de datos");
        BC_Bases.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        TV_DB.getColumns().add(BC_Bases);
        BC_Bases.prefWidthProperty().bind(TV_DB.widthProperty());
    }

    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void UsarBD(ActionEvent event) {
        System.out.println(baseSelect);
    }

    @FXML
    private void CrearBD(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Base de Datos");
        dialog.setHeaderText("Ingrese el nombre de la base de datos");
        dialog.setContentText("Nombre:");

        dialog.showAndWait().ifPresent(nombreBaseDatos -> {
            if (!nombreBaseDatos.trim().isEmpty()) {
                try {
                    Connection connection = cx;
                    if (connection != null) {
                        Statement statement = connection.createStatement();
                        String sql = "CREATE DATABASE " + nombreBaseDatos;
                        statement.executeUpdate(sql);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Nueva Base de Datos");
                        alert.setHeaderText("La base de datos se creó exitosamente.");
                        alert.setContentText("Nombre: " + nombreBaseDatos);
                        alert.showAndWait();
                        cargarBD();
                    } else {
                        mostrarAlertaError("No se pudo establecer la conexión");
                    }
                } catch (SQLException e) {
                    mostrarAlertaError(e.getMessage());
                }
            } else {
                mostrarAlertaError("Debe ingresar un nombre de base de datos válido.");
            }
        });
    }

    @FXML
    private void EliminarBD(ActionEvent event) {

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar base de datos");
        confirmacion.setContentText("¿Estás seguro de que quieres eliminar la base de datos '" + baseSelect + "'?");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                Connection connection = cx;
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String sql = "DROP DATABASE " + baseSelect;
                    statement.executeUpdate(sql);

                    // Mostrar una alerta de éxito
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Base de Datos Eliminada");
                    alert.setHeaderText("La base de datos se eliminó exitosamente.");
                    alert.setContentText("Nombre: " + baseSelect);
                    alert.showAndWait();
                    cargarBD();
                } else {
                    mostrarAlertaError("No se pudo establecer la conexión");
                }
            } catch (SQLException e) {
                mostrarAlertaError(e.getMessage());
            }
        }
    }

    @FXML
    private void Volver(ActionEvent event) {
        Stage stage = (Stage) this.BT_Volver.getScene().getWindow();
        stage.close();
    }

    public void cargarBD() {
        try {
            if (cx != null) {
                PreparedStatement preparedStatement = cx.prepareStatement("SELECT schema_name FROM information_schema.schemata");
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> basesDeDatos = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String nombre = resultSet.getString("schema_name");
                    basesDeDatos.add(nombre);
                    System.out.println(nombre);
                }
                TV_DB.setItems(basesDeDatos);
                
                BT_UsarBD.setDisable(true);
                BT_EliminarBD.setDisable(true);

                TV_DB.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    BT_UsarBD.setDisable(false);
                    BT_EliminarBD.setDisable(false);
                    baseSelect = TV_DB.getSelectionModel().getSelectedItem();
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No se pudo establecer la conexión");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println("El objeto conector no ha sido configurado correctamente.");
        }
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("mensaje");
        alert.showAndWait();
    }

}
