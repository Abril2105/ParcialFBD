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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bryam
 */
public class ViewEstructuraTablaController implements Initializable {

    @FXML
    private Button BT_Salir;
    @FXML
    private Button BT_Volver;
    @FXML
    private TableView<Map<String, Object>> TV_EstructuraTabla;

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

    public void mostrarEstructura() throws SQLException {
        // Obtener la estructura de la tabla
        List<Map<String, Object>> estructura = cargarEstructura(baseSelect, tablaSelect);

        // Crear una lista observable para la estructura
        ObservableList<Map<String, Object>> estructuraObservable = FXCollections.observableArrayList(estructura);

        // Limpiar las columnas existentes en la tabla
        TV_EstructuraTabla.getColumns().clear();

        // Crear columnas dinámicamente basadas en los nombres de columna
        for (String columnName : estructura.get(0).keySet()) {
            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
            column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
            TV_EstructuraTabla.getColumns().add(column);
        }
        
        // Establecer los registros en la tabla
        TV_EstructuraTabla.setItems(estructuraObservable);
    }

    private List<Map<String, Object>> cargarEstructura(String nombreBaseDatos, String nombreTabla) throws SQLException {
        List<Map<String, Object>> Estructura = new ArrayList<>();
        if (getCx() != null) {
            String query = "DESCRIBE " + nombreBaseDatos + "." + nombreTabla;
            PreparedStatement preparedStatement = cx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> columna = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    columna.put(columnName, columnValue);
                }
                Estructura.add(columna);
            }
        } else {
            throw new SQLException("No se pudo establecer la conexión");
        }
        return Estructura;
    }

}
