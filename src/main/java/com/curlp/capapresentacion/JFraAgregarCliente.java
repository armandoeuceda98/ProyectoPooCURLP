/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curlp.capapresentacion;

import com.curlp.capadatos.CDCliente;
import com.curlp.capalogica.CLCliente;
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
public class JFraAgregarCliente extends javax.swing.JFrame {

    /**
     * Creates new form JFraAgregarCliente
     */
    public JFraAgregarCliente() throws SQLException {
        initComponents();
        poblarTabla();
        encontrarCorrelativo();
        this.jTFNombre.requestFocus();
        this.setLocationRelativeTo(null);
    }
    
        //metodo para limpiar los datos de la tabla
    private void limpiarTabla() {
        DefaultTableModel dtm = (DefaultTableModel) this.jTblCliente.getModel();

        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
    }

    //Metodo para poblar de datos la tabla
    private void poblarTabla() throws SQLException {
        limpiarTabla();
        CDCliente cdc = new CDCliente();
        List<CLCliente> miLista = cdc.obtenerCliente();
        DefaultTableModel temp = (DefaultTableModel) this.jTblCliente.getModel();
        miLista.stream().map((CLCliente cl) -> {
            Object[] fila = new Object[8];
            fila[0] = cl.getCodCliente();
            fila[1] = cl.getNombre();
            fila[2] = cl.getDocIdentidad();
            fila[3] = cl.isBeneficio();
            fila[4] = cl.getTelefono();
            fila[5] = cl.getCorreo();
            fila[6] = cl.getPorcentajeDescuento();
            fila[7] = cl.isEstadoCliente();
            return fila;
        }).forEachOrdered(temp::addRow);
    }

    //metodo para encontrar el correlativo del codCliente
    private void encontrarCorrelativo() throws SQLException {
        CDCliente cdc = new CDCliente();
        CLCliente cl = new CLCliente();
        cl.setCodCliente(cdc.autoIncrementarCodCliente());
        this.jTFCodCliente.setText(String.valueOf(cl.getCodCliente()));
    }

    //Metodo para habilitar y desabilitar botones
    private void habilitarBotones(boolean guardar, boolean editar, boolean eliminar, boolean limpiar) {
        this.jBtnGuardar.setEnabled(guardar);
        this.jBtnEditar.setEnabled(editar);
        this.jBtnEliminar.setEnabled(eliminar);
        this.jBtnLimpiar.setEnabled(limpiar);
    }

    //metodo para limpiar las textfield
    private void limpiarTF() {
        this.jTFCodCliente.setText("");
        this.jTFNombre.setText("");
        this.jTFDocIdentidad.setText("");
        this.jCBBeneficio.setSelected(false);
        this.jTFTelefono.setText("");
        this.jTFCorreo.setText("");
        this.jTFPorcentajeDescuento.setText("");
        this.jCBEstado.setSelected(false);
        
        this.jTFNombre.requestFocus();
    }

//    private void limpiarBusqueda() {
//        this.jCBColumna.setSelectedIndex(0);
//        this.jTFBuscar.setText("");
//    }

    //Metodo para validar la textfield
    private boolean validarTF() {
        boolean estado;
        estado = !this.jTFNombre.getText().equals("");
        estado = !this.jTFDocIdentidad.equals("");
        estado = !this.jCBBeneficio.equals(false);
        estado = !this.jTFTelefono.equals("");
        estado = !this.jTFCorreo.equals("");
        estado = !this.jTFPorcentajeDescuento.equals("");
        estado = !this.jCBEstado.equals(false);
        return estado;
    }

