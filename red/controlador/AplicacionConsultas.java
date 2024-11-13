package red.controlador;

import javax.swing.SwingUtilities;
import red.interfaz.VentanaInicial;

/**
 * Clase principal para ejecutar la aplicación de consultas de red.
 * Inicia la interfaz gráfica de la aplicación.
 */
public class AplicacionConsultas {

    /**
     * Método principal para lanzar la ventana principal de la aplicación.
     * Utiliza SwingUtilities para asegurar que la interfaz gráfica se ejecute
     * en el hilo de despacho de eventos de Swing.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaInicial().setVisible(true);
            }
        });
    }
}
