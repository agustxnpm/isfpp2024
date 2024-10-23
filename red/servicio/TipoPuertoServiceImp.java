package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.TipoPuertoDAO;
import red.factory.Factory;

/**
 * Implementación del servicio de gestión de tipos de puerto.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class TipoPuertoServiceImp implements TipoPuertoService {

	private TipoPuertoDAO tipoPuertoDAO;
	
	public TipoPuertoServiceImp() {
		tipoPuertoDAO = (TipoPuertoDAO) Factory.getInstancia("TIPOPUERTO");
	}
	@Override
	public void insertar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.insertar(tipoPuerto);
	}

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de tipos de puerto.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */
    public TipoPuertoServiceImp() throws FileNotFoundException {
        tipoPuertoDAO = new TipoPuertoSecuencialDAO();
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
