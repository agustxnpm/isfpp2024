package red.datos;

import red.dao.EquipoDAO;
import java.io.FileNotFoundException;
import red.dao.ConexionDAO;
import red.dao.TipoCableDAO;
import red.dao.TipoEquipoDAO;
import red.dao.TipoPuertoDAO;
import red.dao.UbicacionDAO;
import red.dao.secuencial.UbicacionesSecuencialDAO;
import red.dao.secuencial.ConexionSecuencialDAO;
import red.dao.secuencial.EquipoSecuencialDAO;
import red.dao.secuencial.TipoCableSecuencialDAO;
import red.dao.secuencial.TipoEquipoSecuencialDAO;
import red.dao.secuencial.TipoPuertoSecuencialDAO;
import red.dao.postgresql.ConexionPostgresqlDAO;
import red.dao.postgresql.EquipoPostgresqlDAO;
import red.dao.postgresql.TipoCablePostgresqlDAO;
import red.dao.postgresql.TipoEquipoPostgresqlDAO;
import red.dao.postgresql.TipoPuertoPostgresqlDAO;
import red.dao.postgresql.UbicacionesPostgresqlDAO;
import red.modelo.TipoCable;
import red.modelo.Ubicacion;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;

/**
 * Clase para migrar los datos de un archivo secuencial a una base de datos PostgreSQL.
 * Esta clase se encarga de transferir datos desde una fuente de datos secuencial a una
 * base de datos relacional.
 */
public class FileTextBD {

    /**
     * Método principal para ejecutar la migración de datos de archivos secuenciales a PostgreSQL.
     * 
     * @param args Argumentos de la línea de comandos.
     * @throws FileNotFoundException Si no se encuentra alguno de los archivos secuenciales.
     */
    public static void main(String[] args) throws FileNotFoundException {

        // Migración de datos de ubicaciones
        UbicacionDAO ubicacionSecuencialDAO = new UbicacionesSecuencialDAO();
        UbicacionDAO ubicacionesPostgresqlDAO = new UbicacionesPostgresqlDAO();

        for (Ubicacion u : ubicacionSecuencialDAO.buscarTodos()) {
            ubicacionesPostgresqlDAO.insertar(u);
        }

        // Migración de datos de tipos de cable
        TipoCableDAO tipoCableSecuencialDAO = new TipoCableSecuencialDAO();
        TipoCableDAO tipoCablePostgresqlDAO = new TipoCablePostgresqlDAO();

        for (TipoCable tc : tipoCableSecuencialDAO.buscarTodos()) {
            tipoCablePostgresqlDAO.insertar(tc);
        }

        // Migración de datos de tipos de equipo
        TipoEquipoDAO tipoEquipoSecuencialDAO = new TipoEquipoSecuencialDAO();
        TipoEquipoDAO tipoEquipoPostgresqlDAO = new TipoEquipoPostgresqlDAO();

        for (TipoEquipo teq : tipoEquipoSecuencialDAO.buscarTodos()) {
            tipoEquipoPostgresqlDAO.insertar(teq);
        }

        // Migración de datos de tipos de puerto
        TipoPuertoDAO tipoPuertoSecuencialDAO = new TipoPuertoSecuencialDAO();
        TipoPuertoDAO tipoPuertoPostgresqlDAO = new TipoPuertoPostgresqlDAO();

        for (TipoPuerto tp : tipoPuertoSecuencialDAO.buscarTodos()) {
            tipoPuertoPostgresqlDAO.insertar(tp);
        }

        // Migración de datos de equipos
        EquipoDAO equipoSecuencialDAO = new EquipoSecuencialDAO();
        EquipoDAO equipoPostgresqlDAO = new EquipoPostgresqlDAO();

        for (Equipo eq : equipoSecuencialDAO.buscarTodos()) {
            equipoPostgresqlDAO.insertar(eq);
        }

        // Migración de datos de conexiones
        ConexionDAO conexionSecuencialDAO = new ConexionSecuencialDAO();
        ConexionDAO conexionPostgresqlDAO = new ConexionPostgresqlDAO();

        for (Conexion con : conexionSecuencialDAO.buscarTodos()) {
            conexionPostgresqlDAO.insertar(con);
        }

        System.out.println("Datos migrados");
    }
}
