package red.modelo;

import java.util.List;
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

	public Equipo agregarEquipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto) throws EquipoRepetidoException {
	    Equipo nuevoEquipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos, tipoPuerto);
	    
	    // verificar que no se añadan equipos con un mismo codigo (equals de Equipo)
	    if (equipos.contains(nuevoEquipo))
	    	throw new EquipoRepetidoException("El equipo ya existe en la red");
	    
	    equipos.add(nuevoEquipo);
	    return nuevoEquipo;
	}
	
	public void agregarEquipo(Equipo equipo) {
		
		  if (equipos.contains(equipo))
		    	throw new EquipoRepetidoException("El equipo ya existe en la red");
		  
	    equipos.add(equipo);
	}
	
	public void agregarEquipo (List<Equipo> equipos) {
		
		for (Equipo e : equipos) {
			agregarEquipo(e);
		}
	}

	public Ubicacion agregarUbicacion(String codigo, String descripcion) throws UbicacionRepetidaException {
		
		Ubicacion ubi = new Ubicacion(codigo, descripcion);
		if (ubicaciones.contains(ubi))
			throw new UbicacionRepetidaException("Ubicacion con el mismo codigo ya existe");
		
		ubicaciones.add(ubi);
		return ubi;
	}
	
	public void agregarUbicacion(Ubicacion ubicacion) throws UbicacionRepetidaException {
		if (ubicaciones.contains(ubicacion))
			throw new UbicacionRepetidaException("Ubicacion con el mismo codigo ya existe");
		
		ubicaciones.add(ubicacion);
	}
	
	public void agregarUbicacion (List<Ubicacion> ubicaciones) {
		for (Ubicacion ubi : ubicaciones) {
			agregarUbicacion(ubi);
		}
	}
	
	public Conexion agregarConexion (Equipo equipo1, Equipo equipo2) throws ConexionRepetidaException{
		
		Conexion conex = new Conexion(equipo1, equipo2);
		if (conexiones.contains(conex))
			throw new ConexionRepetidaException("Ya existe una conexion entre equipo 1 y equipo 2");
		
		conexiones.add(conex);
		return conex;
	}
	
	public void agregarConexion (List<Conexion> conexiones) {
		for (Conexion c : conexiones) {
			agregarConexion(c.getEquipo1(), c.getEquipo2());
		}
	}
	
	
}
