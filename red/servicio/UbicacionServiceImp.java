package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.UbicacionDAO;
import red.dao.secuencial.UbicacionesSecuencialDAO;
import red.modelo.Ubicacion;

/**
 * Implementación del servicio de gestión de ubicaciones.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class UbicacionServiceImp implements UbicacionService {

    private UbicacionDAO ubicacionDAO; // DAO para la gestión de ubicaciones.

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de ubicaciones.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */
    public UbicacionServiceImp() throws FileNotFoundException {
        ubicacionDAO = new UbicacionesSecuencialDAO();
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
