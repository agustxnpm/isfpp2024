package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.TipoEquipoDAO;
import red.dao.secuencial.TipoEquipoSecuencialDAO;
import red.modelo.TipoEquipo;

/**
 * Implementación del servicio de gestión de tipos de equipo.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class TipoEquipoServiceImp implements TipoEquipoService {

    private TipoEquipoDAO tipoEquipoDAO; // DAO para la gestión de tipos de equipo.

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de tipos de equipo.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */
    public TipoEquipoServiceImp() throws FileNotFoundException {
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
