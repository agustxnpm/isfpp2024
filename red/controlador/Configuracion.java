package red.controlador;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Clase que gestiona la configuración de la aplicación, incluyendo el idioma y 
 * el modo de funcionamiento. Aplica el patrón Singleton.
 */
public class Configuracion {
    
    // Instancia única de la clase y otros atributos
    private static Configuracion configuracion = null;
    private Properties prop;
    private ResourceBundle rb;
    private final boolean modoInicial; // Indica si la aplicación inicialmente está en modo simulación o no.

    /**
     * Método de acceso para obtener la única instancia de la clase. 
     * La instanciación de la clase ocurre sólo una vez. 
     *
     * @return La instancia única de la configuración, o null en caso de error.
     */
    public static Configuracion getConfiguracion() {
        if (configuracion == null)
            try {
                configuracion = new Configuracion();
            } catch(IOException e) {
                System.err.println("Error al inicializar la configuración.");
                e.printStackTrace();
            }
        return configuracion;
    }

    /**
     * Constructor privado de la clase para asegurar que solo haya una instancia.
     *
     * @throws IOException Si ocurre un error al cargar el archivo de propiedades.
     */
    private Configuracion() throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream("ISFPP2024\\config.properties"));
        modoInicial = Boolean.parseBoolean(prop.getProperty("simulacion"));
        rb = ResourceBundle.getBundle(prop.getProperty("labels"));
    }

    /**
     * Establece el idioma y el país para la aplicación.
     *
     * @param idioma Código del idioma (por ejemplo, "es" para español).
     * @param pais Código del país (por ejemplo, "AR" para Argentina).
     */
    public void establecerIdiomaYPais(String idioma, String pais) {
        Locale loc = Locale.of(idioma, pais);
        Locale.setDefault(loc);
        JOptionPane.setDefaultLocale(loc);
        UIManager.getDefaults().setDefaultLocale(loc);
        rb = ResourceBundle.getBundle(prop.getProperty("labels"), loc);
    }

    /**
     * Obtiene el bundle de recursos actual para la aplicación.
     *
     * @return El bundle de recursos.
     */
    public ResourceBundle getRb() {
        return rb;
    }
    
    /**
     * Verifica si la aplicación está en modo simulación.
     *
     * @return true si está en modo simulación, false de lo contrario.
     */
    public boolean isSimulacion() {
        return modoInicial;
    }
}
