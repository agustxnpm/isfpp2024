package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.ConexionDAO;
import red.factory.Factory;
import red.modelo.Conexion;

/**
 * Implementación del servicio de gestión de conexiones.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class ConexionServiceImp implements ConexionService {

	private ConexionDAO conexionDAO;
	
	public ConexionServiceImp() throws FileNotFoundException {
		conexionDAO = (ConexionDAO) Factory.getInstancia("CONEXION");
	}
	@Override
	public void insertar(Conexion conexion) {
		conexionDAO.insertar(conexion);
	}

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de conexiones.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */

    @Override
    public void actualizar(Conexion conexion) {
        conexionDAO.actualizar(conexion);
    }

    @Override
    public void borrar(Conexion conexion) {
        conexionDAO.borrar(conexion);
    }

    @Override
    public List<Conexion> buscarTodos() throws FileNotFoundException {
        return conexionDAO.buscarTodos();
    }
}
