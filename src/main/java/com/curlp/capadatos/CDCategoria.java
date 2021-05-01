/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capadatos;


import com.curlp.capalogica.CLCategoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author erick
 */
public class CDCategoria {
    
    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;
    
    
    public CDCategoria() throws SQLException {
        this.cn = Conexion.conectar();
    }
    
    public void insertarCategoria(CLCategoria cl) throws SQLException {
        String sql = "{CALL sp_insertarEmpleado(?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setString(1, cl.getNombre());
            ps.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
        public void actualizarCategoria(CLCategoria cl) throws SQLException {
        String sql = "{CALL sp_actualizarEmpleado(?,?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getIdCategoria());
            ps.setString(2, cl.getNombre());
            ps.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
        
        public void eliminarCategoria(CLCategoria cl) throws SQLException {
        String sql = "{CALL sp_eliminarCategoria(?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getIdCategoria());
            ps.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
        
        public int autoIncrementarCategoria() throws SQLException {
        int idCategoria = 0;
        String sql = "{call sp_autoIncrementarIdCategoria()}";
        try {
           st = cn.createStatement();
           rs = st.executeQuery(sql);
           rs.next();

           idCategoria = rs.getInt("idCategoria");
           if(idCategoria == 0){
                idCategoria = 1;
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return idCategoria;
    }
    
        public List<CLCategoria> obtenerCategoria() throws SQLException{

    String sql = "{call sp_mostrarCategoria()}";
    List<CLCategoria> miLista= null;
    try{
       st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            miLista = new ArrayList<>();
            while(rs.next()){
        CLCategoria cl = new CLCategoria();
        cl.setIdCategoria(rs.getInt("idCategoria"));
        cl.setNombre(rs.getString("nombre"));
        miLista.add(cl);
    }
            
    }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    return miLista;
    
}
        
        public List<String> cargarComboCategoria() throws SQLException{

    String sql = "{call sp_mostrarCategoria()}";
    List<String> miLista= null;
    try{
       st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            miLista = new ArrayList<>();
            miLista.add("--Seleccione--");
            while(rs.next()){
                
        miLista.add(rs.getString("nombre"));
    }
            
    }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    return miLista;
    
}
        
    
    
    
    
}
