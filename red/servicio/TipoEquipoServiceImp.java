package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.dao.TipoEquipoDAO;
import red.dao.secuencial.TipoEquipoSecuencialDAO;
import red.modelo.TipoEquipo;

public class TipoEquipoServiceImp implements TipoEquipoService {

	private TipoEquipoDAO tipoEquipoDAO;
	
	public TipoEquipoServiceImp() {
		tipoEquipoDAO = new TipoEquipoSecuencialDAO();
		
	}
	@Override
	public void insertar(TipoEquipo tipoEquipo) {
		tipoEquipoDAO.insertar(tipoEquipo);
	}

	@Override
	public void actualizar(TipoEquipo tipoEquipo) {
		tipoEquipoDAO.actualizar(tipoEquipo);
	}

	@Override
	public void borrar(TipoEquipo tipoEquipo) {
		tipoEquipoDAO.borrar(tipoEquipo);
	}

	@Override
	public List<TipoEquipo> buscarTodos() throws FileNotFoundException {
		return tipoEquipoDAO.buscarTodos();
	}

}
