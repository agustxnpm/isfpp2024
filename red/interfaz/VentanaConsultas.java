package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

import red.excepciones.DireccionIpNoEncontradaException;
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

	public VentanaConsultas(Calculo calculo, Red red, boolean modo) {
		setTitle("Consultas de la Red");
		setSize(650, 400);
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
		equipo1ComboBox.addActionListener(handler);

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
		Equipo equipo1 = red.getEquipos().stream().filter(e -> e.getCodigo().equals(equipo1Codigo)).findFirst()
				.orElse(null);

		Equipo equipo2 = red.getEquipos().stream().filter(e -> e.getCodigo().equals(equipo2Codigo)).findFirst()
				.orElse(null);

		try {
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
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void realizarPingAEquipo() {
		// Crear un diálogo personalizado
		dialog = new JDialog(this, "Ping a Equipo o Rango de IPs", true);
		dialog.setSize(500, 350);
		dialog.setLayout(new GridBagLayout());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		// Checkbox para seleccionar ping a un rango de equipos
		rangoCheckBox = new JCheckBox("Ping a un rango de equipos");
		dialog.add(rangoCheckBox, gbc);

		gbc.gridy++;
		JLabel equipoLabel = new JLabel("Ingresa la IP del equipo:");
		dialog.add(equipoLabel, gbc);

		gbc.gridx++;
		equipoTextField = new JTextField(15); // Campo de texto para la IP de un solo equipo
		dialog.add(equipoTextField, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		JLabel ipInicioLabel = new JLabel("IP de inicio:");
		ipInicioTextField = new JTextField(15); // Campo de texto para la IP de inicio
		JLabel ipFinLabel = new JLabel("IP de fin:");
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
		JLabel cantPingsLabel = new JLabel("Cantidad de pings:");
		dialog.add(cantPingsLabel, gbc);

		gbc.gridx++;
		cantPingsTextField = new JTextField(5); // Campo de texto para la cantidad de pings
		dialog.add(cantPingsTextField, gbc);

		// Botón para realizar el ping
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		pingButton = new JButton("Ping");
		pingButton.addActionListener(handler);
		dialog.add(pingButton, gbc);

		// Mostrar el diálogo
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private void pingReal(String direccionIp, JDialog dialog, int cantPings) {
	        // Crear el JDialog para mostrar los resultados
	        JDialog resultDialog = new JDialog(dialog, "Resultados del Ping Real", false); // No modal
	        JTextArea textArea = new JTextArea(20, 50);
	        textArea.setEditable(false);
	        JScrollPane scrollPane = new JScrollPane(textArea);
	        JButton detenerButton = new JButton("Detener");

	        // Panel de control para el botón
	        JPanel panelControl = new JPanel();
	        panelControl.add(detenerButton);

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
	                calculo.ping(direccionIp, cantPings, textArea, detener);
	                return null;
	            }

	            @Override
	            protected void done() {
	                if (!detener[0]) {
	                    textArea.append("Ping completado.\n");
	                }
	            }
	        };

	        // Ejecutar el SwingWorker
	        worker.execute();
	}


	// Método para hacer ping a un solo equipo, si modo == true, realiza ping
	// simulado, de otro modo ping real
	private void pingAEquipo(String direccionIp, JDialog dialog, int cantPings) {
		try {
			if (!direccionIp.isEmpty()) {
				if (modo) {
					boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);
					String mensaje = respuestaPing ? "Ping exitoso" : "Ping fallido";
					JOptionPane.showMessageDialog(dialog, mensaje + " al equipo con IP: " + direccionIp,
							"Resultado del Ping", JOptionPane.INFORMATION_MESSAGE);
				} else {
					pingReal(direccionIp, dialog, cantPings);
				}

			} else {
				JOptionPane.showMessageDialog(dialog, "Por favor, ingresa una IP válida.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	// Método para hacer ping a un rango de IPs
	private void pingARango(String inicioIp, String finIp, JDialog dialog, int cantPings) {
	    if (!inicioIp.isEmpty() && !finIp.isEmpty()) {
	        // Crear el JDialog para mostrar los resultados
	        JDialog resultDialog = new JDialog(dialog, "Resultados del Ping", false); // false indica que no es modal
	        JTextArea textArea = new JTextArea(20, 50);
	        textArea.setEditable(false);
	        JScrollPane scrollPane = new JScrollPane(textArea);
	        JButton detenerButton = new JButton("Detener");

	        // Panel de control inferior para el botón
	        JPanel panelControl = new JPanel();
	        panelControl.add(detenerButton);

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

	        // Crear el SwingWorker para realizar los pings en segundo plano
	        SwingWorker<Void, String> worker = new SwingWorker<>() {
	            @Override
	            protected Void doInBackground() throws Exception {
	                if (modo) {
	                    // Modo simulación
	                    List<String> pingResults = calculo.realizarPingARango(inicioIp, finIp);
	                    for (String resultado : pingResults) {
	                        if (detener[0]) {
	                            publish("Ping detenido por el usuario.\n");
	                            break;
	                        }
	                        publish(resultado); // Publicar el resultado
	                        Thread.sleep(2000); // Simular la demora de 2 segundos
	                    }
	                } else {
	                    // Modo real
	                    calculo.pingRango(inicioIp, finIp, cantPings, textArea, detener);
	                }
	                return null;
	            }

	            @Override
	            protected void process(List<String> chunks) {
	                for (String resultado : chunks) {
	                    textArea.append(resultado + "\n"); // Mostrar el resultado en el JTextArea
	                }
	            }

	            @Override
	            protected void done() {
	                if (!detener[0]) {
	                    textArea.append("Ping a rango completado.\n");
	                }
	            }
	        };

	        worker.execute(); // Ejecutar la tarea
	    } else {
	        JOptionPane.showMessageDialog(dialog, "Por favor, ingresa un rango de IPs válido.", "Error",
	                JOptionPane.ERROR_MESSAGE);
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

			if (e.getSource().equals(equipo1ComboBox))
				actualizarEquiposConectados();

			if (e.getSource().equals(pingButton)) {

				try {
					cantPings = Integer.parseInt(cantPingsTextField.getText().trim());
				} catch (NumberFormatException ex) {
					cantPings = 0;
				}

				if (rangoCheckBox.isSelected()) {
					// Obtener datos de los campos de texto y realizar ping al rango
					pingARango(ipInicioTextField.getText().trim(), ipFinTextField.getText().trim(), dialog, cantPings);
				} else {
					// Obtener la IP y realizar ping a un solo equipo
					pingAEquipo(equipoTextField.getText().trim(), dialog, cantPings);
				}
			}

		}
	} // fin clase Handler

}
