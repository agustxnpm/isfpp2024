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
import red.modelo.TipoPuerto;
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
import red.modelo.TipoPuerto;

public class VentanaConexiones extends JFrame implements ActionListener {

    private JTable conexionesTable;
    private DefaultTableModel conexionTableModel;
    private ConexionService conexionService;
    private EquipoService equipoService;
    private TipoCableService tipoCableService;
    private List<Equipo> listaEquiposDisponibles; // Lista de equipos disponibles
    private List<TipoCable> listaCablesDisponibles; // Lista de tipos de cables disponibles
    private String[] equipoArray; // Array para el ComboBox de equipos
    private String[] tipoCableArray; // Array para el ComboBox de tipos de cables

    public VentanaConexiones() {
        setTitle("Gestión de Conexiones");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {// Inicializar los servicios antes de utilizarlos
            equipoService = new EquipoServiceImp();
            tipoCableService = new TipoCableServiceImp();
            conexionService = new ConexionServiceImp();

            // Cargar equipos y tipos de cables
            listaEquiposDisponibles = equipoService.buscarTodos();
            listaCablesDisponibles = tipoCableService.buscarTodos();

            // Inicializar arrays para ComboBox
            equipoArray = listaEquiposDisponibles.stream().map(Equipo::getCodigo).toArray(String[]::new);
            tipoCableArray = listaCablesDisponibles.stream().map(TipoCable::getCodigo).toArray(String[]::new);

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
                conexionService.borrar(conexion); // Lógica para borrar la conexión
                JOptionPane.showMessageDialog(this, "Conexión eliminada correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                mostrarConexionesEnTabla(); // Refrescar la tabla después de eliminar

            } catch (Exception e) {
                e.printStackTrace(); // Mostrar el error en consola
                JOptionPane.showMessageDialog(this, "Error al eliminar la conexión: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        mostrarConexionesEnTabla(); // Refrescar la tabla en la interfaz
    }

    // Método para obtener un equipo según su código
    private Equipo obtenerEquipoPorCodigo(String codigo) {
        return listaEquiposDisponibles.stream()
                .filter(equipo -> equipo.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null); // Devolver null si no se encuentra el equipo
    }

    // Método para obtener un tipo de cable según su código
    private TipoCable obtenerTipoCablePorCodigo(String codigo) {
        return listaCablesDisponibles.stream()
                .filter(tipoCable -> tipoCable.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null); // Devolver null si no se encuentra el tipo de cable
    }

    // Método para obtener un tipo de puerto según su código
    private TipoPuerto obtenerTipoPuertoPorCodigo(String codigo) {
        return listaEquiposDisponibles.stream()
                .flatMap(equipo -> equipo.getPuertos().stream())
                .map(puerto -> puerto.getTipoPuerto())
                .filter(tipoPuerto -> tipoPuerto.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null); // Devolver null si no se encuentra el tipo de puerto
    }

    private void agregarConexion() {
        JPanel panel = new JPanel(new GridLayout(0, 2));

        // Convertir listas a arrays para JComboBox
        String[] equipoArray = listaEquiposDisponibles.stream().map(Equipo::getCodigo).toArray(String[]::new);
        String[] tipoCableArray = listaCablesDisponibles.stream().map(TipoCable::getCodigo).toArray(String[]::new);

        // Crear JComboBox para cada selección
        JComboBox<String> equipo1ComboBox = new JComboBox<>(equipoArray);
        JComboBox<String> equipo2ComboBox = new JComboBox<>(equipoArray);
        JComboBox<String> tipoCableComboBox = new JComboBox<>(tipoCableArray);

        // JComboBox para mostrar los tipos de puerto dinámicamente
        JComboBox<String> tipoPuerto1ComboBox = new JComboBox<>();
        JComboBox<String> tipoPuerto2ComboBox = new JComboBox<>();

        // Listener para actualizar tipos de puerto según equipo
        equipo1ComboBox.addActionListener(e -> actualizarTipoPuerto(equipo1ComboBox, tipoPuerto1ComboBox));
        equipo2ComboBox.addActionListener(e -> actualizarTipoPuerto(equipo2ComboBox, tipoPuerto2ComboBox));

        // Agregar componentes al panel
        panel.add(new JLabel("Equipo 1:"));
        panel.add(equipo1ComboBox);
        panel.add(new JLabel("Tipo de Puerto Equipo 1:"));
        panel.add(tipoPuerto1ComboBox);
        panel.add(new JLabel("Equipo 2:"));
        panel.add(equipo2ComboBox);
        panel.add(new JLabel("Tipo de Puerto Equipo 2:"));
        panel.add(tipoPuerto2ComboBox);
        panel.add(new JLabel("Tipo de Cable:"));
        panel.add(tipoCableComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Conexión", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
                String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();
                String tipoCableCodigo = (String) tipoCableComboBox.getSelectedItem();

                Equipo equipo1 = obtenerEquipoPorCodigo(equipo1Codigo);
                Equipo equipo2 = obtenerEquipoPorCodigo(equipo2Codigo);
                TipoCable tipoCable = obtenerTipoCablePorCodigo(tipoCableCodigo);

                TipoPuerto tipoPuerto1 = obtenerTipoPuertoPorCodigo((String) tipoPuerto1ComboBox.getSelectedItem());
                TipoPuerto tipoPuerto2 = obtenerTipoPuertoPorCodigo((String) tipoPuerto2ComboBox.getSelectedItem());

                Conexion conexion = new Conexion(equipo1, equipo2, tipoCable, tipoPuerto1, tipoPuerto2);
                conexionService.insertar(conexion);
                mostrarConexionesEnTabla();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar la conexión: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para actualizar el JComboBox de TipoPuerto según el equipo
    // seleccionado
    private void actualizarTipoPuerto(JComboBox<String> equipoComboBox, JComboBox<String> tipoPuertoComboBox) {
        String equipoCodigo = (String) equipoComboBox.getSelectedItem();
        Equipo equipo = obtenerEquipoPorCodigo(equipoCodigo);

        tipoPuertoComboBox.removeAllItems(); // Limpiar opciones previas
        equipo.getPuertos().forEach(puerto -> tipoPuertoComboBox.addItem(puerto.getTipoPuerto().getCodigo()));
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
            System.out.println(
                    "Botón eliminar clickeado para la conexión entre equipo: " + equipo1Codigo + " y " + equipo2Codigo);

            eliminarConexion(conexionAEliminar); // Call the method to delete the connection
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar la conexión: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
