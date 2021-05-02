
package com.curlp.capadatos;

import com.curlp.capalogica.CLDetFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CDDetFactura {
    //Variables de conexión y de consulta
    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;

    public CDDetFactura() throws SQLException{
        this.cn = Conexion.conectar();
    }
    
    //Método para insertar una ciudad en tabla
    public void insertarDetFactura(CLDetFactura cl)throws SQLException{
        String sql = "{CALL sp_insertarDetFactura(?,?,?,?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCantidad());
            ps.setDouble(2, cl.getPrecio());
            ps.setInt(3, cl.getCodProducto());
            ps.setInt(4, cl.getCodFactura());
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para actualizar la ciudad en la tabla
    public void actalizarDetFactura(CLDetFactura cl)throws SQLException{
        String sql = "{CALL sp_actualizarDetFactura(?,?,?,?,?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodDetFactura());
            ps.setInt(2, cl.getCantidad());
            ps.setDouble(3, cl.getPrecio());
            ps.setInt(4, cl.getCodProducto());
            ps.setInt(5, cl.getCodFactura());
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para eliminar la ciudad en la tabla
    public void eliminarDetFactura(CLDetFactura cl)throws SQLException{
        String sql = "{CALL sp_eliminarDetFactura(?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodDetFactura());
            ps.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }
    
    //Método para obtener el id autoincrementado de la factura
    public int autoIncrementarDetFacturaCod()throws SQLException{
        
        int codDetFactura = 0;
        
        String sql = "{CALL sp_autoIncrementarCodDetFactura()}";
        
        try{
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            
            codDetFactura = rs.getInt("codDetFactura");
            
            if(codDetFactura == 0){
                codDetFactura = 1;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        return codDetFactura;
    }
    
    //Método para poblar de datos la tabla
    public List<CLDetFactura> obtenerListaDetFacturas(String i) throws SQLException{
        
        String sql = "{CALL sp_mostrarDetFacturaX(?)}";
        int m1;
        Double m2, result;
        
        List<CLDetFactura> miLista = null;
        
        try{
            ps = cn.prepareStatement(sql);
            ps.setString(1, i);
            rs = ps.executeQuery();
            
            miLista  = new ArrayList<>();
            
            while(rs.next()){
                CLDetFactura cl = new CLDetFactura();
                
                cl.setCodDetFactura(rs.getInt("codDetFactura"));
                cl.setCantidad(rs.getInt("cantidad"));
                cl.setPrecio(rs.getDouble("precio"));
                cl.setNomProducto(rs.getString("producto.nombre"));
                cl.setCodFactura(rs.getInt("codFactura"));
                cl.setTotal(rs.getInt("cantidad")*rs.getDouble("precio"));
                miLista.add(cl);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
            return miLista;
    }
    
    //Método para llenar el combo de factura
            
            
           
}
