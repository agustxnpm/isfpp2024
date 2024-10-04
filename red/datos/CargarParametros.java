package red.datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CargarParametros {

	private static String archivoConexion;
	private static String archivoEquipos;
	private static String archivoTipoCable;
	private static String archivoTipoEquipo;
	private static String archivoTipoPuerto;
	private static String archivoUbicacion;

	public static void parametros() throws IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("config.properties");
		// load a properties file
		prop.load(input);
		// get the property value
		archivoConexion = prop.getProperty("conexion");
		archivoEquipos = prop.getProperty("equipo");
		archivoTipoCable = prop.getProperty("tipoCable");
		archivoTipoEquipo = prop.getProperty("tipoEquipo");
		archivoTipoPuerto = prop.getProperty("tipoPuerto");
		archivoUbicacion = prop.getProperty("ubicacion");
	}

	public static String getArchivoConexion() {
		return archivoConexion;
	}

	public static String getArchivoEquipos() {
		return archivoEquipos;
	}

	public static String getArchivoTipoCable() {
		return archivoTipoCable;
	}

	public static String getArchivoTipoEquipo() {
		return archivoTipoEquipo;
	}

	public static String getArchivoTipoPuerto() {
		return archivoTipoPuerto;
	}

	public static String getArchivoUbicacion() {
		return archivoUbicacion;
	}

}
