package red.interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import red.controlador.Constantes;
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
		setBounds(Constantes.INICIAL_X, Constantes.INICIAL_Y, Constantes.INICIAL_ANCHO, Constantes.INICIAL_ALTO);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(Constantes.INICIAL_BORDER, Constantes.INICIAL_BORDER, Constantes.INICIAL_BORDER, Constantes.INICIAL_BORDER));

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
		btnEspanolES.addActionListener(e -> seleccion(Locale.of(Constantes.ESPANOL, Constantes.ESPANA)));
		btnEspanolAR.addActionListener(e -> seleccion(Locale.of(Constantes.ESPANOL, Constantes.ARGENTINA)));
		btnInglesEU.addActionListener(e -> seleccion(Locale.of(Constantes.INGLES, Constantes.ESTADOS_UNIDOS)));
		
		// Salir del programa
		btnSalir.addActionListener(e -> {
			dispose();
			System.exit(NORMAL);
		});
	}
	
	private void seleccion(Locale locale) {
		Locale.setDefault(locale);
		SwingUtilities.invokeLater(() -> {
			dispose();
            new VentanaPrincipal().setVisible(true);
        });
	}
}
