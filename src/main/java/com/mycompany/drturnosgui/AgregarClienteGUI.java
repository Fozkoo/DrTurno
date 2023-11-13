/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drturnosgui;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class AgregarClienteGUI extends JFrame {
    private JTextField dniField;
    private JTextField nombreField;
    private JTextField telefonoField;
    private JComboBox<String> obraSocialComboBox;
    private Set<ObraSocial> obrasSociales;
    private Set<Cliente> clientes;

    public AgregarClienteGUI(Set<Cliente> clientes, Set<ObraSocial> obrasSociales) {
       this.obrasSociales = obrasSociales;
       this.clientes = clientes;
       initUI();
    }
    
    private void initUI() {
        setTitle("Agregar Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        dniField = new JTextField();
        nombreField = new JTextField();
        telefonoField = new JTextField();
        obraSocialComboBox = new JComboBox<>();
       
        for (ObraSocial obraSocial : obrasSociales) {
             obraSocialComboBox.addItem(obraSocial.getObraSocial());
        }

        JButton agregarButton = new JButton("Agregar");
        agregarButton.addActionListener(e -> agregarCliente());

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("DNI:"));
        panel.add(dniField);
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Teléfono:"));
        panel.add(telefonoField);
        panel.add(new JLabel("Obra Social:"));
        panel.add(obraSocialComboBox);   
        panel.add(new JPanel()); // Espacio en blanco para separar
        panel.add(agregarButton);

        add(panel);

        agregarButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Establece un tamaño preferido más pequeño para el botón
        agregarButton.setPreferredSize(new Dimension(100, 30));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void agregarCliente() {
    String nuevoDni = dniField.getText();
    String nuevoNombre = nombreField.getText();
    String nuevoTelefono = telefonoField.getText();
    String nuevaObraSocial = (String) obraSocialComboBox.getSelectedItem();

    Cliente nuevoCliente = new Cliente(nuevoDni, nuevoNombre, nuevoTelefono, nuevaObraSocial);
    if (clientes.contains(nuevoDni)) {
        System.out.println("Agregando...");
        clientes.add(nuevoCliente);
        dispose();
    } else {
        showError("Ya existe un cliente registrado con el mismo DNI.");
    }
 }
  
   private boolean clienteExists(Cliente cliente) {
    for (Cliente existingCliente : clientes) {
        if (existingCliente.getDni().equals(cliente.getDni())) {
            return true;
        }
    }
    return false;
}
   
 private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
}  
  
}