package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.negocio.Calculo;
import red.negocio.Red;

public class VentanaConsultas extends JFrame {

	private Calculo calculo;
	private Red red;
	private Handler handler; // manejador de eventos

	private JButton calcularVelocidadButton;
	private JButton pingEquipoButton;
	private JButton detectarProblemasButton;
	private JButton calcularButton; // boton para confirmar el calculo de la velocidad
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
		calcularVelocidadButton.setBounds(23, 120, 171, 69);
		calcularVelocidadButton.addActionListener(handler);

		pingEquipoButton = new JButton("Realizar Ping a Equipo");
		pingEquipoButton.setBounds(206, 120, 171, 69);
		pingEquipoButton.addActionListener(handler);

		detectarProblemasButton = new JButton("Detectar Problemas");
		detectarProblemasButton.setBounds(387, 120, 171, 69);
		detectarProblemasButton.addActionListener(handler);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(calcularVelocidadButton);
		panel.add(pingEquipoButton);
		panel.add(detectarProblemasButton);

		getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void ventanaVelocidad() {

		calcularButton = new JButton("Calcular");

		JPanel panelCentral = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;

		/**** componentes para elegir los equipos y calcular su velocidad ****/

		String[] equipoArray = red.getEquipos().stream().map(Equipo::getCodigo).toArray(String[]::new);
		equipo1ComboBox = new JComboBox<>(equipoArray);
		equipo2ComboBox = new JComboBox<>(equipoArray);

		JLabel equipo1Label = new JLabel("Equipo 1");
		JLabel equipo2Label = new JLabel("Equipo 2");

		// Añadir los componentes al panel central
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
			JOptionPane.showMessageDialog(this, "No se encontro ruta entre " + equipo1.getCodigo() + " y " + equipo2.getCodigo(), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "La velocidad maxima entre " + equipo1.getCodigo() + " y "
					+ equipo2.getCodigo() + " es de " + calculo.calcularVelocidadMaxima(ruta) + " Mbps");
		}
		
	}

	// Method to perform ping on a selected team
	private void realizarPingAEquipo() {
		String direccionIp = JOptionPane.showInputDialog(this, "Ingrese la dirección IP del equipo:");

		// Verificar si el usuario presiono "OK" y proporcionó una IP
	    if (direccionIp != null && !direccionIp.trim().isEmpty()) {
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
		gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
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
		JOptionPane.showMessageDialog(this, calculo.verificarConectividad(equipo, gateway));
	}
	
	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
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

		}

	} // end class handler
}
