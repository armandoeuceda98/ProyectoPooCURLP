/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capapresentacion;

import com.curlp.capadatos.CDProveedor;
import com.curlp.capalogica.CLProveedor;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 50498
 */
public class jFraProveedor extends javax.swing.JFrame {

    /**
     * Creates new form jFraProveedor
     */
    public jFraProveedor() throws SQLException {
        initComponents();
        poblarTabla();
        encontrarCorrelativo();
        this.jTFEmpresa.requestFocus();
        this.setLocationRelativeTo(null);
    }
    //Limpiar tabla
    private void limpiarTabla(){
        DefaultTableModel dtm = (DefaultTableModel) this.jTblProveedor.getModel();
        
        while (dtm.getRowCount() > 0){
            dtm.removeRow(0);
        }
    }
    //Poblar tabla
    private void poblarTabla() throws SQLException{
        limpiarTabla();
        
        CDProveedor cdp = new CDProveedor();
        List<CLProveedor> myList = cdp.mostrarProveedor();
        DefaultTableModel temp = (DefaultTableModel) this.jTblProveedor.getModel();
        
        myList.stream().map((CLProveedor cl) -> {
            Object[] fila = new Object[6];
            fila[0] = cl.getCodProveedor();
            fila[1] = cl.getNombreEmpresa();
            fila[2] = cl.getTelefono();
            fila[3] = cl.getCorreo();
            fila[4] = cl.getNombreRepresentante();
            fila[5] = cl.getTelefonoRepresentante();
            return fila;
        }).forEachOrdered(temp::addRow);
    }

