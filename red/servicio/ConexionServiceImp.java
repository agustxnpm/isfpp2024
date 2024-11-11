package red.servicio;

import red.dao.ConexionDAO;
import red.dao.EquipoDAO;
import red.dao.postgresql.ConexionPostgresqlDAO;
import red.factory.Factory;
import red.modelo.Conexion;

import java.io.FileNotFoundException;
import java.util.List;

public class ConexionServiceImp implements ConexionService {

    private ConexionDAO conexionDAO;

    public ConexionServiceImp() throws FileNotFoundException { 
        this.conexionDAO = (ConexionDAO) Factory.getInstancia("CONEXION");
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
