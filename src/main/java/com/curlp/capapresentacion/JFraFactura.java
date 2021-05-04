/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capapresentacion;

import com.curlp.capadatos.CDCliente;
import com.curlp.capadatos.CDDetFactura;
import com.curlp.capadatos.CDFactura;
import com.curlp.capadatos.CDProducto;
import com.curlp.capalogica.CLCliente;
import com.curlp.capalogica.CLDetFactura;
import com.curlp.capalogica.CLFactura;
import com.curlp.capalogica.CLProducto;
import java.sql.SQLException;
import java.time.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DELL
 */
public class JFraFactura extends javax.swing.JFrame {

    static Object[] filas = new Object[6];
    static int fila = 0;
    static boolean activar = true;
    static boolean beneficio = false;

    public JFraFactura() throws SQLException {
        initComponents();
    }
    
    //Método para limpiar la tabla
    private static void limpiarTabla(){
        DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
        
        while(dtm.getRowCount() > 0){
            dtm.removeRow(0);   
        }
    }
    
    //Método para poblar de datos la tabla
    private static void poblarTabla(CLDetFactura cldf) throws SQLException{  
        limpiarTabla();
        CDDetFactura cddf = new CDDetFactura();
        List<CLDetFactura> miLista = cddf.obtenerListaDetFacturas(cldf);
        DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
        
        miLista.stream().map((CLDetFactura cl) -> {
            Object[] fila = new Object[6];
            fila[0] = cl.getCodDetFactura();
            fila[1] = cl.getCodProducto();
            fila[2] = cl.getNomProducto();
            fila[3] = cl.getCantidad();
            fila[4] = cl.getPrecio();
            fila[5] = cl.getTotal();
            return fila;
        }).forEachOrdered((fila) -> {
            dtm.addRow(fila);
        });
    }
    
    //Método para ver los resultados de la consulta
    public static void habilitarRecDato(){
        jTFCodFactura.setEnabled(true);
        jTFCodFactura.setEditable(false);
        jTFCodCliente.setEnabled(true);
        jTFCodCliente.setEditable(false);
        jTFCodigoCliente.setEnabled(true);
        jTFCodigoCliente.setEditable(false);
        jTFCliente.setEnabled(true);
        jTFCliente.setEditable(false);
        jTFFecha.setEnabled(true);
        jTFFecha.setEditable(false);
        jTFCodProducto.setEnabled(true);
        jTFCodProducto.setEditable(false);
        jTFStock.setEnabled(true);
        jTFPrecio.setEnabled(true);
        jTFNomProducto.setEnabled(true);
        jTFCantidad.setEnabled(true);
        jTFTotalProduc.setEnabled(true);
        jTFValorTotal.setEnabled(true);
        jTFSubTotal.setEnabled(true);
        jTFIsv.setEnabled(true);
        jTFTotal.setEnabled(true);
        jTFDescuento.setEnabled(true);
        jTFDescuento.setEditable(false);
    }
    
    //Método para buscar un cliente
    public static void buscarProducto() throws SQLException{
        CLProducto clp = new CLProducto();
        CDProducto cdp = new CDProducto();
        clp.setCodProducto(Integer.parseInt(jTFCodProducto.getText().trim()));
        cdp.obtenerProductoPorCodigo(clp);
        jTFNomProducto.setText(clp.getNombre());
        jTFStock.setText(String.valueOf(clp.getExistencia()));
        jTFPrecio.setText(String.valueOf(clp.getPrecioV1()));
    }
    
    //Método para saber el total a partir de la cantidad de producto por el precio de unidad
    private void totalizarPrecio(){
        double cant, prec, tot;
        cant = Double.parseDouble(jTFCantidad.getText());
        prec = Double.parseDouble(jTFPrecio.getText());
        tot = cant * prec;
        jTFTotalProduc.setText(String.valueOf(tot));
//        stock = Double.parseDouble(jTFStock.getText());
//        stockfin = stock - cant;
//        jTFStock.setText(String.valueOf(stockfin));
    }
    
