/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package interfaz;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import gestorbd.Conexion;
import java.sql.Connection;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.CheckComboBox;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.sql.ResultSetMetaData;




/**
 * FXML Controller class
 *
 * 
 */


public class ViewhacerconsultaController implements Initializable {

    @FXML
    private Button BT_Volver;
    @FXML
    private ComboBox<String> Select_Tabla1;
    @FXML
    private ComboBox<String> Select_Tabla2;
    @FXML
    private ComboBox<String> Select_Columna1;
    @FXML
    private ComboBox<String> Select_Columna2;
    @FXML
    private CheckComboBox<String> Select_num_columnas;
    @FXML
    private ComboBox<String> Select_campo1;
    @FXML
    private ComboBox<String> Select_campo2;
    @FXML
    private ComboBox<String> Select_operador1;
    @FXML
    private ComboBox<String> Select_operador2;
    private String Select_Busqueda;
    
    private Connection  cx;
    private String baseSelect;
    
    @FXML
    private CheckBox Add_condicion;
    @FXML
    private CheckBox Add_Tabla2;
    @FXML
    private TextField Select_valor1;
    @FXML
    private TextField Select_valor2;
    @FXML
    private CheckBox addCondition2;
    @FXML
    private Button Buscar;
    @FXML
    private TableView<?> Tabla;
    @FXML
    private Button Vista;

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
    
    @FXML
    public void select_tablauno() throws SQLException{
        this.Select_Tabla2.getItems().clear();
        this.Select_Columna1.getItems().clear();
        this.Select_num_columnas.getCheckModel().clearChecks();
        this.Select_num_columnas.getItems().clear();
        this.Select_campo1.getItems().clear();
        this.Select_campo2.getItems().clear();
        
        
        //fill T2, colum1, checkcolum and Campo1-2 after every change 
        try{
            //Statement st = cx.createStatement();
            PreparedStatement preparedStatement = cx.prepareStatement("SHOW TABLES IN " + baseSelect);
            ResultSet resultSet = preparedStatement.executeQuery();
            

            ObservableList<String> listadeTablas = FXCollections.observableArrayList();
              while (resultSet.next()) {
                  String nombre = resultSet.getString(1);
                  listadeTablas.add(nombre);
                  System.out.println(nombre);
              }
              Select_Tabla2.setItems(listadeTablas);
              Select_Tabla2.getItems().removeAll(Select_Tabla1.getValue());
              
        }catch(Exception e){}
        
        try{ //Colum1, columscheck, campo1-2
            String queue2 = "describe "+baseSelect+"."+Select_Tabla1.getValue();
            PreparedStatement preparedStatement = cx.prepareStatement(queue2);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
                Select_Columna1.getItems().addAll(resultSet.getString(1));
                Select_num_columnas.getItems().addAll(resultSet.getString(1));
                Select_campo1.getItems().addAll(resultSet.getString(1));
                Select_campo2.getItems().addAll(resultSet.getString(1));
            }
        }catch(Exception e){}   
        
        //fill if T2
        if(this.Add_Tabla2.isSelected() == true){
            this.Select_num_columnas.getItems().clear();
            this.Select_num_columnas.getCheckModel().clearChecks();
            this.Select_campo1.getItems().clear();
            this.Select_campo2.getItems().clear();
            try{ //Colum1, columscheck, campo1-2
                String queue2 = "describe "+baseSelect+"."+this.Select_Tabla1.getValue();
                PreparedStatement preparedStatement = cx.prepareStatement(queue2);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    this.Select_num_columnas.getItems().addAll(this.Select_Tabla1.getValue()+"."+resultSet.getString(1));
                    this.Select_campo1.getItems().addAll(this.Select_Tabla1.getValue()+"."+resultSet.getString(1));
                    this.Select_campo2.getItems().addAll(this.Select_Tabla1.getValue()+"."+resultSet.getString(1));
                }
            }catch(Exception e){}
            
