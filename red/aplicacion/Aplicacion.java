package aplicacion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import datos.CargarParametros;
import datos.Datos;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

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
