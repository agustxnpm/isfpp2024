package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.ConexionDAO;
import red.dao.secuencial.ConexionSecuencialDAO;
import red.modelo.Conexion;

/**
 * Implementación del servicio de gestión de conexiones.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class ConexionServiceImp implements ConexionService {

    private ConexionDAO conexionDAO; // DAO para la gestión de conexiones.

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de conexiones.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */
    public ConexionServiceImp() throws FileNotFoundException {
        conexionDAO = new ConexionSecuencialDAO();
    }

    @Override
    public void insertar(Conexion conexion) {
        conexionDAO.insertar(conexion);
    }

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