            try{
                String queue2 = "describe "+baseSelect+"."+this.Select_Tabla2.getValue();
                PreparedStatement preparedStatement = cx.prepareStatement(queue2);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    this.Select_num_columnas.getItems().addAll(this.Select_Tabla2.getValue()+"."+resultSet.getString(1));
                    this.Select_campo1.getItems().addAll(this.Select_Tabla2.getValue()+"."+resultSet.getString(1));
                    this.Select_campo2.getItems().addAll(this.Select_Tabla2.getValue()+"."+resultSet.getString(1));
                }
            }catch(Exception e){} 
            
            
        }
        
    } 
    
    @FXML
    public void select_tablados(){
        this.Select_Columna2.getItems().clear();
        this.Select_num_columnas.getCheckModel().clearChecks();
        this.Select_num_columnas.getItems().clear();        
        this.Select_campo1.getItems().clear();
        this.Select_campo2.getItems().clear();
        //fill colum2 after evert change
        try{
            String queue2 = "describe "+baseSelect+"."+Select_Tabla2.getValue();
            PreparedStatement preparedStatement = cx.prepareStatement(queue2);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
                this.Select_Columna2.getItems().addAll(resultSet.getString(1));
            }
        }catch(Exception e){} 
        
        //fill columscheck, campo1-2 after every change
        try{ //Colum1, columscheck, campo1-2
            String queue2 = "describe "+baseSelect+"."+Select_Tabla1.getValue();
            PreparedStatement preparedStatement = cx.prepareStatement(queue2);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                this.Select_num_columnas.getItems().addAll(this.Select_Tabla1.getValue()+"."+resultSet.getString(1));
                this.Select_campo1.getItems().addAll(this.Select_Tabla1.getValue()+"."+resultSet.getString(1));
                this.Select_campo2.getItems().addAll(this.Select_Tabla1.getValue()+"."+resultSet.getString(1));
            }
        }catch(Exception e){}

        try{
            String queue2 = "describe "+baseSelect+"."+Select_Tabla1.getValue();
            PreparedStatement preparedStatement = cx.prepareStatement(queue2);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                this.Select_num_columnas.getItems().addAll(this.Select_Tabla2.getValue()+"."+resultSet.getString(1));
                this.Select_campo1.getItems().addAll(this.Select_Tabla2.getValue()+"."+resultSet.getString(1));
                this.Select_campo2.getItems().addAll(this.Select_Tabla2.getValue()+"."+resultSet.getString(1));
            }
        }catch(Exception e){} 
    }
    
    public void prueba(){
        try{
            Select_Tabla1.getItems().clear();
            //Statement st = cx.createStatement();
            PreparedStatement preparedStatement = cx.prepareStatement("SHOW TABLES IN " + baseSelect);
            ResultSet resultSet = preparedStatement.executeQuery();
            

            ObservableList<String> listadeTablas = FXCollections.observableArrayList();
              while (resultSet.next()) {
                  String nombre = resultSet.getString(1);
                  listadeTablas.add(nombre);
                  System.out.println(nombre);
              }
              Select_Tabla1.setItems(listadeTablas);
              
        }catch(Exception e){
        

            
        }
    }
    
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
       }
     
    
    @FXML
    private void Volver(ActionEvent event) {
        Stage stage = (Stage) this.BT_Volver.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Check_addcondicion(ActionEvent event) {
        this.Select_valor1.setDisable(!this.Select_valor1.disableProperty().getValue());
        this.Select_operador1.setDisable(!this.Select_operador1.disableProperty().getValue());
        this.Select_campo1.setDisable(!this.Select_campo1.disableProperty().getValue());
        this.addCondition2.setDisable(!this.addCondition2.disableProperty().getValue());
        this.addCondition2.selectedProperty().setValue(false);
        this.Select_valor2.setDisable(true);
        this.Select_operador2.setDisable(true);
        this.Select_campo2.setDisable(true);
        
    }

    @FXML
    private void Check_addtabla2(ActionEvent event) {
        this.Select_Tabla2.setDisable(!this.Select_Tabla2.disableProperty().getValue());
        this.Select_Columna1.setDisable(!this.Select_Columna1.disableProperty().getValue());
        this.Select_Columna2.setDisable(!this.Select_Columna2.disableProperty().getValue());
    }

    @FXML
    private void Condition2added(ActionEvent event) {
        this.Select_campo2.setDisable(!this.Select_campo1.disableProperty().getValue());
        this.Select_operador2.setDisable(!this.Select_operador1.disableProperty().getValue());
        this.Select_valor2.setDisable(!this.Select_valor1.disableProperty().getValue());
    }

    @FXML
    private void makeConsult(ActionEvent event) {
        try{
            ResultSet resultSet= null;
            String colum="";
            String colums="";
            String from ="";
            String where ="";
            Select_Busqueda ="";
            colum = this.Select_num_columnas.getCheckModel().getCheckedItems().toString();
            colum = colum.replace("[", "");
            colum = colum.replace("]", "");
            String[] partes = colum.split(",");
            for(int i = 0; i <= partes.length-1; i++){
                String modify = partes[i];
                String colName = modify;
                colName = colName.replace(".", "_");
                modify = modify.concat(" AS "+colName+",");
                partes[i]=modify;
                colums = colums + partes[i];
            }
            colum = colums;
            colum = colum.substring(0,colum.length() - 1);


            if(this.Add_Tabla2.isSelected() == false && this.addCondition2.isSelected() == false){
                from = baseSelect+"."+this.Select_Tabla1.getValue();
                if(this.Add_condicion.isSelected() == true){
                    where =" where "+this.Select_campo1.getValue()+" "+this.Select_operador1.getValue()+" "+this.Select_valor1.getText();
                }
                try{
                    String queue2 = "Select "+colum+" From "+from+where;
                    System.out.println(queue2);
                    this.Select_Busqueda =queue2;
                    PreparedStatement preparedStatement = cx.prepareStatement(queue2);
                    resultSet = preparedStatement.executeQuery();
                    System.out.println(resultSet);
                }catch(Exception e){System.out.println("No se encontró resultado");}
            }

            else if(this.Add_Tabla2.isSelected() == true && this.addCondition2.isSelected() == false){
                from = baseSelect+"."+this.Select_Tabla1.getValue()+", "+baseSelect+"."+this.Select_Tabla2.getValue();
                where = " where "+this.Select_Tabla1.getValue()+"."+this.Select_Columna1.getValue()+"="+this.Select_Tabla2.getValue()+"."+this.Select_Columna2.getValue();
                if(this.Add_condicion.isSelected() == true){
                    where = " where "+this.Select_Tabla1.getValue()+"."+this.Select_Columna1.getValue()+"="+this.Select_Tabla2.getValue()+"."+this.Select_Columna2.getValue()+" And "+this.Select_campo1.getValue()+" "+this.Select_operador1.getValue()+" "+this.Select_valor1.getText();
                }
                try{
                    String queue2 = "Select "+colum+" From "+from+where;
                    System.out.println(queue2);
                    this.Select_Busqueda =queue2;
                    PreparedStatement preparedStatement = cx.prepareStatement(queue2);
                    resultSet = preparedStatement.executeQuery();
                    System.out.println(resultSet);
                }catch(Exception e){}
            }

            else if(this.Add_Tabla2.isSelected() == false && this.addCondition2.isSelected() == true){
                from = baseSelect+"."+this.Select_Tabla1.getValue();
                if(this.Add_condicion.isSelected() == true){
                    where = " where "+this.Select_campo1.getValue()+" "+this.Select_operador1.getValue()+" "+this.Select_valor1.getText()+" "+"AND"+" "+this.Select_campo2.getValue()+" "+this.Select_operador2.getValue()+" "+this.Select_valor2.getText();
                }
                try{
                    String queue2 = "Select "+colum+" From "+from+where;
                    System.out.println(queue2);
                    this.Select_Busqueda =queue2;
                    PreparedStatement preparedStatement = cx.prepareStatement(queue2);
                    resultSet = preparedStatement.executeQuery();
                    System.out.println(resultSet);
                }catch(Exception e){}
            }

            else if(this.Add_Tabla2.isSelected() == true && this.addCondition2.isSelected() == true){
                from = baseSelect+"."+this.Select_Tabla1.getValue()+", "+baseSelect+"."+this.Select_Tabla2.getValue();
                where = " where "+this.Select_Tabla1.getValue()+"."+this.Select_Columna1.getValue()+"="+this.Select_Tabla2.getValue()+"."+this.Select_Columna2.getValue();
                if(this.Add_condicion.isSelected() == true){
                    where = " where "+this.Select_Tabla1.getValue()+"."+this.Select_Columna1.getValue()+"="+this.Select_Tabla2.getValue()+"."+this.Select_Columna2.getValue()+" And "+this.Select_campo1.getValue()+" "+this.Select_operador1.getValue()+" "+this.Select_valor1.getText()+" "+"AND"+" "+this.Select_campo2.getValue()+" "+this.Select_operador2.getValue()+" "+this.Select_valor2.getText();
                }
                try{
                    String queue2 = "Select "+colum+" From "+from+where;
                    System.out.println(queue2);
                    this.Select_Busqueda =queue2;
                    PreparedStatement preparedStatement = cx.prepareStatement(queue2);
                    resultSet = preparedStatement.executeQuery();
                    System.out.println(resultSet);
                }catch(Exception e){}
            }
            
            //Fill Table
            mostrarRegistros(this.Select_Busqueda, this.Tabla);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Complete todos los campos");
            }
    }
    

    
    private List<Map<String, Object>> obtenerRegistros(String Que) throws SQLException {
        List<Map<String, Object>> registros = new ArrayList<>();
        if (cx != null) {
            String query = Que;
            PreparedStatement preparedStatement = cx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount(); 

            while (resultSet.next()) {
                Map<String, Object> registro = new HashMap<>();
                //int i = 1; i <= columnCount; i++
                for (int i = columnCount; i >= 1; i--) {
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
    
        public void mostrarRegistros(String Que, TableView tablaActual) throws SQLException {
            List<Map<String, Object>> registros = obtenerRegistros(Que);
            
            ObservableList<Map<String, Object>> registrosObservable = FXCollections.observableArrayList(registros);
            
            tablaActual.getColumns().clear();
            
            for (String columnName : registros.get(0).keySet()) {
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
                tablaActual.getColumns().add(column);
            }
            
            tablaActual.setItems(registrosObservable);
    }

    @FXML
    private void VistaGuardar(ActionEvent event) {
        try{
            String nombreVista = JOptionPane.showInputDialog(null, "Nombre de la Vista");
            String query = "CREATE VIEW "+baseSelect+"."+nombreVista+" As "+this.Select_Busqueda;
            System.out.println(query);
            System.out.println(cx);
            PreparedStatement preparedStatement = cx.prepareStatement(query);
            preparedStatement.executeQuery();
            JOptionPane.showMessageDialog(null, " La vista ha sido Guardada");
        }catch(Exception e){System.out.println("No se encontró resultado");} 
    }
    
}
