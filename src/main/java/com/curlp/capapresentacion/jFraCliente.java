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
 * @author GustavoAdolfo
 */
public class jFraCliente extends javax.swing.JFrame {

    public jFraCliente() throws SQLException {
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
    private void limpiarBusqueda(){
        this.jCBColumna.setSelectedIndex(0);
        this.jTFBuscar.setText("");
    }

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
            
    private void buscar() throws SQLException {
        if (!validarBuscar()) {
            JOptionPane.showMessageDialog(null, "Tiene que ingresar todos los datos de busqueda.", "Inventarios Master", 1);
        } else {
            boolean estado = false;
            for (int i = 0; i < this.jTblCliente.getRowCount(); i++) {
                if (this.jCBColumna.getSelectedIndex() == 1) {
                    if (this.jTblCliente.getValueAt(i, 0) == Integer.valueOf(this.jTFBuscar.getText())) {;
                        String dato = "Codigo Cliente: " + this.jTblCliente.getValueAt(i, 0)
                                + "\nNombre: " + this.jTblCliente.getValueAt(i, 1)
                                + "\nDocumento Identidad: " + this.jTblCliente.getValueAt(i, 2)
                                + "\nBeneficio: " + this.jTblCliente.getValueAt(i, 3)
                                + "\nTelefono: " + this.jTblCliente.getValueAt(i, 4)
                                + "\nCorreo: " + this.jTblCliente.getValueAt(i, 5)
                                + "\nPorcentaje Descuento: " + this.jTblCliente.getValueAt(i, 6)
                                + "\nEstado Cliente: " + this.jTblCliente.getValueAt(i, 7);
                        JOptionPane.showMessageDialog(null, dato, "Dato encontrado", 1);
                        limpiarBusqueda();
                        estado = true;
                    }
                } else if (this.jCBColumna.getSelectedIndex() == 2) {
                    String c = (String) this.jTblCliente.getValueAt(i, 2);
                    if (c.equals(this.jTFBuscar.getText())) {
                        String dato = "Codigo Cliente: " + this.jTblCliente.getValueAt(i, 0)
                                + "\nNombre: " + this.jTblCliente.getValueAt(i, 1)
                                + "\nDocumento Identidad: " + this.jTblCliente.getValueAt(i, 2)
                                + "\nBeneficio: " + this.jTblCliente.getValueAt(i, 3)
                                + "\nTelefono: " + this.jTblCliente.getValueAt(i, 4)
                                + "\nCorreo: " + this.jTblCliente.getValueAt(i, 5)
                                + "\nPorcentaje Descuento: " + this.jTblCliente.getValueAt(i, 6)
                                + "\nEstado Cliente: " + this.jTblCliente.getValueAt(i, 7);
                        JOptionPane.showMessageDialog(null, dato, "Dato encontrado", 1);
                        limpiarBusqueda();
                        estado = true;
                    }
                }
            }
            if(estado == false){
                JOptionPane.showMessageDialog(null, "No se ha encontrado registro", "Dato no encontrado", 1);
            }
        }
        
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


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
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
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblCliente = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jCBColumna = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jTFBuscar = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(174, 159, 228));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Gestión de Clientes");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel10.setText("X");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel10MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 338, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(24, Short.MAX_VALUE))
        );

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

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jTFPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jBtnGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addComponent(jBtnEliminar)
                                .addGap(47, 47, 47)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBEstado)
                            .addComponent(jBtnLimpiar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(79, 79, 79))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTFDocIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTFCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCBBeneficio)
                        .addGap(90, 90, 90))))
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
                    .addComponent(jBtnLimpiar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(51, 34, 89));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

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

        jLabel8.setForeground(java.awt.Color.white);
        jLabel8.setText("Buscar:");

        jLabel11.setForeground(java.awt.Color.white);
        jLabel11.setText("Columna:");

        jCBColumna.setBackground(new java.awt.Color(119, 74, 217));
        jCBColumna.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "Codigo Cliente", "Identidad" }));

        jLabel12.setForeground(java.awt.Color.white);
        jLabel12.setText("Dato:");

        jBtnBuscar.setText("Buscar");
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jCBColumna, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(jTFBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnBuscar)
                        .addGap(46, 46, 46))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBtnBuscar)
                            .addComponent(jTFBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jCBColumna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                        .addGap(53, 53, 53)))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MousePressed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel10MousePressed

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        try {
            guardar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al almacenar el registro:" + ex);
        }
    }//GEN-LAST:event_jBtnGuardarActionPerformed

    private void jTblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblClienteMouseClicked
        filaSeleccionada();
        habilitarBotones(false, true, true, true);
    }//GEN-LAST:event_jTblClienteMouseClicked

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

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
        try {
            buscar();
        } catch (SQLException ex) {
            Logger.getLogger(jFraMarca.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(jFraCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jFraCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jFraCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jFraCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new jFraCliente().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(jFraCliente.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JCheckBox jCBBeneficio;
    private javax.swing.JComboBox<String> jCBColumna;
    private javax.swing.JCheckBox jCBEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JTextField jTFCodCliente;
    private javax.swing.JTextField jTFCorreo;
    private javax.swing.JTextField jTFDocIdentidad;
    private javax.swing.JTextField jTFNombre;
    private javax.swing.JTextField jTFPorcentajeDescuento;
    private javax.swing.JTextField jTFTelefono;
    private javax.swing.JTable jTblCliente;
    // End of variables declaration//GEN-END:variables
}
