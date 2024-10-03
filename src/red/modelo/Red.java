package red.modelo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import red.excepciones.ConexionRepetidaException;
import red.excepciones.EquipoRepetidoException;
import red.excepciones.UbicacionRepetidaException;

import java.util.ArrayList;

public class Red {

	private List<Conexion> conexiones;
	private List<Equipo> equipos;
	private List<Ubicacion> ubicaciones;
	private String nombre;

	public Red(String nombre) {
		super();
		this.nombre = nombre;
		conexiones = new ArrayList<Conexion>();
		equipos = new ArrayList<Equipo>();
		ubicaciones = new ArrayList<Ubicacion>();
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

	@Override
	public int hashCode() {
		return Objects.hash(conexiones, equipos, nombre, ubicaciones);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Red other = (Red) obj;
		return Objects.equals(conexiones, other.conexiones) && Objects.equals(equipos, other.equipos)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(ubicaciones, other.ubicaciones);
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
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto) throws EquipoRepetidoException {
		Equipo nuevoEquipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
				tipoPuerto);

		// verificar que no se añadan equipos con un mismo codigo (equals de Equipo)
		if (equipos.contains(nuevoEquipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(nuevoEquipo);
		return nuevoEquipo;
	}

	/**
	 * Sobrecarga del metodo agregarEquipo que recibe directamente una instancia de
	 * Equipo
	 * 
	 * @param equipo
	 * @throws EquipoRepetidoException
	 */
	public void agregarEquipo(Equipo equipo) throws EquipoRepetidoException {

		if (equipos.contains(equipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(equipo);
	}

	/**
	 * Sobrecarga del metodo agregarEquipo que recibe un mapa con equipos cargados
	 * 
	 * @param equipos
	 */
	public void agregarEquipo(Map<String, Equipo> equipos) {

		for (Equipo e : equipos.values()) {
			agregarEquipo(e);
		}
	}

	/**
	 * Agrega una ubicacion a la red
	 * 
	 * @param codigo
	 * @param descripcion
	 * @return instancia de Ubicacion con los parametros dados
	 * @throws UbicacionRepetidaException no pueden existir dos ubicaciones con el
	 *                                    mismo codigo por ejemplo "SS"
	 */
	public Ubicacion agregarUbicacion(String codigo, String descripcion) throws UbicacionRepetidaException {

		Ubicacion ubi = new Ubicacion(codigo, descripcion);
		if (ubicaciones.contains(ubi))
			throw new UbicacionRepetidaException("Ubicacion con el mismo codigo ya existe");

		ubicaciones.add(ubi);
		return ubi;
	}

	/**
	 * Sobrecarga del metodo agregarUbicacion que recibe directamente una instancia
	 * de Ubicacion
	 * 
	 * @param ubicacion
	 * @throws UbicacionRepetidaException
	 */
	public void agregarUbicacion(Ubicacion ubicacion) throws UbicacionRepetidaException {
		if (ubicaciones.contains(ubicacion))
			throw new UbicacionRepetidaException("Ubicacion con el mismo codigo ya existe");

		ubicaciones.add(ubicacion);
	}

	/**
	 * Sobrecarga del metodo agregarUbicacion que recibe un mapa con ubicaciones
	 * cargadas
	 * 
	 * @param ubicaciones
	 */
	public void agregarUbicacion(Map<String, Ubicacion> ubicaciones) {
		for (Ubicacion ubi : ubicaciones.values()) {
			agregarUbicacion(ubi);
		}
	}

	/**
	 * Agrega una conexion a la red
	 * 
	 * @param equipo1 -> origen
	 * @param equipo2 -> destino
	 * @param tipoCable
	 * @return instancia de Conexion con los parametros dados
	 * @throws ConexionRepetidaException se lanza una excepcion si equipo1 ya esta
	 *                                   conectado con equipo2
	 */
	public Conexion agregarConexion(Equipo equipo1, Equipo equipo2, TipoCable tipoCable)
			throws ConexionRepetidaException {

		Conexion conex = new Conexion(equipo1, equipo2, tipoCable);
		if (conexiones.contains(conex))
			throw new ConexionRepetidaException("Ya existe una conexion entre equipo 1 y equipo 2");

		conexiones.add(conex);
		return conex;
	}

	public void agregarConexion(List<Conexion> conexiones) {
		for (Conexion c : conexiones) {
			agregarConexion(c.getEquipo1(), c.getEquipo2(), c.getTipoCable());
		}
	}

}