    //Método para actualizar la factura
    private static void actualizarDatosFactura() {
        try{
            CDFactura cdf = new CDFactura();
            CLFactura cle = new CLFactura();
            cle.setCodFactura(Integer.parseInt(jTFCodFactura.getText()));
            cle.setFecha(jTFFecha.getText());
            cle.setCodCliente(Integer.parseInt(jTFCodigoCliente.getText()));
            cle.setCodEmpleado(1);
            cdf.actalizarFactura(cle);
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
        
    }
    
    //Método para buscar un cliente
    private static void buscarCliente() throws SQLException{
        CLCliente clc = new CLCliente();
        CDCliente cdc = new CDCliente();
        clc.setDocIdentidad(jTFCodCliente.getText().trim());
        cdc.obtenerListaClienteXId(clc);
        if(clc.getNombre()==null){
            JOptionPane.showMessageDialog(null, "No se encontró ningún cliente.");
        }else{
            if(jTFCliente.getText().isEmpty()){
                jTFCliente.setText(clc.getNombre());
                jTFCodigoCliente.setText(String.valueOf(clc.getCodCliente()));   
                crearFactura();
            }else{
                jTFCliente.setText(clc.getNombre());
                jTFCodigoCliente.setText(String.valueOf(clc.getCodCliente()));
                actualizarDatosFactura();
            }
            
            DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
            if(clc.isBeneficio() == true){
                beneficio = true;
            }else{
                beneficio = false;
            }
        }
    }
    
    
    
    //poblar Todo el formulario y jTable a partir de abrir factura
    public static void poblarForm() throws SQLException{
        String i;
        CDFactura cdf = new CDFactura();
        CLFactura cle = new CLFactura();
        CLDetFactura cldf = new CLDetFactura();
        cle.setCodFactura(Integer.parseInt(jTFCodFactura.getText().trim()));
        String cl = cdf.poblarForm(cle).getFecha();
        jTFFecha.setText(cle.getFecha());
        jTFCliente.setText(cle.getNombreCliente());
        jTFCodCliente.setText(cle.getDocIdentidad());
        jTFCodigoCliente.setText(String.valueOf(cle.getCodCliente()));
        if(cle.isBeneficio()){
            beneficio = true;
        }else{
            beneficio = false;
        }
        cldf.setCodFactura(Integer.parseInt(jTFCodFactura.getText().trim()));
        poblarTabla(cldf);
        activar = false;
        suma();
    }
    
    //Método para crear una factura
    private static void crearFactura(){
        try{
            DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
            if(dtm.getRowCount() == 0){
                CDFactura cdf = new CDFactura();
                CLFactura clf = new CLFactura();
                clf.setCodFactura(Integer.parseInt(jTFCodFactura.getText()));
                clf.setFecha(jTFFecha.getText());
                clf.setCodCliente(Integer.parseInt(jTFCodigoCliente.getText()));
                clf.setCodEmpleado(1); 
                cdf.insertarFactura(clf);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getLocalizedMessage());
        }
        
    }
    
    //Método para evaluar la existencia
    private void evaluarExistencia(){
        int stock, cantidad;
        stock = Integer.parseInt(jTFStock.getText());
        cantidad = Integer.parseInt(jTFCantidad.getText());
    }
    
    
    //Método para hacer una suma de todos los totales de factura
    private static void suma() {
        
        int contar  = jTblDetFactura.getRowCount();
        double suma, total,isv, subtot, desc, descf;
        suma = 0;
        for (int i = 0 ; i < contar ; i++) {
            suma += Double.parseDouble(jTblDetFactura.getValueAt(i, 5).toString());
        }
        isv = suma * 0.15;
        subtot = suma - isv;
        if (beneficio) {
            desc = suma * 0.10;
            total = suma - desc;
            jTFDescuento.setText(String.valueOf(desc));
        } else {
            desc = 0;
            total = suma;
            jTFDescuento.setText("0");
        }
        desc = 0;
        descf = 0;
        jTFValorTotal.setText(String.valueOf(suma));
        jTFIsv.setText(String.valueOf(isv));
        jTFSubTotal.setText(String.valueOf(subtot));
        jTFTotal.setText(String.valueOf(total));
    }
    
    //Obtener el número de factura
    private void obtenerCodFactura() throws SQLException{
        int codFactura = 0;
        CDFactura cdf = new CDFactura();
        codFactura = cdf.autoIncrementarFacturaCod();
        jTFCodFactura.setText(String.valueOf(codFactura));
    }
    
    //Obtener fecha actual
    private void obtenerFecha(){
        LocalDate.now();
        jTFFecha.setText(String.valueOf(LocalDate.now()));
    }
    
    //Método para habilitar textFields y botones
    private void habilitarBotones(){
        jTFCodFactura.setEnabled(true);
        jTFCodCliente.setEnabled(true);
        jTFCodCliente.setEditable(true);
        jBtnBuscar.setEnabled(true);
        jBtnAgregarCliente.setEnabled(true);
        jTFDescuento.setEnabled(true);
        jTFCliente.setEnabled(true);
        jTFFecha.setEnabled(true);
        jTFCodProducto.setEnabled(true);
        jTFCodProducto.setEditable(true);
        jBtnBuscarProduc.setEnabled(true);
        jTFStock.setEnabled(true);
        jTFPrecio.setEnabled(true);
        jTFNomProducto.setEnabled(true);
        jTFCantidad.setEnabled(true);
        jTFTotalProduc.setEnabled(true);
        jBtnAgregar.setEnabled(true);
        jTFValorTotal.setEnabled(true);
        jTFSubTotal.setEnabled(true);
        jTFIsv.setEnabled(true);
        jTFTotal.setEnabled(true);
        jBtnTablaProducto.setEnabled(true);
        jTFCodigoCliente.setEnabled(true);
        jBtnCancelar.setEnabled(true);
        activar = true;
    }
    
    //Método para desactivar y limpiar todo
    private static void limpiar(){
        jTFCodFactura.setText("");
        jTFCodFactura.setEnabled(false);
        jTFCodCliente.setText("");
        jTFCodCliente.setEnabled(false);
        jTFCodigoCliente.setText("");
        jTFCodigoCliente.setEnabled(false);
        jTFDescuento.setText("");
        jTFDescuento.setEnabled(false);
        jBtnBuscar.setEnabled(false);
        jBtnAgregarCliente.setEnabled(false);
        jTFCliente.setText("");
        jTFCliente.setEnabled(false);
        jTFFecha.setText("");
        jTFFecha.setEnabled(false);
        jTFCodProducto.setText("");
        jTFCodProducto.setEnabled(false);
        jBtnBuscarProduc.setEnabled(false);
        jTFStock.setText("");
        jTFStock.setEnabled(false);
        jTFPrecio.setText("");
        jTFPrecio.setEnabled(false);
        jTFNomProducto.setText("");
        jTFCantidad.setText("");
        jTFTotalProduc.setText("");
        jTFNomProducto.setEnabled(false);
        jTFCantidad.setEnabled(false);
        jTFTotalProduc.setEnabled(false);
        jBtnAgregar.setEnabled(false);
        jBtnEliminar.setEnabled(false);
        jBtnModificar.setEnabled(false);
        jTFValorTotal.setText("");
        jTFSubTotal.setText("");
        jTFIsv.setText("");
        jTFTotal.setText("");
        jTFValorTotal.setEnabled(false);
        jBtnTablaProducto.setEnabled(false);
        jTFSubTotal.setEnabled(false);
        jTFIsv.setEnabled(false);
        jTFTotal.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
        int a = jTblDetFactura.getRowCount()-1;
        for (int i = a; i >= 0; i--) {          
        dtm.removeRow(dtm.getRowCount()-1);
        }
    }
    
    //Método para validar integración de producto
    private static boolean validar(){
        boolean val;
        if(jTFCodCliente.getText().isEmpty()){
            val = false;
            jTFCodCliente.requestFocus();
        }else if (jTFCliente.getText().isEmpty()){
            val = false;
            jTFCliente.requestFocus();
        }else if (jTFCodProducto.getText().isEmpty()){
            val = false;
            jTFCodProducto.requestFocus();
        }else if (jTFNomProducto.getText().isEmpty()){
            val = false;
            jTFNomProducto.requestFocus();
        }else if (jTFCantidad.getText().isEmpty()){
            val = false;
            jTFCantidad.requestFocus();
        }else if(!jTFStock.getText().isEmpty() && !jTFCantidad.getText().isEmpty()){
            int stock, cantidad;
            stock = Integer.parseInt(jTFStock.getText());
            cantidad = Integer.parseInt(jTFCantidad.getText());
            if(cantidad > stock){
                val = false;
                JOptionPane.showMessageDialog(null, "El número de producto solicitado es mayor al disponible");
                jTFCantidad.requestFocus();
            }else{
                val = true;
            }
        }else{
            val = true;
        }
        return val;
    }
    
    //Método para despejar los campos de producto
    private static void vaciarCamposProductos(){
        jTFCodProducto.setText("");
        jTFStock.setText("");
        jTFPrecio.setText("");
        jTFCantidad.setText("");
        jTFNomProducto.setText("");
        jTFTotalProduc.setText("");
    }
    
    //Método para deseleccionar
    private static void desseleccionar() {
        jTblDetFactura.clearSelection();
        jBtnModificar.setEnabled(false);
        jBtnEliminar.setEnabled(false);
        jBtnAgregar.setEnabled(true);
        vaciarCamposProductos();
        jBtnDes.setEnabled(false);
    }
    
    //Método para actualizar un detalle de factura
    private static void actulizarDetFactura() {
        DefaultTableModel model = (DefaultTableModel) jTblDetFactura.getModel();
        if (validar()) {   
            int codDetFactura = 0;
            codDetFactura = Integer.parseInt(jTblDetFactura.getValueAt(fila, 0).toString());
            System.out.print(jTblDetFactura.getValueAt(fila, 0).toString());
            try {
                CDDetFactura cddf = new CDDetFactura();
                CLDetFactura cldf = new CLDetFactura();
                cldf.setCodDetFactura(codDetFactura);
                cldf.setCantidad(Integer.parseInt(jTFCantidad.getText()));
                cldf.setCodFactura(Integer.parseInt(jTFCodFactura.getText()));
                cldf.setPrecio(Double.parseDouble(jTFPrecio.getText()));
                cldf.setCodProducto(Integer.parseInt(jTFCodProducto.getText()));
                cddf.actalizarDetFactura(cldf);
                filas[0] = codDetFactura;
                filas[1] = jTFCodProducto.getText();
                filas[2] = jTFNomProducto.getText();
                filas[3] = jTFCantidad.getText();
                filas[4] = jTFPrecio.getText();
                filas[5] = jTFTotalProduc.getText();
                for (int i = 0; i < 4; i++) {
                    model.setValueAt(filas[i], fila, i);
                }
                jTblDetFactura.setModel(model);
                 desseleccionar();
            } catch(SQLException e){
                 JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
            }
        }
    }
    //Método para eliminar un detalle de factura
    private static void eliminarDetFactura() throws SQLException{
        int opcion;
            Object[] options = {"Si", "No"};
            opcion = JOptionPane.showOptionDialog(null, "¿Esta seguro de cancelar esta factura? Esta información no se podrá recuperar.",
                    "Inventarios master", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);
        if (opcion == 0) {
            int codDetFactura = 0;
            DefaultTableModel model = (DefaultTableModel) jTblDetFactura.getModel();
            codDetFactura = Integer.parseInt(jTblDetFactura.getValueAt(fila, 0).toString());
            System.out.print(jTblDetFactura.getValueAt(fila, 0).toString());
             CDDetFactura cddf = new CDDetFactura();
             CLDetFactura cldf = new CLDetFactura();
             cldf.setCodDetFactura(codDetFactura);
             cddf.eliminarDetFactura(cldf);
             model.removeRow(fila);
        } else {
            
        }
    }
    
    //Insertar detalle factura a la Tabla Mysql
    private void insertarDetFactura() throws SQLException{
        int codDetFactura = 0;
        CDDetFactura cddf = new CDDetFactura();
        CLDetFactura cldf = new CLDetFactura();
        codDetFactura = cddf.autoIncrementarDetFacturaCod();
        cldf.setCodDetFactura(codDetFactura);
        cldf.setCantidad(Integer.parseInt(jTFCantidad.getText()));
        cldf.setPrecio(Double.parseDouble(jTFPrecio.getText()));
        cldf.setCodProducto(Integer.parseInt(jTFCodProducto.getText()));
        cldf.setCodFactura(Integer.parseInt(jTFCodFactura.getText().trim()));
        cddf.insertarDetFactura(cldf);
        System.out.print(cldf.getCodDetFactura());
    }
    
    //Método para borrar todo de las dos tablas
    private static void borrarRegistros() throws SQLException{
        int opcion;
            Object[] options = {"Si", "No"};
            opcion = JOptionPane.showOptionDialog(null, "¿Esta seguro de cancelar esta factura? Esta información no se podrá recuperar.",
                    "Inventarios master", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);
         if (opcion == 0) {
            CDDetFactura cddf = new CDDetFactura();
            CLDetFactura cldf = new CLDetFactura();
            CDFactura cdf = new CDFactura();
            CLFactura cl = new CLFactura();
            cldf.setCodFactura(Integer.parseInt(jTFCodFactura.getText()));
            cddf.eliminarDetFacturaPorCodFactura(cldf);
            cl.setCodFactura(Integer.parseInt(jTFCodFactura.getText()));
            cdf.eliminarFactura(cl); 
            limpiar();
         }   
        
    }
    
    //Método para ir agregando productos a la tabla
    private static void agregarProducto() throws SQLException{
        int codDetFactura = 0;
        CDDetFactura cdf = new CDDetFactura();
        codDetFactura = cdf.autoIncrementarDetFacturaCod();
        Object[] fila = new Object[6];
        DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
        fila[0] = codDetFactura;
        fila[1] = jTFCodProducto.getText();
        fila[2] = jTFNomProducto.getText();
        fila[3] = jTFCantidad.getText();
        fila[4] = jTFPrecio.getText();
        fila[5] = jTFTotalProduc.getText();
        dtm.addRow(fila);
        int cant, stock, stockFin;
        cant = Integer.parseInt(jTFCantidad.getText());
        stock = Integer.parseInt(jTFStock.getText());
        stockFin = stock - cant;
        suma();
        vaciarCamposProductos();
    }
    
    //Método para tomar la selección de un producto
    private static void seleccionTable() throws SQLException{
        jBtnModificar.setEnabled(true);
        jBtnEliminar.setEnabled(true);
        jBtnDes.setEnabled(true);
        jBtnAgregar.setEnabled(false);
        jTFCodProducto.setText(String.valueOf(jTblDetFactura.getValueAt(fila, 0)));
        vaciarCamposProductos();
        jTFCodProducto.setText(String.valueOf(jTblDetFactura.getValueAt(fila, 1)));
        buscarProducto();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFCodFactura = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTFFecha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTFCliente = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTFCodProducto = new javax.swing.JTextField();
        jBtnBuscarProduc = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTFStock = new javax.swing.JTextField();
        jTFPrecio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTFNomProducto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFCantidad = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFTotalProduc = new javax.swing.JTextField();
        jBtnAgregar = new javax.swing.JButton();
        jBtnEliminar = new javax.swing.JButton();
        jBtnModificar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jTFCodCliente = new javax.swing.JTextField();
        jBtnAgregarCliente = new javax.swing.JButton();
        jBtnTablaProducto = new javax.swing.JButton();
        jLbl = new javax.swing.JLabel();
        jTFCodigoCliente = new javax.swing.JTextField();
        jBtnDes = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblDetFactura = new javax.swing.JTable();
        jBtnGenerarVenta = new javax.swing.JButton();
        jBtnCancelar = new javax.swing.JButton();
        jBtnBuscarFact = new javax.swing.JButton();
        jBtnEliminarFact = new javax.swing.JButton();
        jBtnNuevo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTFTotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTFIsv = new javax.swing.JTextField();
        jTFSubTotal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTFValorTotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTFDescuento = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(119, 74, 217));

        jLabel1.setFont(new java.awt.Font("Poppins Medium", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Venta de Productos");

        jTFCodFactura.setEditable(false);
        jTFCodFactura.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("N° de Factura:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTFCodFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jTFCodFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Fecha:");

        jTFFecha.setEnabled(false);

        jLabel4.setText("Cliente:");

        jTFCliente.setEditable(false);
        jTFCliente.setEnabled(false);

        jBtnBuscar.setText("Buscar");
        jBtnBuscar.setEnabled(false);
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        jLabel5.setText("Cod Producto:");

        jTFCodProducto.setEnabled(false);

        jBtnBuscarProduc.setText("Buscar");
        jBtnBuscarProduc.setEnabled(false);
        jBtnBuscarProduc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarProducActionPerformed(evt);
            }
        });

