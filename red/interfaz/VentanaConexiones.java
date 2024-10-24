package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import red.modelo.Conexion;
import red.modelo.TipoCable;
import red.servicio.ConexionService;
import red.servicio.ConexionServiceImp;
import red.servicio.EquipoService;
import red.servicio.EquipoServiceImp;
import red.servicio.TipoCableService;
import red.servicio.TipoCableServiceImp;
import red.servicio.ConexionService;
import red.servicio.ConexionServiceImp;
import red.interfaz.ButtonEditor;
import red.modelo.Equipo;

public class VentanaConexiones extends JFrame implements ActionListener {

    private ConexionService conexionService;
    private JTable conexionesTable;
    private DefaultTableModel conexionTableModel;

    public VentanaConexiones() {
        setTitle("Gestión de Conexiones");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            conexionService = new ConexionServiceImp();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de las conexiones.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String[] conexionColumnNames = { "Equipo 1", "Equipo 2", "Tipo de Cable", "Acciones" };
        conexionTableModel = new DefaultTableModel(conexionColumnNames, 0);
        conexionesTable = new JTable(conexionTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna de "Acciones" es editable
            }
        };

        JScrollPane scrollConexiones = new JScrollPane(conexionesTable);
        add(scrollConexiones, BorderLayout.CENTER);

        JButton agregarConexionButton = new JButton("Agregar Conexión");
        agregarConexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarConexion();
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.add(agregarConexionButton);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarConexionesEnTabla(); // Mostrar conexiones al iniciar
    }

    private void mostrarConexionesEnTabla() {
        try {
            List<Conexion> conexiones = conexionService.buscarTodos();
            conexionTableModel.setRowCount(0); // Limpiar la tabla
            for (Conexion conexion : conexiones) {
                conexionTableModel.addRow(new Object[] { conexion.getEquipo1().getCodigo(),
                        conexion.getEquipo2().getCodigo(), conexion.getTipoCable().getDescripcion(), "Eliminar" });
            }

            conexionesTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
            conexionesTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), conexionesTable));

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las conexiones.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarConexion(Conexion conexion) {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar esta conexión?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                conexionService.borrar(conexion);  // Lógica para borrar la conexión
                JOptionPane.showMessageDialog(this, "Conexión eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrarConexionesEnTabla();  // Refrescar la tabla después de eliminar
                
            } catch (Exception e) {
                e.printStackTrace();  // Mostrar el error en consola
                JOptionPane.showMessageDialog(this, "Error al eliminar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        mostrarConexionesEnTabla();  // Refrescar la tabla en la interfaz
    }

    private List<Equipo> obtenerEquiposDisponibles() {
    try {
        // Usar tu servicio de equipos
        EquipoService equipoService = new EquipoServiceImp();  
        return equipoService.buscarTodos();
    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los equipos.", "Error", JOptionPane.ERROR_MESSAGE);
        return new ArrayList<>();
    }
}

private List<TipoCable> obtenerCablesDisponibles() {
    try {
        // Usar tu servicio de tipos de cables
        TipoCableService tipoCableService = new TipoCableServiceImp();  
        return tipoCableService.buscarTodos();
    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los tipos de cables.", "Error", JOptionPane.ERROR_MESSAGE);
        return new ArrayList<>();
    }
}


    

   private void agregarConexion() {
    JPanel panel = new JPanel(new GridLayout(0, 2));

    // Obtener listas de equipos y tipos de cables
    List<Equipo> equiposDisponibles = obtenerEquiposDisponibles();
    List<TipoCable> cablesDisponibles = obtenerCablesDisponibles();

    // Convertir las listas a arrays para usarlas en JComboBox
    String[] equipoArray = equiposDisponibles.stream().map(Equipo::getCodigo).toArray(String[]::new);
    String[] tipoCableArray = cablesDisponibles.stream().map(TipoCable::getCodigo).toArray(String[]::new);

    // Crear JComboBox para equipos y tipos de cables
    JComboBox<String> equipo1ComboBox = new JComboBox<>(equipoArray);
    JComboBox<String> equipo2ComboBox = new JComboBox<>(equipoArray);
    JComboBox<String> tipoCableComboBox = new JComboBox<>(tipoCableArray);

    // Añadir componentes al panel
    panel.add(new JLabel("Equipo 1:"));
    panel.add(equipo1ComboBox);
    panel.add(new JLabel("Equipo 2:"));
    panel.add(equipo2ComboBox);
    panel.add(new JLabel("Tipo de Cable:"));
    panel.add(tipoCableComboBox);

    int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Conexión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        try {
            // Obtener valores seleccionados
            String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
            String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();
            String tipoCableCodigo = (String) tipoCableComboBox.getSelectedItem();

            // Buscar los objetos correspondientes según las selecciones
            Equipo equipo1 = equiposDisponibles.stream().filter(e -> e.getCodigo().equals(equipo1Codigo)).findFirst().orElse(null);
            Equipo equipo2 = equiposDisponibles.stream().filter(e -> e.getCodigo().equals(equipo2Codigo)).findFirst().orElse(null);
            TipoCable tipoCable = cablesDisponibles.stream().filter(c -> c.getCodigo().equals(tipoCableCodigo)).findFirst().orElse(null);

            // Crear la nueva conexión con los valores seleccionados
            Conexion conexion = new Conexion(equipo1, equipo2, tipoCable, null, null);
            conexionService.insertar(conexion);

            // Refrescar la tabla después de la inserción
            mostrarConexionesEnTabla(); 
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


    @Override
    public void actionPerformed(ActionEvent e) {
        int row = conexionesTable.getSelectedRow();
        String equipo1Codigo = (String) conexionTableModel.getValueAt(row, 0); // Assuming the first column has the team
                                                                               // code
        String equipo2Codigo = (String) conexionTableModel.getValueAt(row, 1); // Assuming the second column has the
                                                                               // second team code

        try {
            // Assuming there’s a method to fetch Conexion based on equipo1 and equipo2
            Conexion conexionAEliminar = conexionService.buscarPorCodigo(equipo1Codigo, equipo2Codigo);
            System.out.println("Botón eliminar clickeado para la conexión entre equipo: " + equipo1Codigo + " y " + equipo2Codigo);

            eliminarConexion(conexionAEliminar); // Call the method to delete the connection
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar la conexión: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
