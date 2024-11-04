package red.interfaz;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import red.controlador.Constantes;
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

//	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private Calculo calculo;
	private Red red;
	private Handler manejador;
	
	private JMenuBar menuBar;
	private JMenuItem menuCreditos;
	private JMenuItem menuSalir;
	private JMenuItem menuEquipos;
	private JMenuItem menuConexiones;
	private JMenuItem menuConsultas;
	private JMenu mnPrograma;
	private JMenu mnOpciones;

	public VentanaPrincipal() {
        setTitle("Gestión de Red - Menú Principal");
        setSize(617, 411);
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
		
		// Crear menú
        menuBar = new JMenuBar();

        mnPrograma = new JMenu("Programa");
        mnOpciones = new JMenu("Opciones");
        menuBar.add(mnPrograma);
        menuBar.add(mnOpciones);
        
        menuCreditos = new JMenuItem("Créditos");
        menuSalir = new JMenuItem("Salir");
        mnPrograma.add(menuCreditos);
        mnPrograma.add(menuSalir);

        menuEquipos = new JMenuItem("Gestionar Equipos");
        menuConexiones = new JMenuItem("Gestionar Conexiones");
        menuConsultas = new JMenuItem("Consultas de Red"); // Nueva opción para VentanaConsultas
        mnOpciones.add(menuEquipos);
        mnOpciones.add(menuConexiones);
        mnOpciones.add(menuConsultas);

        setJMenuBar(menuBar);
        
        manejador = new Handler();

        menuCreditos.addActionListener(e -> JOptionPane.showMessageDialog(this, Constantes.CREDITOS, "Créditos", JOptionPane.PLAIN_MESSAGE));
        
        // Acción para cerrar la ventana, enviar mensaje de despedida y finalizar el programa
        menuSalir.addActionListener(e -> {
        	int salir = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que querés salir?",
                    "Salida", JOptionPane.YES_NO_OPTION);
            if (salir == JOptionPane.YES_OPTION) {
                dispose();
                JOptionPane.showMessageDialog(this, "¡Gracias por usar el programa!", "Despedida", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        });

        // Acción para abrir la ventana de equipos
        menuEquipos.addActionListener(manejador);

        // Acción para abrir la ventana de conexiones
        menuConexiones.addActionListener(manejador);
        
        // Acción para abrir la ventana de consultas
        menuConsultas.addActionListener(manejador);
        
        contentPane = new JPanel();
    	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    	setContentPane(contentPane);
    	
    	JLabel lblISFPP = new JLabel("Instancia Supervisada de Formación Práctica y Profesional");
    	lblISFPP.setFont(new Font("Dialog", Font.BOLD, 16));
    	
    	JLabel lblPOO = new JLabel("Programación Orientada a Objetos");
    	lblPOO.setFont(new Font("Dialog", Font.BOLD, 14));
    	
    	JLabel lblGestion = new JLabel("Gestión de redes de computadoras");
    	lblGestion.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
    	
    	JLabel lblFotoRedes = new JLabel("");
    	lblFotoRedes.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/red/interfaz/red.jpeg")));
    	
    	JLabel lblUNPSJB = new JLabel("");
    	lblUNPSJB.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/red/interfaz/unpsjb.png")));
    	GroupLayout gl_contentPane = new GroupLayout(contentPane);
    	gl_contentPane.setHorizontalGroup(
    		gl_contentPane.createParallelGroup(Alignment.LEADING)
    			.addGroup(gl_contentPane.createSequentialGroup()
    				.addGap(35)
    				.addComponent(lblFotoRedes)
    				.addPreferredGap(ComponentPlacement.UNRELATED)
    				.addComponent(lblUNPSJB)
    				.addContainerGap(26, Short.MAX_VALUE))
    			.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
    				.addContainerGap(79, Short.MAX_VALUE)
    				.addComponent(lblISFPP)
    				.addGap(60))
    			.addGroup(gl_contentPane.createSequentialGroup()
    				.addGap(180)
    				.addComponent(lblPOO)
    				.addContainerGap(186, Short.MAX_VALUE))
    			.addGroup(gl_contentPane.createSequentialGroup()
    				.addGap(200)
    				.addComponent(lblGestion)
    				.addContainerGap(209, Short.MAX_VALUE))
    	);
    	gl_contentPane.setVerticalGroup(
    		gl_contentPane.createParallelGroup(Alignment.LEADING)
    			.addGroup(gl_contentPane.createSequentialGroup()
    				.addComponent(lblISFPP)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lblPOO)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(lblGestion)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
    					.addGroup(gl_contentPane.createSequentialGroup()
    						.addGap(38)
    						.addComponent(lblUNPSJB))
    					.addGroup(gl_contentPane.createSequentialGroup()
    						.addGap(65)
    						.addComponent(lblFotoRedes)))
    				.addContainerGap(44, Short.MAX_VALUE))
    	);
    	contentPane.setLayout(gl_contentPane);
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
