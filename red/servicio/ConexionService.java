package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Conexion;

public interface ConexionService {
	void insertar(Conexion conexion);

	void actualizar(Conexion conexion);

	void borrar(Conexion conexion);

	List<Conexion> buscarTodos() throws FileNotFoundException;
}
