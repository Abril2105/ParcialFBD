/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bryam
 */
public class ViewTablasController implements Initializable {

    //ID FXML
    @FXML
    private Button BT_UsarTabla;
    @FXML
    private Button BT_CrearTabla;
    @FXML
    private Button BT_EliminarTabla;
    @FXML
    private Button BT_Salir;
    @FXML
    private Button BT_Volver;
    @FXML
    private TableView<String> TV_Tablas;

    //Variables
    private Connection cx;
    private String baseSelect;
    private String tablaSelect;

    //Getters y Setters
    public Connection getCx() {
        return cx;
    }

    public void setCx(Connection cx) {
        this.cx = cx;
    }

    public String getBaseSelect() {
        return baseSelect;
    }

    public void setBaseSelect(String baseSelect) {
        this.baseSelect = baseSelect;
    }

    public String getTablaSelect() {
        return tablaSelect;
    }

    public void setTablaSelect(String tablaSelect) {
        this.tablaSelect = tablaSelect;
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<String, String> TC_Tablas = new TableColumn<>("Tablas");
        TC_Tablas.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        TV_Tablas.getColumns().add(TC_Tablas);
        TC_Tablas.prefWidthProperty().bind(TV_Tablas.widthProperty());
    }

    @FXML
    private void UsarTabla(ActionEvent event) {
        System.out.println(getTablaSelect());
    }

    @FXML
    private void CrearTabla(ActionEvent event) {
    }

    @FXML
    private void EliminarTabla(ActionEvent event) {
    }

    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void Volver(ActionEvent event) {
        Stage stage = (Stage) this.BT_Volver.getScene().getWindow();
        stage.close();
    }

    public void cargarTablas() {
        try {
            PreparedStatement preparedStatement = cx.prepareStatement("SHOW TABLES IN " + baseSelect);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<String> tablas = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String nombreTabla = resultSet.getString(1);
                tablas.add(nombreTabla);
                System.out.println(nombreTabla);
            }
            TV_Tablas.setItems(tablas);

            BT_UsarTabla.setDisable(true);
            BT_EliminarTabla.setDisable(true);
            
            TV_Tablas.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                BT_UsarTabla.setDisable(false);
                BT_EliminarTabla.setDisable(false);
                setTablaSelect(TV_Tablas.getSelectionModel().getSelectedItem());
            });

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println("El objeto conector no ha sido configurado correctamente.");
        }
    }
}
