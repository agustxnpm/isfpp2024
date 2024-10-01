package red.modelo;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class Equipo {

	private String codigo;
	private String modelo;
	private String marca;
	private String descripcion;
	private List<String> direccionesIp;
	private Ubicacion ubicacion;
	private TipoEquipo tipoEquipo;
	private List<Puerto> puertos;

	public Equipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto) {
		super();
		this.codigo = codigo;
		this.modelo = modelo;
		this.marca = marca;
		this.descripcion = descripcion;
		this.ubicacion = ubicacion;
		this.tipoEquipo = tipoEquipo;
		direccionesIp = new ArrayList<String>();
		puertos = new ArrayList<Puerto>();
		agregarPuerto(cantPuertos, tipoPuerto);

	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public TipoEquipo getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(TipoEquipo tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
	}

	public List<String> getDireccionesIp() {
		return direccionesIp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipo other = (Equipo) obj;
		return Objects.equals(codigo, other.codigo);
	}

	@Override
	public String toString() {
		return "Equipo [codigo=" + codigo + ", descripcion=" + descripcion + "]";
	}

	public void agregarPuerto(int cantPuertos, TipoPuerto tipoPuerto) throws IllegalArgumentException {

		if (cantPuertos <= 0)
			throw new IllegalArgumentException("Ingrese un numero mayor que 0");

		Puerto puerto = new Puerto(cantPuertos, tipoPuerto);
		if (puertos.contains(puerto)) {
			
	        // Obtener el indice del puerto existente
			int index = puertos.indexOf(puerto);
			
	        // Obtener la instancia de Puerto en ese indice
			Puerto puertoExistente = puertos.get(index);
			
	        // Sumar la cantidad de puertos de la nueva instancia a la existente
			puertoExistente.setCantidadPuertos(puertoExistente.getCantidadPuertos() + puerto.getCantidadPuertos());
			
	        // Actualizar la lista de puertos con el puerto modificado
			puertos.set(index, puertoExistente);
			return;
		}

		puertos.add(puerto);

	}

	public void agregarIp(String ip) {
		// implementar...
	}

	// ---------------------- clase interna puerto
	// ------------------------------------//
	private class Puerto {

		private int cantidadPuertos;
		private TipoPuerto tipoPuerto;

		public Puerto(int cantidadPuertos, TipoPuerto tipoPuerto) {
			super();
			this.cantidadPuertos = cantidadPuertos;
			this.tipoPuerto = tipoPuerto;
		}

		public int getCantidadPuertos() {
			return cantidadPuertos;
		}

		public void setCantidadPuertos(int cantidadPuertos) {
			this.cantidadPuertos = cantidadPuertos;
		}

		public TipoPuerto getTipoPuerto() {
			return tipoPuerto;
		}

		public void setTipoPuerto(TipoPuerto tipoPuerto) {
			this.tipoPuerto = tipoPuerto;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(tipoPuerto);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Puerto other = (Puerto) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(tipoPuerto, other.tipoPuerto);
		}

		private Equipo getEnclosingInstance() {
			return Equipo.this;
		}
	}
	// ---------------------- fin clase interna puerto
	// ---------------------------------//

}
