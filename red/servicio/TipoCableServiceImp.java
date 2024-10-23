package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.TipoCableDAO;
import red.factory.Factory;
import red.modelo.TipoCable;

/**
 * Implementación del servicio de gestión de tipos de cable.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class TipoCableServiceImp implements TipoCableService {

	private TipoCableDAO tipoCableDAO;
	
	public TipoCableServiceImp() {
		tipoCableDAO = (TipoCableDAO) Factory.getInstancia("TIPOCABLE");
		
	}
	@Override
	public void insertar(TipoCable tipoCable) {
		tipoCableDAO.insertar(tipoCable);
	}

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de tipos de cable.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */
    public TipoCableServiceImp() throws FileNotFoundException {
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
