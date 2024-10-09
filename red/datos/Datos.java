//no modificar esta clase 

package red.datos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import red.dao.ConexionDAO;
import red.dao.EquipoDAO;
import red.dao.TipoCableDAO;
import red.dao.TipoEquipoDAO;
import red.dao.TipoPuertoDAO;
import red.dao.UbicacionDAO;
import red.dao.secuencial.ConexionDAOImp;
import red.dao.secuencial.EquipoDAOImp;
import red.dao.secuencial.TipoCableDAOImp;
import red.dao.secuencial.TipoEquipoDAOImp;
import red.dao.secuencial.TipoPuertoDAOImp;
import red.dao.secuencial.UbicacionDAOImp;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoCable;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;

public class Datos {

	private Map<String, Equipo> equipos;
	private List<Conexion> conexiones;
	private Map<String, TipoCable> tipoCable;
	private Map<String, TipoPuerto> tipoPuerto;
	private Map<String, TipoEquipo> tipoEquipo;
	private Map<String, Ubicacion> ubicaciones;

	public void cargarDatos() {

		try {
			CargarParametros.parametros();
		} catch (IOException e) {
			System.out.println(e);
			System.err.print("Error al cargar parametros");
			System.exit(-1);
		}

		try {
			// Cargar TipoEquipo
			TipoEquipoDAO tipoEquipoDAO = new TipoEquipoDAOImp();
			tipoEquipo = tipoEquipoDAO.cargar(CargarParametros.getArchivoTipoEquipo());

			// Cargar Ubicaciones
			UbicacionDAO ubicacionDAO = new UbicacionDAOImp();
			ubicaciones = ubicacionDAO.cargar(CargarParametros.getArchivoUbicacion());

			// Cargar TipoPuerto
			TipoPuertoDAO tipoPuertoDAO = new TipoPuertoDAOImp();
			tipoPuerto = tipoPuertoDAO.cargar(CargarParametros.getArchivoTipoPuerto());

			// Cargar TipoCable
			TipoCableDAO tipoCableDAO = new TipoCableDAOImp();
			tipoCable = tipoCableDAO.cargar(CargarParametros.getArchivoTipoCable());

			// Cargar Equipos
			EquipoDAO equipoDAO = new EquipoDAOImp(tipoEquipo, ubicaciones, tipoPuerto);
			equipos = equipoDAO.cargar(CargarParametros.getArchivoEquipos());

			// Cargar Conexiones
			ConexionDAO conexionDAO = new ConexionDAOImp(equipos, tipoCable);
			conexiones = conexionDAO.cargar(CargarParametros.getArchivoConexion());
		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.err.print("Error al cargar archivos de datos");
			System.exit(-1);
		}
	}

	public Map<String, Equipo> getEquipos() {
		return equipos;
	}

	public List<Conexion> getConexiones() {
		return conexiones;
	}

	public Map<String, Ubicacion> getUbicaciones() {
		return ubicaciones;
	}

}
