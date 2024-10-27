package red.pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import red.modelo.*;
import red.negocio.*;
import red.excepciones.*;

// Clase de JUnit5 para probar los métodos de las clases de red.modelo y las excepciones lanzadas por éstos

class TestModelo {
	
	// atributos de la clase que usaremos en el presente test
	private TipoEquipo tipoEquipo1;
	private TipoEquipo tipoEquipo2;
	private TipoEquipo tipoEquipo3;
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
	private Equipo equipo1;
	private Equipo equipo2;
	private Equipo equipo3;
	private Conexion conex1;
	private Conexion conex2;
	private Red red;
	private Calculo calculo;
	
	// El método que se invoca en la clase antes de los tests
	@BeforeEach
	void inicioTest() {
		calculo = new Calculo();
		try {
			red = Red.getRed();
		} catch(Exception e) {
			e.printStackTrace();
			red = null;
		}
		tipoEquipo1 = new TipoEquipo("TE1", "Computadora");
		tipoEquipo2 = new TipoEquipo("TE2", "Router de banda ancha");
		tipoEquipo3 = new TipoEquipo("TE3", "Antena satelital");
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
		equipo1 = new Equipo("eq1", "amd64", "ASUS", "Máquina doméstica", ubicac1, tipoEquipo1, 6, puerto1, false);
		equipo2 = new Equipo("eq2", "Dell", "HP", "Router de sala de estar", ubicac2, tipoEquipo2, 10, puerto2, true);
		equipo3 = new Equipo("eq3", null, "", null, ubicac3, tipoEquipo3, 2, null, false);
		equipo1.agregarPuerto(40, puerto1);
		equipo1.agregarPuerto(20, puerto2);
		equipo1.agregarPuerto(3, puerto3);
		equipo1.agregarPuerto(7, puerto4);
		equipo1.agregarPuerto(2, puerto5);
		equipo1.agregarPuerto(10, puerto6);
		conex1 = new Conexion(equipo1, equipo2, cable1, puerto1, puerto2);
		conex2 = new Conexion(equipo2, equipo3, cable2, puerto3, puerto4);
	}
	
	// Tests de igualdad y desigualdad entre tipos de equipos, cables y puertos y ubicaciones
	@Test
	void testEquals() {
		assertEquals(tipoEquipo2, new TipoEquipo("TE2", "Escáner industrial"));
		assertNotEquals(cable1, cable2);
	}

	// Tests de métodos de la clase Equipo
	@Test
	void testEquipos() {
		assertEquals(equipo1.getVelocidadMaxima(), 40);
		assertEquals(equipo2.getVelocidadMaxima(), 1000);
		assertFalse(equipo1.realizarPing());
		assertTrue(equipo2.realizarPing());
		assertFalse(equipo3.realizarPing());
		assertThrows(IllegalArgumentException.class, () -> equipo1.agregarIp("Tu madre"));
		equipo1.agregarIp("200.10.240.251");
		assertThrows(DireccionIpRepetidaException.class, () -> equipo1.agregarIp("200.10.240.251"));
		assertThrows(IllegalArgumentException.class, () -> equipo2.agregarPuerto(0, puerto1));
		assertEquals(equipo1.getPuertosInfo(), "TP1,46;TP2,20;TP3,3;TP4,7;TP5,2;TP6,10");
		assertEquals(equipo2.getPuertosInfo(), "TP2,10");
		assertEquals(equipo3.getPuertosInfo(), "N/A,2");
	}
	
	// Test de excepciones lanzadas por la clase Conexión
	@Test
	void testConexion() {
		assertThrows(EquipoRepetidoException.class, () -> new Conexion(equipo1, equipo1, cable3, puerto5, puerto5));
		assertThrows(EquipoRepetidoException.class, () -> conex1.setEquipo1(equipo2));
		assertThrows(EquipoRepetidoException.class, () -> conex2.setEquipo2(equipo2));
	}

}
