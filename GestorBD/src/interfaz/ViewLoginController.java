/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

import java.sql.Connection;
import gestorbd.Conexion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
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
    private Button BT_Salir;
    @FXML
    private Button BT_Iniciar;
    @FXML
    private TextField TF_Contrasena;
    @FXML
    private TextField TF_Servidor;
    @FXML
    private TextField TF_Puerto;
    
    private Conexion conexion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
    }    

    

    @FXML
    private void Iniciar(ActionEvent event) {
        conexion.setUser(TF_Usuario.getText());
        conexion.setPassword(TF_Contrasena.getText());
        conexion.setUrl(TF_Servidor.getText(),TF_Puerto.getText());
        conexion.conectar();
        if(conexion.getCx() != null){
            abrirViewBD();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error en la conexión");
            alert.show();
        }
    }
    
    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }
    private void abrirViewBD(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewBasesDatos.fxml"));
            Parent root = loader.load();
            ViewBasesDatosController viewBasesDatos = loader.getController();
            viewBasesDatos.setCx(conexion.getCx());
            viewBasesDatos.cargarBD();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);  // No se puede cambiar el tamaño de la ventana
            stage.setOnCloseRequest(event -> {event.consume();}); //Desabilitar la X
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException ex) {
            System.out.println("Error");
        }
    }
}
