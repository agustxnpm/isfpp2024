package red.dao;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Ubicacion;

public interface UbicacionDAO {
	void insertar(Ubicacion ubicacion);

	void actualizar(Ubicacion ubicacion);

	void borrar(Ubicacion ubicacion);

	List<Ubicacion> buscarTodos() throws FileNotFoundException;
}