    //Buscar en tabla
    private void buscar() throws SQLException {
        if (!validarBuscar()) {
            JOptionPane.showMessageDialog(null, "Tiene que ingresar todos los datos de busqueda.", "Inventarios Master", 1);
        } else {
            boolean estado = false;
            for (int i = 0; i < this.jTblProveedor.getRowCount(); i++) {
                if (this.jCBColumna.getSelectedIndex() == 1) {
                    if (this.jTblProveedor.getValueAt(i, 0) == Integer.valueOf(this.jTFBuscar.getText())) {;
                        String dato = "Código: " + this.jTblProveedor.getValueAt(i, 0)
                                + "\nEmpresa: " + this.jTblProveedor.getValueAt(i, 1)
                                + "\nTelefono: " + this.jTblProveedor.getValueAt(i, 2)
                                + "\nCorreo: " + this.jTblProveedor.getValueAt(i, 3)
                                + "\nRepresentante: " + this.jTblProveedor.getValueAt(i, 4)
                                + "\nTel. Representante: " + this.jTblProveedor.getValueAt(i, 5);
                        JOptionPane.showMessageDialog(null, dato, "Dato encontrado", 1);
                        estado = true;
                    }
                } else if (this.jCBColumna.getSelectedIndex() == 2) {
                    String c = (String) this.jTblProveedor.getValueAt(i, 1);
                    if (c.equals(this.jTFBuscar.getText())) {
                        String dato = "Código: " + this.jTblProveedor.getValueAt(i, 0)
                                + " Empresa: " + this.jTblProveedor.getValueAt(i, 1)
                                + " Telefono: " + this.jTblProveedor.getValueAt(i, 2)
                                + " Correo: " + this.jTblProveedor.getValueAt(i, 3)
                                + " Representante: " + this.jTblProveedor.getValueAt(i, 4)
                                + " Tel. Representante: " + this.jTblProveedor.getValueAt(i, 5);
                        JOptionPane.showMessageDialog(null, dato, "Dato encontrado", 1);
                        estado = true;
                    }
                }
            }
            if(estado == false){
                JOptionPane.showMessageDialog(null, "No se ha encontrado registro", "Dato no encontrado", 1);
            }
        }
        
    }
    //Encontrar Correlativo
    private void encontrarCorrelativo() throws SQLException{
        CDProveedor cdp = new CDProveedor();
        CLProveedor cl = new CLProveedor();
        
        cl.setCodProveedor(cdp.autoIncrementarCodProveedor());
        this.jTFCodProveedor.setText(String.valueOf(cl.getCodProveedor()));
    }
    //Habilitar y deshabilitar botones
    private void habilitarBotones(boolean guardar, boolean editar, boolean eliminar, boolean limpiar){
        this.jBtnGuardar.setEnabled(guardar);
        this.jBtnEditar.setEnabled(editar);
        this.jBtnEliminar.setEnabled(eliminar);
        this.jBtnLimpiar.setEnabled(limpiar);
    }
    //Limpiar TextFields
    private void limpiarTF(){
        this.jTFEmpresa.setText("");
        this.jTFTelefono.setText("");
        this.jTFCorreo.setText("");
        this.jTFRepresentante.setText("");
        this.jTFTelefonoRepresentante.setText("");
        this.jTFBuscar.setText("");
        this.jCBColumna.setSelectedIndex(0);
        this.jTblProveedor.clearSelection();
        habilitarBotones(true, false, false, true);
        this.jTFEmpresa.requestFocus();
    }
    //Validar TextFields
    private boolean validarTF(){
        boolean estado = true;
        
        if(this.jTFEmpresa.getText().equals("")){
            estado = false;
            this.jTFEmpresa.requestFocus();
        }else if(this.jTFTelefono.getText().equals("")){
            estado = false;
            this.jTFEmpresa.requestFocus();
        }else if(this.jTFCorreo.getText().equals("")){
            estado = false;
            this.jTFCorreo.requestFocus();
        }else if(this.jTFRepresentante.getText().equals("")){
            estado = false;
            this.jTFRepresentante.requestFocus();
        }else if(this.jTFTelefonoRepresentante.getText().equalsIgnoreCase("")){
            estado = false;
            this.jTFTelefonoRepresentante.requestFocus();
        }
        
        return estado;
    }
    //Validar busqueda
    private boolean validarBuscar(){
        boolean estado = true;
        
        if(this.jCBColumna.getSelectedIndex() == 0){
            estado = false;
            this.jCBColumna.requestFocus();
        }else if(this.jTFBuscar.getText().equals("")){
            estado = false;
            this.jTFBuscar.requestFocus();
        }
        
        return estado;
    }
    //Insertar a tabla
    private void insertarProveedor(){
        if (!validarTF()){
            JOptionPane.showMessageDialog(null, "Tiene que ingresar todos los datos.", "Inventarios Master", 1);
        }else{
            try {
                CDProveedor cdp = new CDProveedor();
                CLProveedor cl = new CLProveedor();
                cl.setNombreEmpresa(this.jTFEmpresa.getText().trim());
                cl.setTelefono(this.jTFTelefono.getText());
                cl.setCorreo(this.jTFCorreo.getText());
                cl.setNombreRepresentante(this.jTFRepresentante.getText());
                cl.setTelefonoRepresentante(this.jTFTelefonoRepresentante.getText());
                
                cdp.insertarProveedor(cl);
                JOptionPane.showMessageDialog(null, "Registro ingresado de manera correcta", "Inventarios Master", 1);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error: " + ex);
            }
        }
    }
    //Método para el evento guardar
    private void guardar() throws SQLException{
        insertarProveedor();
        poblarTabla();
        habilitarBotones(true, false, false, true);
        limpiarTF();
        encontrarCorrelativo();
    }
    //Método para el evento editar
    private void editar() throws SQLException{
        actualizarProveedor();
        poblarTabla();
        habilitarBotones(true, false, false, true);
        limpiarTF();
        encontrarCorrelativo();
    }
    //Método para actualizar
    private void actualizarProveedor(){
        if (!validarTF()){
            JOptionPane.showMessageDialog(null, "Tiene que ingresar todos los datos.", "Inventarios Master", 1);
        }else{
            try {
                CDProveedor cdp = new CDProveedor();
                CLProveedor cl = new CLProveedor();
                cl.setCodProveedor(Integer.valueOf(this.jTFCodProveedor.getText()));
                cl.setNombreEmpresa(this.jTFEmpresa.getText().trim());
                cl.setTelefono(this.jTFTelefono.getText());
                cl.setCorreo(this.jTFCorreo.getText());
                cl.setNombreRepresentante(this.jTFRepresentante.getText());
                cl.setTelefonoRepresentante(this.jTFTelefonoRepresentante.getText());
                
                cdp.actualizarProveedor(cl);
                JOptionPane.showMessageDialog(null, "Registro actualizado de manera correcta", "Inventarios Master", 1);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error: " + ex);
            }
        }
    }
    //Método para seleccionar datos
    private void filaSeleccionada(){
        if (this.jTblProveedor.getSelectedRow() != -1){
            this.jTFCodProveedor.setText(String.valueOf(this.jTblProveedor.getValueAt(this.jTblProveedor.getSelectedRow(), 0)));
            this.jTFEmpresa.setText(String.valueOf(this.jTblProveedor.getValueAt(this.jTblProveedor.getSelectedRow(), 1)));
            this.jTFTelefono.setText(String.valueOf(this.jTblProveedor.getValueAt(this.jTblProveedor.getSelectedRow(), 2)));
            this.jTFCorreo.setText(String.valueOf(this.jTblProveedor.getValueAt(this.jTblProveedor.getSelectedRow(), 3)));
            this.jTFRepresentante.setText(String.valueOf(this.jTblProveedor.getValueAt(this.jTblProveedor.getSelectedRow(), 4)));
            this.jTFTelefonoRepresentante.setText(String.valueOf(this.jTblProveedor.getValueAt(this.jTblProveedor.getSelectedRow(), 5)));
        }
    }
    //Método para eliminar
    private void eliminarProveedor(){
        if (!validarTF()){
            JOptionPane.showMessageDialog(null, "Tiene que ingresar todos los datos.", "Inventarios Master", 1);
        }else{
            try {
                CDProveedor cdp = new CDProveedor();
                CLProveedor cl = new CLProveedor();
                cl.setCodProveedor(Integer.valueOf(this.jTFCodProveedor.getText()));
                
                cdp.eliminarProveedor(cl);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error al eliminar registro: " + ex);
                this.jTFEmpresa.requestFocus();
            }
        }
    }
    //Método para el evento eliminar
    private void eliminar() throws SQLException{
        int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Inventarios Master", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION){
            try {
                eliminarProveedor();
                poblarTabla();
                habilitarBotones(true, false, false, true);
                limpiarTF();
                encontrarCorrelativo();
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error: " + ex);
            }
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
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFCodProveedor = new javax.swing.JTextField();
        jTFEmpresa = new javax.swing.JTextField();
        jBtnGuardar = new javax.swing.JButton();
        jBtnEditar = new javax.swing.JButton();
        jBtnEliminar = new javax.swing.JButton();
        jBtnLimpiar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTFTelefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTFCorreo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTFRepresentante = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFTelefonoRepresentante = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblProveedor = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jCBColumna = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jTFBuscar = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(174, 159, 228));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Gestión de Proveedores");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("X");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 890, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 44, 890, 20));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(51, 34, 89));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Código Proveedor");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 17, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Empresa");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        jTFCodProveedor.setEditable(false);
        jPanel4.add(jTFCodProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 12, 99, -1));
        jPanel4.add(jTFEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 38, 99, -1));

        jBtnGuardar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnGuardar.setText("Guardar");
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });
        jPanel4.add(jBtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 186, -1, -1));

        jBtnEditar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnEditar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnEditar.setText("Editar");
        jBtnEditar.setEnabled(false);
        jBtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditarActionPerformed(evt);
            }
        });
        jPanel4.add(jBtnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 186, -1, -1));

        jBtnEliminar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnEliminar.setText("Eliminar");
        jBtnEliminar.setEnabled(false);
        jBtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEliminarActionPerformed(evt);
            }
        });
        jPanel4.add(jBtnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(179, 186, -1, -1));

        jBtnLimpiar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnLimpiar.setText("Limpiar");
        jBtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLimpiarActionPerformed(evt);
            }
        });
        jPanel4.add(jBtnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 215, 237, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Teléfono");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 66, -1, -1));
        jPanel4.add(jTFTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 64, 99, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Correo");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 92, -1, -1));
        jPanel4.add(jTFCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 90, 99, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Representante");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 118, -1, -1));
        jPanel4.add(jTFRepresentante, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 116, 99, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Tel. Representante");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 144, -1, -1));
        jPanel4.add(jTFTelefonoRepresentante, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 142, 99, -1));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 261, 250));

        jPanel5.setBackground(new java.awt.Color(51, 34, 89));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTblProveedor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTblProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod. Proveedor", "Empresa", "Teléfono", "Correo", "Representante", "Tel. Representante"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblProveedor.setShowGrid(true);
        jTblProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblProveedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTblProveedor);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 590, 200));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Buscar:");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Columna:");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        jCBColumna.setBackground(new java.awt.Color(119, 74, 217));
        jCBColumna.setForeground(new java.awt.Color(255, 255, 255));
        jCBColumna.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "Código de Proveedor", "Empresa" }));
        jPanel5.add(jCBColumna, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 110, -1));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Dato:");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 220, -1, -1));
        jPanel5.add(jTFBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, 120, -1));

        jBtnBuscar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnBuscar.setText("Buscar");
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });
        jPanel5.add(jBtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, -1, -1));

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(289, 11, 590, 250));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 890, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        this.dispose();
    }//GEN-LAST:event_jLabel5MousePressed

    private void jBtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLimpiarActionPerformed
        try {
            limpiarTF();
            encontrarCorrelativo();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
        
    }//GEN-LAST:event_jBtnLimpiarActionPerformed

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        try {
            guardar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }//GEN-LAST:event_jBtnGuardarActionPerformed

    private void jTblProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblProveedorMouseClicked
        filaSeleccionada();
        habilitarBotones(false, true, true, true);
    }//GEN-LAST:event_jTblProveedorMouseClicked

    private void jBtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditarActionPerformed
        try {
            editar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }//GEN-LAST:event_jBtnEditarActionPerformed

    private void jBtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEliminarActionPerformed
        try {
            eliminar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }//GEN-LAST:event_jBtnEliminarActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
        try {
            buscar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }//GEN-LAST:event_jBtnBuscarActionPerformed

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
            java.util.logging.Logger.getLogger(jFraProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jFraProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jFraProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jFraProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new jFraProveedor().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(jFraProveedor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnEditar;
    private javax.swing.JButton jBtnEliminar;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JButton jBtnLimpiar;
    private javax.swing.JComboBox<String> jCBColumna;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFBuscar;
    private javax.swing.JTextField jTFCodProveedor;
    private javax.swing.JTextField jTFCorreo;
    private javax.swing.JTextField jTFEmpresa;
    private javax.swing.JTextField jTFRepresentante;
    private javax.swing.JTextField jTFTelefono;
    private javax.swing.JTextField jTFTelefonoRepresentante;
    private javax.swing.JTable jTblProveedor;
    // End of variables declaration//GEN-END:variables
}
