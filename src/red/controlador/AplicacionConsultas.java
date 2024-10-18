package red.controlador;

import java.io.FileNotFoundException;
import java.util.List;

import red.excepciones.EquipoRepetidoException;
import red.interfaz.Interfaz;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.Ubicacion;
import red.negocio.Calculo;
import red.negocio.Red;

public class AplicacionConsultas {

	// modelo
	private Red red;
	private Calculo calculo;

	// vista
	private Interfaz interfaz;

	// controlador
	private Coordinador coordinador;

	public static void main(String[] args) throws FileNotFoundException {
		AplicacionConsultas app = new AplicacionConsultas();
		app.iniciar();
		app.consultar();
	}

	private void iniciar() throws FileNotFoundException {
		/* Se instancian las clases */
		red = Red.getRed();

		calculo = new Calculo();
		coordinador = new Coordinador();
		interfaz = new Interfaz();

		/* Se establecen las relaciones entre clases */
		calculo.setCoordinador(coordinador);
		interfaz.setCoordinador(coordinador);

		/* Se establecen relaciones con la clase coordinador */
		coordinador.setRed(red);
		coordinador.setCalculo(calculo);

		calculo.cargarDatos(coordinador.listarEquipos(), coordinador.listarConexiones());
	}

	private void consultar() {
		boolean continuar = true;

		while (continuar) {
			System.out.println(Constantes.OPCIONES);

			int opcion = calcularOpcion();

			switch (opcion) {
			case 1:
				interfaz.mostrarEquipos(coordinador.listarEquipos());
				break;
			case 2:
				interfaz.mostrarConexiones(coordinador.listarConexiones());
				break;
			case 3:
				interfaz.mostrarUbicaciones(coordinador.listarUbicaciones());
				break;
			case 4:
				Equipo nuevoEquipo = interfaz.solicitarDatosEquipo();
				try {
					coordinador.agregarEquipo(nuevoEquipo);
					interfaz.mostrarExito("Equipo agregado exitosamente.");
				} catch (EquipoRepetidoException e) {
					interfaz.mostrarError(e.getMessage());
				}
				break;
			case 5:
				String codigo = interfaz.solicitarCodigoEquipo();
				Equipo equipoEncontrado = coordinador.buscarEquipo(codigo);

				if (equipoEncontrado != null) {
					interfaz.mostrarEquipos(List.of(equipoEncontrado));
				} else {
					interfaz.mostrarError("No se encontró el equipo con código: " + codigo);
				}
				break;
			case 6:
				interfaz.mostrarEquipos(calculo.buscarRuta(calculo.obtenerEquipo("AP09"), calculo.obtenerEquipo("FW02")));
				break;
			case 7: 
				calculo.realizarPingARango("192.168.16.2", "192.168.16.13");
				break;
			case 8: 
				interfaz.mostrarMapaDeEstado(coordinador.listarEquipos());
				break;
			case 9:
				calculo.verificarConectividad(coordinador.buscarEquipo("AP01"), coordinador.buscarEquipo("FW02"));
				break;
			case 10:
				continuar = false;
				break;
			default:
				interfaz.mostrarError("Opción no válida. Por favor, seleccione una opción del 1 al 10");
				break;
			}
		}
	}
	
	private int calcularOpcion() {
		int salida;
		try {
			salida = Integer.parseInt(interfaz.solicitarOpcion());
			return salida;
		} catch(Exception e) {
			interfaz.mostrarError("Opción no válida. Por favor, seleccione un número del 1 al 10");
			return calcularOpcion();
		}
	}
}
