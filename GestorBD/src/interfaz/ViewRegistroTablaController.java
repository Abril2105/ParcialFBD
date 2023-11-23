/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author jdac1
 */
public class ViewRegistroTablaController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> TV_RegistroTabla;
    @FXML
    private Button BT_Salir;
    @FXML
    private Button BT_Volver;
    @FXML
    private Button BT_CrearRegistro;
    @FXML
    private Button BT_EliminarRegistro;
    @FXML
    private Button BT_ActualizarRegistro;
    @FXML
    private ComboBox<String> CB_Llave;

    //Variables
    private Connection cx;
    private String baseSelect;
    private String tablaSelect;
    private Map<String, Object> informacionFila;
    private String[] registro;

    //Getters y setters
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
        // TODO
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

    public void mostrarRegistros() throws SQLException {
        try {
            List<Map<String, Object>> registros = obtenerRegistros(baseSelect, tablaSelect);

            ObservableList<Map<String, Object>> registrosObservable = FXCollections.observableArrayList(registros);
            TV_RegistroTabla.getColumns().clear();

            for (String columnName : registros.get(0).keySet()) {
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
                TV_RegistroTabla.getColumns().add(column);
            }
            TV_RegistroTabla.setItems(registrosObservable);
            tomarLlaves();

            CB_Llave.setDisable(true);
            BT_ActualizarRegistro.setDisable(true);
            BT_EliminarRegistro.setDisable(true);

            TV_RegistroTabla.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                CB_Llave.setDisable(false);
                BT_ActualizarRegistro.setDisable(false);
                BT_EliminarRegistro.setDisable(false);
            });

        } catch (Exception e) {
        }

    }

    private List<Map<String, Object>> obtenerRegistros(String nombreBaseDatos, String nombreTabla) throws SQLException {
        List<Map<String, Object>> registros = new ArrayList<>();
        if (getCx() != null) {
            String query = "SELECT * FROM " + nombreBaseDatos + "." + nombreTabla;
            PreparedStatement preparedStatement = cx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> registro = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    registro.put(columnName, columnValue);
                }
                registros.add(registro);
            }
        } else {
            throw new SQLException("No se pudo establecer la conexión");
        }
        return registros;
    }

    @FXML
    private void CrearRegistro(ActionEvent event) throws SQLException {

        String dynamicValues = "";
        String dynamicColum = "";

        try {
            String query = "SELECT * FROM " + getBaseSelect() + "." + getTablaSelect();
            PreparedStatement preparedStatement = cx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {

                String argumento = JOptionPane.showInputDialog(null, "Ingrese información del nuevo registro: " + metaData.getColumnName(i));

                //this.registro[i] = argumento;
                int entero = 0;
                int fraccionario = 0;

                //Prueba-error entre float y integer.    
                try {

                    int prueba = Integer.parseInt(argumento);
                } catch (NumberFormatException e) {
                    //No es entero;
                    entero = 1;
                }

                try {

                    float prueba = Float.parseFloat(argumento);

                } catch (NumberFormatException e) {
                    //No es fraccionario;
                    fraccionario = 1;
                }

                //Verifica si la propiedad es numerica o no.
                if (i == columnCount) {

                    if (entero == 1 && fraccionario == 1) {
                        dynamicValues = dynamicValues + "'" + argumento + "'";
                        dynamicColum = dynamicColum + metaData.getColumnName(i);
                    } else {
                        dynamicValues = dynamicValues + argumento;
                        dynamicColum = dynamicColum + metaData.getColumnName(i);
                    }

                    System.out.println(dynamicValues + " Ultimo valor");

                } else {

                    if (entero == 1 && fraccionario == 1) {
                        dynamicValues = dynamicValues + "'" + argumento + "'" + ",";
                        dynamicColum = dynamicColum + metaData.getColumnName(i) + ",";
                    } else {
                        dynamicValues = dynamicValues + argumento + ",";
                        dynamicColum = dynamicColum + metaData.getColumnName(i) + ",";
                    }
                    System.out.println(dynamicValues + " no es el ultimo valor");
                }
            }

        } catch (Exception e) {
        }

        try {
            String query2 = "INSERT INTO " + getBaseSelect() + "." + getTablaSelect() + " (" + dynamicColum + ") VALUES (" + dynamicValues + ")";
            System.out.println(query2);
            Statement st1 = cx.createStatement();
            st1.executeUpdate(query2);

        } catch (Exception e) {

        }
        mostrarRegistros();

    }

    @FXML
    private void EliminarRegistro(ActionEvent event) throws SQLException {

        if (this.CB_Llave.getValue() != null) {
            int index = 0;
            Object[] keynames = this.TV_RegistroTabla.getSelectionModel().getSelectedItem().keySet().toArray();
            for (int i = 0; i <= keynames.length - 1; i++) {
                if (keynames[i].toString().equals(this.CB_Llave.getValue())) {
                    index = i;
                }
            }
            Object[] keyvalues = this.TV_RegistroTabla.getSelectionModel().getSelectedItem().values().toArray();
            String del = "\"" + keyvalues[index].toString() + "\"";
            this.informacionFila = this.TV_RegistroTabla.getSelectionModel().getSelectedItem();
            String queue2 = "DELETE FROM " + baseSelect + "." + tablaSelect + " WHERE " + this.CB_Llave.getValue() + " = " + keyvalues[index];
            try {
                Statement st1 = this.cx.createStatement();
                st1.executeUpdate(queue2);
            } catch (SQLException e) {
                System.out.println("No se elimino el registro");
            }
            System.out.println(queue2);
            JOptionPane.showMessageDialog(null, "Registro eliminado");
        } else {
            JOptionPane.showMessageDialog(null, "Recuerde seleccionar una llave");
        }
        mostrarRegistros();
    }

    @FXML
    private void ActualizarRegistro(ActionEvent event) throws SQLException {

        if (this.CB_Llave.getValue() != null) {

            String dynamicValues = "";
            int index = 0;

            Object[] keynames = this.TV_RegistroTabla.getSelectionModel().getSelectedItem().keySet().toArray();
            for (int i = 0; i <= keynames.length - 1; i++) {
                if (keynames[i].toString().equals(this.CB_Llave.getValue())) {
                    index = i;
                }
            }
            Object[] keyvalues = this.TV_RegistroTabla.getSelectionModel().getSelectedItem().values().toArray();
            String del = "\"" + keyvalues[index].toString() + "\"";

            try {

                String query = "SELECT * FROM " + getBaseSelect() + "." + getTablaSelect();
                PreparedStatement preparedStatement = cx.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {

                    String argumento = JOptionPane.showInputDialog(null, "Ingrese información del nuevo registro: " + metaData.getColumnName(i));

                    int entero = 0;
                    int fraccionario = 0;
  
                    try {

                        int prueba = Integer.parseInt(argumento);
                    } catch (NumberFormatException e) {
                        entero = 1;
                    }

                    try {

                        float prueba = Float.parseFloat(argumento);

                    } catch (NumberFormatException e) {
                        fraccionario = 1;
                    }

                    if (i == columnCount) {

                        if (entero == 1 && fraccionario == 1) {
                            dynamicValues = dynamicValues + metaData.getColumnName(i) + " = " + "'" + argumento + "'";

                        } else {
                            dynamicValues = dynamicValues + metaData.getColumnName(i) + " = " + argumento;

                        }

                        System.out.println(dynamicValues + " Ultimo valor");

                    } else {

                        if (entero == 1 && fraccionario == 1) {
                            dynamicValues = dynamicValues + metaData.getColumnName(i) + " = " + "'" + argumento + "'" + ",";
                        } else {
                            dynamicValues = dynamicValues + metaData.getColumnName(i) + " = " + argumento + ",";
                        }
                        System.out.println(dynamicValues + " No es el ultimo");
                    }
                }

            } catch (Exception e) {
            }

            try {
                String query2 = "UPDATE " + getBaseSelect() + "." + getTablaSelect() + " SET " + dynamicValues + " WHERE " + this.CB_Llave.getValue() + " = " + del;
                System.out.println(query2);
                Statement st1 = cx.createStatement();
                st1.executeUpdate(query2);

            } catch (Exception e) {

            }
        }else{
            JOptionPane.showMessageDialog(null, "Recuerde seleccionar una llave");
        }

        mostrarRegistros();

    }

    private void tomarLlaves() {
        try { //SelectKey
            CB_Llave.getItems().clear();
            String queue2 = "describe " + baseSelect + "." + tablaSelect;
            Statement st1 = this.cx.createStatement();
            ResultSet res = st1.executeQuery(queue2);

            while (res.next()) {
                CB_Llave.getItems().addAll(res.getString(1));
            }
        } catch (Exception e) {
        }
    }

}
