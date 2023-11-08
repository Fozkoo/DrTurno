package com.mycompany.drturnosgui;
//esto es el final
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Ventana Principal
public class DrTurnosGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Set<String> turnosSet = new HashSet<>();
    public static Set<Cliente> clientes = new HashSet<>();
    public static Set<ObraSocial> obrasSociales = new HashSet<>();

    public DrTurnosGUI() {
        CargarHashSets();
        initUI();
    }
    // Inicializar la GUI
    private void initUI() {
        setTitle("DrTurnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 386);
        model = createTableModel();
        createUIComponents();
        loadTableData();

        setLocationRelativeTo(null);
        setVisible(true);
    }
    // Modelo de la tabla
    private DefaultTableModel createTableModel() {
        String[] columnNames = {"Día", "Hora", "DNI", "Nombre", "Teléfono", "Obra Social", "Motivo"};
        return new DefaultTableModel(columnNames, 0);
    }
    
    //Botones y Tabla
    private void createUIComponents() {
    table = new JTable(model);
    add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
    addButton(buttonPanel, "Clientes", e -> openClientesGUI());
    addButton(buttonPanel, "Obras Sociales", e -> openObrasSocialesGUI());
    addButton(buttonPanel, "Agregar/Modificar", e -> openModificarTurnoGUI());
    addButton(buttonPanel, "Eliminar", e -> limpiarCamposSeleccionados());
    addButton(buttonPanel, "Cerrar", e -> Cerrar());

    add(buttonPanel, BorderLayout.SOUTH);
}
    // Metodo para agregar botones y agregarlos al contenedor
    private void addButton(Container container, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        container.add(button);
    }
    
    private void openClientesGUI(){
        ClientesGUI clientesGUI = new ClientesGUI(clientes, obrasSociales);
        clientesGUI.setVisible(true);
    }
    
    private void openObrasSocialesGUI(){
        ObrasSocialesGUI obrasSocialesGUI = new ObrasSocialesGUI(obrasSociales);
        obrasSocialesGUI.setVisible(true);
    }
    
    // Llama a la clase ModificarTurnoGUI()
    private void openModificarTurnoGUI() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            ModificarTurnoGUI modificarTurnoGUI = new ModificarTurnoGUI(model, selectedRow, clientes, obrasSociales); // Aqui Este el error
            modificarTurnoGUI.setVisible(true);
        } else {
            showError("Selecciona un turno para modificar.");
        }
    }

    private void openAgregarDias() {      
        try {
            int lastRow = model.getRowCount() - 1;
            
            String fechaInicioStr = model.getValueAt(lastRow, 0).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaInicioDate = sdf.parse(fechaInicioStr);
            
            Calendar c = Calendar.getInstance();
            c.setTime(fechaInicioDate);
            c.add(Calendar.DATE, 30);
            Date fechaFin = c.getTime();
            
            String fechaInicio = sdf.format(fechaInicioDate);
            String fechaFinStr = sdf.format(fechaFin);
            
            AgregarTurnosVacios agregarTurnosVacios = new AgregarTurnosVacios(fechaInicio, fechaFinStr);
            agregarTurnosVacios.setVisible(true);
        } catch (ParseException ex) {
            
        }
    }

    // Borra los campos de la tabla
    private void limpiarCamposSeleccionados() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String fecha = model.getValueAt(selectedRow, 0).toString();
            String hora = model.getValueAt(selectedRow, 1).toString();
            String dni = model.getValueAt(selectedRow, 2).toString();
            
            model.setValueAt("", selectedRow, 2); // DNI
            model.setValueAt("", selectedRow, 3); // Nombre
            model.setValueAt("", selectedRow, 4); // Teléfono
            model.setValueAt("", selectedRow, 5); // Obra Social
            model.setValueAt("", selectedRow, 6); // Motivo
            
            eliminarTurnoEnArchivo(fecha, hora, dni);
            
        } else {
            showError("Selecciona un turno para limpiar los campos.");
        }
    }
    // Borra los campos de la tabla en el archivo 
    private void eliminarTurnoEnArchivo(String fecha, String hora, String dni) {
       try {
           BufferedReader br = new BufferedReader(new FileReader("turnos.txt"));
           String line;
           StringBuilder fileContent = new StringBuilder();

            while ((line = br.readLine()) != null) {
               String[] fields = line.split(", ");
               if (fields.length < 3 || !fecha.equals(fields[0]) || !hora.equals(fields[1]) || !dni.equals(fields[2])) {
                   fileContent.append(line).append("\n");
               } else {
                   // Crear una nueva línea con los primeros dos campos y el resto en blanco
                   String newLine = fields[0] + ", " + fields[1] + ", , , , , ";
                   fileContent.append(newLine).append("\n");
               }
            }
           br.close();

           Files.write(Paths.get("turnos.txt"), fileContent.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
           loadTableData();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    
    // Carga los datos del archivo a la tabla
    private void loadTableData() {
       try {
           BufferedReader br = new BufferedReader(new FileReader("turnos.txt"));
           String line;
           while ((line = br.readLine()) != null) {
               String[] fields = line.split(", ");
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
               LocalDate fecha = LocalDate.parse(fields[0], formatter);
               LocalDate now = LocalDate.now();

               if (fields.length == 1) {
                   if (!fecha.isBefore(now)) {
                      model.addRow(fields);
                   }
               }

               if (fields.length >= 2) {
                   if (!fecha.isBefore(now)) {
                      model.addRow(fields);
                      String hora = fields[1];
                      String turnoKey = fecha + ", " + hora;
                      turnosSet.add(turnoKey);
                   }
               }
           }
           br.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrTurnosGUI gui = new DrTurnosGUI();
        });
    }
    // Cerrar aplicacion
    private void Cerrar() {
        GuardarHashSets();
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

    private <T extends Serializable> Set<T> cargarHashSet(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Set<T> setDeserializado = (Set<T>) objectInputStream.readObject();
            objectInputStream.close();
            return setDeserializado;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void CargarHashSets(){
        clientes = cargarHashSet("clientes.ser");
        if (clientes == null) {
            clientes = new HashSet<>();
        }
        obrasSociales = cargarHashSet("obrasSociales.ser");
        if (obrasSociales == null) {
            obrasSociales = new HashSet<>();
        }
    }
    
    public void GuardarHashSets(){
        guardarHashSet(obrasSociales, "obrasSociales.ser");
        guardarHashSet(clientes, "clientes.ser");
    }
}