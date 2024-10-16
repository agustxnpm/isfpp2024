package red.modelo;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import red.excepciones.DireccionIpRepetidaException;

public class Equipo {

	private String codigo;
	private String modelo;
	private String marca;
	private String descripcion;
	private List<String> direccionesIp;
	private Ubicacion ubicacion;
	private TipoEquipo tipoEquipo;
	private List<Puerto> puertos;
	private boolean estado; // Estado del equipo (Activo/Inactivo)

	public Equipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto, boolean estado) {
		super();
		this.codigo = codigo;
		this.modelo = (modelo == null || modelo.isEmpty()) ? "Modelo desconocido" : modelo;
		this.marca = (marca == null || marca.isEmpty()) ? "Marca desconocida" : marca;
		this.descripcion = (descripcion == null || descripcion.isEmpty()) ? "Sin descripción" : descripcion;
		this.ubicacion = ubicacion;
		this.tipoEquipo = tipoEquipo;
		direccionesIp = new ArrayList<String>();
		puertos = new ArrayList<Puerto>();
		agregarPuerto(cantPuertos, tipoPuerto);
		this.estado = estado;
	}

	/**
	 * Obtiene la velocidad máxima del equipo basada en los puertos disponibles. La
	 * velocidad máxima está limitada por el puerto más lento.
	 * 
	 * @return La velocidad máxima del equipo en Mbps.
	 */
	public int getVelocidadMaxima() {
		int velocidadMaxima = Integer.MAX_VALUE; // Inicializamos con un valor muy alto
		// Iteramos sobre los puertos del equipo para encontrar el puerto más lento
		for (Puerto puerto : puertos)
			velocidadMaxima = Math.min(velocidadMaxima, puerto.getTipoPuerto().getVelocidad());
		return velocidadMaxima; // Retornamos la velocidad más baja
	}

	/**
	 * Simula la respuesta del equipo a un ping. Un equipo responde al ping si está
	 * en estado ACTIVO.
	 * 
	 * @return true si el equipo está ACTIVO, false si está INACTIVO.
	 */
	public boolean realizarPing() {
		return estado; // El ping es exitoso si el equipo está ACTIVO
	}
	
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
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
			throw new IllegalArgumentException("El equipo debe tener al menos un puerto");

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

	public String getPuertosInfo() {
	    StringBuilder puertosInfo = new StringBuilder();
	    for (Puerto p : puertos) {  
	        puertosInfo.append(p.getTipoPuerto().getCodigo())   // Código del tipo de puerto
	                    .append(",")
	                    .append(p.getCantidadPuertos())               // Cantidad de puertos
	                    .append(";");
	    }

	    // Eliminar el ultimo punto y coma para que no haya un separador extra
	    if (puertosInfo.length() > 0) {
	        puertosInfo.setLength(puertosInfo.length() - 1);
	    }

	    return puertosInfo.toString();
	}
	public void agregarIp(String ip) throws DireccionIpRepetidaException {
		// Expresion regular para validar IPv4
		String ipv4Regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		Pattern pattern = Pattern.compile(ipv4Regex);
		Matcher matcher = pattern.matcher(ip);

		// Validar si la IP tiene formato valido
		if (!matcher.matches()) {
			throw new IllegalArgumentException("La direccion IP no es valida");
		}

		// Si ya existe la IP dentro del equipo
		if (direccionesIp.contains(ip))
			throw new DireccionIpRepetidaException("La direccion ip ya existe");

		direccionesIp.add(ip);

	}

	// ----------------------clase interna
	// puerto------------------------------------//
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

		@Override
		public String toString() {
			return "Puerto [cantidadPuertos=" + cantidadPuertos + ", tipoPuerto=" + tipoPuerto + "]";
		}
	}
	// ---------------------- fin clase interna puerto
	// ---------------------------------//

}
