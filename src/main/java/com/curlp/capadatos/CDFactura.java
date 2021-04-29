
package com.curlp.capadatos;

import com.curlp.capalogica.CLFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CDFactura {
    
    //Variables de conexión y de consulta
    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;

    public CDFactura() throws SQLException{
        this.cn = Conexion.conectar();
    }
    
    //Método para insertar una ciudad en tabla
    public void insertarFactura(CLFactura cl)throws SQLException{
        String sql = "{CALL sp_insertarFactura(?)}";
        
        try{
            ps = cn.prepareCall(sql);
            ps.setDate(1, cl.getFecha());
            ps.setInt(2, cl.getCodCliente());
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
            ps.setDate(2, cl.getFecha());
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
        
        String sql = "{CALL sp_mostrarFactura()}";
        
        List<CLFactura> miLista = null;
        
        try{
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            miLista  = new ArrayList<>();
            
            while(rs.next()){
                CLFactura cl = new CLFactura();
                
                cl.setCodFactura(rs.getInt("codFactura"));
                cl.setFecha(rs.getDate("fecha"));
                cl.setCodCliente(rs.getInt("codCliente"));
                miLista.add(cl);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
            return miLista;
    }
    
    //Método para llenar el combo de factura
            
            
           
}
