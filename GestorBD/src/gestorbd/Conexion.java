package gestorbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private String bd = "";
    private String url = "";
    private String user = "";
    private String password = "";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private Connection cx;
    private String port = "";

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    //Constructor
    public Conexion() {
        this.cx = cx;
    }
    //Getters y Setters
    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public Connection getCx() {
        return cx;
    }

    public void setCx(Connection cx) {
        this.cx = cx;
    }
    
    //Creacion URL para JDBC
    public void setUrl(String servidor, String puerto) {
        this.url = "jdbc:mysql://" + servidor + ":" + puerto + "/";
    }
    
    //Coneccion con MySQL
    public Connection conectar(){
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url, user, password);
            System.out.println("SE CONECTO A LA BASE DE DATOS " + bd);
            System.out.println(cx);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("NO SE CONECTO A LA BASE DE DATOS " + bd);
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }
    
    public void desconectar(){
        try {
            cx.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
