package red.pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import red.modelo.*;
import red.negocio.Calculo;
import red.servicio.TipoEquipoService;
import red.servicio.TipoEquipoServiceImp;
import red.excepciones.*;

// Clase de JUnit5 para probar los métodos de las clases de red.modelo y las excepciones lanzadas por éstos

class TestModelo {
	
	/**
	 * Atributos de la clase que usaremos en el presente test. Las conexiones hacen de arcos entre los equipos del sistema,
	 * y se usarán junto a éstos para armar un ejemplo de grafo para el test. 
	*/
	private Calculo calculo;
	private TipoEquipo tipoEquipo1;
	private TipoEquipo tipoEquipo2;
	private TipoEquipo tipoEquipo3;
	private TipoEquipo tipoEquipo4;
	private TipoCable cable1;
	private TipoCable cable2;
	private TipoCable cable3;
	private TipoPuerto puerto1;
	private TipoPuerto puerto2;
	private TipoPuerto puerto3;
	private TipoPuerto puerto4;
	private TipoPuerto puerto5;
	private TipoPuerto puerto6;
	private Ubicacion ubicac1;
	private Ubicacion ubicac2;
	private Ubicacion ubicac3;
	private Ubicacion ubicac4;
	private Equipo equipo1;
	private Equipo equipo2;
	private Equipo equipo3;
	private Equipo equipo4;
	private Equipo equipo5;
	private Equipo equipo6;
	private Equipo equipo7;
	private Equipo equipo8;
	private Conexion conex1;
	private Conexion conex2;
	private Conexion conex3;
	private Conexion conex4;
	private Conexion conex5;
	private Conexion conex6;
	private Conexion conex7;

	// El método que se invoca en la clase antes de cada test
	@BeforeEach
	void inicioTest() {
		calculo = new Calculo();
		tipoEquipo1 = new TipoEquipo("TE1", "Computadora");
		tipoEquipo2 = new TipoEquipo("TE2", "Router de banda ancha");
		tipoEquipo3 = new TipoEquipo("TE3", "Antena satelital");
		tipoEquipo4 = new TipoEquipo("TE4", "Teléfono celular");
		cable1 = new TipoCable("cable1", "UTP Categoría 6", 5);
		cable2 = new TipoCable("cable2", "Monomodo", 10);
		cable3 = new TipoCable("cable3", "Fibra óptica", 20);
		puerto1 = new TipoPuerto("TP1", "150 Mbps", 150);
		puerto2 = new TipoPuerto("TP2", "1 Gbps", 1000);
		puerto3 = new TipoPuerto("TP3", "220 mbps", 220);
		puerto4 = new TipoPuerto("TP4", "40 mbps", 40);
		puerto5 = new TipoPuerto("TP5", "12 gbps", 12000);
		puerto6 = new TipoPuerto("TP6", "600 mbps", 600);
		ubicac1 = new Ubicacion("PMY", "Puerto Madryn");
		ubicac2 = new Ubicacion("TW", "Trelew");
		ubicac3 = new Ubicacion("ESQ", "Esquel");
		ubicac4 = new Ubicacion("HOU", "Houston, Texas");
		equipo1 = new Equipo("eq1", "amd64", "ASUS", "Máquina doméstica", ubicac1, tipoEquipo1, 6, puerto1, false);
		equipo2 = new Equipo("eq2", "Dell", "HP", "Router de sala de estar", ubicac2, tipoEquipo2, 10, puerto2, true);
		equipo3 = new Equipo("eq3", null, "", null, ubicac3, tipoEquipo3, 2, puerto6, false);
		equipo4 = new Equipo("eq4", "A05", "Samsung", "Teléfono celular", ubicac1, tipoEquipo4, 4, puerto3, false);
		equipo5 = new Equipo("eq5", "Clasificado", "NASA", "Antena de control satelital", ubicac4, tipoEquipo3, 200, puerto5, true);
		equipo6 = new Equipo("eq6", "", null, "Dispositivo de rastreo militar", ubicac3, tipoEquipo1, 220, puerto5, true);
		equipo7 = new Equipo("eq7", "X32", "ASUS", "Máquina de escritorio", ubicac3, tipoEquipo1, 8, puerto4, false);
		equipo8 = new Equipo("eq8", "", "Pescapuerta", "Radar de barco", ubicac1, new TipoEquipo("XXX", "Radar satelital"), 1, puerto2, true);
		equipo1.agregarPuerto(40, puerto1);
		equipo1.agregarPuerto(20, puerto2);
		equipo1.agregarPuerto(3, puerto3);
		equipo1.agregarPuerto(7, puerto4);
		equipo1.agregarPuerto(2, puerto5);
		equipo1.agregarPuerto(10, puerto6);
		equipo1.agregarIp("200.10.240.251");
		equipo5.agregarIp("166.82.1.10");
		conex1 = new Conexion(equipo1, equipo2, cable1, puerto1, puerto2);
		conex2 = new Conexion(equipo2, equipo3, cable2, puerto3, puerto4);
		conex3 = new Conexion(equipo4, equipo1, cable3, puerto5, puerto5);
		conex4 = new Conexion(equipo3, equipo5, cable2, puerto1, puerto6);
		conex5 = new Conexion(equipo6, equipo7, cable3, puerto4, puerto5);
		conex6 = new Conexion(equipo4, equipo5, cable2, puerto2, puerto2);
		conex7 = new Conexion(equipo8, equipo6, cable3, puerto2, puerto4);
		calculo.cargarDatos( // Construye el grafo
			List.of(equipo1, equipo2, equipo3, equipo4, equipo5, equipo6, equipo7, equipo8),
			List.of(conex1, conex2, conex3, conex4, conex5, conex6, conex7)
		);
	}
	
