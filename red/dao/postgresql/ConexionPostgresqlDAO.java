package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import red.dao.ConexionDAO;
import red.dao.EquipoDAO;
import red.dao.TipoCableDAO;
import red.dao.TipoPuertoDAO;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoCable;
import red.modelo.TipoPuerto;
import red.factory.BDConexion;

public class ConexionPostgresqlDAO implements ConexionDAO {

	private Map<String, Equipo> equipos;
	private Map<String, TipoCable> tipoCable;
	private Map<String, TipoPuerto> tipoPuerto;

	public ConexionPostgresqlDAO() throws FileNotFoundException {
		equipos = cargarEquipos();
		tipoCable = cargarTipoCable();
		tipoPuerto = cargarTipoPuerto();
	}

	@Override
	public void insertar(Conexion conexion) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "INSERT INTO poo2024.conexion_palma (equipo1, equipo2, tipo_cable, tipo_puerto1, tipo_puerto2) ";
			sql += "VALUES(?,?,?,?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, conexion.getEquipo1().getCodigo());
			pstm.setString(2, conexion.getEquipo2().getCodigo());
			pstm.setString(3, conexion.getTipoCable().getCodigo());
			pstm.setString(4, conexion.getTipoPuerto1().getCodigo());
			pstm.setString(5, conexion.getTipoPuerto2().getCodigo());
			pstm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void actualizar(Conexion conexion) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "UPDATE poo2024.conexion_palma ";
			sql += "SET tipo_cable = ?, tipo_puerto1 = ?, tipo_puerto2 = ? ";
			sql += "WHERE equipo1 = ? AND equipo2 = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, conexion.getTipoCable().getCodigo());
			pstm.setString(2, conexion.getTipoPuerto1().getCodigo());
			pstm.setString(3, conexion.getTipoPuerto2().getCodigo());
			pstm.setString(4, conexion.getEquipo1().getCodigo());
			pstm.setString(5, conexion.getEquipo2().getCodigo());
			pstm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void borrar(Conexion conexion) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "DELETE FROM poo2024.conexion_palma WHERE equipo1 = ? AND equipo2 = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, conexion.getEquipo1().getCodigo());
			pstm.setString(2, conexion.getEquipo2().getCodigo());
			pstm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public List<Conexion> buscarTodos() throws FileNotFoundException {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT equipo1, equipo2, tipo_cable, tipo_puerto1, tipo_puerto2 FROM poo2024.conexion_palma ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Conexion> ret = new ArrayList<Conexion>();
			while (rs.next()) {
				ret.add(new Conexion(equipos.get(rs.getString("equipo1")), equipos.get(rs.getString("equipo2")),
						tipoCable.get(rs.getString("tipo_cable")), tipoPuerto.get(rs.getString("tipo_puerto1")),
						tipoPuerto.get(rs.getString("tipo_puerto2"))));
			}
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	private Map<String, Equipo> cargarEquipos() throws FileNotFoundException {
		Map<String, Equipo> equipos = new HashMap<String, Equipo>();
		EquipoDAO equipoDAO = new EquipoPostgresqlDAO();
		List<Equipo> eq = equipoDAO.buscarTodos();
		for (Equipo e : eq) {
			equipos.put(e.getCodigo(), e);
		}

		return equipos;
	}

	private Map<String, TipoCable> cargarTipoCable() throws FileNotFoundException {
		Map<String, TipoCable> cables = new HashMap<String, TipoCable>();
		TipoCableDAO cablesDAO = new TipoCablePostgresqlDAO();
		List<TipoCable> cbls = cablesDAO.buscarTodos();
		for (TipoCable c : cbls) {
			cables.put(c.getCodigo(), c);
		}

		return cables;
	}

	private Map<String, TipoPuerto> cargarTipoPuerto() throws FileNotFoundException {
		Map<String, TipoPuerto> puertos = new HashMap<String, TipoPuerto>();
		TipoPuertoDAO puertosDAO = new TipoPuertoPostgresqlDAO();
		List<TipoPuerto> prts = puertosDAO.buscarTodos();
		for (TipoPuerto p : prts) {
			puertos.put(p.getCodigo(), p);
		}

		return puertos;
	}
}
