package red.controlador;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

// Clase que gestiona el idioma de la aplicación. Aplica el patrón Singleton.
public class Configuracion {
	
	// Instancia única de la clase y otros atributos
	private static Configuracion configuracion = null;
	//private Coordinador coordinador;
	private Properties prop;
	private ResourceBundle rb;
	private final boolean modoInicial; // Indica si la aplicación inicialmente está en modo simulación o no.

	/**  Método de acceso para la única instancia de la clase. La instanciación de la clase ocurre sólo una vez y por
	 * parte de la ventana principal, siendo la única vez que falle y haga que el presente método retorne null.
	 * @return La instancia única de la configuración, o null en caso de error
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

	// Constructor único privado de la clase
	private Configuracion() throws IOException{
		prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		modoInicial = Boolean.parseBoolean(prop.getProperty("simulacion"));
		rb = ResourceBundle.getBundle(prop.getProperty("labels"));
	}

	public void establecerIdiomaYPais(String idioma, String pais) {
		Locale loc = Locale.of(idioma, pais);
		Locale.setDefault(loc);
		JOptionPane.setDefaultLocale(loc);
		UIManager.getDefaults().setDefaultLocale(loc);
		rb = ResourceBundle.getBundle(prop.getProperty("labels"), loc);
	}

	// Métodos de acceso (getters y setters)
	public ResourceBundle getRb() {
		return rb;
	}
	
	public boolean isSimulacion() {
		return modoInicial;
	}
	
}
