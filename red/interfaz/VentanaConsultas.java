package red.interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import red.controlador.Configuracion;
import red.modelo.Equipo;
import red.negocio.Calculo;
import red.negocio.Red;

/**
 * Clase que representa la ventana de consultas de la red. Permite realizar
 * operaciones como calcular la velocidad máxima entre equipos, hacer pings a
 * equipos o rangos de IPs, detectar problemas de conectividad y visualizar el
 * estado de la red.
 */
public class VentanaConsultas extends JFrame {

    private Calculo calculo;
    private Red red;
    private Handler handler; // Event handler

    private JButton calcularVelocidadButton;
    private JButton pingEquipoButton;
    private JButton mostrarRutaButton; // Botón para mostrar la ruta
    private JButton detectarProblemasButton;
    private JButton calcularButton; // Button to confirm speed calculation
    private JButton verMapaEstadoButton;
    private JDialog dialog; // JDialog de la ventana ping
    private JButton verificarButton;
    private JCheckBox rangoCheckBox; // checkbox para seleccionar ping / ping rango
    private JTextField equipoTextField; // campo de texto para ip de equipo
    private JButton pingButton; // boton para realizar ping
    private JComboBox<String> equipo1ComboBox;
    private JComboBox<String> equipo2ComboBox;
    private JTextField ipInicioTextField; // Campo de texto para la IP de inicio
    private JTextField ipFinTextField; // Campo de texto para la IP de fin
    private JTextField cantPingsTextField; // campo para ingresar la cantidad de pings
    private int cantPings;
    private boolean modo; // true = modo simulacion, false = modo real;

