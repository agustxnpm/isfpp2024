package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;

import red.dao.UbicacionDAO;
import red.modelo.Ubicacion;
import red.factory.Factory;

public class UbicacionServiceImp implements UbicacionService{

	private UbicacionDAO ubicacionDAO;
	
	public UbicacionServiceImp() {
		ubicacionDAO = (UbicacionDAO) Factory.getInstancia("UBICACIONES");
		
	}
	@Override
	public void insertar(Ubicacion ubicacion) {
		ubicacionDAO.insertar(ubicacion);
	}

	@Override
	public void actualizar(Ubicacion ubicacion) {
		ubicacionDAO.actualizar(ubicacion);
	}

	@Override
	public void borrar(Ubicacion ubicacion) {
		ubicacionDAO.borrar(ubicacion);
	}

	@Override
	public List<Ubicacion> buscarTodos() throws FileNotFoundException {
		return ubicacionDAO.buscarTodos();
	}

}
