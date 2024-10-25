package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.dao.EquipoDAO;
import red.factory.Factory;
import red.modelo.Equipo;

/**
 * Implementación del servicio de gestión de equipos.
 * Utiliza un DAO (Data Access Object) para interactuar con la capa de persistencia de datos.
 */
public class EquipoServiceImp implements EquipoService {

	private EquipoDAO equipoDAO;
	
	public EquipoServiceImp() throws FileNotFoundException {
		equipoDAO = (EquipoDAO) Factory.getInstancia("EQUIPO");
		
	}
	@Override
	public void insertar(Equipo equipo) {
		equipoDAO.insertar(equipo);
	}

    /**
     * Constructor que inicializa el DAO para interactuar con los datos de equipos.
     * 
     * @throws FileNotFoundException Si no se encuentra el archivo de configuración o datos.
     */

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
    // Implementación del método buscarPorCodigo
    @Override
    public Equipo buscarPorCodigo(String codigo) throws FileNotFoundException {
        List<Equipo> equipos = equipoDAO.buscarTodos();  // Buscar todos los equipos
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(codigo)) {
                return equipo;  // Retornar el equipo que coincida con el código
            }
        }
        return null;  // Retornar null si no se encuentra
    }
}
