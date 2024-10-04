package red.aplicacion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import red.datos.CargarParametros;
import red.datos.Datos;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoCable;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;

public class Aplicacion {

	public static void main(String args[]) {
		// Cargar parametros
		try {
			CargarParametros.parametros();
		} catch (IOException e) {
			System.out.println(e);
			System.err.print("Error al cargar parametros");
			System.exit(-1);
		}

		Map<String, Equipo> equipos = null;
		Map<String, Ubicacion> ubicaciones = null;
		List<Conexion> conexiones = null;
		Map<String, TipoCable> tipoCable = null;
		Map<String, TipoPuerto> tipoPuerto = null;
		Map<String, TipoEquipo> tipoEquipo = null;
		
		try {
			ubicaciones = Datos.cargarUbicaciones(CargarParametros.getArchivoUbicacion());
			tipoCable = Datos.cargarTipoCable(CargarParametros.getArchivoTipoCable());
			tipoPuerto = Datos.cargarTipoPuerto(CargarParametros.getArchivoTipoPuerto());
			tipoEquipo = Datos.cargarTipoEquipo(CargarParametros.getArchivoTipoEquipo());
			equipos = Datos.cargarEquipos(CargarParametros.getArchivoEquipos());
			conexiones = Datos.cargarConexiones(CargarParametros.getArchivoConexion());
		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.err.print("Error al cargar archivos de datos");
			System.exit(-1);
		}
		
		System.out.println ("datos cargados correctamente");
		


	}
	
}
