package red.controlador;

import javax.swing.SwingUtilities;
import red.interfaz.VentanaInicial;

/**
 * Clase principal para ejecutar la aplicación de consultas de red.
 * Inicia la interfaz gráfica de la aplicación.
 */
public class AplicacionConsultas {
    
    /**
     * Método principal para lanzar la ventana inicial de la aplicación, que pide al
     * usuario el idioma de la misma. Utiliza SwingUtilities para asegurar que la
     * interfaz gráfica se ejecute en el hilo de despacho de eventos de Swing. Si no
     * se tiene éxito iniciando la configuración, se aborta la aplicación.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        if (Configuracion.getConfiguracion() == null)
        	System.exit(-1);
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaInicial().setVisible(true);
            }
        });
    }
}
