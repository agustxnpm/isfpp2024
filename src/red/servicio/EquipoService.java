package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Equipo;

public interface EquipoService {
	void insertar(Equipo equipo);

	void actualizar(Equipo equipo);

	void borrar(Equipo equipo);

	List<Equipo> buscarTodos() throws FileNotFoundException;

}
