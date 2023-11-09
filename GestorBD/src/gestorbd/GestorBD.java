/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorbd;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author bryam
 */
public class GestorBD extends Application{

    @Override
    public void start(Stage ventana) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/interfaz/ViewLogin.fxml")); //Arma la ventana con el XML
        Scene scene = new Scene(root);
        ventana.setScene(scene);
        ventana.setTitle("GestorBD");
        ventana.setResizable(false);  // No se puede cambiar el tamaño de la ventana
        ventana.setOnCloseRequest(event -> {event.consume();}); //Desabilitar la X
        ventana.show();   //Muestra la ventana
    }

    public static void main(String[] args) {
        launch(args);
        
        /**Conexion conexion = new Conexion();
        conexion.setBd("world");
        conexion.conectar();
        */
    }
    
}
