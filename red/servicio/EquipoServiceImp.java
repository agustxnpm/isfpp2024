package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.EquipoDAO;
import red.dao.secuencial.EquipoSecuencialDAO;
import red.modelo.Equipo;

/**
 * Implementación del servicio de gestión de equipos.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class EquipoServiceImp implements EquipoService {

    private EquipoDAO equipoDAO; // DAO para la gestión de equipos.

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de equipos.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */
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
