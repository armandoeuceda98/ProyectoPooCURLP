
package com.curlp.capadatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class CDProducto {
    //Variables de conexion y de consulta
    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;

    public CDProducto(Connection cn) throws SQLException {
        this.cn = Conexion.conectar();
    }
    
}
