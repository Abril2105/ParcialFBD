/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author bryam
 */
public class ViewTablasController implements Initializable {

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
    private TableView<?> TV_Tablas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void UsarTabla(ActionEvent event) {
    }

    @FXML
    private void CrearTabla(ActionEvent event) {
    }

    @FXML
    private void EliminarTabla(ActionEvent event) {
    }

    @FXML
    private void salir(ActionEvent event) {
    }

    @FXML
    private void Volver(ActionEvent event) {
    }
    
}
