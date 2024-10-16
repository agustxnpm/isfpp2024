package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.dao.TipoCableDAO;
import red.dao.secuencial.TipoCableSecuencialDAO;
import red.modelo.TipoCable;

public class TipoCableServiceImp implements TipoCableService {

	private TipoCableDAO tipoCableDAO;
	
	public TipoCableServiceImp() {
		tipoCableDAO = new TipoCableSecuencialDAO();
		
	}
	@Override
	public void insertar(TipoCable tipoCable) {
		tipoCableDAO.insertar(tipoCable);
	}

	@Override
	public void actualizar(TipoCable tipoCable) {
		tipoCableDAO.actualizar(tipoCable);
	}

	@Override
	public void borrar(TipoCable tipoCable) {
		tipoCableDAO.borrar(tipoCable);
	}

	@Override
	public List<TipoCable> buscarTodos() throws FileNotFoundException {
		return tipoCableDAO.buscarTodos();
	}

}
