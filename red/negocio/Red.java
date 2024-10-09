package red.negocio;

import java.util.List;

import red.excepciones.EquipoRepetidoException;
import red.modelo.*;
import red.servicio.ConexionService;
import red.servicio.ConexionServiceImp;
import red.servicio.EquipoService;
import red.servicio.EquipoServiceImp;
import red.servicio.UbicacionService;
import red.servicio.UbicacionServiceImp;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Red {

	private static Red red = null;	

	private String nombre;
	private List<Equipo> equipos;
	private EquipoService equipoService;
	private List<Conexion> conexiones;
	private ConexionService conexionService;
	private List<Ubicacion> ubicaciones;
	private UbicacionService ubicacionService;

	
	public static Red getRed() throws FileNotFoundException {
		if (red == null) {
			red = new Red();
		}
		return red;
	}
	
	public Red() throws FileNotFoundException {
		super();
		equipos = new ArrayList<Equipo>();
		equipoService = new EquipoServiceImp();
		equipos.addAll(equipoService.buscarTodos());
		conexiones = new ArrayList<Conexion>();
		conexionService = new ConexionServiceImp();
		conexiones.addAll(conexionService.buscarTodos());
		ubicaciones = new ArrayList<Ubicacion>();
		ubicacionService = new UbicacionServiceImp();
		ubicaciones.addAll(ubicacionService.buscarTodos());
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Conexion> getConexiones() {
		return conexiones;
	}

	public List<Equipo> getEquipos() {
		return equipos;
	}

	public List<Ubicacion> getUbicaciones() {
		return ubicaciones;
	}

	@Override
	public String toString() {
		return "Red [nombre=" + nombre + "]";
	}

	/**
	 * Agrega un equipo a la red
	 * 
	 * @param codigo
	 * @param modelo
	 * @param marca
	 * @param descripcion
	 * @param ubicacion
	 * @param tipoEquipo
	 * @param cantPuertos
	 * @param tipoPuerto
	 * @return instancia de Equipo con los parametros dados
	 * @throws EquipoRepetidoException no pueden haber dos equipos con el mismo
	 *                                 codigo por ejemplo dos servidores "SE01"
	 */
	public Equipo agregarEquipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto, boolean estado) throws EquipoRepetidoException {
		Equipo nuevoEquipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
				tipoPuerto, estado);

		// verificar que no se añadan equipos con un mismo codigo (equals de Equipo)
		if (equipos.contains(nuevoEquipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(nuevoEquipo);
		equipoService.insertar(nuevoEquipo);
		return nuevoEquipo;
	}

	public void agregarEquipo(Equipo equipo) throws EquipoRepetidoException {

		if (equipos.contains(equipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(equipo);
		equipoService.insertar(equipo);
	}
	
	public void modificarEquipo(Equipo equipo) {
		int pos = equipos.indexOf(equipo);
		equipos.set(pos, equipo);
		equipoService.actualizar(equipo);
	}

	public void borrarEquipo(Equipo equipo) {
		equipos.remove(buscarEquipo(equipo));
		equipoService.borrar(equipo);
	}
	
	public Equipo buscarEquipo(Equipo equipo) {
		int pos = equipos.indexOf(equipo);
		if (pos == -1)
			return null;
		return equipos.get(pos);
	}
	

}