        jLabel6.setText("Stock:");

        jTFStock.setEditable(false);
        jTFStock.setEnabled(false);

        jTFPrecio.setEditable(false);
        jTFPrecio.setEnabled(false);

        jLabel7.setText("Precio por unidad:");

        jLabel8.setText("Nombre Producto:");

        jTFNomProducto.setEditable(false);
        jTFNomProducto.setEnabled(false);

        jLabel9.setText("Cantidad:");

        jTFCantidad.setEnabled(false);
        jTFCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFCantidadKeyReleased(evt);
            }
        });

        jLabel10.setText("Total:");

        jTFTotalProduc.setEditable(false);
        jTFTotalProduc.setEnabled(false);

        jBtnAgregar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnAgregar.setText("AGREGAR");
        jBtnAgregar.setEnabled(false);
        jBtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAgregarActionPerformed(evt);
            }
        });

        jBtnEliminar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnEliminar.setText("ELIMINAR");
        jBtnEliminar.setEnabled(false);
        jBtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEliminarActionPerformed(evt);
            }
        });

        jBtnModificar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnModificar.setText("MODIFICAR");
        jBtnModificar.setEnabled(false);
        jBtnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnModificarActionPerformed(evt);
            }
        });

        jLabel15.setText("Identidad Cliente:");

        jTFCodCliente.setEnabled(false);

        jBtnAgregarCliente.setText("Agregar");
        jBtnAgregarCliente.setEnabled(false);
        jBtnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAgregarClienteActionPerformed(evt);
            }
        });

        jBtnTablaProducto.setText("Buscar en tabla");
        jBtnTablaProducto.setEnabled(false);
        jBtnTablaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTablaProductoActionPerformed(evt);
            }
        });

        jLbl.setText("Código de Cliente:");

        jTFCodigoCliente.setEditable(false);
        jTFCodigoCliente.setEnabled(false);

        jBtnDes.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnDes.setText("QUITAR SELECCIÓN");
        jBtnDes.setEnabled(false);
        jBtnDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jBtnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jBtnEliminar)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnModificar)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnDes))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTFNomProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(169, 169, 169)
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFStock, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addGap(1, 1, 1))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFTotalProduc, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTFCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnBuscarProduc)
                                .addGap(16, 16, 16)
                                .addComponent(jBtnTablaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFCliente)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jBtnBuscar)
                                    .addComponent(jLbl))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jBtnAgregarCliente)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTFFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTFCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jTFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtnBuscar)
                            .addComponent(jBtnAgregarCliente)
                            .addComponent(jLabel3)
                            .addComponent(jTFFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLbl)
                    .addComponent(jTFCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnBuscarProduc)
                    .addComponent(jLabel6)
                    .addComponent(jTFStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jBtnTablaProducto))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTFNomProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTFCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTFTotalProduc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnAgregar)
                    .addComponent(jBtnEliminar)
                    .addComponent(jBtnModificar)
                    .addComponent(jBtnDes))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTblDetFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod de Venta", "CodProducto", "Nombre", "Cantidad", "Precio", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblDetFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblDetFacturaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTblDetFactura);

        jBtnGenerarVenta.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnGenerarVenta.setText("GENERAR VENTA");
        jBtnGenerarVenta.setEnabled(false);

        jBtnCancelar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnCancelar.setText("CANCELAR");
        jBtnCancelar.setEnabled(false);
        jBtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelarActionPerformed(evt);
            }
        });

        jBtnBuscarFact.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnBuscarFact.setText("BUSCAR FACTURA");
        jBtnBuscarFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarFactActionPerformed(evt);
            }
        });

        jBtnEliminarFact.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnEliminarFact.setText("ELIMINAR");
        jBtnEliminarFact.setEnabled(false);
        jBtnEliminarFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEliminarFactActionPerformed(evt);
            }
        });

        jBtnNuevo.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnNuevo.setText("NUEVO");
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(51, 34, 89));

        jTFTotal.setEditable(false);
        jTFTotal.setFont(new java.awt.Font("Poppins SemiBold", 1, 11)); // NOI18N
        jTFTotal.setForeground(new java.awt.Color(0, 255, 0));

        jLabel11.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Total a pagar:");

        jLabel14.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("ISV:");

        jTFIsv.setEditable(false);

        jTFSubTotal.setEditable(false);

        jLabel13.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Sub total:");

        jLabel12.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Valor total:");

        jTFValorTotal.setEditable(false);
        jTFValorTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFValorTotalKeyReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Descuento:");

        jTFDescuento.setEditable(false);
        jTFDescuento.setFont(new java.awt.Font("Poppins SemiBold", 1, 11)); // NOI18N
        jTFDescuento.setForeground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTFValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel12)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jTFSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTFIsv, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTFDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLabel13)
                        .addGap(158, 158, 158)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addGap(68, 68, 68)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jTFTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(71, 71, 71))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFIsv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBtnNuevo)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnGenerarVenta)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnBuscarFact)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnEliminarFact)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnBuscarFact, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnEliminarFact, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        try {
            // TODO add your handling code here:
            limpiar();
            obtenerCodFactura();
            habilitarBotones();
            obtenerFecha();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage());
        }
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnBuscarFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarFactActionPerformed
        // TODO add your handling code here:
        try {
            JFraControlFacturas jfra = new JFraControlFacturas();
            jBtnEliminarFact.setEnabled(true);
            jBtnCancelar.setEnabled(true);
            jfra.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jBtnBuscarFactActionPerformed

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        if (activar == true) {
            try {
                // TODO add your handling code here:
                borrarRegistros();
                jBtnCancelar.setEnabled(false);
                limpiar();
            } catch (SQLException ex) {
                Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            limpiar();
            jBtnCancelar.setEnabled(false);
        }
        
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
        try {
            // TODO add your handling code here:
            buscarCliente();
        } catch (SQLException ex) {
            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnBuscarActionPerformed

    private void jBtnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAgregarClienteActionPerformed
//        try {
//            // TODO add your handling code here:
//            jFraCliente jfc = new jFraCliente();
//            jfc.setVisible(true);
//        } catch (SQLException ex) {
//            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jBtnAgregarClienteActionPerformed

    private void jBtnTablaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTablaProductoActionPerformed
        // TODO add your handling code here:
        JFraBuscarProducto jfrap;
        try {
            jfrap = new JFraBuscarProducto();
            jfrap.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jBtnTablaProductoActionPerformed

    private void jBtnBuscarProducActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarProducActionPerformed
        try {
            // TODO add your handling code here:
            buscarProducto();
        } catch (SQLException ex) {
            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnBuscarProducActionPerformed

    private void jTFCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFCantidadKeyReleased
        // TODO add your handling code here:
        if(!jTFNomProducto.getText().isEmpty()){
            totalizarPrecio();
        } 
    }//GEN-LAST:event_jTFCantidadKeyReleased

    private void jBtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAgregarActionPerformed
        try {
            // TODO add your handling code here:
            if(validar()){
               insertarDetFactura();
               agregarProducto();
            }else{
                JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage());
        }
    }//GEN-LAST:event_jBtnAgregarActionPerformed

    private void jTblDetFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblDetFacturaMouseClicked
        // TODO add your handling code here:
        if (activar == false) {
            
        } else {
            fila = jTblDetFactura.rowAtPoint(evt.getPoint());
            try {
                seleccionTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage());
            }
        }

    }//GEN-LAST:event_jTblDetFacturaMouseClicked

    private void jBtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEliminarActionPerformed
        try {
            // TODO add your handling code here:
            eliminarDetFactura();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: "+ex);
        }
    }//GEN-LAST:event_jBtnEliminarActionPerformed

    private void jBtnEliminarFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEliminarFactActionPerformed
        try {
            borrarRegistros();
            jBtnCancelar.setEnabled(false);
            jBtnEliminarFact.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnEliminarFactActionPerformed

    private void jTFValorTotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFValorTotalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFValorTotalKeyReleased

    private void jBtnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnModificarActionPerformed
        // TODO add your handling code here:
        actulizarDetFactura();
    }//GEN-LAST:event_jBtnModificarActionPerformed

    private void jBtnDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDesActionPerformed
        // TODO add your handling code here:
        desseleccionar();
    }//GEN-LAST:event_jBtnDesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFraFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFraFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFraFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFraFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JFraFactura().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jBtnAgregar;
    private static javax.swing.JButton jBtnAgregarCliente;
    private static javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnBuscarFact;
    private static javax.swing.JButton jBtnBuscarProduc;
    private javax.swing.JButton jBtnCancelar;
    private static javax.swing.JButton jBtnDes;
    private static javax.swing.JButton jBtnEliminar;
    private javax.swing.JButton jBtnEliminarFact;
    private javax.swing.JButton jBtnGenerarVenta;
    private static javax.swing.JButton jBtnModificar;
    private javax.swing.JButton jBtnNuevo;
    private static javax.swing.JButton jBtnTablaProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextField jTFCantidad;
    public static javax.swing.JTextField jTFCliente;
    public static javax.swing.JTextField jTFCodCliente;
    public static javax.swing.JTextField jTFCodFactura;
    public static javax.swing.JTextField jTFCodProducto;
    private static javax.swing.JTextField jTFCodigoCliente;
    private static javax.swing.JTextField jTFDescuento;
    public static javax.swing.JTextField jTFFecha;
    private static javax.swing.JTextField jTFIsv;
    public static javax.swing.JTextField jTFNomProducto;
    public static javax.swing.JTextField jTFPrecio;
    public static javax.swing.JTextField jTFStock;
    private static javax.swing.JTextField jTFSubTotal;
    private static javax.swing.JTextField jTFTotal;
    public static javax.swing.JTextField jTFTotalProduc;
    private static javax.swing.JTextField jTFValorTotal;
    public static javax.swing.JTable jTblDetFactura;
    // End of variables declaration//GEN-END:variables
}
