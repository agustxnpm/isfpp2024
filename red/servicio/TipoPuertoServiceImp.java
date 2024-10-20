package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.TipoPuerto;
import red.dao.TipoPuertoDAO;
import red.factory.Factory;

public class TipoPuertoServiceImp implements TipoPuertoService{

	private TipoPuertoDAO tipoPuertoDAO;
	
	public TipoPuertoServiceImp() {
		tipoPuertoDAO = (TipoPuertoDAO) Factory.getInstancia("TIPOPUERTO");
	}
	@Override
	public void insertar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.insertar(tipoPuerto);
	}

	@Override
	public void actualizar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.actualizar(tipoPuerto);
	}

	@Override
	public void borrar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.borrar(tipoPuerto);
	}

	@Override
	public List<TipoPuerto> buscarTodos() throws FileNotFoundException {
		return tipoPuertoDAO.buscarTodos();
	}

}