    //metodo para insertar
    private void insertarCliente() {
        if (!validarTF()) {
            JOptionPane.showMessageDialog(null, "Tiene que ingresar los datos del cliente", "Control",
                    JOptionPane.INFORMATION_MESSAGE);
            this.jTFNombre.requestFocus();
        } else {
            try {
                CDCliente cdc = new CDCliente();
                CLCliente cl = new CLCliente();
                cl.setNombre(this.jTFNombre.getText().trim());
                cl.setDocIdentidad(this.jTFDocIdentidad.getText().trim());
                cl.setBeneficio(this.jCBBeneficio.isSelected());
                cl.setTelefono(this.jTFTelefono.getText().trim());
                cl.setCorreo(this.jTFCorreo.getText().trim());
                cl.setPorcentajeDescuento(Float.parseFloat(this.jTFPorcentajeDescuento.getText().trim()));
                cl.setEstadoCliente(this.jCBEstado.isSelected());
                cdc.insertarCliente(cl);
                JOptionPane.showMessageDialog(null, "Registro almacenado", "Control",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al almacenar el registro:" + e);
                this.jTFNombre.requestFocus();
            }
        }
    }

    //metodo para llamar el metodo de insertar 
    private void guardar() throws SQLException {
        insertarCliente();
        poblarTabla();
        habilitarBotones(true, false, false, true);
        limpiarTF();
        encontrarCorrelativo();
    }

    //metodo para actualiar
    private void actualizarCliente() {
        if (!validarTF()) {
            JOptionPane.showMessageDialog(null, "Tiene que ingresar los datos del cliente", "Control",
                    JOptionPane.INFORMATION_MESSAGE);
            this.jTFNombre.requestFocus();
        } else {
            try {
                CDCliente cdc = new CDCliente();
                CLCliente cl = new CLCliente();
                cl.setCodCliente(Integer.parseInt(this.jTFCodCliente.getText().trim()));
                cl.setNombre(this.jTFNombre.getText().trim());
                cl.setDocIdentidad(this.jTFDocIdentidad.getText().trim());
                cl.setBeneficio(this.jCBBeneficio.isSelected());
                cl.setTelefono(this.jTFTelefono.getText().trim());
                cl.setCorreo(this.jTFCorreo.getText().trim());
                cl.setPorcentajeDescuento(Float.parseFloat(this.jTFPorcentajeDescuento.getText().trim()));
                cl.setEstadoCliente(this.jCBEstado.isSelected());
                cdc.actualizarCliente(cl);
                JOptionPane.showMessageDialog(null, "Registro almacenado", "Control",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar el registro:" + e);
                this.jTFNombre.requestFocus();
            }
        }
    }

    //metodo para selleccionar los datos de las filas para modificarlos
    private void filaSeleccionada() {
        if (this.jTblCliente.getSelectedRow() != -1) {
            this.jTFCodCliente.setText(String.valueOf(this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 0)));
            this.jTFNombre.setText(String.valueOf(this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 1)));
            this.jTFDocIdentidad.setText(String.valueOf(this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 2)));
            this.jCBBeneficio.setSelected((boolean) this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 3));
            this.jTFTelefono.setText(String.valueOf(this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 4)));
            this.jTFCorreo.setText(String.valueOf(this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 5)));
            this.jTFPorcentajeDescuento.setText(String.valueOf(this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 6)));
            this.jCBEstado.setSelected((boolean) (this.jTblCliente.getValueAt(this.jTblCliente.getSelectedRow(), 7)));
            this.jBtnAbrir.setEnabled(true);
        }
    }

    // metodo para llamar el metodo de actualizar
    private void editar() throws SQLException {
        actualizarCliente();
        poblarTabla();
        habilitarBotones(true, false, false, true);
        limpiarTF();
        encontrarCorrelativo();
    }

    //metodo para eliminar
    private void eliminarCliente() {
        try {
            CDCliente cdc = new CDCliente();
            CLCliente cl = new CLCliente();
            cl.setCodCliente(Integer.parseInt(this.jTFCodCliente.getText().trim()));
            cdc.eliminarCliente(cl);
            JOptionPane.showMessageDialog(null, "Registro eliminado", "Control",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro:" + e);
            this.jTFNombre.requestFocus();
        }
    }

    private void eliminar() throws SQLException {
        int resp = JOptionPane.showConfirmDialog(null, "Seguro de que desea eliminar?", "Control",
                JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                eliminarCliente();
                poblarTabla();
                habilitarBotones(true, false, false, true);
                limpiarTF();
                encontrarCorrelativo();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el registro:" + ex);
            }
        } else {
            limpiarTF();
        }
    }

