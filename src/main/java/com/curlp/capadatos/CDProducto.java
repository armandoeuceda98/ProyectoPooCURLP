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
    
      //Método para insertar un producto
    public void insertarProducto(CLProducto cl)throws SQLException{
        String sql = "{CALL sp_insertarProducto(?,?,?,?,?,?,?,?,?,?,?)}";
        
        try{
            ps = cn.prepareCall(sql);
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
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para actualizar producto
    public void actalizarProducto(CLProducto cl)throws SQLException{
        String sql = "{CALL sp_actualizarProducto(?,?,?,?,?,?,?,?,?,?,?)}";
        
        try{
           ps = cn.prepareCall(sql);
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
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para eliminar producto 
    public void eliminarProducto(CLProducto cl)throws SQLException{
        String sql = "{CALL sp_eliminarProducto(?)}";
        
        try{
            ps = cn.prepareCall(sql);
            cl.setCodProducto(rs.getInt("codProducto"));
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
      
    
    //Método para obtener el id autoincrementado de productos
    public int autoIncrementarCodProducto()throws SQLException{
        
        int codProducto= 0;
        String sql = "{CALL sp_autoIncrementarCodProducto()}";
        try{
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            codProducto = rs.getInt("codProducto");
            if(codProducto == 0){
                codProducto = 1;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        return codProducto;
    }
    //Método para buscar producto por medio de código
    public List<CLProducto> obtenerProductos() throws SQLException{
        
        String sql = "{call sp_mostrarProducto()}";
        
        List<CLProducto> miLista = null;
        
        try{
            miLista  = new ArrayList<>();
            
            
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            miLista  = new ArrayList<>(11);
                     
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
    //Método para buscar producto por medio de código
    public List<CLProducto> obtenerProductoFiltrado(JTable jtable, String nombre) throws SQLException{
        
        String sql = "{call sp_mostrarProductoXNombre(?)}";
        
        List<CLProducto> miLista = null;
        
        try{
            miLista  = new ArrayList<>();
            
            
            ps = cn.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            
            miLista  = new ArrayList<>(11);
                     
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
    
    
           
}