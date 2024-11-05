package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import red.modelo.Equipo;
import red.negocio.Calculo;
import red.negocio.Red;
import java.util.List;

public class VentanaConsultas extends JFrame {

    private Calculo calculo;
    private Red red;
    private Handler handler; // Event handler

    private JButton calcularVelocidadButton;
    private JButton pingEquipoButton;
    private JButton detectarProblemasButton;
    private JButton calcularButton; // Button to confirm speed calculation
    private JButton verMapaEstadoButton;

    private JButton verificarButton;
    private JComboBox<String> equipo1ComboBox;
    private JComboBox<String> equipo2ComboBox;
    private boolean modo; // true = modo simulacion, false = modo real;
    
    public VentanaConsultas(Calculo calculo, Red red, boolean modo) {
        setTitle("Consultas de la Red");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.calculo = calculo;
        this.red = red;
        this.modo = modo;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        handler = new Handler();

        calcularVelocidadButton = new JButton("Calcular Velocidad Máxima");
        calcularVelocidadButton.setBounds(20, 80, 180, 50);
        calcularVelocidadButton.addActionListener(handler);

        pingEquipoButton = new JButton("Realizar Ping a Equipo");
        pingEquipoButton.setBounds(220, 80, 180, 50);
        pingEquipoButton.addActionListener(handler);

        detectarProblemasButton = new JButton("Detectar Problemas");
        detectarProblemasButton.setBounds(420, 80, 180, 50);
        detectarProblemasButton.addActionListener(handler);

        verMapaEstadoButton = new JButton("Ver Mapa de Estado");
        verMapaEstadoButton.setBounds(20, 160, 180, 50);
        verMapaEstadoButton.addActionListener(handler);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.add(calcularVelocidadButton);
        panel.add(pingEquipoButton);
        panel.add(detectarProblemasButton);
        panel.add(verMapaEstadoButton);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void ventanaVelocidad() {
        calcularButton = new JButton("Calcular");

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

        JDialog dialog = new JDialog(this, "Calcular Velocidad", true);

        JPanel panelInferior = new JPanel();
        panelInferior.add(calcularButton);
        equipo1ComboBox.addActionListener(e -> actualizarEquiposConectados());

        calcularButton.addActionListener(handler);

        dialog.add(panelCentral);
        dialog.add(panelInferior, BorderLayout.SOUTH);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Método para actualizar el JComboBox de equipo2 según las conexiones del
    // equipo1 seleccionado
    private void actualizarEquiposConectados() {
        equipo2ComboBox.removeAllItems();
        String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
        Equipo equipo1 = red.getEquipos().stream().filter(e -> e.getCodigo().equals(equipo1Codigo)).findFirst()
                .orElse(null);

        if (equipo1 != null) {
            Set<Equipo> equiposConectados = calculo.obtenerEquiposConectadosTransitivamente(equipo1);
            equiposConectados.remove(equipo1); // Remover el equipo1 de la lista de opciones del ComboBox

            if (equiposConectados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron equipos conectados a " + equipo1Codigo,
                        "Sin Conexiones", JOptionPane.WARNING_MESSAGE);
            } else {
                for (Equipo equipo : equiposConectados) {
                    equipo2ComboBox.addItem(equipo.getCodigo());
                }
            }
        }
    }

    private void calcularVelocidad() {
        String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
        String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();

        if (equipo1Codigo == null || equipo2Codigo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione ambos equipos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buscar los equipos en la red
        Equipo equipo1 = red.getEquipos().stream()
                .filter(e -> e.getCodigo().equals(equipo1Codigo))
                .findFirst().orElse(null);

        Equipo equipo2 = red.getEquipos().stream()
                .filter(e -> e.getCodigo().equals(equipo2Codigo))
                .findFirst().orElse(null);

        if (equipo1 != null && equipo2 != null) {
            // Obtener la ruta entre los equipos y calcular la velocidad
            List<Equipo> ruta = calculo.buscarRuta(equipo1, equipo2);
            int velocidadMaxima = calculo.calcularVelocidadMaxima(ruta);

            JOptionPane.showMessageDialog(this, "La velocidad máxima entre " + equipo1Codigo + " y " + equipo2Codigo
                    + " es: " + velocidadMaxima + " Mbps");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar uno de los equipos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarPingAEquipo() {
        // Crear un panel para contener los elementos de entrada
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(400, 250)); // Ajusta el tamaño de la ventana
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
    
        JCheckBox rangoCheckBox = new JCheckBox("Ping a un rango de equipos");
        panel.add(rangoCheckBox, gbc);
    
        gbc.gridy++;
        JLabel equipoLabel = new JLabel("Selecciona un equipo:");
        panel.add(equipoLabel, gbc);
    
        gbc.gridx++;
        JComboBox<String> equipoComboBox = new JComboBox<>();
        for (Equipo equipo : red.getEquipos()) {
            for (String ip : equipo.getDireccionesIp()) {
                equipoComboBox.addItem(equipo.getCodigo() + " (" + ip + ")");
            }
        }
        panel.add(equipoComboBox, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel ipInicioLabel = new JLabel("IP de inicio:");
        JComboBox<String> ipInicioComboBox = new JComboBox<>();
        JLabel ipFinLabel = new JLabel("IP de fin:");
        JComboBox<String> ipFinComboBox = new JComboBox<>();
    
        for (Equipo equipo : red.getEquipos()) {
            for (String ip : equipo.getDireccionesIp()) {
                ipInicioComboBox.addItem(ip);
                ipFinComboBox.addItem(ip);
            }
        }
    
        panel.add(ipInicioLabel, gbc);
        gbc.gridx++;
        panel.add(ipInicioComboBox, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(ipFinLabel, gbc);
        gbc.gridx++;
        panel.add(ipFinComboBox, gbc);
    
        ipInicioLabel.setVisible(false);
        ipInicioComboBox.setVisible(false);
        ipFinLabel.setVisible(false);
        ipFinComboBox.setVisible(false);
    
        rangoCheckBox.addActionListener(e -> {
            boolean isSelected = rangoCheckBox.isSelected();
            equipoLabel.setVisible(!isSelected);
            equipoComboBox.setVisible(!isSelected);
            ipInicioLabel.setVisible(isSelected);
            ipInicioComboBox.setVisible(isSelected);
            ipFinLabel.setVisible(isSelected);
            ipFinComboBox.setVisible(isSelected);
        });
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Ping a Equipo o Rango de IPs",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            if (rangoCheckBox.isSelected()) {
                // Realizar ping a un rango de IPs
                String inicioIp = (String) ipInicioComboBox.getSelectedItem();
                String finIp = (String) ipFinComboBox.getSelectedItem();
                if (inicioIp != null && finIp != null) {
                    List<String> pingResults = calculo.realizarPingARango(inicioIp, finIp);
                    JOptionPane.showMessageDialog(this, String.join("\n", pingResults), "Resultados del Ping", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, selecciona un rango de IPs válido.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Realizar ping a un solo equipo
                String selected = (String) equipoComboBox.getSelectedItem();
                if (selected != null) {
                    String direccionIp = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
                    boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);
                    String mensaje = respuestaPing ? "Ping exitoso" : "Ping fallido";
                    JOptionPane.showMessageDialog(this, mensaje + " al equipo con IP: " + direccionIp, "Resultado del Ping", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
     
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
        panel.add(new JLabel("Equipo 1:"), gbc);
        gbc.gridx = 1;
        panel.add(equipo1ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Equipo gateway:"), gbc);
        gbc.gridx = 1;
        panel.add(equipo2ComboBox, gbc);

        JDialog dialog = new JDialog(this, "Verificar conectividad", true);

        JPanel panelInferior = new JPanel();

        verificarButton = new JButton("Verificar conectividad");
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

    private class Handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(calcularVelocidadButton))
                ventanaVelocidad();

            if (e.getSource().equals(pingEquipoButton))
                realizarPingAEquipo();

            if (e.getSource().equals(detectarProblemasButton))
                detectarProblemasConectividad();

            if (e.getSource().equals(calcularButton))
                calcularVelocidad();

            if (e.getSource().equals(verificarButton))
                verificarConectividad();

            if (e.getSource().equals(verMapaEstadoButton))
                verMapaDeEstado();
        }
    }

    private void verMapaDeEstado() {
        // Create a new dialog to show the network map
        JDialog dialog = new JDialog(this, "Mapa de Estado de la Red", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        // Get the graph panel from Calculo
        JPanel graphPanel = calculo.crearMapaDeEstado();
        dialog.add(graphPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

}
