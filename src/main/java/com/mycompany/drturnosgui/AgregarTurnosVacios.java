/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drturnosgui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class AgregarTurnosVacios extends JFrame {
   private JTextField empiezaPor;
   private JTextField finalizaEn;

   private final String fechaInicio;
   private final String fechaFin;

   public AgregarTurnosVacios(String fechaInicio, String fechaFin) {
       this.fechaInicio = fechaInicio;
       this.fechaFin = fechaFin;
       initUI();
   }

   private void initUI() {
        setTitle("Agregar Turnos Vacíos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 386);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton(buttonPanel, "Si", e -> escribirTurnos(fechaInicio, fechaFin));
        addButton(buttonPanel, "No", e -> dispose());
        add(buttonPanel, BorderLayout.SOUTH);
        
        empiezaPor = new JTextField();
        finalizaEn = new JTextField();
        
        empiezaPor.setText(fechaInicio);
        finalizaEn.setText(fechaFin);
        
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Dia de inicio:"));
        panel.add(empiezaPor);
        panel.add(new JLabel("Dia Final:"));
        panel.add(finalizaEn);

        setLocationRelativeTo(null);
        setVisible(true);
   }

   private void addButton(Container container, String text, ActionListener listener) {
      JButton button = new JButton(text);
      button.addActionListener(listener);
      container.add(button);
   }
   
   public void escribirTurnos(String fechaInicio, String fechaFin) {
       LocalDate inicio = LocalDate.parse(fechaInicio);
       LocalDate fin = LocalDate.parse(fechaFin);

       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

       try (BufferedWriter writer = new BufferedWriter(new FileWriter("turnos.txt"))) {
           for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
               for (int hora = 0; hora < 24; hora++) {
                 writer.write(fecha.format(formatter) + ", " + String.format("%02d", hora) + ", , , , ,\n");
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
