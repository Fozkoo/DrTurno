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
        setSize(300, 200);

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
        panel.add(agregarButton);

        add(panel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void agregarCliente() {
        String nuevoDni = dniField.getText();
        String nuevoNombre = nombreField.getText();
        String nuevoTelefono = telefonoField.getText();
        String nuevaObraSocial = (String) obraSocialComboBox.getSelectedItem();

        Cliente nuevoCliente = new Cliente(nuevoDni, nuevoNombre, nuevoTelefono, nuevaObraSocial);
            if (!clientes.contains(nuevoCliente)) {
                System.out.println("Agregando...");
                clientes.add(nuevoCliente);
                dispose();
            }

    }
}