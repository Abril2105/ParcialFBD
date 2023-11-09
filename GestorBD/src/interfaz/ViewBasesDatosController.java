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
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Button BT_EditarBD;
    @FXML
    private Button BT_Salir;
    @FXML
    private Button BT_Volver;
    @FXML
    private TableView<String> TV_DB;
    
    private Connection cx;
    
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
    }

    @FXML
    private void CrearBD(ActionEvent event) {
    }

    @FXML
    private void EliminarBD(ActionEvent event) {
    }

    @FXML
    private void EditarBD(ActionEvent event) {
    }

    @FXML
    private void Volver(ActionEvent event) {
        Stage stage = (Stage) this.BT_Volver.getScene().getWindow();
        stage.close();
    }
    private Stage stage2;
    Stage currentStage = stage2;
    
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
}
