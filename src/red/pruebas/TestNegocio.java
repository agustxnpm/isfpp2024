package red.pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import red.negocio.*;

class TestNegocio {

	private Red red;
	private Calculo calculo;
	
	@BeforeEach
	void antesDeTest() throws FileNotFoundException{
		red = Red.getRed();
		calculo = new Calculo();
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
