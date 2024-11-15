package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import red.controlador.Configuracion;
import red.modelo.Conexion;
import red.modelo.TipoCable;
import red.modelo.TipoPuerto;
import red.negocio.Calculo;
import red.negocio.Red;
import red.modelo.Equipo;

/**
 * Clase VentanaConexiones que representa la ventana de gestión de conexiones de equipos en la red.
 * Permite visualizar, agregar y eliminar conexiones.
 */
public class VentanaConexiones extends JFrame {

    private Red red; // Objeto Red para gestionar la red.
    private Calculo calculo; // Objeto Calculo para realizar operaciones en la red.
    private JTable conexionesTable; // Tabla para mostrar las conexiones.
    private DefaultTableModel conexionTableModel; // Modelo de la tabla de conexiones.

    private List<Equipo> listaEquiposDisponibles; // Lista de equipos disponibles en la red.
    private List<TipoCable> listaCablesDisponibles; // Lista de tipos de cables disponibles en la red.

    /**
     * Constructor de la clase VentanaConexiones.
     * 
     * @param calculo Objeto Calculo para realizar operaciones de cálculo en la red.
     * @param red     Objeto Red que representa la red de equipos.
     */
    public VentanaConexiones(Calculo calculo, Red red) {
        setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_titulo"));
        setSize(800, 400); // Tamaño de la ventana.
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.red = red;
        this.calculo = calculo;

        try {
            // Cargar listas de equipos y tipos de cables disponibles.
            listaEquiposDisponibles = red.getEquipoService().buscarTodos();
            listaCablesDisponibles = red.getTipoCableService().buscarTodos();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_error_datos_conexiones"),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
        }

        inicializarComponentes();
    }

    /**
     * Inicializa los componentes de la ventana y configura la tabla de conexiones.
     */
    private void inicializarComponentes() {
        // Definición de las columnas de la tabla.
        String[] conexionColumnNames = {
            Configuracion.getConfiguracion().getRb().getString("Conexion_equipo_1"),
            Configuracion.getConfiguracion().getRb().getString("Conexion_equipo_2"),
            Configuracion.getConfiguracion().getRb().getString("Conexion_tipo_cable"),
            Configuracion.getConfiguracion().getRb().getString("Conexion_tipo_puerto_1"),
            Configuracion.getConfiguracion().getRb().getString("Conexion_tipo_puerto_2"),
            Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_acciones")
        };
        conexionTableModel = new DefaultTableModel(conexionColumnNames, 0);
        conexionesTable = new JTable(conexionTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de "Acciones" es editable.
            }
        };

        JScrollPane scrollConexiones = new JScrollPane(conexionesTable);
        add(scrollConexiones, BorderLayout.CENTER);

        JButton agregarConexionButton = new JButton(Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_agregar_conexion"));
        agregarConexionButton.addActionListener(e -> agregarConexion());

        JPanel panelInferior = new JPanel();
        panelInferior.add(agregarConexionButton);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarConexionesEnTabla(); // Mostrar conexiones al iniciar.
    }

