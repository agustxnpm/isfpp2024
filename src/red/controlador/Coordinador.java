package red.controlador;

import java.util.List;

import red.excepciones.EquipoRepetidoException;
import red.interfaz.Interfaz;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.Ubicacion;
import red.negocio.Calculo;
import red.negocio.Red;

public class Coordinador {

	private Red red;
	private Interfaz interfaz;
	private Calculo calculo;

	public Red getRed() {
		return red;
	}

	public void setRed(Red red) {
		this.red = red;
	}

	public Interfaz getInterfaz() {
		return interfaz;
	}

	public void setInterfaz(Interfaz interfaz) {
		this.interfaz = interfaz;
	}

	public Calculo getCalculo() {
		return calculo;
	}

	public void setCalculo(Calculo calculo) {
		this.calculo = calculo;
	}

	public Equipo buscarEquipo(Equipo equipo) {
		return red.buscarEquipo(equipo);
	}
	
	public Equipo buscarEquipo (String codigo) {
		for (Equipo e : red.getEquipos()) {
			if (e.getCodigo().equals(codigo))
				return e;
		}
		
		return null;
	}
	
	public void agregarEquipo(Equipo equipo) throws EquipoRepetidoException {
	    red.agregarEquipo(equipo);
	}
	
	public List<Equipo> listarEquipos() {
		return red.getEquipos();
	}
	
	public List<Conexion> listarConexiones() {
		return red.getConexiones();
	}
	
	public List<Ubicacion> listarUbicaciones() {
		return red.getUbicaciones();
	}

}
