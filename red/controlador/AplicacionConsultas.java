package red.controlador;

import javax.swing.SwingUtilities;
import red.interfaz.VentanaPrincipal;


public class AplicacionConsultas {

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
