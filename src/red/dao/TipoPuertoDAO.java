package red.dao;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.TipoPuerto;

public interface TipoPuertoDAO {

	void insertar(TipoPuerto tipoPuerto);

	void actualizar(TipoPuerto tipoPuerto);

	void borrar(TipoPuerto tipoPuerto);

	List<TipoPuerto> buscarTodos() throws FileNotFoundException;

}
