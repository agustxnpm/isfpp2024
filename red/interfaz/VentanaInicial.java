package red.interfaz;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import red.controlador.Constantes;
import red.factory.Factory;
import red.controlador.Configuracion;

// Clase para que el usuario seleccione el idioma inicialmente antes de abrir la aplicación
public class VentanaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 *  Ventana para seleccionar el idioma
	 */
	public VentanaInicial() {
		setTitle(Constantes.TITULO_INICIAL);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnEspanolES = new JButton(Constantes.INICIAL_ESPANOL_ES);
		btnEspanolES.setBounds(46, 36, 362, 27);
		contentPane.add(btnEspanolES);
		
		JButton btnEspanolAR = new JButton(Constantes.INICIAL_ESPANOL_AR);
		btnEspanolAR.setBounds(46, 75, 362, 27);
		contentPane.add(btnEspanolAR);
		
		JButton btnInglesEU = new JButton(Constantes.INICIAL_INGLES_EU);
		btnInglesEU.setBounds(46, 114, 362, 27);
		contentPane.add(btnInglesEU);
		
		JButton btnSalir = new JButton(Constantes.INICIAL_SALIR);
		btnSalir.setBounds(46, 153, 362, 27);
		contentPane.add(btnSalir);
		
		// Listeners para configurar el idioma y país de la aplicación
		btnEspanolES.addActionListener(e -> seleccion(Constantes.ESPANOL, Constantes.ESPANA));
		btnEspanolAR.addActionListener(e -> seleccion(Constantes.ESPANOL, Constantes.ARGENTINA));
		btnInglesEU.addActionListener(e -> seleccion(Constantes.INGLES, Constantes.ESTADOS_UNIDOS));
		
		// Salir del programa
		btnSalir.addActionListener(e -> {
			int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro?/Sure?", "Exit", JOptionPane.YES_NO_OPTION);
			if (confirmacion == JOptionPane.YES_OPTION) {
				dispose();
				JOptionPane.showMessageDialog(this, "¡Adiós!", "Goodbye!", JOptionPane.PLAIN_MESSAGE);
				System.exit(NORMAL);
			}
		});
	}
	
	/** Verifica que la configuración pueda instanciarse correctamente, establece el idioma y país configurados, abre la ventana principal, y
	 * cierra esta ventana. En caso de que se capture una excepción al crear la instancia de Configuración, la excepción queda indicada en la
	 * consola, y la configuración permanece nula. Por lo tanto, el método, tras la comparación, cierra el programa.
	 * @param idioma: Idioma que asumirá el programa en principio
	 * @param pais: País indicado por el usuario
	 */
	private void seleccion(String idioma, String pais) {
		Configuracion.getConfiguracion().establecerIdiomaYPais(idioma, pais);
		dispose();
		int decision = JOptionPane.showConfirmDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaInicial_opcion_bbdd_secuenciales"),
				Configuracion.getConfiguracion().getRb().getString("VentanaInicial_tipo_acceso_datos"), JOptionPane.YES_NO_CANCEL_OPTION);
		if (decision == JOptionPane.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaInicial_gracias_todos_modos"),
					Configuracion.getConfiguracion().getRb().getString("VentanaInicial_despedida"), JOptionPane.INFORMATION_MESSAGE);
			System.exit(NORMAL);
		} else {
			Factory.setModo(decision == JOptionPane.YES_OPTION);
			SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
		}
	}
}
