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

public class FileTextBD {

	/* clase para migrar los datos de un archivo secuencial a la base de datos */
	public static void main(String[] args) throws FileNotFoundException {

		UbicacionDAO ubicacionSecuencialDAO = new UbicacionesSecuencialDAO();
		UbicacionDAO ubicacionesPostgresqlDAO = new UbicacionesPostgresqlDAO();
		
		for (Ubicacion u : ubicacionSecuencialDAO.buscarTodos()) {
			ubicacionesPostgresqlDAO.insertar(u);
		}
		
		TipoCableDAO tipoCableSecuencialDAO = new TipoCableSecuencialDAO();
		TipoCableDAO tipoCablePostgresqlDAO = new TipoCablePostgresqlDAO();
		
		for (TipoCable tc : tipoCableSecuencialDAO.buscarTodos()) {
			tipoCablePostgresqlDAO.insertar(tc);
		}
		
		TipoEquipoDAO tipoEquipoSecuencialDAO = new TipoEquipoSecuencialDAO();
		TipoEquipoDAO tipoEquipoPostgresqlDAO = new TipoEquipoPostgresqlDAO();
		
		for (TipoEquipo teq : tipoEquipoSecuencialDAO.buscarTodos()){
			tipoEquipoPostgresqlDAO.insertar(teq);
		}
		
		TipoPuertoDAO tipoPuertoSecuencialDAO = new TipoPuertoSecuencialDAO();
		TipoPuertoDAO tipoPuertoPostgresqlDAO = new TipoPuertoPostgresqlDAO();
		
		for (TipoPuerto tp : tipoPuertoSecuencialDAO.buscarTodos()) {
			tipoPuertoPostgresqlDAO.insertar(tp);
		}

		EquipoDAO equipoSecuencialDAO = new EquipoSecuencialDAO();
		EquipoDAO equipoPostgresqlDAO = new EquipoPostgresqlDAO();
		
		for (Equipo eq : equipoSecuencialDAO.buscarTodos()) {
			equipoPostgresqlDAO.insertar(eq);
		}
		
		ConexionDAO conexionSecuencialDAO = new ConexionSecuencialDAO();
		ConexionDAO conexionPostgresqlDAO = new ConexionPostgresqlDAO();
		
		for (Conexion con : conexionSecuencialDAO.buscarTodos()) {
			conexionPostgresqlDAO.insertar(con);
		}
		
	}
}
