package com.curlp.capadatos;

import com.curlp.capalogica.CLCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CDCliente {

    //Variables de conexión y de consulta
    private final Connection cn;
    PreparedStatement ps;
    ResultSet rs;
    Statement st;

    public CDCliente() throws SQLException {
        this.cn = Conexion.conectar();
    }
    // metodo para insertar un cliente en la tabla

    public void insertarCliente(CLCliente cl) throws SQLException {
        String sql = "{CALL sp_insertarCliente(?,?,?,?,?,?,?)}";

        try {
            ps = cn.prepareCall(sql);
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getDocIdentidad());
            ps.setBoolean(3, cl.isBeneficio());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getCorreo());
            ps.setFloat(6, cl.getPorcentajeDescuento());
            ps.setBoolean(7, cl.isEstadoCliente());

            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    //Metodo para actualizar un cliente
    public void actualizarCliente(CLCliente cl) throws SQLException {
        String sql = "{CALL sp_actualizarCliente(?,?,?,?,?,?,?,?)}";

        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodCliente());
            ps.setString(2, cl.getNombre());
            ps.setString(3, cl.getDocIdentidad());
            ps.setBoolean(4, cl.isBeneficio());
            ps.setString(5, cl.getTelefono());
            ps.setString(6, cl.getCorreo());
            ps.setFloat(7, cl.getPorcentajeDescuento());
            ps.setBoolean(8, cl.isEstadoCliente());

            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    //Metodo para eliminar cliente

    public void eliminarCliente(CLCliente cl) throws SQLException {
        String sql = "{CALL sp_eliminarCliente(?)}";

        try {
            ps = cn.prepareCall(sql);
            ps.setInt(1, cl.getCodCliente());

            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    //metodo para autoincrementar el id
    public int autoIncrementarCodCliente() throws SQLException {
        int codCliente = 0;

        String sql = "{call sp_autoIncrementarCodCliente()}";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.next();

            codCliente = rs.getInt("codCliente");

            if (codCliente == 0) {
                codCliente = 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return codCliente;
    }

    //Metodo para poblar de adtos la tabla
    public List<CLCliente> obtenerCliente() throws SQLException {
        String sql = "{call sp_mostrarCliente()}";

        List<CLCliente> miLista = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            miLista = new ArrayList<>();

            while (rs.next()) {
                CLCliente cl = new CLCliente();

                cl.setCodCliente(rs.getInt("codCliente"));
                cl.setNombre(rs.getString("nombre"));
                cl.setDocIdentidad(rs.getString("docIdentidad"));
                cl.setBeneficio(rs.getBoolean("beneficio"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setCorreo(rs.getString("correo"));
                cl.setPorcentajeDescuento(rs.getFloat("porcentajeDescuento"));
                cl.setEstadoCliente(rs.getBoolean("estadoCliente"));
                miLista.add(cl);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return miLista;
    }

    //metodo que nos va permitir llenar el combo de ciudad
    public List<String> cargarComboCliente() throws SQLException {
        String sql = "{call sp_mostrarCliente()}";

        List<String> miLista = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            miLista = new ArrayList<>();
            miLista.add("--Seleccione--");

            while (rs.next()) {
                miLista.add(rs.getString("codCliente"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return miLista;
    }

    //metodo para buscar por identidad
    public List<CLCliente> obtenerListaClienteXId(String docIdentidad) throws SQLException {
        String sql;
        sql = "{call sp_mostrarClienteXIdentidad(?)}";
        List<CLCliente> miLista = null;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, docIdentidad);
            rs = ps.executeQuery();
            miLista = new ArrayList<>();
            while (rs.next()) {
                CLCliente clc = new CLCliente();
                clc.setCodCliente(rs.getInt("codCliente"));
                clc.setNombre(rs.getString("nombre"));
                clc.setDocIdentidad(rs.getString("docIdentidad"));
                clc.setBeneficio(rs.getBoolean("beneficio"));
                clc.setTelefono(rs.getString("telefono"));
                clc.setCorreo(rs.getString("correo"));
                clc.setPorcentajeDescuento(rs.getFloat("porcentajeDescuento"));
                clc.setEstadoCliente(rs.getBoolean("estadoCliente"));
                miLista.add(clc);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return miLista;
    }

}
