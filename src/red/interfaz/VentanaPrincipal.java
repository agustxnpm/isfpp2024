package red.interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import red.controlador.Constantes;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public VentanaPrincipal() {
        setTitle("Gestión de Red - Menú Principal");
        setSize(617, 411);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear menú
        JMenuBar menuBar = new JMenuBar();

        JMenu mnPrograma = new JMenu("Programa");
        JMenu mnOpciones = new JMenu("Opciones");
        menuBar.add(mnPrograma);
        menuBar.add(mnOpciones);
        
        JMenuItem menuCreditos = new JMenuItem("Créditos");
        JMenuItem menuSalir = new JMenuItem("Salir");
        mnPrograma.add(menuCreditos);
        mnPrograma.add(menuSalir);

        JMenuItem menuEquipos = new JMenuItem("Gestionar Equipos");
        JMenuItem menuConexiones = new JMenuItem("Gestionar Conexiones");
        JMenuItem menuConsultas = new JMenuItem("Consultas de Red"); // Nueva opción para VentanaConsultas
        mnOpciones.add(menuEquipos);
        mnOpciones.add(menuConexiones);
        mnOpciones.add(menuConsultas);

        setJMenuBar(menuBar);
        
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
        menuEquipos.addActionListener(e -> {
            VentanaEquipos ventanaEquipos = new VentanaEquipos();
            ventanaEquipos.setVisible(true);  // Mostrar la ventana de equipos
        });

        // Acción para abrir la ventana de conexiones
        menuConexiones.addActionListener(e -> {
            VentanaConexiones ventanaConexiones = new VentanaConexiones();
            ventanaConexiones.setVisible(true);  // Mostrar la ventana de conexiones
        });
        
        // Acción para abrir la ventana de consultas
        menuConsultas.addActionListener(e -> {
            VentanaConsultas ventanaConsultas = new VentanaConsultas();
        	ventanaConsultas.setVisible(true);  // Mostrar la ventana de consultas
        });
        
        contentPane = new JPanel();
    	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    	setContentPane(contentPane);
    	
    	JLabel lblISFPP = new JLabel("Instancia Supervisada de Formación Práctica y Profesional");
    	lblISFPP.setFont(new Font("Dialog", Font.BOLD, 16));
    	
    	JLabel lblPOO = new JLabel("Programación Orientada a Objetos");
    	lblPOO.setFont(new Font("Dialog", Font.BOLD, 14));
    	
    	JLabel lblGestion = new JLabel("Gestión de redes de computadoras");
    	lblGestion.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
    	
    	JLabel lblFoto = new JLabel("");
    	lblFoto.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/red/interfaz/red.jpeg")));
    	
    	JLabel lblUNPSJB = new JLabel("");
    	lblUNPSJB.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/red/interfaz/unpsjb.png")));
    	GroupLayout gl_contentPane = new GroupLayout(contentPane);
    	gl_contentPane.setHorizontalGroup(
    		gl_contentPane.createParallelGroup(Alignment.LEADING)
    			.addGroup(gl_contentPane.createSequentialGroup()
    				.addGap(35)
    				.addComponent(lblFoto)
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
    						.addComponent(lblFoto)))
    				.addContainerGap(44, Short.MAX_VALUE))
    	);
    	contentPane.setLayout(gl_contentPane);
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