	// Tests de igualdad y desigualdad entre tipos de equipos, cables y puertos y ubicaciones
	@Test
	void testEquals() {
		assertEquals(tipoEquipo2, new TipoEquipo("TE2", "Escáner industrial"));
		assertNotEquals(cable1, cable2);
		assertNotEquals(puerto2, new Ubicacion("TP2", "No es un puerto así que el equals va a fallar"));
	}

	// Tests de métodos de la clase Equipo
	@Test
	void testEquipos() {
		assertEquals(equipo6.getModelo(), "Modelo desconocido");
		assertEquals(equipo3.getMarca(), "Marca desconocida");
		assertEquals(equipo3.getDescripcion(), "Sin descripción");
		assertEquals(equipo1.getVelocidadMaxima(), 40);
		assertEquals(equipo2.getVelocidadMaxima(), 1000);
		assertFalse(equipo1.realizarPing());
		assertTrue(equipo2.realizarPing());
		assertFalse(equipo3.realizarPing());
		assertThrows(IllegalArgumentException.class, () -> equipo1.agregarIp("Tu madre"));
		assertThrows(DireccionIpRepetidaException.class, () -> equipo1.agregarIp("200.10.240.251")); // IP ya agregada antes del test
		assertThrows(IllegalArgumentException.class, () -> equipo2.agregarPuerto(0, puerto1));
		assertThrows(IllegalArgumentException.class, () -> equipo3.agregarPuerto(5, null));
		assertThrows(IllegalArgumentException.class, () -> new Equipo("eq9", null, "", null, ubicac3, tipoEquipo3, 2, null, false)); // tipo de puerto nulo
		assertEquals(equipo1.getPuertosInfo(), "TP1,46;TP2,20;TP3,3;TP4,7;TP5,2;TP6,10");
		assertEquals(equipo2.getPuertosInfo(), "TP2,10");
	}
	
	// Test del equals y de excepciones lanzadas por la clase Conexión
	@Test
	void testConexion() {
		// El equals considera iguales a dos conexiones con los mismos equipos y tipo de cable en el mismo orden
		assertEquals(conex2, new Conexion(equipo2, equipo3, cable2, puerto4, puerto3));
		assertNotEquals(conex2, new Conexion(equipo3, equipo2, cable2, puerto3, puerto4));
		assertThrows(EquipoRepetidoException.class, () -> new Conexion(equipo1, equipo1, cable3, puerto5, puerto5));
		assertThrows(EquipoRepetidoException.class, () -> conex1.setEquipo1(equipo2));
		assertThrows(EquipoRepetidoException.class, () -> conex2.setEquipo2(equipo2));
	}
	
	// Test del grafo de Cálculo
	@Test
	void testCalculo() {
		//assertEquals(equipo3, calculo.obtenerEquipo("eq3"));
		//assertNotEquals(equipo2, calculo.obtenerEquipo("eq5"));
		//assertNull(calculo.obtenerEquipo("Hola"));
		assertThrows(ConexionRepetidaException.class, () -> calculo.agregarConexionAlGrafo(conex1));
		assertThrows(EquipoRepetidoException.class, () -> calculo.agregarEquipoAlGrafo(equipo1));
		assertEquals(List.of(equipo5, equipo4, equipo1), calculo.buscarRuta(equipo5, equipo1));
		assertNull(calculo.buscarRuta(equipo2, equipo7));
		assertEquals(calculo.calcularVelocidadMaxima(List.of(equipo1, equipo2, equipo3, equipo5)), 5);
		assertEquals(calculo.calcularVelocidadMaxima(List.of(equipo4, equipo5, equipo3)), 10);
		
		/**
		 *  Los equipos 1 y 6 no están conectados directamente, ni tampoco 1 y 5, pese a que hay un camino del equipo 1 al 5
		 *  No hay, de hecho, un camino que vaya del equipo 1 al equipo 6
		*/
		assertThrows(ConexionNoConectadaException.class, () -> calculo.calcularVelocidadMaxima(List.of(equipo1, equipo6, equipo7)));
		assertThrows(ConexionNoConectadaException.class, () -> calculo.calcularVelocidadMaxima(List.of(equipo1, equipo5, equipo3)));
		assertThrows(ConexionNoConectadaException.class, () -> calculo.verificarConectividad(equipo1, equipo6));
		
		assertFalse(calculo.realizarPingAEquipo("200.10.240.251"));
		assertTrue(calculo.realizarPingAEquipo("166.82.1.10"));
		assertThrows(DireccionIpNoEncontradaException.class, () -> calculo.realizarPingAEquipo("166.82.100.10"));
		assertThrows(DireccionIpNoEncontradaException.class, () -> calculo.realizarPingAEquipo("Buen día."));
		//assertThrows(DireccionIpNoEncontradaException.class, () -> calculo.realizarPingARango("200", "40"));
	}

}
