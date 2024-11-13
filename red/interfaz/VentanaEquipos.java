package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;
import red.negocio.Calculo;
import red.negocio.Red;
import red.controlador.Configuracion;

/**
 * Clase que representa la ventana para la gestión de equipos en la red.
 * Permite visualizar, agregar y modificar equipos, así como asignar IPs aleatorias.
 */
public class VentanaEquipos extends JFrame {

    private Red red;
    private Calculo calculo;
    private JTable equiposTable;
    private DefaultTableModel equipoTableModel;
    private List<TipoEquipo> listTipoEquipo; // Lista de TipoEquipo disponible
    private List<TipoPuerto> listTipoPuerto; // Lista de TipoPuerto disponible
    private List<Ubicacion> listUbicaciones; // Lista de Ubicaciones disponible

    /**
     * Constructor de la clase VentanaEquipos.
     *
     * @param calculo Instancia de Calculo para realizar operaciones en la red.
     * @param red Instancia de Red que contiene los datos de la red.
     */
    public VentanaEquipos(Calculo calculo, Red red) {
        setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_titulo"));
        setSize(1200, 400); // Ajustar el tamaño para mostrar todas las columnas
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.red = red;
        this.calculo = calculo;

        try {
            // Cargar los datos necesarios de los servicios de la red
            listTipoEquipo = red.getTipoEquipoService().buscarTodos();
            listTipoPuerto = red.getTipoPuertoService().buscarTodos();
            listUbicaciones = red.getUbicaciones();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_error_cargar_datos"),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
        }

        inicializarComponentes();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void inicializarComponentes() {
        // Definición de las columnas de la tabla
        String[] equipoColumnNames = {
                Configuracion.getConfiguracion().getRb().getString("Equipo_codigo"),
                Configuracion.getConfiguracion().getRb().getString("Equipo_descripcion"),
                Configuracion.getConfiguracion().getRb().getString("Equipo_marca"),
                Configuracion.getConfiguracion().getRb().getString("Equipo_modelo"),
                Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_equipo"),
                Configuracion.getConfiguracion().getRb().getString("Equipo_ubicacion"),
                Configuracion.getConfiguracion().getRb().getString("Equipo_estado"),
                Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_info_puertos"),
                Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_direccion_ip"),
                Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_acciones"),
                Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_modificar")
        };

        equipoTableModel = new DefaultTableModel(equipoColumnNames, 0);
        equiposTable = new JTable(equipoTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9 || column == 10; // Solo las columnas de acciones son editables
            }
        };

        JScrollPane scrollEquipos = new JScrollPane(equiposTable);
        add(scrollEquipos, BorderLayout.CENTER);

        JButton agregarEquipoButton = new JButton(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_agregar_equipo"));
        agregarEquipoButton.addActionListener(e -> agregarEquipo());

        JPanel panelInferior = new JPanel();
        panelInferior.add(agregarEquipoButton);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarEquiposEnTabla(); // Mostrar los equipos al iniciar
    }

    /**
     * Muestra la lista de equipos en la tabla.
     */
    private void mostrarEquiposEnTabla() {
        List<Equipo> equipos = red.getEquipos();
        equipoTableModel.setRowCount(0); // Limpiar la tabla existente

        for (Equipo equipo : equipos) {
            // Convertir el estado booleano a un texto descriptivo
            String estadoTexto = equipo.isEstado() ? Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_activo")
                    : Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_inactivo");

            equipoTableModel.addRow(new Object[]{
                    equipo.getCodigo(),
                    equipo.getDescripcion(),
                    equipo.getMarca(),
                    equipo.getModelo(),
                    equipo.getTipoEquipo().getDescripcion(),
                    equipo.getUbicacion().getDescripcion(),
                    estadoTexto,
                    equipo.getPuertosInfo(),
                    equipo.getDireccionesIp().toString(),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar"),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar")
            });
        }

        // Configuración de los botones "Eliminar" y "Modificar"
        equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_acciones"))
                .setCellRenderer(new ButtonRenderer(Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar")));
        equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_acciones"))
                .setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable, Configuracion.getConfiguracion().getRb().getString("Modelo_equipo"), red, calculo));

        equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar"))
                .setCellRenderer(new ButtonRenderer(Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar_minuscula")));
        equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar"))
                .setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable,
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar_minuscula"), red, calculo));
    }

    /**
     * Genera una dirección IP aleatoria que no esté en uso.
     *
     * @param calculo Instancia de Calculo para obtener las IPs existentes.
     * @return Una dirección IP aleatoria única.
     */
    private String generarIPAleatoria(Calculo calculo) {
        List<String> ipsExistentes = calculo.obtenerTodasLasIPs();
        Random random = new Random();
        int tercerOcteto = 16; // Inicialmente 192.168.16.x
        int cuartoOcteto;

        while (true) {
            cuartoOcteto = random.nextInt(256); // Genera un número entre 0 y 255
            String ipGenerada = "192.168." + tercerOcteto + "." + cuartoOcteto;

            if (!ipsExistentes.contains(ipGenerada)) {
                return ipGenerada; // Devuelve la IP si no está en uso
            }

            if (cuartoOcteto == 255 && !ipsExistentes.contains("192.168." + (tercerOcteto + 1) + ".0")) {
                tercerOcteto++;
            }
        }
    }

    /**
     * Agrega un nuevo equipo a la red.
     */
    private void agregarEquipo() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField codigoField = new JTextField();
        JTextField modeloField = new JTextField();
        JTextField marcaField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField cantPuertosField = new JTextField();

        String[] tipoEquipoArray = listTipoEquipo.stream().map(TipoEquipo::getCodigo).toArray(String[]::new);
        String[] tipoPuertoArray = listTipoPuerto.stream().map(TipoPuerto::getCodigo).toArray(String[]::new);
        String[] ubicacionArray = listUbicaciones.stream().map(Ubicacion::getCodigo).toArray(String[]::new);

        JComboBox<String> tipoEquipoComboBox = new JComboBox<>(tipoEquipoArray);
        JComboBox<String> tipoPuertoComboBox = new JComboBox<>(tipoPuertoArray);
        JComboBox<String> ubicacionComboBox = new JComboBox<>(ubicacionArray);

        // Añadir componentes al panel
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_codigo_opcion")));
        panel.add(codigoField);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_modelo_opcion")));
        panel.add(modeloField);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_marca_opcion")));
        panel.add(marcaField);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_descripcion_opcion")));
        panel.add(descripcionField);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_cantidad_puertos_opcion")));
        panel.add(cantPuertosField);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_equipo_opcion")));
        panel.add(tipoEquipoComboBox);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_puerto_opcion")));
        panel.add(tipoPuertoComboBox);
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_ubicacion_opcion")));
        panel.add(ubicacionComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_agregar_equipo"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int cantPuertos = Integer.parseInt(cantPuertosField.getText());
                String tipoEquipo = (String) tipoEquipoComboBox.getSelectedItem();
                String tipoPuerto = (String) tipoPuertoComboBox.getSelectedItem();
                String ubicacion = (String) ubicacionComboBox.getSelectedItem();

                TipoPuerto selectedPuerto = listTipoPuerto.stream().filter(tp -> tp.getCodigo().equals(tipoPuerto))
                        .findFirst().orElse(null);
                Ubicacion selectedUbicacion = listUbicaciones.stream().filter(u -> u.getCodigo().equals(ubicacion))
                        .findFirst().orElse(null);

                Equipo equipo = new Equipo(codigoField.getText(), modeloField.getText(), marcaField.getText(),
                        descripcionField.getText(), selectedUbicacion, new TipoEquipo(tipoEquipo, ""), cantPuertos,
                        selectedPuerto, true);

                String ipAsignada = generarIPAleatoria(calculo);
                equipo.agregarIp(ipAsignada);

                calculo.agregarEquipoAlGrafo(equipo);
                red.agregarEquipo(equipo);
                mostrarEquiposEnTabla(); // Refrescar la tabla
                JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_equipo_anadido_correctamente"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        String.format(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_error_agregar_equipo"), e.getMessage()),
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
