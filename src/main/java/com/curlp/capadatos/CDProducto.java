
package com.curlp.capadatos;

import com.curlp.capalogica.CLFactura;
import com.curlp.capalogica.CLProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;


public class CDProducto {
    //Variables de conexion y de consulta
    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;
    
    public CDProducto() throws SQLException {
        this.cn = Conexion.conectar();
    }
    
      //Método para insertar una ciudad en tabla
    public void insertarProducto(CLFactura cl)throws SQLException{
        String sql = "{CALL sp_insertarProducto(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setString(1, cl.getFecha());
            ps.setInt(2, cl.getCodCliente());
            ps.setInt(3, cl.getCodEmpleado());
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para actualizar la ciudad en la tabla
    public void actalizarFactura(CLFactura cl)throws SQLException{
        String sql = "{CALL sp_actualizarFactura(?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodFactura());
            ps.setString(2, cl.getFecha());
            ps.setInt(3, cl.getCodCliente());
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para eliminar la ciudad en la tabla
    public void eliminarFactura(CLFactura cl)throws SQLException{
        String sql = "{CALL sp_eliminarFactura(?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodFactura());
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    
    
    //Método para obtener el id autoincrementado de la factura
    public int autoIncrementarFacturaCod()throws SQLException{
        
        int codFactura = 0;
        
        String sql = "{CALL sp_autoIncrementarCodFactura()}";
        
        try{
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            
            codFactura = rs.getInt("codFactura");
            
            if(codFactura == 0){
                codFactura = 1;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        return codFactura;
    }
    
    //Método para poblar de datos la tabla
    public List<CLFactura> obtenerListaFacturas() throws SQLException{
        
        String sql = "{call sp_mostrarFactura()}";
        
        List<CLFactura> miLista = null;
        
        try{
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            miLista  = new ArrayList<>();
            
            while(rs.next()){
                CLFactura cl = new CLFactura();
                
                cl.setCodFactura(rs.getInt("codFactura"));
                cl.setFecha(rs.getString("fecha"));
                cl.setNombreCliente(rs.getString("nombre"));
                cl.setNombreEmpleado(rs.getString("empleado.nombre"));
                miLista.add(cl);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
            return miLista;
    }
    
    //Método para poblar form factura
    public CLFactura poblarForm(CLFactura cl) throws SQLException{
        
        String sql = "{call sp_mostrarFacturaX(?)}";
        
        try{
            ps = cn.prepareStatement(sql);
            ps.setInt(1, cl.getCodFactura());
            rs = ps.executeQuery();
            
            while(rs.next()){
                cl.setCodFactura(rs.getInt("codFactura"));
                cl.setFecha(rs.getString("fecha"));
                cl.setNombreCliente(rs.getString("nombre"));
                cl.setNombreEmpleado(rs.getString("empleado.nombre"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
            return cl;
    }
    
    //Método para buscar factura por medio de código
    public List<CLProducto> obtenerProductoFiltrado(JTable jtable, String nombre) throws SQLException{
        
        String sql = "{call sp_mostrarProductoXNombre(?)}";
        
        List<CLProducto> miLista = null;
        
        try{
            miLista  = new ArrayList<>();
            
            
            ps = cn.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            
            miLista  = new ArrayList<>(4);
                     
            while(rs.next()){ 
                CLProducto prod = new CLProducto();
                
                
                prod.setCodProducto(rs.getInt("codProducto"));
                prod.setNombre(rs.getString("nombre"));
                prod.setDescripcion(rs.getString("descripcion"));
                prod.setCosto(rs.getDouble("costo"));
                prod.setExistencia(rs.getInt("existencia"));
                prod.setPrecioV1(rs.getDouble("precioV1"));
                prod.setPrecioV2(rs.getDouble("precioV2"));
                prod.setPrecioV3(rs.getDouble("precioV3"));
                
                prod.setNombreCategoria(rs.getString("categoria.nombre"));
                prod.setNombreMarca(rs.getString("marca.nombre"));
                prod.setNombreEmpresa(rs.getString("proveedor.nombreEmpresa"));
                miLista.add(prod);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
            return miLista;
    }
    
   //Método para buscar Producto por medio de código
    public CLProducto obtenerProductoPorCodigo(CLProducto cl) throws SQLException{
        
        String sql = "{call sp_mostrarProductoXCodigo(?)}";
        
        
        try{
            
            
            ps = cn.prepareStatement(sql);
            ps.setInt(1, cl.getCodProducto());
            rs = ps.executeQuery();
                     
            if(rs.next()){ 
                cl.setCodProducto(rs.getInt("codProducto"));
                cl.setNombre(rs.getString("nombre"));
                cl.setDescripcion(rs.getString("descripcion"));
                cl.setCosto(rs.getDouble("costo"));
                cl.setExistencia(rs.getInt("existencia"));
                cl.setPrecioV1(rs.getDouble("precioV1"));
                cl.setPrecioV2(rs.getDouble("precioV2"));
                cl.setPrecioV3(rs.getDouble("precioV3"));                
                cl.setNombreCategoria(rs.getString("categoria.nombre"));
                cl.setNombreMarca(rs.getString("marca.nombre"));
                cl.setNombreEmpresa(rs.getString("proveedor.nombreEmpresa"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
            return cl;
    }
    
    
    //Método para llenar el combo de factura
    public List<String> cargarFactura() throws SQLException{
        String sql = "{call sp_mostrarFactura()}";
        
        List<String> myList= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            myList = new ArrayList<>();
            myList.add("--Seleccione--");
            while(rs.next()){
                myList.add(rs.getString("codFactura"));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return myList;
    }        
            
           
}