//    private void buscarClienteXIdentidad() throws SQLException {
//        CLCliente clc = new CLCliente();
//        CDCliente cdc = new CDCliente();
//        clc.setDocIdentidad(jTFBuscar.getText().trim());
//        cdc.obtenerListaClienteXId(clc);
//        if (clc.getNombre() == null) {
//            JOptionPane.showMessageDialog(null, "No se encontró ningún cliente.");
//        } else {
//            jTFCodCliente.setText(String.valueOf(clc.getCodCliente()));
//            jTFNombre.setText(clc.getNombre());
//            jTFDocIdentidad.setText(clc.getDocIdentidad());
//            jCBBeneficio.setSelected((boolean)(clc.isBeneficio()));            
//            jTFTelefono.setText(clc.getNombre());
//            jTFCorreo.setText(clc.getNombre());
//            jTFPorcentajeDescuento.setText(clc.getNombre());
//            jCBEstado.setSelected((boolean)(clc.isEstadoCliente()));
//            habilitarBotones(false, true, true, true);
//        }
//        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTblCliente = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTFCodCliente = new javax.swing.JTextField();
        jTFNombre = new javax.swing.JTextField();
        jTFDocIdentidad = new javax.swing.JTextField();
        jTFTelefono = new javax.swing.JTextField();
        jTFCorreo = new javax.swing.JTextField();
        jTFPorcentajeDescuento = new javax.swing.JTextField();
        jBtnGuardar = new javax.swing.JButton();
        jBtnEditar = new javax.swing.JButton();
        jBtnEliminar = new javax.swing.JButton();
        jBtnLimpiar = new javax.swing.JButton();
        jCBBeneficio = new javax.swing.JCheckBox();
        jCBEstado = new javax.swing.JCheckBox();
        jBtnAbrirGuardar = new javax.swing.JButton();
        jBtnAbrir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTFCodClienteEnviar = new javax.swing.JTextField();
        jLblCerrar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jTblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Cliente", "Nombre", "Documento Identidad", "Beneficio", "Telefono", "Correo", "Porcentaje Descuento", "Estado Cliente"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTblCliente);

        jPanel4.setBackground(new java.awt.Color(51, 34, 89));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Codigo Cliente");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Documento Identidad");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Telefono");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Correo");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Porcentaje Descuento");

        jTFCodCliente.setEditable(false);

        jBtnGuardar.setText("Guardar");
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });

        jBtnEditar.setText("Editar");
        jBtnEditar.setEnabled(false);
        jBtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditarActionPerformed(evt);
            }
        });

        jBtnEliminar.setText("Eliminar");
        jBtnEliminar.setEnabled(false);
        jBtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEliminarActionPerformed(evt);
            }
        });

        jBtnLimpiar.setText("Limpiar");
        jBtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLimpiarActionPerformed(evt);
            }
        });

        jCBBeneficio.setBackground(new java.awt.Color(51, 34, 89));
        jCBBeneficio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCBBeneficio.setForeground(new java.awt.Color(255, 255, 255));
        jCBBeneficio.setText("Beneficio");

        jCBEstado.setBackground(new java.awt.Color(51, 34, 89));
        jCBEstado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCBEstado.setForeground(new java.awt.Color(255, 255, 255));
        jCBEstado.setText("Estado Cliente");

        jBtnAbrirGuardar.setText("Guardar y abrir");
        jBtnAbrirGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAbrirGuardarActionPerformed(evt);
            }
        });

        jBtnAbrir.setText("Abrir");
        jBtnAbrir.setEnabled(false);
        jBtnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAbrirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTFDocIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTFCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCBBeneficio)
                        .addGap(90, 90, 90))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jTFPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCBEstado)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jTFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jBtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jBtnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnAbrir, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnAbrirGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTFDocIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBBeneficio))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTFPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBEstado))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnGuardar)
                    .addComponent(jBtnEditar)
                    .addComponent(jBtnEliminar)
                    .addComponent(jBtnLimpiar)
                    .addComponent(jBtnAbrirGuardar)
                    .addComponent(jBtnAbrir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(119, 74, 217));

        jLabel4.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Agregar Cliente");

        jLabel8.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Código de Cliente:");

        jTFCodClienteEnviar.setEditable(false);

        jLblCerrar.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLblCerrar.setForeground(new java.awt.Color(255, 255, 255));
        jLblCerrar.setText("X");
        jLblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLblCerrarMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTFCodClienteEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jLblCerrar)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTFCodClienteEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLblCerrar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblClienteMouseClicked
        filaSeleccionada();
        habilitarBotones(false, true, true, true);
    }//GEN-LAST:event_jTblClienteMouseClicked

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        try {
            guardar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al almacenar el registro:" + ex);
        }
    }//GEN-LAST:event_jBtnGuardarActionPerformed

    private void jBtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditarActionPerformed
        try {
            editar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al editar el registro:" + ex);
        }
    }//GEN-LAST:event_jBtnEditarActionPerformed

    private void jBtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEliminarActionPerformed
        try {
            eliminar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro:" + ex);
        }
    }//GEN-LAST:event_jBtnEliminarActionPerformed

    private void jBtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLimpiarActionPerformed
        limpiarTF();
    }//GEN-LAST:event_jBtnLimpiarActionPerformed

    private void jBtnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAbrirActionPerformed
        // TODO add your handling code here:
        JFraFactura.jTFCodCliente.setText(jTFDocIdentidad.getText());
        try {
            JFraFactura.buscarCliente();
        } catch (SQLException ex) {
            Logger.getLogger(JFraAgregarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_jBtnAbrirActionPerformed

    private void jBtnAbrirGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAbrirGuardarActionPerformed
        // TODO add your handling code here:
        try {
            JFraFactura.jTFCodCliente.setText(jTFDocIdentidad.getText());
            guardar();
            JFraFactura.buscarCliente();
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al almacenar el registro:" + ex);
        }
    }//GEN-LAST:event_jBtnAbrirGuardarActionPerformed

    private void jLblCerrarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLblCerrarMousePressed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLblCerrarMousePressed

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
            java.util.logging.Logger.getLogger(JFraAgregarCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFraAgregarCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFraAgregarCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFraAgregarCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JFraAgregarCliente().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(JFraAgregarCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAbrir;
    private javax.swing.JButton jBtnAbrirGuardar;
    private javax.swing.JButton jBtnEditar;
    private javax.swing.JButton jBtnEliminar;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JButton jBtnLimpiar;
    private static javax.swing.JCheckBox jCBBeneficio;
    private static javax.swing.JCheckBox jCBEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLblCerrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextField jTFCodCliente;
    private javax.swing.JTextField jTFCodClienteEnviar;
    private static javax.swing.JTextField jTFCorreo;
    private static javax.swing.JTextField jTFDocIdentidad;
    private static javax.swing.JTextField jTFNombre;
    private static javax.swing.JTextField jTFPorcentajeDescuento;
    private static javax.swing.JTextField jTFTelefono;
    private static javax.swing.JTable jTblCliente;
    // End of variables declaration//GEN-END:variables
}
