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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bryam
 */
public class ViewLoginController implements Initializable {

    @FXML
    private TextField TF_Usuario;
    @FXML
    private TextField TF_Contraseña;
    @FXML
    private Button BT_Salir;
    @FXML
    private Button BT_Iniciar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    

    @FXML
    private void Iniciar(ActionEvent event) {
    }
    
    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }
}
