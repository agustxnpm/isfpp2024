package red.interfaz;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import red.controlador.Constantes;
import red.controlador.Configuracion;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.negocio.Calculo;
import red.negocio.Red;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Calculo calculo;
	private Red red;
	private Handler manejador;
	private Configuracion c;

	private JMenuBar menuBar;
	private JMenu mnPrograma;
	private JMenu mnOpciones;
	private JMenu mnModo;
	private JMenu mnIdiomas;
	private JMenuItem mntmCreditos;
	private JMenuItem mntmSalir;
	private JMenuItem mntmEquipos;
	private JMenuItem mntmConexiones;
	private JMenuItem mntmConsultas;
	private JMenuItem modoSimulacion;
	private JMenuItem modoReal;
	private JMenuItem mntmEspanolAR;
	private JMenuItem mntmEspanolES;
	private JMenuItem mntmInglesEU;
	private boolean modo; // true = modo simulacion, false = modo real
	private JLabel lblModoActual; // Etiqueta para mostrar el modo actual

	public VentanaPrincipal() {
		/* cargar servicios, calculo y configuración */
		try {
			calculo = new Calculo();
			red = Red.getRed();
			List<Equipo> equipos = red.getEquipos();
			List<Conexion> conexiones = red.getConexiones();
			calculo.cargarDatos(equipos, conexiones);
		} catch (FileNotFoundException e) {
			System.err.println(Constantes.ERROR_DATOS);
			e.printStackTrace();
			System.exit(ERROR);
		}
		
		if ((c = Configuracion.getConfiguracion()) == null)
			System.exit(ERROR);
		
		setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_titulo"));
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		inicializarComponentes();

	}

	private void inicializarComponentes() {

		// Crear menú
		menuBar = new JMenuBar();

		mnPrograma = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa"));
		mnOpciones = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones"));
		mnModo = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo"));
		mnIdiomas = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas"));

		menuBar.add(mnPrograma);
		menuBar.add(mnOpciones);
		menuBar.add(mnModo);
		menuBar.add(mnIdiomas);

		modoSimulacion = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo_simulacion"));
		modoReal = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo_real"));
		mnModo.add(modoSimulacion);
		mnModo.add(modoReal);

		mntmCreditos = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_creditos"));
		mntmSalir = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_salir"));
		mnPrograma.add(mntmCreditos);
		mnPrograma.add(mntmSalir);

		mntmEquipos = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_gestionar_equipos"));
		mntmConexiones = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_gestionar_conexiones"));
		mntmConsultas = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_consultas_red")); // Nueva opción para VentanaConsultas
		mnOpciones.add(mntmEquipos);
		mnOpciones.add(mntmConexiones);
		mnOpciones.add(mntmConsultas);

		mntmEspanolAR = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_espanol_ar"));
		mntmEspanolES = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_espanol_es"));
		mntmInglesEU = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_ingles_eu"));
		mnIdiomas.add(mntmEspanolAR);
		mnIdiomas.add(mntmEspanolES);
		mnIdiomas.add(mntmInglesEU);

		setJMenuBar(menuBar);

		manejador = new Handler();

		mntmCreditos.addActionListener(
				e -> JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_creditos_texto"),
						Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_creditos_texto"), JOptionPane.PLAIN_MESSAGE));

		// Acción para cerrar la ventana, enviar mensaje de despedida y finalizar el programa
		mntmSalir.addActionListener(e -> {
			int salir = JOptionPane.showConfirmDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_seguro_salir"),
					Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_salir"), JOptionPane.YES_NO_OPTION);
			if (salir == JOptionPane.YES_OPTION) {
				dispose();
				JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_gracias_usar_programa"),
						Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_despedida"), JOptionPane.PLAIN_MESSAGE);
				System.exit(NORMAL);
			}
		});

		// Acción para abrir la ventana de equipos
		mntmEquipos.addActionListener(manejador);

		// Acción para abrir la ventana de conexiones
		mntmConexiones.addActionListener(manejador);

		// Acción para abrir la ventana de consultas
		mntmConsultas.addActionListener(manejador);

		// Etiqueta para mostrar el modo actual
		lblModoActual = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_simulacion"));
		lblModoActual.setFont(new Font("Dialog", Font.BOLD, 14));

		// Establece el modo inicial del programa
		cambiarModo(Configuracion.getConfiguracion().isSimulacion());

		// Listeners para cambiar el modo
		modoSimulacion.addActionListener(manejador);
		modoReal.addActionListener(manejador);
		
		// Listeners para cambiar el idioma
		mntmEspanolAR.addActionListener(manejador);
		mntmEspanolES.addActionListener(manejador);
		mntmInglesEU.addActionListener(manejador);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		/**
		 * ----------------- Componentes esteticos
		 * ------------------------------------------
		 **/
		JLabel lblISFPP = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_isfpp"));
		lblISFPP.setFont(new Font("Dialog", Font.BOLD, 16));

		JLabel lblPOO = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_poo"));
		lblPOO.setFont(new Font("Dialog", Font.BOLD, 14));

		JLabel lblGestion = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_gestion"));
		lblGestion.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));

		JLabel lblFotoRedes = new JLabel("");
		lblFotoRedes.setIcon(new ImageIcon(VentanaPrincipal.class.getResource(Constantes.RUTA_REDES)));

		JLabel lblUNPSJB = new JLabel("");
		lblUNPSJB.setIcon(new ImageIcon(VentanaPrincipal.class.getResource(Constantes.RUTA_UNPSJB)));

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblModoActual))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(35)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblISFPP)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblFotoRedes)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblUNPSJB)))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addContainerGap(109, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(247, Short.MAX_VALUE)
					.addComponent(lblGestion)
					.addGap(245))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(216)
					.addComponent(lblPOO)
					.addContainerGap(233, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(26)
					.addComponent(lblISFPP)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPOO)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(11)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(99)
									.addComponent(lblFotoRedes))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(69)
									.addComponent(lblUNPSJB)))
							.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
							.addComponent(lblModoActual))
						.addComponent(lblGestion))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		/**
		 * ----------------- Componentes esteticos
		 * ------------------------------------------
		 **/

	}

	private void cambiarModo(boolean esSimulacion) {
		this.modo = esSimulacion;
		if (modo)
			lblModoActual.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_simulacion"));
		else
			lblModoActual.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_real"));
	}

	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource().equals(mntmEquipos)) {
				VentanaEquipos ventanaEquipos = new VentanaEquipos(calculo, red);
				ventanaEquipos.setVisible(true); // Mostrar la ventana de equipos
			}

			if (e.getSource().equals(mntmConexiones)) {
				VentanaConexiones ventanaConexiones = new VentanaConexiones(calculo, red);
				ventanaConexiones.setVisible(true); // Mostrar la ventana de conexiones
			}

			if (e.getSource().equals(mntmConsultas)) {
				VentanaConsultas ventanaConsultas = new VentanaConsultas(calculo, red, modo);
				ventanaConsultas.setVisible(true); // Mostrar la ventana de consultas
			}

			if (e.getSource().equals(modoReal))
				cambiarModo(false);

			if (e.getSource().equals(modoSimulacion))
				cambiarModo(true);

			if (e.getSource().equals(mntmEspanolES)) {
				Configuracion.getConfiguracion().establecerIdiomaYPais(Constantes.ESPANOL, Constantes.ESPANA);
			}
			
			if (e.getSource().equals(mntmEspanolAR)) {
				Configuracion.getConfiguracion().establecerIdiomaYPais(Constantes.ESPANOL, Constantes.ARGENTINA);
			}
			
			if (e.getSource().equals(mntmInglesEU)) {
				Configuracion.getConfiguracion().establecerIdiomaYPais(Constantes.INGLES, Constantes.ESTADOS_UNIDOS);
			}
		}

	}

}