    /**
     * Muestra las conexiones en la tabla.
     */
    private void mostrarConexionesEnTabla() {
        List<Conexion> conexiones = red.getConexiones();
        conexionTableModel.setRowCount(0); // Limpiar la tabla.
        for (Conexion conexion : conexiones) {
            conexionTableModel.addRow(new Object[]{
                conexion.getEquipo1().getCodigo(),
                conexion.getEquipo2().getCodigo(),
                conexion.getTipoCable().getDescripcion(),
                conexion.getTipoPuerto1().getCodigo(),
                conexion.getTipoPuerto2().getCodigo(),
                Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar")
            });
        }

        conexionesTable.getColumn(Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_acciones"))
            .setCellRenderer(new ButtonRenderer(Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar_minuscula")));
        conexionesTable.getColumn(Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_acciones"))
            .setCellEditor(new ButtonEditor(new JCheckBox(), conexionesTable, Configuracion.getConfiguracion().getRb().getString("Modelo_conexion"),
                    red, calculo));
    }

    /**
     * Obtiene un equipo de la lista de equipos disponibles según su código.
     * 
     * @param codigo El código del equipo a buscar.
     * @return El equipo encontrado o null si no existe.
     */
    private Equipo obtenerEquipoPorCodigo(String codigo) {
        return listaEquiposDisponibles.stream().filter(equipo -> equipo.getCodigo().equals(codigo)).findFirst().orElse(null);
    }

    /**
     * Obtiene un tipo de cable de la lista de tipos de cables disponibles según su código.
     * 
     * @param codigo El código del tipo de cable a buscar.
     * @return El tipo de cable encontrado o null si no existe.
     */
    private TipoCable obtenerTipoCablePorCodigo(String codigo) {
        return listaCablesDisponibles.stream().filter(tipoCable -> tipoCable.getCodigo().equals(codigo)).findFirst().orElse(null);
    }

    /**
     * Agrega una nueva conexión a la red y la muestra en la tabla.
     */
    private void agregarConexion() {
        JPanel panel = new JPanel(new GridLayout(0, 2));

        // Convertir listas a arrays para JComboBox.
        String[] equipoArray = listaEquiposDisponibles.stream().map(Equipo::getCodigo).toArray(String[]::new);
        String[] tipoCableArray = listaCablesDisponibles.stream().map(TipoCable::getCodigo).toArray(String[]::new);

        JComboBox<String> equipo1ComboBox = new JComboBox<>(equipoArray);
        JComboBox<String> equipo2ComboBox = new JComboBox<>(equipoArray);
        JComboBox<String> tipoCableComboBox = new JComboBox<>(tipoCableArray);

        JComboBox<String> tipoPuerto1ComboBox = new JComboBox<>();
        JComboBox<String> tipoPuerto2ComboBox = new JComboBox<>();

        equipo1ComboBox.addActionListener(e -> actualizarTipoPuerto(equipo1ComboBox, tipoPuerto1ComboBox));
        equipo2ComboBox.addActionListener(e -> actualizarTipoPuerto(equipo2ComboBox, tipoPuerto2ComboBox));

        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Conexion_equipo_1")));
        panel.add(equipo1ComboBox);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Conexion_tipo_puerto_1")));
        panel.add(tipoPuerto1ComboBox);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Conexion_equipo_2")));
        panel.add(equipo2ComboBox);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Conexion_tipo_puerto_2")));
        panel.add(tipoPuerto2ComboBox);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Conexion_tipo_cable")));
        panel.add(tipoCableComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_agregar_conexion"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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

                calculo.agregarConexionAlGrafo(conexion);
                red.agregarConexion(conexion);
                mostrarConexionesEnTabla();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        String.format(Configuracion.getConfiguracion().getRb().getString("VentanaConexiones_error_agregar_conexion"), e.getMessage()),
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Actualiza los tipos de puerto disponibles en el JComboBox según el equipo seleccionado.
     * 
     * @param equipoComboBox    JComboBox que contiene los equipos.
     * @param tipoPuertoComboBox JComboBox donde se mostrarán los tipos de puerto.
     */
    private void actualizarTipoPuerto(JComboBox<String> equipoComboBox, JComboBox<String> tipoPuertoComboBox) {
        tipoPuertoComboBox.removeAllItems(); // Limpiar opciones previas.

        try {
            List<TipoPuerto> puertos = red.getTipoPuertoService().buscarTodos();
            for (TipoPuerto p : puertos)
                tipoPuertoComboBox.addItem(p.getCodigo());
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
        			JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene un tipo de puerto de la lista de tipos de puertos disponibles según su código.
     * 
     * @param codigo El código del tipo de puerto a buscar.
     * @return El tipo de puerto encontrado o null si no existe.
     */
    private TipoPuerto obtenerTipoPuertoPorCodigo(String codigo) {
        try {
            List<TipoPuerto> puertos = red.getTipoPuertoService().buscarTodos();
            for (TipoPuerto p : puertos)
                if (p.getCodigo().equals(codigo))
                    return p;
            return null;
        } catch (FileNotFoundException e) {
            // Manejo de la excepción (sin bloque de impresión).
        	JOptionPane.showMessageDialog(this, e.getMessage(), Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
        			JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
