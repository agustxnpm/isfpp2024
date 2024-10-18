package red.interfaz;

import java.util.List;
import java.util.Scanner;

import red.controlador.Coordinador;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;

public class Interfaz {

	private Coordinador coordinador;
	private Scanner scanner = new Scanner(System.in);

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public void mostrarEquipos(List<Equipo> equipos) {
		for (Equipo equipo : equipos) {
			System.out.println(equipo);
		}
	}

	public void mostrarConexiones(List<Conexion> conexiones) {
		for (Conexion conexion : conexiones) {
			System.out.println(conexion);
		}
	}

	public void mostrarUbicaciones(List<Ubicacion> ubicaciones) {
		for (Ubicacion ubicacion : ubicaciones) {
			System.out.println(ubicacion);
		}
	}

	public void mostrarUbicacionesPorEquipo(List<Equipo> eq) {
		for (Equipo e : eq) {
			System.out.println("Equipo: " + e.getDescripcion() + " -> Ubicacion: " + e.getUbicacion().getDescripcion());
		}
	}

	public Equipo solicitarDatosEquipo() {
		System.out.println("Ingrese los datos del nuevo equipo:");

		System.out.print("Código: ");
		String codigo = scanner.nextLine();

		System.out.print("Modelo: ");
		String modelo = scanner.nextLine();

		System.out.print("Marca: ");
		String marca = scanner.nextLine();

		System.out.print("Descripción: ");
		String descripcion = scanner.nextLine();

		System.out.print("Ubicacion codigo: ");
		String ubiCod = scanner.nextLine();

		System.out.print("Ubicacion descripcion: ");
		String ubiDesc = scanner.nextLine();

		System.out.print("TipoEquipo codigo: ");
		String teCod = scanner.nextLine();

		System.out.print("TipoEquipo descripcion: ");
		String teDesc = scanner.nextLine();

		System.out.print("TipoPuerto codigo: ");
		String tpCod = scanner.nextLine();

		System.out.print("TipoPuerto descripcion: ");
		String tpDesc = scanner.nextLine();

		System.out.print("TipoPuerto velocidad: ");
		String tpVel = scanner.nextLine();

		System.out.print("TipoPuerto velocidad: ");
		String cantPuerto = scanner.nextLine();

		Ubicacion ubicacion = new Ubicacion(ubiCod, ubiDesc);
		TipoEquipo tipoEquipo = new TipoEquipo(teCod, teDesc);
		TipoPuerto tipoPuerto = new TipoPuerto(tpCod, tpDesc, Integer.parseInt(tpVel));
		boolean estado = true;

		return new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, Integer.parseInt(cantPuerto),
				tipoPuerto, estado);
	}

	/**
	 * Muestra el estado actual de todos los equipos conectados a la red.
	 */
	public void mostrarMapaDeEstado(List<Equipo> eq) {
		System.out.println("Mapa del estado actual de la red:");
		for (Equipo equipo : eq) {
			System.out.println(
					equipo.getCodigo() + " - IPs: " + equipo.getDireccionesIp() + " - Estado: " + equipo.isEstado());
		}
	}

	public void mostrarError(String mensaje) {
		System.err.println("Error: " + mensaje);
	}

	public void mostrarExito(String mensaje) {
		System.out.println("Éxito: " + mensaje);
	}

	public String solicitarCodigoEquipo() {
		System.out.print("Ingrese el código del equipo: ");
		return scanner.nextLine();
	}

	public String solicitarOpcion() {
		System.out.print("Ingrese su opción: ");
		return scanner.nextLine();
	}
}
