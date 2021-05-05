package com.curlp.capadatos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import com.curlp.capalogica.CLEmpleado;
import java.util.ArrayList;
import java.util.List;

public class CDEmpleado {

    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;

    public CDEmpleado() throws SQLException {
        this.cn = Conexion.conectar();
    }

    public void insertarEmpleado(CLEmpleado cl) throws SQLException {
        String sql = "{CALL sp_insertarEmpleado(?,?,?,?,?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getApellidos());
            ps.setString(3, cl.getDireccion());
            ps.setString(4, cl.getTelefono());
            ps.setBoolean(5, cl.isEstadoEmpleado());
            ps.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void actualizarEmpleado(CLEmpleado cl) throws SQLException {
        String sql = "{CALL sp_actualizarEmpleado(?,?,?,?,?,?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodEmpleado());
            ps.setString(2, cl.getNombre());
            ps.setString(3, cl.getApellidos());
            ps.setString(4, cl.getDireccion());
            ps.setString(5, cl.getTelefono());
            ps.setBoolean(6, cl.isEstadoEmpleado());
            ps.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void eliminarEmpleado(CLEmpleado cl) throws SQLException {
        String sql = "{CALL sp_eliminarEmpleado(?)}";
        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodEmpleado());
            ps.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public int autoIncrementarEmpleado() throws SQLException {
        int codEmpleado = 0;
        String sql = "{call sp_autoIncrementarCodEmpleado()}";
        try {
           st = cn.createStatement();
           rs = st.executeQuery(sql);
           rs.next();

           codEmpleado = rs.getInt("codEmpleado");
           if(codEmpleado == 0){
                codEmpleado = 1;
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return codEmpleado;
    }
    
public List<CLEmpleado> obtenerEmpleado() throws SQLException{

    String sql = "{call sp_mostrarEmpleado()}";
    List<CLEmpleado> miLista= null;
    try{
       st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            miLista = new ArrayList<>();
            while(rs.next()){
        CLEmpleado cl = new CLEmpleado();
        cl.setCodEmpleado(rs.getInt("codEmpleado"));
        cl.setNombre(rs.getString("nombre"));
        cl.setApellidos(rs.getString("apellidos"));
        cl.setDireccion(rs.getString("direccion"));
        cl.setTelefono(rs.getString("telefono"));
        cl.setEstadoEmpleado(rs.getBoolean("estadoEmpleado"));
        miLista.add(cl);
    }
            
    }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    return miLista;
    
}
public List<String> cargarComboEmpleado() throws SQLException{

    String sql = "{call sp_mostrarEmpleado()}";
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
public List<CLEmpleado> buscarEmpleado(CLEmpleado cl) throws SQLException {

        String sql = "{call sp_buscarEmpleado(?)}";

        List<CLEmpleado> miLista = null;

        try {
            miLista = new ArrayList<>();

            ps = cn.prepareStatement(sql);
            ps.setInt(1, cl.getCodEmpleado());
            rs = ps.executeQuery();

            miLista = new ArrayList<>(2);

            while (rs.next()) {
                cl.setCodEmpleado(rs.getInt("codEmpleado"));
                cl.setNombre(rs.getString("nombre"));
                cl.setApellidos(rs.getString("Apellidos"));
                cl.setDireccion(rs.getString("Direccion"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setEstadoEmpleado(rs.getBoolean("estadoEmpleado"));
                miLista.add(cl);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return miLista;
    }



}