    /**
     * Constructor de la clase VentanaConsultas.
     *
     * @param calculo Instancia de Calculo para realizar operaciones de análisis
     * en la red.
     * @param red Instancia de Red que representa la red de equipos.
     * @param modo Indica si se usa el modo de simulación (true) o real (false).
     */
    public VentanaConsultas(Calculo calculo, Red red, boolean modo) {
        setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_titulo"));
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.calculo = calculo;
        this.red = red;
        this.modo = modo;
        inicializarComponentes();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void inicializarComponentes() {
        handler = new Handler();

        calcularVelocidadButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_calcular_velocidad_maxima"));
        calcularVelocidadButton.setBounds(20, 80, 180, 50);
        calcularVelocidadButton.addActionListener(handler);

        pingEquipoButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_realizar_ping_equipo"));
        pingEquipoButton.setBounds(220, 80, 180, 50);
        pingEquipoButton.addActionListener(handler);

        detectarProblemasButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_detectar_problemas"));
        detectarProblemasButton.setBounds(420, 80, 180, 50);
        detectarProblemasButton.addActionListener(handler);

        verMapaEstadoButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_mapa_estado"));
        verMapaEstadoButton.setBounds(20, 160, 180, 50);
        verMapaEstadoButton.addActionListener(handler);

        mostrarRutaButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_mostrar_ruta_entre_equipos"));
        mostrarRutaButton.setBounds(220, 160, 180, 50);
        mostrarRutaButton.addActionListener(handler);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(calcularVelocidadButton);
        panel.add(pingEquipoButton);
        panel.add(detectarProblemasButton);
        panel.add(verMapaEstadoButton);
        panel.add(mostrarRutaButton);
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    /**
     * Muestra un diálogo para seleccionar equipos y calcular la velocidad
     * máxima entre ellos.
     */
    private void ventanaVelocidad() {
        calcularButton = new JButton(Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_calcular"));

        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        String[] equipoArray = red.getEquipos().stream().map(Equipo::getCodigo).toArray(String[]::new);
        equipo1ComboBox = new JComboBox<>(equipoArray);
        equipo2ComboBox = new JComboBox<>();

        JLabel equipo1Label = new JLabel("Equipo 1");
        JLabel equipo2Label = new JLabel("Equipo 2");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(equipo1Label, gbc);
        gbc.gridx = 1;
        panelCentral.add(equipo1ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCentral.add(equipo2Label, gbc);
        gbc.gridx = 1;
        panelCentral.add(equipo2ComboBox, gbc);

        JDialog dialog = new JDialog(this,
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_calcular_velocidad"), true);

        JPanel panelInferior = new JPanel();
        panelInferior.add(calcularButton);
        equipo1ComboBox.addActionListener(handler);

        calcularButton.addActionListener(handler);

        dialog.add(panelCentral);
        dialog.add(panelInferior, BorderLayout.SOUTH);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Actualiza el JComboBox de equipo2 según las conexiones del equipo1
     * seleccionado.
     */
    private void actualizarEquiposConectados() {
        equipo2ComboBox.removeAllItems();
        String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
        Equipo equipo1 = red.getEquipos().stream().filter(e -> e.getCodigo().equals(equipo1Codigo)).findFirst()
                .orElse(null);

        if (equipo1 != null) {
            Set<Equipo> equiposConectados = calculo.obtenerEquiposConectadosTransitivamente(equipo1);
            equiposConectados.remove(equipo1); // Remover el equipo1 de la lista de opciones del ComboBox

            if (equiposConectados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        String.format(Configuracion.getConfiguracion().getRb()
                                .getString("VentanaConsultas_no_equipos_conectados_a"), equipo1Codigo),
                        Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_sin_conexiones"),
                        JOptionPane.WARNING_MESSAGE);
            } else {
                for (Equipo equipo : equiposConectados) {
                    equipo2ComboBox.addItem(equipo.getCodigo());
                }
            }
        }
    }

    /**
     * Calcula la velocidad máxima entre los dos equipos seleccionados y muestra
     * el resultado.
     */
    private void calcularVelocidad() {
        String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
        String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();

        if (equipo1Codigo == null || equipo2Codigo == null) {
            JOptionPane.showMessageDialog(this,
                    Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_seleccione_ambos_equipos"),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buscar los equipos en la red
        Equipo equipo1 = red.getEquipos().stream().filter(e -> e.getCodigo().equals(equipo1Codigo)).findFirst()
                .orElse(null);

        Equipo equipo2 = red.getEquipos().stream().filter(e -> e.getCodigo().equals(equipo2Codigo)).findFirst()
                .orElse(null);

        try {
            if (equipo1 != null && equipo2 != null) {
                // Obtener la ruta entre los equipos y calcular la velocidad
                List<Equipo> ruta = calculo.buscarRuta(equipo1, equipo2);
                int velocidadMaxima = calculo.calcularVelocidadMaxima(ruta);

                JOptionPane.showMessageDialog(this,
                        String.format(
                                Configuracion.getConfiguracion().getRb()
                                        .getString("VentanaConsultas_velocidad_maxima_entre_y_es_mbps"),
                                equipo1Codigo, equipo2Codigo, velocidadMaxima),
                        Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_velocidad_maxima"),
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        Configuracion.getConfiguracion().getRb()
                                .getString("VentanaConsultas_no_se_pudo_encontrar_uno_de_equipos"),
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Muestra un diálogo para realizar un ping a un equipo o a un rango de IPs.
     */
    private void realizarPingAEquipo() {
        // Crear un diálogo personalizado
        dialog = new JDialog(this,
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ping_equipo_o_rango_ips"), true);
        dialog.setSize(500, 350);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Checkbox para seleccionar ping a un rango de equipos
        rangoCheckBox = new JCheckBox(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ping_rango_equipos"));
        dialog.add(rangoCheckBox, gbc);

        gbc.gridy++;
        JLabel equipoLabel = new JLabel(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ingresa_ip_equipo"));
        dialog.add(equipoLabel, gbc);

        gbc.gridx++;
        equipoTextField = new JTextField(15); // Campo de texto para la IP de un solo equipo
        dialog.add(equipoTextField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel ipInicioLabel = new JLabel(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ip_inicio"));
        ipInicioTextField = new JTextField(15); // Campo de texto para la IP de inicio
        JLabel ipFinLabel = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ip_fin"));
        ipFinTextField = new JTextField(15); // Campo de texto para la IP de fin

        dialog.add(ipInicioLabel, gbc);
        gbc.gridx++;
        dialog.add(ipInicioTextField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        dialog.add(ipFinLabel, gbc);
        gbc.gridx++;
        dialog.add(ipFinTextField, gbc);

        // Ocultar los campos de rango al principio
        ipInicioLabel.setVisible(false);
        ipInicioTextField.setVisible(false);
        ipFinLabel.setVisible(false);
        ipFinTextField.setVisible(false);

        rangoCheckBox.addActionListener(e -> {
            boolean isSelected = rangoCheckBox.isSelected();
            equipoLabel.setVisible(!isSelected);
            equipoTextField.setVisible(!isSelected);
            ipInicioLabel.setVisible(isSelected);
            ipInicioTextField.setVisible(isSelected);
            ipFinLabel.setVisible(isSelected);
            ipFinTextField.setVisible(isSelected);
        });

        // Campo de texto para la cantidad de pings
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel cantPingsLabel = new JLabel(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_cantidad_pings"));
        dialog.add(cantPingsLabel, gbc);

        gbc.gridx++;
        cantPingsTextField = new JTextField(5); // Campo de texto para la cantidad de pings
        dialog.add(cantPingsTextField, gbc);

        // Botón para realizar el ping
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        pingButton = new JButton(Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ping"));
        pingButton.addActionListener(handler);
        dialog.add(pingButton, gbc);

        // Mostrar el diálogo
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Realiza un ping a una dirección IP y muestra los resultados.
     *
     * @param direccionIp Dirección IP a la que se realizará el ping.
     * @param dialog El diálogo que muestra la operación en curso.
     * @param cantPings La cantidad de pings a enviar.
     */
    private void pingReal(String direccionIp, JDialog dialog, int cantPings) {
        // Crear el JDialog para mostrar los resultados
        JDialog resultDialog = new JDialog(dialog,
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_resultados_ping_real"), false); // No
        // modal
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton detenerButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_detener"));

        // Crear la barra de progreso
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        // Panel de control para el botón y la barra de progreso
        JPanel panelControl = new JPanel(new BorderLayout());
        panelControl.add(progressBar, BorderLayout.NORTH);
        panelControl.add(detenerButton, BorderLayout.EAST);

        // Configurar el JDialog
        resultDialog.setLayout(new BorderLayout());
        resultDialog.add(scrollPane, BorderLayout.CENTER);
        resultDialog.add(panelControl, BorderLayout.SOUTH);
        resultDialog.pack();
        resultDialog.setLocationRelativeTo(null);
        resultDialog.setAlwaysOnTop(true);
        resultDialog.setVisible(true);

        // Variable de control para detener el proceso
        final boolean[] detener = {false};

        // Acción del botón "Detener"
        detenerButton.addActionListener(e -> detener[0] = true);

        // Crear un SwingWorker para realizar el ping
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                calculo.ping(direccionIp, cantPings, textArea, detener, progressBar);
                return null;
            }

            @Override
            protected void done() {
                if (!detener[0]) {
                    textArea.append(
                            Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ping_completado"));
                    progressBar.setValue(progressBar.getMaximum()); // Asegurarse de que la barra esté al 100%
                }
            }
        };

        // Ejecutar el SwingWorker
        worker.execute();
    }

    /**
     * Método para hacer ping a un solo equipo, si modo == true, realiza ping
     * simulado, de otro modo ping real
     *
     * @param direccionIp
     * @param dialog
     * @param cantPings
     */
    private void pingAEquipo(String direccionIp, JDialog dialog, int cantPings) {

        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        try {
            if (!direccionIp.isEmpty()) {
                if (modo) {
                    boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);
                    String mensaje = respuestaPing
                            ? Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ping_exitoso")
                            : Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ping_fallido");
                    JOptionPane.showMessageDialog(dialog,
                            String.format(Configuracion.getConfiguracion().getRb()
                                    .getString("VentanaConsultas_al_equipo_con_ip"), mensaje, direccionIp),
                            Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_resultado_ping"),
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    pingReal(direccionIp, dialog, cantPings);
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ingresa_ip_valida"),
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, e.getMessage(),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Método para hacer ping a un rango de IPs
     *
     * @param inicioIp
     * @param finIp
     * @param dialog
     * @param cantPings
     */
    private void pingARango(String inicioIp, String finIp, JDialog dialog, int cantPings) {
        if (!inicioIp.isEmpty() && !finIp.isEmpty()) {
            // Crear el JDialog para mostrar los resultados
            JDialog resultDialog = new JDialog(dialog, Configuracion.getConfiguracion().getRb()
                    .getString("VentanaConsultas_resultados_ping"), false); // false indica que no es modal
            JTextArea textArea = new JTextArea(20, 50);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JButton detenerButton = new JButton(
                    Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_detener"));

            // Crear la barra de progreso
            JProgressBar progressBar = new JProgressBar();
            progressBar.setStringPainted(true);

            // Panel de control inferior para el botón y la barra de progreso
            JPanel panelControl = new JPanel(new BorderLayout());
            panelControl.add(detenerButton, BorderLayout.EAST);
            panelControl.add(progressBar, BorderLayout.CENTER);

            // Configurar el JDialog
            resultDialog.setLayout(new BorderLayout());
            resultDialog.add(scrollPane, BorderLayout.CENTER);
            resultDialog.add(panelControl, BorderLayout.SOUTH);
            resultDialog.pack();
            resultDialog.setLocationRelativeTo(null);

            // Asegurar que el JDialog esté siempre al frente
            resultDialog.setAlwaysOnTop(true);
            resultDialog.setVisible(true);

            // Variable de control para detener el proceso
            final boolean[] detener = {false};

            // Agregar acción al botón "Detener"
            detenerButton.addActionListener(e -> detener[0] = true);

            // Calcular el total de pings a realizar
            int totalPings = calculo.calcularTotalIPsEnRango(inicioIp, finIp); // total de ips en el rango (no
            // simulacion)
            progressBar.setMaximum(totalPings);

            // Crear el SwingWorker para realizar los pings en segundo plano
            SwingWorker<Void, String> worker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() throws Exception {
                    if (modo) {
                        List<String> pingResults = calculo.realizarPingARango(inicioIp, finIp);
                        int totalPings = pingResults.size(); // Total de IPs en el rango (simulacion)
                        progressBar.setMaximum(totalPings - 1);
                        for (int i = 0; i < totalPings; i++) {
                            if (detener[0]) {
                                publish(Configuracion.getConfiguracion().getRb()
                                        .getString("VentanaConsultas_ping_detenido_por_usuario"));
                                break;
                            }
                            progressBar.setValue(i);

                            publish(pingResults.get(i)); // Publicar resultado
                            Thread.sleep(2000); // Simulación de demora de 2 segundos
                        }
                    } else
						try {
                        calculo.pingRango(inicioIp, finIp, cantPings, textArea, detener, progressBar);
                    } catch (IllegalArgumentException | UnknownHostException e) {
                        JOptionPane.showMessageDialog(VentanaConsultas.this, e.getMessage(),
                                Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                                JOptionPane.ERROR_MESSAGE);
                    }

                    return null;
                }

                @Override
                protected void process(List<String> chunks) {
                    for (String resultado : chunks) {
                        textArea.append(resultado + "\n"); // Mostrar resultado en JTextArea

                    }
                }

                @Override
                protected void done() {
                    if (!detener[0]) {
                        textArea.append(Configuracion.getConfiguracion().getRb()
                                .getString("VentanaConsultas_ping_rango_completado"));
                    }
                }
            };

            // Agregar un listener para actualizar la barra de progreso
            worker.addPropertyChangeListener(evt -> {
                if ("progress".equals(evt.getPropertyName())) {
                    int progreso = (int) evt.getNewValue();
                    progressBar.setValue(progreso); // Actualizar la barra de progreso
                }
            });

            worker.execute(); // Ejecutar la tarea
        } else {
            JOptionPane.showMessageDialog(dialog,
                    Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ingresa_rango_ips_valido"),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para detectar problemas de conectividad
     */
    private void detectarProblemasConectividad() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        String[] equipoArray = red.getEquipos().stream().map(Equipo::getCodigo).toArray(String[]::new);
        equipo1ComboBox = new JComboBox<>(equipoArray);
        equipo2ComboBox = new JComboBox<>(equipoArray);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Conexion_equipo_1_opcion")), gbc);
        gbc.gridx = 1;
        panel.add(equipo1ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_equipo_gateway")),
                gbc);
        gbc.gridx = 1;
        panel.add(equipo2ComboBox, gbc);

        JDialog dialog = new JDialog(this,
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_verificar_conectividad"), true);

        JPanel panelInferior = new JPanel();

        verificarButton = new JButton(
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_verificar_conectividad"));
        panelInferior.add(verificarButton);

        verificarButton.addActionListener(handler);

        dialog.add(panel);
        dialog.add(panelInferior, BorderLayout.SOUTH);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void verificarConectividad() {
        String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
        String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();
        Equipo equipo = red.buscarEquipoPorCodigo(equipo1Codigo);
        Equipo gateway = red.buscarEquipoPorCodigo(equipo2Codigo);

        try {
            String resultado = calculo.verificarConectividad(equipo, gateway);
            JOptionPane.showMessageDialog(this, resultado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }

    private void verMapaDeEstado() {
        // Crea un nuevo diálogo para mostrar el mapa de las redes
        JDialog dialog = new JDialog(this,
                Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_mapa_estado_red"), true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        // Obtiene el panel del grafo de Calculo
        JPanel graphPanel = calculo.crearMapaDeEstado();
        dialog.add(graphPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    /**
     * Muestra la ruta entre dos equipos seleccionados y resalta la misma en el
     * grafo.
     */
    private void mostrarRutaEntreEquipos() {
        JDialog dialog = new JDialog(this, "Seleccione Equipos", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(3, 2));

        JLabel equipo1Label = new JLabel("Equipo 1:");
        equipo1ComboBox = new JComboBox<>(red.getEquipos().stream().map(Equipo::getCodigo).toArray(String[]::new));

        JLabel equipo2Label = new JLabel("Equipo 2:");
        equipo2ComboBox = new JComboBox<>(red.getEquipos().stream().map(Equipo::getCodigo).toArray(String[]::new));

        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.addActionListener(e -> {
            String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
            String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();

            if (equipo1Codigo == null || equipo2Codigo == null) {
                JOptionPane.showMessageDialog(this,
                        Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_seleccione_ambos_equipos"),
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Cierra la ventana de selección de equipos
            dialog.dispose();

            // Llama al método para mostrar la ruta después de cerrar la ventana de selección
            mostrarRutaEntreEquipos(equipo1Codigo, equipo2Codigo);
        });

        dialog.add(equipo1Label);
        dialog.add(equipo1ComboBox);
        dialog.add(equipo2Label);
        dialog.add(equipo2ComboBox);
        dialog.add(new JLabel()); // Espacio vacío para alineación.
        dialog.add(confirmarButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void mostrarRutaEntreEquipos(String equipo1Codigo, String equipo2Codigo) {
        // Buscar los equipos en la red
        Equipo equipo1 = red.buscarEquipoPorCodigo(equipo1Codigo);
        Equipo equipo2 = red.buscarEquipoPorCodigo(equipo2Codigo);

        if (equipo1 != null && equipo2 != null) {
            try {
                List<Equipo> ruta = calculo.buscarRuta(equipo1, equipo2);

                if (ruta != null && !ruta.isEmpty()) {
                    // Mostrar un panel con el grafo resaltando la ruta
                    JPanel graphPanel = calculo.crearMapaDeEstadoConRuta(ruta);
                    JDialog rutaDialog = new JDialog(this,
                            Configuracion.getConfiguracion().getRb().getString("VentanaConsultas_ruta_entre_equipos"),
                            true);
                    rutaDialog.setSize(800, 600);
                    rutaDialog.setLocationRelativeTo(this);
                    rutaDialog.add(graphPanel);
                    rutaDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            Configuracion.getConfiguracion().getRb()
                                    .getString("VentanaConsultas_no_se_encontro_ruta_entre_equipos"),
                            Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    Configuracion.getConfiguracion().getRb()
                            .getString("VentanaConsultas_no_se_pudo_encontrar_uno_de_equipos"),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private class Handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(calcularVelocidadButton)) {
                ventanaVelocidad();
            }

            if (e.getSource().equals(pingEquipoButton)) {
                realizarPingAEquipo();
            }

            if (e.getSource().equals(detectarProblemasButton)) {
                detectarProblemasConectividad();
            }

            if (e.getSource().equals(calcularButton)) {
                calcularVelocidad();
            }

            if (e.getSource().equals(verificarButton)) {
                verificarConectividad();
            }

            if (e.getSource().equals(verMapaEstadoButton)) {
                verMapaDeEstado();
            }

            if (e.getSource().equals(equipo1ComboBox)) {
                actualizarEquiposConectados();
            }

            if (e.getSource().equals(pingButton)) {

                try {
                    cantPings = Integer.parseInt(cantPingsTextField.getText().trim());
                } catch (NumberFormatException ex) {
                    cantPings = 0;
                }

                if (rangoCheckBox.isSelected()) // Obtener datos de los campos de texto y realizar ping al rango
                {
                    pingARango(ipInicioTextField.getText().trim(), ipFinTextField.getText().trim(), dialog, cantPings);
                } else // Obtener la IP y realizar ping a un solo equipo
                {
                    pingAEquipo(equipoTextField.getText().trim(), dialog, cantPings);
                }
            }
            if (e.getSource().equals(mostrarRutaButton)) {
                mostrarRutaEntreEquipos();
            }

        }
    } // fin clase Handler

}
