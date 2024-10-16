package red.dao;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.TipoCable;

public interface TipoCableDAO {

	void insertar(TipoCable tipoCable);

	void actualizar(TipoCable tipoCable);

	void borrar(TipoCable tipoCable);

	List<TipoCable> buscarTodos() throws FileNotFoundException;

}
