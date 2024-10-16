package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Ubicacion;

public interface UbicacionService {
	void insertar(Ubicacion ubicacion);

	void actualizar(Ubicacion ubicacion);

	void borrar(Ubicacion ubicacion);

	List<Ubicacion> buscarTodos() throws FileNotFoundException;
}
