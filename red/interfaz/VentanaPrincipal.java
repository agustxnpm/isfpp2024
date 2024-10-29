package red.interfaz;

import javax.swing.*;

import red.modelo.Conexion;
import red.modelo.Equipo;
import red.negocio.Calculo;
import red.negocio.Red;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

public class VentanaPrincipal extends JFrame {

	private Calculo calculo;
	private Red red;

	private JMenuBar menuBar;
	private JMenuItem menuEquipos;
	private JMenuItem menuConexiones;
	private JMenuItem menuConsultas;
	private JMenu menuOpciones;

	public VentanaPrincipal() {
		setTitle("Gestión de Red - Menú Principal");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		/*** cargar servicios y calculo ***/
		try {
			calculo = new Calculo();
			red = Red.getRed();
			List<Equipo> equipos = red.getEquipos();
			List<Conexion> conexiones = red.getConexiones();
			calculo.cargarDatos(equipos, conexiones);
		} catch (FileNotFoundException e) {
			System.out.println("Error al cargar los datos.");
			e.printStackTrace();
		}

		inicializarComponentes();

	}

	private void inicializarComponentes() {
		// Crear menú
		menuBar = new JMenuBar();

		menuOpciones = new JMenu("Opciones");
		menuBar.add(menuOpciones);

		menuEquipos = new JMenuItem("Gestionar Equipos");
		menuConexiones = new JMenuItem("Gestionar Conexiones");
		menuConsultas = new JMenuItem("Consultas de Red"); // Nueva opción para VentanaConsultas
		
		menuOpciones.add(menuEquipos);
		menuOpciones.add(menuConexiones);
		menuOpciones.add(menuConsultas);

		setJMenuBar(menuBar);

		Handler handler = new Handler();
		
		menuEquipos.addActionListener(handler);
		menuConexiones.addActionListener(handler);	
		menuConsultas.addActionListener(handler);
	
	}

	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource().equals(menuEquipos)) {
				VentanaEquipos ventanaEquipos = new VentanaEquipos(calculo, red);
				ventanaEquipos.setVisible(true); // Mostrar la ventana de equipos
			}
			
			if (e.getSource().equals(menuConexiones)) {
				VentanaConexiones ventanaConexiones = new VentanaConexiones(calculo, red);
				ventanaConexiones.setVisible(true); // Mostrar la ventana de conexiones
			}
			
			if (e.getSource().equals(menuConsultas)) {
				VentanaConsultas ventanaConsultas = new VentanaConsultas(calculo, red);
				ventanaConsultas.setVisible(true); // Mostrar la ventana de consultas
			}
				
		}

	}

	// Método para lanzar la ventana principal
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VentanaPrincipal ventana = new VentanaPrincipal();
				ventana.setVisible(true);
			}
		});
	}
}
