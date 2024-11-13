package red.controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import red.modelo.Conexion;
import red.modelo.Equipo;

// Clase que gestiona el idioma de la aplicación. Aplica el patrón Singleton.
public class Configuracion {
	
	// Instancia única de la clase y otros atributos
	private static Configuracion configuracion = null;
	//private Coordinador coordinador;
	private Properties prop;
	private ResourceBundle rb;
	private String idioma;
	private String pais;
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
		//Locale.setDefault(Locale.of(prop.getProperty("language"), prop.getProperty("country")));
		rb = ResourceBundle.getBundle(prop.getProperty("labels"));
	}

	public void establecerIdiomaYPais(String idioma, String pais) {
		Locale.setDefault(Locale.of(idioma, pais));
		rb = ResourceBundle.getBundle(prop.getProperty("labels"));
	}

	// Métodos de acceso (getters y setters)
	public ResourceBundle getRb() {
		return rb;
	}
	
	public String getIdioma() {
		return idioma;
	}
	
	public String getPais() {
		return pais;
	}

	/*public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}*/
	
	public boolean isSimulacion() {
		return modoInicial;
	}
	
}
