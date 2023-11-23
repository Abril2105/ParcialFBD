/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author bryam
 */
public class ViewTablasController implements Initializable {

    //ID FXML
    @FXML
    private Button BT_Registro;
    @FXML
    private Button BT_Estructura;
    @FXML
    private Button BT_EliminarTabla;
    @FXML
    private Button BT_Consuta;
    @FXML
    private Button BT_CrearTabla;
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
    private void VerRegistro(ActionEvent event) throws SQLException {
        if (baseSelect != null) {
            abrirViewregistrosTabla();
        } else {
            System.out.println("Seleccione una BD");
        }
    }

    @FXML
    private void VerEstructura(ActionEvent event) throws SQLException {
        if (baseSelect != null) {
            abrirViewestructuraTabla();
        } else {
            System.out.println("Seleccione una BD");
        }
    }

    @FXML
    private void EliminarTabla(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar Tabla");
        confirmacion.setContentText("¿Estás seguro de que quieres eliminar la tabla '" + tablaSelect + "'?");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                Statement statement = cx.createStatement();
                String sqlSelectBD = "USE " + baseSelect;
                String sqlDropTable = "DROP TABLE " + tablaSelect;
                statement.executeUpdate(sqlSelectBD);
                statement.executeUpdate(sqlDropTable);

                // Mostrar una alerta de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tabla Eliminada");
                alert.setHeaderText("La Tabla se eliminó exitosamente.");
                alert.setContentText("Nombre: " + tablaSelect);
                alert.showAndWait();
                setTablaSelect(null);
                cargarTablas();
            } catch (SQLException e) {
                mostrarAlertaError(e.getMessage());
            }
        }
    }

    @FXML
    private void HacerConsulta(ActionEvent event) {
    }

    @FXML
    private void CrearTabla(ActionEvent event) {
        String nombreTabla = mostrarDialogoNombreTabla();
        if (nombreTabla != null) {
            int cantidadColumnas = mostrarDialogoCantidadColumnas();
            if (cantidadColumnas > 0) {
                List<Columna> columnas = mostrarDialogoDetallesColumnas(cantidadColumnas);
                if (!columnas.isEmpty()) {
                    try {
                        Connection connection = cx;
                        if (connection != null) {
                            // Preparar la sentencia SQL para crear la nueva tabla
                            String crear = "CREATE TABLE " + nombreTabla + " (";
                            for (int i = 0; i < columnas.size(); i++) {
                                Columna columna = columnas.get(i);
                                if(columna.getLlave() == null){
                                    crear = crear + columna.getNombre() + " " + columna.getTipo();
                                }else{
                                    crear = crear + columna.getNombre() + " " + columna.getTipo() + " " + columna.getLlave();
                                }
                                
                                if (i < columnas.size() - 1) {
                                    crear = crear + ", ";
                                }
                            }
                            crear = crear + ")";
                            System.out.println(crear);

                            Statement Statement = connection.createStatement();
                            connection.createStatement().executeUpdate("USE " + baseSelect);
                            Statement.executeUpdate(crear);

                            // Actualizar la lista de tablas después de crear la nueva tabla
                            cargarTablas();

                            // Mostrar un mensaje de éxito
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Creación completa");
                            alert.setHeaderText("La tabla se creó correctamente");
                            alert.setContentText("Nombre: " + nombreTabla);
                            alert.showAndWait();
                        } else {
                            mostrarAlertaError("No se pudo establecer la conexión");
                        }
                    } catch (SQLException e) {
                        mostrarAlertaError(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
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

            BT_Registro.setDisable(true);
            BT_Estructura.setDisable(true);
            BT_EliminarTabla.setDisable(true);

            TV_Tablas.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                BT_Registro.setDisable(false);
                BT_Estructura.setDisable(false);
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

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    //Crear tablas y columnas
    private String mostrarDialogoNombreTabla() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Tabla");
        dialog.setHeaderText("Ingrese el nombre de la nueva tabla");
        dialog.setContentText("Nombre:");

        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }

    private int mostrarDialogoCantidadColumnas() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Tabla");
        dialog.setHeaderText("Ingrese la cantidad de columnas");
        dialog.setContentText("Cantidad:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            try {
                return Integer.parseInt(resultado.get());
            } catch (NumberFormatException e) {
                // El valor ingresado no es un número válido
                mostrarAlertaError("Ingrese un valor numérico válido");
            }
        }
        return 0;
    }

    private List<Columna> mostrarDialogoDetallesColumnas(int cantidadColumnas) {
        List<Columna> columnas = new ArrayList<>();

        for (int i = 0; i < cantidadColumnas; i++) {
            Dialog<Columna> dialog = new Dialog<>();
            dialog.setTitle("Nueva Tabla");
            dialog.setHeaderText("Ingrese los detalles de la columna " + (i + 1));

            ButtonType crearButton = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(crearButton, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            TextField nombreField = new TextField();
            ComboBox<String> tipoComboBox = new ComboBox<>();
            tipoComboBox.getItems().addAll("VARCHAR(30)", "INTEGER", "BOOLEAN", "FLOAT", "DOUBLE");

            gridPane.add(new Label("Nombre:"), 0, 0);
            gridPane.add(nombreField, 1, 0);
            gridPane.add(new Label("Tipo:"), 0, 1);
            gridPane.add(tipoComboBox, 1, 1);

            dialog.getDialogPane().setContent(gridPane);
            int a = i;
            System.out.println(a);
            dialog.setResultConverter(dialogButton -> {

                if (dialogButton == crearButton) {

                    String nombre = nombreField.getText();
                    String tipo = tipoComboBox.getValue();

                    if (nombre.isEmpty() || tipo == null) {
                        mostrarAlertaError("Ingrese todos los detalles de la columna");
                        return null;
                    }
                    if (a == 0) {
                        String llave = "PRIMARY KEY";
                        System.out.println("si");
                        return new Columna(nombre, tipo,llave);
                        
                    }else{
                        System.out.println("no");
                        return new Columna(nombre, tipo);
                    }
                }
                return null;
            });

            Optional<Columna> resultado = dialog.showAndWait();
            resultado.ifPresent(columnas::add);
        }

        return columnas;
    }

    private void abrirViewestructuraTabla() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewEstructuraTabla.fxml"));
            Parent root = loader.load();
            ViewEstructuraTablaController viewEstructuraTabla = loader.getController();
            viewEstructuraTabla.setCx(getCx());
            viewEstructuraTabla.setBaseSelect(getBaseSelect());
            viewEstructuraTabla.setTablaSelect(getTablaSelect());
            if ((viewEstructuraTabla.getBaseSelect() != null) && (viewEstructuraTabla.getCx() != null) && (viewEstructuraTabla.getTablaSelect() != null)) {
                viewEstructuraTabla.mostrarEstructura();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);  // No se puede cambiar el tamaño de la ventana
                stage.setOnCloseRequest(event -> {
                    event.consume();
                }); //Desabilitar la X
                stage.setScene(scene);
                stage.showAndWait();
            } else {
                System.out.println("Error 1");
            }

        } catch (IOException ex) {
            System.out.println("Error 2");
        }
    }

    private static class Columna {

        private final String nombre;
        private final String tipo;
        private final String llave;

        public Columna(String nombre, String tipo) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.llave = null;
        }

        public Columna(String nombre, String tipo, String llave) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.llave = llave;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public String getLlave() {
            return llave;
        }

    }

    private void abrirViewregistrosTabla() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewRegistroTabla.fxml"));
            Parent root = loader.load();
            ViewRegistroTablaController viewRegistroTabla = loader.getController();
            viewRegistroTabla.setCx(getCx());
            viewRegistroTabla.setBaseSelect(getBaseSelect());
            viewRegistroTabla.setTablaSelect(getTablaSelect());
            if ((viewRegistroTabla.getBaseSelect() != null) && (viewRegistroTabla.getCx() != null) && (viewRegistroTabla.getTablaSelect() != null)) {
                viewRegistroTabla.mostrarRegistros();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);  // No se puede cambiar el tamaño de la ventana
                stage.setOnCloseRequest(event -> {
                    event.consume();
                }); //Desabilitar la X
                stage.setScene(scene);
                stage.showAndWait();
            } else {
                System.out.println("Error 1");
            }

        } catch (IOException ex) {
            System.out.println("Error 2");
        }
    }
}
