/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drturnosgui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ObrasSocialesGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Set<ObraSocial> obrasSociales;
    
    public ObrasSocialesGUI(Set<ObraSocial> obrasSociales){
        this.obrasSociales = obrasSociales;
        initUI();
    }
    
    private void initUI() {
        setTitle("Obras Sociales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 386);
        model = createTableModel();
        createUIComponents();
        loadTableData();

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private DefaultTableModel createTableModel() {
        String[] columnNames = {"Obra Social"};
        return new DefaultTableModel(columnNames, 0);
    }
    
    private void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
        
       for (ObraSocial obraSocial : obrasSociales) {
           model.addRow(new Object[]{obraSocial.getObraSocial()});
       }
    }
    
    private void createUIComponents() {
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton(buttonPanel, "Agregar", e -> openAgregarObraSocialGUI());
        addButton(buttonPanel, "Modificar", e -> openModificarObraSocialGUI());
        addButton(buttonPanel, "Eliminar", e -> eliminarObraSocialSeleccionada());
        addButton(buttonPanel, "Cerrar", e -> Cerrar());
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addButton(Container container, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        container.add(button);
    }
    
  private void openAgregarObraSocialGUI() {
   
    ObraSocial obraSocialPorDefecto = new ObraSocial("Particular"); 
    String input = JOptionPane.showInputDialog(null, "Ingrese la obra social a agregar:");
    obrasSociales.add(obraSocialPorDefecto);
    ObraSocial nuevaObraSocial = new ObraSocial(input);
    if (input != null && !input.isEmpty() && !obrasSociales.contains(nuevaObraSocial)) {
        obrasSociales.add(nuevaObraSocial);
        loadTableData();
    }
}

    private void openModificarObraSocialGUI() {
        int selectedRow = table.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String selectedItem = (String) model.getValueAt(selectedRow, 0);
        if (selectedItem != null) {
            String input = JOptionPane.showInputDialog(null, "Ingrese la nueva obra social:", selectedItem);
            ObraSocial nuevaObraSocial = new ObraSocial(input);
            if (input != null && !input.isEmpty() && !obrasSociales.contains(nuevaObraSocial))  {
                String obraSocial = model.getValueAt(selectedRow, 0).toString();
                eliminarObraSocial(obraSocial);
                obrasSociales.add(nuevaObraSocial);
            }
        }
        loadTableData();
    }

    private void eliminarObraSocialSeleccionada() {
       int selectedRow = table.getSelectedRow();

       if (selectedRow != -1) {
           DefaultTableModel model = (DefaultTableModel) table.getModel();
           String obraSocial = model.getValueAt(selectedRow, 0).toString();

           model.setValueAt("", selectedRow, 0);

           eliminarObraSocial(obraSocial);

       } else {
           showError("Selecciona una obra social para eliminar.");
       }
    }
    
    private void eliminarObraSocial(String obraSocial) {
       Iterator<ObraSocial> iterator = obrasSociales.iterator();
       while (iterator.hasNext()) {
           ObraSocial obra = iterator.next();
           if (obra.getObraSocial().equals(obraSocial)) {
               iterator.remove();
               break;
           }
       }
       loadTableData();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void Cerrar(){
        GuardarHashSet();
        dispose();
    }
    
    private void guardarHashSet(Set<? extends Serializable> set, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(set);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void GuardarHashSet(){
        guardarHashSet(obrasSociales, "obrasSociales.ser");
    }
}