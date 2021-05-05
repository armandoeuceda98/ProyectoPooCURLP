/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capapresentacion;

import com.curlp.capadatos.CDDetFactura;
import com.curlp.capadatos.CDFactura;
import com.curlp.capalogica.CLDetFactura;
import com.curlp.capalogica.CLFactura;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DELL
 */
public class JFraControlFacturas extends javax.swing.JFrame {

    Object[] filas = new Object[4];
    int fila = 0;

    /**
     * Creates new form JFraControlFacturas
     */
    public JFraControlFacturas() throws SQLException {
        initComponents();
        poblarTabla();
    }

    //Método para limpiar la tabla
    private void limpiarTabla() {
        DefaultTableModel dtm = (DefaultTableModel) this.jTblFacturas.getModel();

        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
    }

    //Método para obtener la selección de usuario
    private int seleccion() {
        int resp = 0;
        if (this.jRBCodFactura.isSelected()) {
            jTFCodFactura.setEnabled(true);
            jTFNomCliente.setEnabled(false);
            jTFNomCliente.setText("");
            this.jTFNumFactura.setText("");
            this.jBtnBuscar.setEnabled(true);
            resp = 1;
        } else if (this.jRBNomCliente.isSelected()) {
            jTFNomCliente.setEnabled(true);
            jTFCodFactura.setEnabled(false);
            jTFCodFactura.setText("");
            this.jTFNumFactura.setText("");
            this.jBtnBuscar.setEnabled(true);
            resp = 2;
        } else {
            jTFNomCliente.setEnabled(false);
            this.jTblFacturas.setEnabled(false);
            this.jBtnBuscar.setEnabled(false);
        }
        return resp;

    }

    //Método para poblar de datos la tabla
    private void poblarTabla() throws SQLException {
        limpiarTabla();
        CDFactura cdf = new CDFactura();
        List<CLFactura> miLista = cdf.obtenerListaFacturas();
        DefaultTableModel temp = (DefaultTableModel) this.jTblFacturas.getModel();

        miLista.stream().map((CLFactura cl) -> {
            Object[] fila = new Object[4];
            fila[0] = cl.getCodFactura();
            fila[1] = cl.getFecha();
            fila[2] = cl.getNombreCliente();
            fila[3] = cl.getNombreEmpleado();
            return fila;
        }).forEachOrdered(temp::addRow);
    }

    //Limpiar todo
    private void limpiar() {
        jTFCodFactura.setText("");
        jTFNomCliente.setText("");
        this.jRBCodFactura.setSelected(false);
        this.jRBNomCliente.setSelected(false);
    }

    //Mandar datos al Form Factura
    private void enviarDatos() throws SQLException {
        if (!jTFNumFactura.getText().isEmpty()) {
            JFraFactura.jTFCodFactura.setEnabled(true);
            JFraFactura.jTFCodFactura.setText(jTFNumFactura.getText());
            JFraFactura.habilitarRecDato();
            JFraFactura.poblarForm();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione una factura en la tabla.");
        }
    }

