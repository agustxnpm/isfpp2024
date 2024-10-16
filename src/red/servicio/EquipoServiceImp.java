package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.dao.EquipoDAO;
import red.dao.secuencial.EquipoSecuencialDAO;
import red.modelo.Equipo;

public class EquipoServiceImp implements EquipoService {

	private EquipoDAO equipoDAO;
	
	public EquipoServiceImp() throws FileNotFoundException {
		equipoDAO = new EquipoSecuencialDAO();
		
	}
	@Override
	public void insertar(Equipo equipo) {
		equipoDAO.insertar(equipo);
	}

	@Override
	public void actualizar(Equipo equipo) {
		equipoDAO.actualizar(equipo);
		
	}

	@Override
	public void borrar(Equipo equipo) {
		equipoDAO.borrar(equipo);
	}

	@Override
	public List<Equipo> buscarTodos() throws FileNotFoundException {
		return equipoDAO.buscarTodos();
	}

}
