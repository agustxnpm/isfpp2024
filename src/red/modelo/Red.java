package red.modelo;

import java.util.List;
import java.util.Objects;
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

}
