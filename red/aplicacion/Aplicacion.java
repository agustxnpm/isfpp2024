package red.aplicacion;

import red.datos.Datos;
import red.logica.Red;
import red.modelo.Equipo;
import red.modelo.Conexion;

public class Aplicacion {

	public static void main(String args[]) {

		Datos datos = new Datos();

		// cargar datos
		datos.cargarDatos();

		Red calculo = new Red("Red UNPSJB");

		calculo.agregarUbicacion(datos.getUbicaciones());
		calculo.agregarEquipo(datos.getEquipos());
		calculo.agregarConexion(datos.getConexiones());

		/* listar los equipos en la red */
		System.out.println("Equipos en la red");
		for (Equipo e : calculo.getEquipos()) {
			System.out.println(e);
		}

		System.out.println();

		/* listar las conexiones en la red */
		System.out.println("Conexiones en la red");
		for (Conexion c : calculo.getConexiones()) {
			System.out.println(c);
		}

		System.out.println();

		/* listar las ubicaciones de cada equipo */
		System.out.println("Ubicaciones por equipo");
		for (Equipo e : calculo.getEquipos()) {
			System.out.println("Equipo: " + e.getDescripcion() + " | Ubicacion: " + e.getUbicacion().getDescripcion());
		}

		/* Ejemplos de uso de algunas funcionalidades */
		System.out.println();

		System.out.println("Ruta entre dos equipos");
		/* Ruta de conexion entre dos equipos */
		for (Equipo e : calculo.buscarRuta(calculo.obtenerEquipo("AP09"), calculo.obtenerEquipo("FW02"))) {
			System.out.println(e);
		}

		System.out.println();

		/* Calcular velocidad maxima de una ruta */
		System.out
				.println("Velocidad maxima entre " + calculo.obtenerEquipo("SWAM").getCodigo() + " y "
						+ calculo.obtenerEquipo("APL0").getCodigo() + " es "
						+ calculo.calcularVelocidadMaxima(
								calculo.buscarRuta(calculo.obtenerEquipo("SWAM"), calculo.obtenerEquipo("APL0")))
						+ " mb/s");
		System.out.println();

		/* Ping a un rango de IP */

		calculo.realizarPingARango("192.168.16.2", "192.168.16.13");

		System.out.println();

		/* Mapa de estado de la red */

		calculo.mostrarMapaDeEstado();

		/* Verificar conectividad a internet */
		
		calculo.verificarConectividad(calculo.obtenerEquipo("AP09"), calculo.obtenerEquipo("FW02"));
	}

}
