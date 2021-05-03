/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capapresentacion;

import com.curlp.capadatos.CDCliente;
import com.curlp.capadatos.CDDetFactura;
import com.curlp.capadatos.CDFactura;
import com.curlp.capalogica.CLCliente;
import com.curlp.capalogica.CLDetFactura;
import com.curlp.capalogica.CLFactura;
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

    /**
     * Creates new form JFraFactura
     */

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
    private static void poblarTabla(String i) throws SQLException{  
        limpiarTabla();
        CDDetFactura cddf = new CDDetFactura();
        List<CLDetFactura> miLista = cddf.obtenerListaDetFacturas(i);
        DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
        
        miLista.stream().map((CLDetFactura cl) -> {
            Object[] fila = new Object[5];
            fila[0] = cl.getCodDetFactura();
            fila[1] = cl.getNomProducto();
            fila[2] = cl.getCantidad();
            fila[3] = cl.getPrecio();
            fila[4] = cl.getTotal();
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
        jTFCliente.setEnabled(true);
        jTFFecha.setEnabled(true);
        jTFFecha.setEditable(false);
        jTFCodProducto.setEnabled(true);
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
    }
    
    //Método para buscar un cliente
    private static void buscarCliente() throws SQLException{
        CLCliente clc = new CLCliente();
        CDCliente cdc = new CDCliente();
        clc.setDocIdentidad(jTFCodCliente.getText().trim());
        cdc.obtenerListaClienteXId(clc);
        if(cdc.obtenerListaClienteXId(clc)==null){
            JOptionPane.showMessageDialog(null, "No se encontró ningún cliente.");
        }else{
            jTFCliente.setText(clc.getNombre());
        }
    }
    
    //poblar Todo el formulario y jTable a partir de abrir factura
    public static void poblarForm() throws SQLException{
        String i;
        CDFactura cdf = new CDFactura();
        CLFactura cle = new CLFactura();
        cle.setCodFactura(Integer.parseInt(jTFCodFactura.getText().trim()));
        String cl = cdf.poblarForm(cle).getFecha();
        jTFFecha.setText(cle.getFecha());
        jTFCliente.setText(cle.getNombreCliente());
        i = jTFCodFactura.getText();
        poblarTabla(i);
        suma();
    }
    
    //Método para hacer una suma de todos los totales de factura
    private static void suma(){
        int contar  = jTblDetFactura.getRowCount();
        double suma, total,isv, subtot, desc;
        suma = 0;
        desc = 0.10;
        for(int i = 0 ; i < contar ; i++){
            suma += Double.parseDouble(jTblDetFactura.getValueAt(i, 4).toString());
        }
        isv = suma * 0.15;
        subtot = suma - isv;
        total = suma - desc;
        jTFValorTotal.setText(String.valueOf(suma));
        jTFIsv.setText(String.valueOf(isv));
        jTFSubTotal.setText(String.valueOf(subtot));
        jTFTotal.setText(String.valueOf(suma));
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
        this.jBtnBuscar.setEnabled(true);
        this.jBtnAgregarCliente.setEnabled(true);
        this.jTFCliente.setEnabled(true);
        this.jTFFecha.setEnabled(true);
        this.jTFCodProducto.setEnabled(true);
        this.jBtnBuscarProduc.setEnabled(true);
        this.jTFStock.setEnabled(true);
        this.jTFPrecio.setEnabled(true);
        this.jTFNomProducto.setEnabled(true);
        this.jTFCantidad.setEnabled(true);
        this.jTFTotalProduc.setEnabled(true);
        this.jBtnAgregar.setEnabled(true);
        this.jBtnEliminar.setEnabled(true);
        this.jBtnModificar.setEnabled(true);
        this.jBtnCancelar.setEnabled(true);
        this.jTFValorTotal.setEnabled(true);
        this.jTFSubTotal.setEnabled(true);
        this.jTFIsv.setEnabled(true);
        this.jTFTotal.setEnabled(true);  
    }
    
    //Método para desactivar y limpiar todo
    private void limpiar(){
        jTFCodFactura.setText("");
        jTFCodFactura.setEnabled(false);
        jTFCodCliente.setText("");
        jTFCodCliente.setEnabled(false);
        this.jBtnBuscar.setEnabled(false);
        this.jBtnAgregarCliente.setEnabled(false);
        jTFCliente.setText("");
        jTFCliente.setEnabled(false);
        jTFFecha.setText("");
        jTFFecha.setEnabled(false);
        jTFCodProducto.setText("");
        jTFCodProducto.setEnabled(false);
        this.jBtnBuscarProduc.setEnabled(false);
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
        this.jBtnAgregar.setEnabled(false);
        this.jBtnEliminar.setEnabled(false);
        this.jBtnModificar.setEnabled(false);
        jTFValorTotal.setText("");
        jTFSubTotal.setText("");
        jTFIsv.setText("");
        jTFTotal.setText("");
        jTFValorTotal.setEnabled(false);
        jTFSubTotal.setEnabled(false);
        jTFIsv.setEnabled(false);
        jTFTotal.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) jTblDetFactura.getModel();
        int a = jTblDetFactura.getRowCount()-1;
        for (int i = a; i >= 0; i--) {          
        dtm.removeRow(dtm.getRowCount()-1);
        }
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

        jLabel6.setText("Stock:");

        jTFStock.setEditable(false);
        jTFStock.setEnabled(false);

        jTFPrecio.setEditable(false);
        jTFPrecio.setEnabled(false);

        jLabel7.setText("Precio por unidad:");

        jLabel8.setText("Nombre Producto:");

        jTFNomProducto.setEnabled(false);

        jLabel9.setText("Cantidad:");

        jTFCantidad.setEnabled(false);

        jLabel10.setText("Total:");

        jTFTotalProduc.setEditable(false);
        jTFTotalProduc.setEnabled(false);

        jBtnAgregar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnAgregar.setText("AGREGAR");
        jBtnAgregar.setEnabled(false);

        jBtnEliminar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnEliminar.setText("ELIMINAR");
        jBtnEliminar.setEnabled(false);

        jBtnModificar.setFont(new java.awt.Font("Poppins ExtraBold", 0, 11)); // NOI18N
        jBtnModificar.setText("MODIFICAR");
        jBtnModificar.setEnabled(false);

        jLabel15.setText("Identidad Cliente:");

        jTFCodCliente.setEnabled(false);

        jBtnAgregarCliente.setText("Agregar");
        jBtnAgregarCliente.setEnabled(false);
        jBtnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAgregarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBtnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jBtnEliminar)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnModificar)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTFNomProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTFStock, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 21, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFTotalProduc))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTFCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnBuscarProduc))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jTFCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jTFCliente)))
                        .addGap(18, 18, 18)
                        .addComponent(jBtnBuscar)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnAgregarCliente)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTFFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addContainerGap(200, Short.MAX_VALUE))
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
                    .addComponent(jLabel4))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnBuscarProduc)
                    .addComponent(jLabel6)
                    .addComponent(jTFStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
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
                    .addComponent(jBtnModificar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTblDetFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod de Venta", "Nombre", "Cantidad", "Precio", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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

        jLabel16.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Descuento:");

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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(88, 88, 88))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jTFTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addContainerGap(83, Short.MAX_VALUE))
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
        // TODO add your handling code here:
        limpiar();
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
        try {
            // TODO add your handling code here:
            jFraCliente jfc = new jFraCliente();
            jfc.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(JFraFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnAgregarClienteActionPerformed

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
    private javax.swing.JButton jBtnAgregar;
    private javax.swing.JButton jBtnAgregarCliente;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnBuscarFact;
    private javax.swing.JButton jBtnBuscarProduc;
    private javax.swing.JButton jBtnCancelar;
    private javax.swing.JButton jBtnEliminar;
    private javax.swing.JButton jBtnEliminarFact;
    private javax.swing.JButton jBtnGenerarVenta;
    private javax.swing.JButton jBtnModificar;
    private javax.swing.JButton jBtnNuevo;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextField jTFCantidad;
    public static javax.swing.JTextField jTFCliente;
    public static javax.swing.JTextField jTFCodCliente;
    public static javax.swing.JTextField jTFCodFactura;
    public static javax.swing.JTextField jTFCodProducto;
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
