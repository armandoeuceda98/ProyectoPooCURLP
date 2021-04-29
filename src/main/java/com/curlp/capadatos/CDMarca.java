
package com.curlp.capadatos;

import com.curlp.capalogica.CLMarca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CDMarca {
    
    //Variables de conexión y consulta.
    private final Connection cn;
    PreparedStatement ps;
    Statement st;
    ResultSet rs;

    public CDMarca(Connection cn) throws SQLException{
        this.cn = Conexion.conectar();
    }
    
    //Métodos para tabla Marca
    public void insertarMarca(CLMarca cl) throws SQLException{
        String sql = "{call sp_insertarMarca(?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setString(1, cl.getNombre());
            ps.execute();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void actualizarMarca(CLMarca cl) throws SQLException{
        String sql = "{call sp_actualizarMarca(?,?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getIdMarca());
            ps.setString(2, cl.getNombre());
            ps.execute();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void eliminarMarca(CLMarca cl) throws SQLException{
        String sql = "{call sp_eliminarMarca(?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getIdMarca());
            ps.execute();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public int autoIncrementarIdMarca() throws SQLException{
        
        String sql = "{call sp_autoIncrementarIdMarca()}";
        int idMarca = 0;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            
            idMarca = rs.getInt("idMarca");
            if(idMarca == 0){
                idMarca = 1;
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return idMarca;
    }
    
    public List<CLMarca> mostrarMarca() throws SQLException{
        String sql = "{call sp_mostrarMarca()}";
        
        List<CLMarca> myList= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            myList = new ArrayList<>();
            
            while(rs.next()){
                CLMarca cl = new CLMarca();
                
                cl.setIdMarca(rs.getInt("idMarca"));
                cl.setNombre(rs.getString("nombre"));
                myList.add(cl);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return myList;
    }
    
    public List<String> cargarMarca() throws SQLException{
        String sql = "{call sp_mostrarMarca()}";
        
        List<String> myList= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            myList = new ArrayList<>();
            myList.add("--Seleccione--");
            while(rs.next()){
                myList.add(rs.getString("nombre"));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return myList;
    }
}