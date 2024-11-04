package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import red.modelo.Equipo;
import red.negocio.Calculo;
import red.negocio.Red;


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

	public VentanaConsultas(Calculo calculo, Red red) {
		setTitle("Consultas de la Red");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.calculo = calculo;
		this.red = red;

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
		equipo2ComboBox = new JComboBox<>(equipoArray);

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

		calcularButton.addActionListener(handler);

		dialog.add(panelCentral);
		dialog.add(panelInferior, BorderLayout.SOUTH);
		dialog.setSize(500, 300);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private void calcularVelocidad() {
		String equipo1Codigo = (String) equipo1ComboBox.getSelectedItem();
		String equipo2Codigo = (String) equipo2ComboBox.getSelectedItem();

		Equipo equipo1 = red.buscarEquipoPorCodigo(equipo1Codigo);
		Equipo equipo2 = red.buscarEquipoPorCodigo(equipo2Codigo);

		List<Equipo> ruta = calculo.buscarRuta(equipo1, equipo2);

		if (ruta == null) {
			JOptionPane.showMessageDialog(this,
					"No se encontró ruta entre " + equipo1.getCodigo() + " y " + equipo2.getCodigo(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "La velocidad máxima entre " + equipo1.getCodigo() + " y "
					+ equipo2.getCodigo() + " es de " + calculo.calcularVelocidadMaxima(ruta) + " Mbps");
		}
	}
	private void realizarPingAEquipo() {
		JComboBox<String> equipoIpComboBox = new JComboBox<>();
		for (Equipo equipo : red.getEquipos()) {
			for (String ip : equipo.getDireccionesIp()) {
				equipoIpComboBox.addItem(equipo.getCodigo() + " (" + ip + ")");
			}
		}

		int result = JOptionPane.showConfirmDialog(this, equipoIpComboBox, "Seleccione equipo y dirección IP",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION && equipoIpComboBox.getSelectedItem() != null) {
			String selected = (String) equipoIpComboBox.getSelectedItem();
			String direccionIp = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));

			boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);

			if (respuestaPing) {
				JOptionPane.showMessageDialog(this, "Ping exitoso al equipo con IP: " + direccionIp);
			} else {
				JOptionPane.showMessageDialog(this, "Ping fallido o equipo no encontrado.");
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

		String resultado = calculo.verificarConectividad(equipo, gateway);
		JOptionPane.showMessageDialog(this, resultado);
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

