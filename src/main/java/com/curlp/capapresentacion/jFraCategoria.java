/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capapresentacion;

import com.curlp.capadatos.CDCategoria;
import com.curlp.capalogica.CLCategoria;
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
public class jFraCategoria extends javax.swing.JFrame {

    /**
     * Creates new form jFraCategoria
     */
    public jFraCategoria() throws SQLException{
        initComponents();
        poblarTabla();
        encontrarCorrelativo();
        this.jTFNombreCategoria.requestFocus();
        this.setLocationRelativeTo(null);
    }
    //Limpiar tabla
    private void limpiarTabla(){
        DefaultTableModel dtm = (DefaultTableModel) this.jTblCategoria.getModel();
        
        while (dtm.getRowCount() > 0){
            dtm.removeRow(0);
        }
    }
    //Poblar tabla
    private void poblarTabla() throws SQLException{
        limpiarTabla();
        
        CDCategoria cdm = new CDCategoria();
        List<CLCategoria> myList = cdm.obtenerCategoria();
        DefaultTableModel temp = (DefaultTableModel) this.jTblCategoria.getModel();
        
        myList.stream().map((CLCategoria cl) -> {
            Object[] fila = new Object[2];
            fila[0] = cl.getIdCategoria();
            fila[1] = cl.getNombre();
            return fila;
        }).forEachOrdered(temp::addRow);
    }
    //Buscar y mostrar
    //Buscar y mostrar en tabla
    private void buscarTabla() throws SQLException{
        limpiarTabla();
        if (!validarBuscar()) {
            JOptionPane.showMessageDialog(null, "Tiene que ingresar todos los datos de busqueda.", "Inventarios Master", 1);
        } else {
            CDCategoria cdm = new CDCategoria();
            CLCategoria clm = new CLCategoria();
            
            List<CLCategoria> myList = null;
            if(this.jCBColumna.getSelectedIndex() == 1){
                clm.setIdCategoria(Integer.valueOf(this.jTFBuscar.getText().trim()));
                myList = cdm.buscarCategoria(clm);
            }else if(this.jCBColumna.getSelectedIndex() == 2){
                clm.setNombre(this.jTFBuscar.getText().trim());
                myList = cdm.buscarCategoria(clm);
            }
            
            DefaultTableModel temp = (DefaultTableModel) this.jTblCategoria.getModel();

            myList.stream().map((CLCategoria cl) -> {
                Object[] fila = new Object[2];
                fila[0] = cl.getIdCategoria();
                fila[1] = cl.getNombre();
                return fila;
            }).forEachOrdered(temp::addRow);
        }
    }
    //Encontrar Correlativo
    private void encontrarCorrelativo() throws SQLException{
        CDCategoria cdm = new CDCategoria();
        CLCategoria cl = new CLCategoria();
        
        cl.setIdCategoria(cdm.autoIncrementarCategoria());
        this.jTFIdCategoria.setText(String.valueOf(cl.getIdCategoria()));
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
        this.jTFNombreCategoria.setText("");
        this.jTFBuscar.setText("");
        this.jCBColumna.setSelectedIndex(0);
        this.jTblCategoria.clearSelection();
        habilitarBotones(true, false, false, true);
        this.jTFNombreCategoria.requestFocus();
    }
    //Validar TextFields
    private boolean validarTF(){
        boolean estado;
        
        estado = !this.jTFIdCategoria.getText().equals("");
        
        return estado;
    }
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
    private void insertarCategoria(){
        if (!validarTF()){
            JOptionPane.showMessageDialog(null, "Tiene que ingresar nombre de la Categoria", "Inventarios Master", 1);
            this.jTFNombreCategoria.requestFocus();
        }else{
            try {
                CDCategoria cdm = new CDCategoria();
                CLCategoria cl = new CLCategoria();
                cl.setNombre(this.jTFNombreCategoria.getText().trim());
                
                cdm.insertarCategoria(cl);
                JOptionPane.showMessageDialog(null, "Registro ingresado de manera Categoria", "Inventarios Master", 1);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error: " + ex);
                this.jTFNombreCategoria.requestFocus();
            }
        }
    }
    //Método para el evento guardar
    private void guardar() throws SQLException{
        insertarCategoria();
        poblarTabla();
        habilitarBotones(true, false, false, true);
        limpiarTF();
        encontrarCorrelativo();
    }
    //Método para el evento editar
    private void editar() throws SQLException{
        actualizarCategoria();
        poblarTabla();
        habilitarBotones(true, false, false, true);
        limpiarTF();
        encontrarCorrelativo();
    }
    //Método para actualizar
    private void actualizarCategoria(){
        if (!validarTF()){
            JOptionPane.showMessageDialog(null, "Tiene que ingresar nombre de la Categoria", "Inventarios Master", 1);
            this.jTFNombreCategoria.requestFocus();
        }else{
            try {
                CDCategoria cdm = new CDCategoria();
                CLCategoria cl = new CLCategoria();
                cl.setIdCategoria(Integer.valueOf(this.jTFIdCategoria.getText()));
                cl.setNombre(this.jTFNombreCategoria.getText().trim());
                
                cdm.actualizarCategoria(cl);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error al actualizar registro: " + ex);
                this.jTFNombreCategoria.requestFocus();
            }
        }
    }
    //Método para seleccionar datos
    private void filaSeleccionada(){
        if (this.jTblCategoria.getSelectedRow() != -1){
            this.jTFIdCategoria.setText(String.valueOf(this.jTblCategoria.getValueAt(this.jTblCategoria.getSelectedRow(), 0)));
            this.jTFNombreCategoria.setText(String.valueOf(this.jTblCategoria.getValueAt(this.jTblCategoria.getSelectedRow(), 1)));
        }
    }
    //Método para eliminar
    private void eliminarCategoria(){
        if (!validarTF()){
            JOptionPane.showMessageDialog(null, "Tiene que ingresar nombre de la Categoria", "Inventarios Master", 1);
            this.jTFNombreCategoria.requestFocus();
        }else{
            try {
                CDCategoria cdm = new CDCategoria();
                CLCategoria cl = new CLCategoria();
                cl.setIdCategoria(Integer.valueOf(this.jTFIdCategoria.getText()));
                
                cdm.eliminarCategoria(cl);
            } catch (SQLException ex){
                JOptionPane.showMessageDialog(null, "Error al eliminar registro: " + ex);
                this.jTFNombreCategoria.requestFocus();
            }
        }
    }
    //Método para el evento eliminar
    private void eliminar() throws SQLException{
        int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Inventarios Master", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION){
            try {
                eliminarCategoria();
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFIdCategoria = new javax.swing.JTextField();
        jTFNombreCategoria = new javax.swing.JTextField();
        jBtnGuardar = new javax.swing.JButton();
        jBtnEditar = new javax.swing.JButton();
        jBtnEliminar = new javax.swing.JButton();
        jBtnLimpiar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTFBuscar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jCBColumna = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jBtnBuscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblCategoria = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(174, 159, 228));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("Gestión de Categoria");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel5.setForeground(java.awt.Color.white);
        jLabel5.setText("X");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 44, 650, 20));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jPanel4.setBackground(new java.awt.Color(51, 34, 89));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("ID Categoria");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Nombre de Categoria");

        jTFIdCategoria.setEditable(false);

        jBtnGuardar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnGuardar.setForeground(java.awt.Color.white);
        jBtnGuardar.setText("Guardar");
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });

        jBtnEditar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnEditar.setForeground(java.awt.Color.white);
        jBtnEditar.setText("Editar");
        jBtnEditar.setEnabled(false);
        jBtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditarActionPerformed(evt);
            }
        });

        jBtnEliminar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnEliminar.setForeground(java.awt.Color.white);
        jBtnEliminar.setText("Eliminar");
        jBtnEliminar.setEnabled(false);
        jBtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEliminarActionPerformed(evt);
            }
        });

        jBtnLimpiar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnLimpiar.setForeground(java.awt.Color.white);
        jBtnLimpiar.setText("Limpiar");
        jBtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFIdCategoria)
                            .addComponent(jTFNombreCategoria))
                        .addGap(97, 97, 97))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBtnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jBtnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnEditar)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnEliminar)))
                        .addGap(93, 93, 93))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFIdCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTFNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnGuardar)
                    .addComponent(jBtnEditar)
                    .addComponent(jBtnEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnLimpiar)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(51, 34, 89));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setForeground(java.awt.Color.white);
        jLabel6.setText("Buscar:");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 138, -1, -1));
        jPanel5.add(jTFBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 64, -1));

        jLabel7.setForeground(java.awt.Color.white);
        jLabel7.setText("Columna:");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 152, -1, -1));

        jCBColumna.setBackground(new java.awt.Color(119, 74, 217));
        jCBColumna.setForeground(java.awt.Color.white);
        jCBColumna.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "Id Categoia", "Nombre Categoria" }));
        jPanel5.add(jCBColumna, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 149, -1, -1));

        jLabel8.setForeground(java.awt.Color.white);
        jLabel8.setText("Dato:");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, -1, -1));

        jBtnBuscar.setBackground(new java.awt.Color(119, 74, 217));
        jBtnBuscar.setForeground(java.awt.Color.white);
        jBtnBuscar.setText("Buscar");
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });
        jPanel5.add(jBtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, -1, -1));

        jTblCategoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Categoria", "Nombre Categoria"
            }
        ));
        jTblCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblCategoriaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTblCategoria);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 130));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 650, 220));

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
            Logger.getLogger(jFraCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnLimpiarActionPerformed

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        try {
            guardar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        }
    }//GEN-LAST:event_jBtnGuardarActionPerformed

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
            buscarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(jFraCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnBuscarActionPerformed

    private void jTblCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblCategoriaMouseClicked
        filaSeleccionada();
    }//GEN-LAST:event_jTblCategoriaMouseClicked

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
            java.util.logging.Logger.getLogger(jFraCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jFraCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jFraCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jFraCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new jFraCategoria().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(jFraCategoria.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTFBuscar;
    private javax.swing.JTextField jTFIdCategoria;
    private javax.swing.JTextField jTFNombreCategoria;
    private javax.swing.JTable jTblCategoria;
    // End of variables declaration//GEN-END:variables
}
