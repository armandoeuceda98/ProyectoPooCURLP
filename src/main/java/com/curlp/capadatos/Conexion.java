
package com.curlp.capadatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    //Variables de conexión.
    private static String url = "jdbc:mysql://localhost:3306/inventario_master?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static String user = "root";
    private static String clave = "2Mas2son4.";
    
    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            //Obtener cadena de conexión.
            return DriverManager.getConnection(url, user, clave);
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }
    }
}
