package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Equipo;

public interface EquipoDAO {

	void insertar(Equipo equipo);

	void actualizar(Equipo equipo);

	void borrar(Equipo equipo);

	List<Equipo> buscarTodos() throws FileNotFoundException;

}
