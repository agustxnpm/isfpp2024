package red.pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import red.modelo.*;
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
	private Ubicacion ubicac1;
	private Ubicacion ubicac2;
	private Ubicacion ubicac3;
	private Ubicacion ubicac4;
	private Equipo equipo1;
	private Equipo equipo2;
	private Equipo equipo3;
	private Conexion conexion1;
	private Conexion conexion2;
	
	// El método que se invoca en la clase antes de cada test
	@BeforeEach
	void inicioTest() {
		tipoEquipo1 = new TipoEquipo("TE1", "Computadora");
		tipoEquipo2 = new TipoEquipo("TE2", "Router de banda ancha");
		tipoEquipo3 = new TipoEquipo("TE3", "Antena satelital");
		cable1 = new TipoCable("cable1", "UTP Categoría 6", 5);
		cable2 = new TipoCable("cable2", "Monomodo", 10);
		cable3 = new TipoCable("cable3", "Fibra óptica", 20);
		puerto1 = new TipoPuerto("TP1", "150 Mbps", 150);
		puerto2 = new TipoPuerto("TP2", "1 Gbps", 1000);
		ubicac1 = new Ubicacion("PMY", "Puerto Madryn");
		ubicac2 = new Ubicacion("TW", "Trelew");
		ubicac3 = new Ubicacion("ESQ", "Esquel");
		ubicac4 = new Ubicacion("MDP", "Mar del Plata");
		equipo1 = new Equipo("eq1", "amd64", "ASUS", "Máquina doméstica", ubicac1, tipoEquipo1, 6, puerto1, false);
		equipo2 = new Equipo("eq2", "Dell", "HP", "Router de sala de estar", ubicac2, tipoEquipo2, 10, puerto2, true);
		equipo3 = new Equipo("eq3", null, "", null, ubicac3, tipoEquipo3, 2, puerto2, false);
	}
	
	@Test
	void testTipoEquipos() {
		tipoEquipo3.setCodigo("TE2");
		assertEquals(tipoEquipo2, tipoEquipo3);
	}

	@Test
	void testCables() {
		assertNotEquals(cable1, cable2);
	}
	
	@Test
	void testEquipos() {
		assertThrows(IllegalArgumentException.class, () -> equipo1.agregarIp("Tu madre"));
		equipo1.agregarIp("200.10.240.251");
		assertThrows(DireccionIpRepetidaException.class, () -> equipo1.agregarIp("200.10.240.251"));
	}

}
