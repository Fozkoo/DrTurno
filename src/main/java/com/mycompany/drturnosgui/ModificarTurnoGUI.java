package com.mycompany.drturnosgui;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
// Ventana ModificarTurnoGUI
class ModificarTurnoGUI extends JFrame {
    // Atributos
    private DefaultTableModel model;
    private int selectedRow;
    private JTextField campoDia;
    private JTextField campoHora;
    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoTelefono;
    private JComboBox<String> campoObrasocial;
    private JTextField campoMotivo;
    
    private Set<ObraSocial> obrasSociales;
    private Set<Cliente> clientes;

    public ModificarTurnoGUI(DefaultTableModel model, int selectedRow, Set<Cliente> clientes, Set<ObraSocial> obrasSociales) {
        this.model = model;
        this.selectedRow = selectedRow;
        this.clientes = clientes;
        this.obrasSociales = obrasSociales;
        

        campoDia = new JTextField();
        campoHora = new JTextField();
        campoDni = new JTextField();
        campoNombre = new JTextField();
        campoTelefono = new JTextField();
        campoObrasocial = new JComboBox<String>();
        campoMotivo = new JTextField();
        
        for (ObraSocial obraSocial : obrasSociales) {
            campoObrasocial.addItem(obraSocial.getObraSocial());
        }

        // Obtener campos de la tabla
        campoDia.setText((String) model.getValueAt(selectedRow, 0));
        campoHora.setText((String) model.getValueAt(selectedRow, 1));
        campoDni.setText((String) model.getValueAt(selectedRow, 2));
        campoNombre.setText((String) model.getValueAt(selectedRow, 3));
        campoTelefono.setText((String) model.getValueAt(selectedRow, 4));
        System.out.println("Tamaño del HashSet: " + obrasSociales.size());
        campoObrasocial.setSelectedItem((String) model.getValueAt(selectedRow, 5));
        campoMotivo.setText((String) model.getValueAt(selectedRow, 6));
        // Bloquear Fecha y hora
        campoDia.setEditable(false);
        campoHora.setEditable(false);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonBuscar = new JButton("Buscar");

        
        setLayout(new GridLayout(10, 2));
        add(new JLabel("Día:"));
        add(campoDia);
        add(new JLabel("Hora:"));
        add(campoHora);
        add(new JLabel("DNI:"));
        add(campoDni);
        add(new JLabel("Nombre:"));
        add(campoNombre);
        add(new JLabel("Teléfono:"));
        add(campoTelefono);
        add(new JLabel("Obra Social:"));
        add(campoObrasocial);
        add(new JLabel("Motivo:"));
        add(campoMotivo);
        add(botonGuardar);
        add(botonBuscar);

        // Funcion del boton Buscar
        botonBuscar.addActionListener(e -> {
           String dniBuscado = campoDni.getText();
           System.out.println("Tamaño del HashSet: " + clientes.size());
           for (Cliente cliente : clientes) {
               System.out.println("DNI en el HashSet: " + cliente.getDni());
               if (cliente.getDni().equals(dniBuscado)) {
                   System.out.println("Encontrado");
                   campoNombre.setText(cliente.getNombre());
                   campoTelefono.setText(cliente.getTelefono());
                   campoObrasocial.setSelectedItem(cliente.getObraSocial());
                   break;
               }
           }
        });
        
        // Funcion del boton 
        botonGuardar.addActionListener(e -> {
           String nuevoDia = campoDia.getText();
           String nuevaHora = campoHora.getText();
           String nuevoDni = campoDni.getText();
           String nuevoNombre = campoNombre.getText();
           String nuevoTelefono = campoTelefono.getText();
           String nuevaObrasocial = (String) campoObrasocial.getSelectedItem();
           String nuevoMotivo = campoMotivo.getText();

           model.setValueAt(nuevoDia, selectedRow, 0);
           model.setValueAt(nuevaHora, selectedRow, 1);
           model.setValueAt(nuevoDni, selectedRow, 2);
           model.setValueAt(nuevoNombre, selectedRow, 3);
           model.setValueAt(nuevoTelefono, selectedRow, 4);
           model.setValueAt(nuevaObrasocial, selectedRow, 5);
           model.setValueAt(nuevoMotivo, selectedRow, 6);        
           
           // Guardar Cliente
           Cliente nuevoCliente = new Cliente(nuevoDni, nuevoNombre, nuevoTelefono, nuevaObrasocial);
            if (clientes.contains(nuevoCliente)) {
               clientes.add(nuevoCliente);
            }
            
            // Guardar Turno
            try {
                FileWriter fileWriter = new FileWriter("turnos.txt");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                for (int i = 0; i < model.getRowCount(); i++) {
                   for (int j = 0; j < model.getColumnCount(); j++) {
                       Object value = model.getValueAt(i, j);
                       if (value != null) {
                           bufferedWriter.write(value.toString());
                       }
                       if (j != model.getColumnCount() - 1) {
                           bufferedWriter.write(", ");
                       }
                   }
                   bufferedWriter.newLine();
                }
                bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            dispose();
          });
        // Propiedades de la ventana
        setTitle("Agregar/Modificar Turno");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
        
        campoDni.requestFocusInWindow();
        
    }
       
}

