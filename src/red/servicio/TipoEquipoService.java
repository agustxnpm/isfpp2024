package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.TipoEquipo;

public interface TipoEquipoService {
	void insertar(TipoEquipo tipoEquipo);

	void actualizar(TipoEquipo tipoEquipo);

	void borrar(TipoEquipo tipoEquipo);

	List<TipoEquipo> buscarTodos() throws FileNotFoundException;
}