    //Eliminar factura
    private void eliminarFact() throws SQLException {
        CLFactura cl = new CLFactura();
        CDFactura cd = new CDFactura();
        CDDetFactura cddf = new CDDetFactura();
        CLDetFactura cldf = new CLDetFactura();
        if (!jTFNumFactura.getText().isEmpty()) {
            int opcion;
            Object[] options = {"Si", "No"};
            opcion = JOptionPane.showOptionDialog(null, "¿Está seguro que desea eliminar la factura: \"" + jTFNumFactura.getText() + "\"? Esta acción es irreversible.",
                    "Inventario Master", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);
            if (opcion == 0) {
                cldf.setCodFactura(Integer.parseInt(jTFNumFactura.getText()));
                cddf.eliminarDetFacturaPorCodFactura(cldf);
                cl.setCodFactura(Integer.parseInt(jTFNumFactura.getText().trim()));
                cd.eliminarFactura(cl);
                poblarTabla();
                jTFNumFactura.setText("");
                JOptionPane.showMessageDialog(null, "Factura eliminada exitosamente.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna factura, por favor seleccione una factura en la tabla.");
        }
    }

    //Método para filtrar facturas
    private void buscarFacturas() throws SQLException {
        CLFactura cle = new CLFactura();
        cle.setCodFactura(Integer.parseInt(this.jTFCodFactura.getText().trim()));
        limpiarTabla();
        CDFactura cdf = new CDFactura();
        List<CLFactura> miLista = cdf.obtenerFacturasFiltradas(cle);
        DefaultTableModel temp = (DefaultTableModel) this.jTblFacturas.getModel();

        miLista.stream().map((CLFactura cl) -> {
            Object[] fila = new Object[4];
            fila[0] = cl.getCodFactura();
            fila[1] = cl.getFecha();
            fila[2] = cl.getNombreCliente();
            fila[3] = cl.getNombreEmpleado();
            return fila;
        }).forEachOrdered(temp::addRow);
    }

    //Método para rellenar textfield de codfatura
    private void seleccionTable() {
        this.jTFNumFactura.setText(String.valueOf(this.jTblFacturas.getValueAt(fila, 0)));
    }

    //Método para filtrar facturas
    private void buscarFacturasPorIdentidad(String docIdentidad) throws SQLException {
        limpiarTabla();
        CDFactura cdf = new CDFactura();
        List<CLFactura> miLista = cdf.obtenerFacturasFiltradasPorIdentidad(docIdentidad);
        DefaultTableModel temp = (DefaultTableModel) this.jTblFacturas.getModel();

        miLista.stream().map((CLFactura cl) -> {
            Object[] fila = new Object[4];
            fila[0] = cl.getCodFactura();
            fila[1] = cl.getFecha();
            fila[2] = cl.getNombreCliente();
            fila[3] = cl.getNombreEmpleado();
            return fila;
        }).forEachOrdered((fila) -> {
            temp.addRow(fila);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGFactura = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFNumFactura = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jRBCodFactura = new javax.swing.JRadioButton();
        jRBNomCliente = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFCodFactura = new javax.swing.JTextField();
        jTFNomCliente = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblFacturas = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jBtnAbrir = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setBackground(new java.awt.Color(119, 74, 217));

        jLabel1.setFont(new java.awt.Font("Poppins Medium", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Control Facturas");

        jLabel2.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Para buscar una factura especifique el código de factura o número de identidad del cliente.");

        jTFNumFactura.setEditable(false);

        jLabel6.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("N° Factura:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jTFNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTFNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(51, 34, 89));

        jLabel3.setFont(new java.awt.Font("Poppins SemiBold", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Filtrar por:");

        jRBCodFactura.setBackground(new java.awt.Color(85, 65, 118));
        btnGFactura.add(jRBCodFactura);
        jRBCodFactura.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jRBCodFactura.setForeground(new java.awt.Color(255, 255, 255));
        jRBCodFactura.setText("Código de factura");
        jRBCodFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBCodFacturaActionPerformed(evt);
            }
        });

        btnGFactura.add(jRBNomCliente);
        jRBNomCliente.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jRBNomCliente.setForeground(new java.awt.Color(255, 255, 255));
        jRBNomCliente.setText("Identidad de Cliente");
        jRBNomCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBNomClienteActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Código de Factura:");

        jLabel5.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Identidad de Cliente:");

        jTFCodFactura.setEnabled(false);

        jTFNomCliente.setEnabled(false);

        jBtnBuscar.setBackground(new java.awt.Color(85, 65, 118));
        jBtnBuscar.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jBtnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnBuscar.setText("Buscar");
        jBtnBuscar.setEnabled(false);
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jRBCodFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRBNomCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addComponent(jTFCodFactura)
                    .addComponent(jTFNomCliente)
                    .addComponent(jBtnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jRBCodFactura)
                .addGap(18, 18, 18)
                .addComponent(jRBNomCliente)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTFCodFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTFNomCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jBtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTblFacturas.setFont(new java.awt.Font("Poppins Medium", 0, 11)); // NOI18N
        jTblFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código de Factura", "Fecha", "Nombre de Cliente", "Código de Empleado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblFacturas.setSelectionBackground(new java.awt.Color(51, 34, 89));
        jTblFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblFacturasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTblFacturas);

        jPanel3.setBackground(new java.awt.Color(64, 43, 100));

        jBtnAbrir.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jBtnAbrir.setText("Abrir");
        jBtnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAbrirActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jBtnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBtnAbrir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRBCodFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBCodFacturaActionPerformed
        // TODO add your handling code here:
        seleccion();
    }//GEN-LAST:event_jRBCodFacturaActionPerformed

    private void jRBNomClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBNomClienteActionPerformed
        // TODO add your handling code here:
        seleccion();
    }//GEN-LAST:event_jRBNomClienteActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
        // TODO add your handling code here:
        if (seleccion() == 1) {
            try {
                buscarFacturas();
            } catch (SQLException ex) {
                Logger.getLogger(JFraControlFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (seleccion() == 2) {
            try {
                String docIdentidad;
                docIdentidad = jTFNomCliente.getText();
                buscarFacturasPorIdentidad(docIdentidad);
            } catch (SQLException ex) {
                Logger.getLogger(JFraControlFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jBtnBuscarActionPerformed

    private void jBtnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAbrirActionPerformed
        try {
            enviarDatos();
        } catch (SQLException ex) {
            Logger.getLogger(JFraControlFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnAbrirActionPerformed

    private void jTblFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblFacturasMouseClicked
        // TODO add your handling code here:
        fila = this.jTblFacturas.rowAtPoint(evt.getPoint());
        seleccionTable();
    }//GEN-LAST:event_jTblFacturasMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // TODO add your handling code here:
            eliminarFact();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(JFraControlFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFraControlFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFraControlFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFraControlFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JFraControlFacturas().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(JFraControlFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGFactura;
    private javax.swing.JButton jBtnAbrir;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRBCodFactura;
    private javax.swing.JRadioButton jRBNomCliente;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextField jTFCodFactura;
    public static javax.swing.JTextField jTFNomCliente;
    private javax.swing.JTextField jTFNumFactura;
    private javax.swing.JTable jTblFacturas;
    // End of variables declaration//GEN-END:variables
}
