package red.controlador;
/* Clase que contiene valores constantes numéricos o Strings, de clase y finales
 * Estas constantes son invocadas por las otras clases del programa para su uso
 * Ayuda a facilitar la utilización y escalabilidad del programa, evadiendo el
 * hardcodeo de números y Strings
*/
public class Constantes {
	
	public static final String ERROR_DATOS = "Error al cargar los datos.";
	
	// Constantes referidas a idiomas, por siglas oficiales (dos letras en minúscula).
	public static final String ESPANOL = "es";
	public static final String INGLES = "en";
	
	//Constantes referidas a países, por siglas oficiales (dos letras en mayúscula)
	public static final String ESPANA = "ES";
	public static final String ARGENTINA = "AR";
	public static final String ESTADOS_UNIDOS = "US";
	
	// Constantes referidas a los componentes de la ventana inicial
	public static final int INICIAL_BORDER = 5;
	public static final int INICIAL_X = 100;
	public static final int INICIAL_Y = 100;
	public static final int INICIAL_ANCHO = 450;
	public static final int INICIAL_ALTO = 240;
	public static final String TITULO_INICIAL = "Select the language";
	public static final String INICIAL_ESPANOL_ES = "Español (España)";
	public static final String INICIAL_ESPANOL_AR = "Español (Argentina)";
	public static final String INICIAL_INGLES_EU = "Inglés (Estados Unidos)";
	public static final String INICIAL_SALIR = "Exit/Salir";
	
	// Constante con la ruta para las fotos de la ventana inicial
	public static final String RUTA_REDES = "/red/interfaz/red.jpeg";
	public static final String RUTA_UNPSJB = "/red/interfaz/unpsjb.png";
}
