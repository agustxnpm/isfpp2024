package red.controlador;
/* Clase que contiene valores constantes numéricos o Strings, de clase y finales
 * Estas constantes son invocadas por las otras clases del programa para su uso
 * Ayuda a facilitar la utilización y escalabilidad del programa, evadiendo el
 * hardcodeo de números y Strings
*/
public class Constantes {
	
	// Constantes referidas a idiomas, por siglas oficiales (dos letras en minúscula).
	public static final String ESPANOL = "es";
	public static final String INGLES = "en";
	
	//Constantes referidas a países, por siglas oficiales (dos letras en mayúscula)
	public static final String ESPANA = "ES";
	public static final String ARGENTINA = "AR";
	public static final String ESTADOS_UNIDOS = "US";
	
	// Constantes referidas a los componentes de la ventana inicial
	public static final String ERROR_CONFIG = "Error al inicializar la configuración.";
	public static final String TITULO_INICIAL = "Select the language";
	public static final String INICIAL_ESPANOL_ES = "Español (España)";
	public static final String INICIAL_ESPANOL_AR = "Español (Argentina)";
	public static final String INICIAL_INGLES_EU = "English (U.S.A.)";
	public static final String INICIAL_SALIR = "Exit/Salir";
	
	// Constante con la ruta para las fotos de la ventana inicial
	public static final String RUTA_REDES = "/red/interfaz/red.jpeg";
	public static final String RUTA_UNPSJB = "/red/interfaz/unpsjb.png";
	
	/** Constantes referidas al tipo de acceso a los datos en la clase Factory, ya sea mediante una base de datos
	 * o mediante archivos secuenciales de texto.
	 */
	public static final String FACTORY_BBDD = "factory";
	public static final String FACTORY_SECUENCIAL = "secuencial";
}
