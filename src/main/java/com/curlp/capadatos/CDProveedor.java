
package com.curlp.capadatos;

import com.curlp.capalogica.CLProveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CDProveedor {
    
    //Variables de conexión y consulta.
    private final Connection cn;
    PreparedStatement ps;
    Statement st;
    ResultSet rs;

    public CDProveedor() throws SQLException{
        this.cn = Conexion.conectar();
    }
    
    //Métodos para tabla Proveedor.
    public void insertarProveedor(CLProveedor cl) throws SQLException{
        String sql = "{call sp_insertarProveedor(?,?,?,?,?)}";
        
        try {
            ps = cn.prepareCall(sql);
            ps.setString(1, cl.getNombreEmpresa());
            ps.setString(2, cl.getTelefono());
            ps.setString(3, cl.getCorreo());
            ps.setString(4, cl.getNombreRepresentante());
            ps.setString(5, cl.getTelefonoRepresentante());
            ps.execute();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void actualizarProveedor(CLProveedor cl) throws SQLException{
        String sql = "{call sp_actualizarProveedor(?,?,?,?,?,?)}";
        
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodProveedor());
            ps.setString(2, cl.getNombreEmpresa());
            ps.setString(3, cl.getTelefono());
            ps.setString(4, cl.getCorreo());
            ps.setString(5, cl.getNombreRepresentante());
            ps.setString(6, cl.getTelefonoRepresentante());
            ps.execute();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void eliminarProveedor(CLProveedor cl) throws SQLException{
        String sql = "{call sp_eliminarProveedor(?)}";
        
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodProveedor());
            ps.execute();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public int autoIncrementarCodProveedor() throws SQLException{
        
        String sql = "{call sp_autoIncrementarCodProveedor()}";
        int codProveedor = 0;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            
            codProveedor = rs.getInt("codProveedor");
            if(codProveedor == 0){
                codProveedor = 1;
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return codProveedor;
    }
    
    public List<CLProveedor> mostrarProveedor() throws SQLException{
        String sql = "{call sp_mostrarMarca()}";
        
        List<CLProveedor> myList= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            myList = new ArrayList<>();
            
            while(rs.next()){
                CLProveedor cl = new CLProveedor();
                
                cl.setCodProveedor(rs.getInt("codProveedor"));
                cl.setNombreEmpresa(rs.getString("nombreEmpresa"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setCorreo(rs.getString("correo"));
                cl.setNombreRepresentante(rs.getString("nombreRepresentante"));
                cl.setTelefonoRepresentante(rs.getString("telefonoRepresentante"));
                myList.add(cl);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return myList;
    }
    
    public List<String> cargarProveedor() throws SQLException{
        String sql = "{call sp_mostrarMarca()}";
        
        List<String> myList= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            myList = new ArrayList<>();
            myList.add("--Seleccione--");
            
            while(rs.next()){
                myList.add(rs.getString("nombreEmpresa"));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return myList;
    }
}
