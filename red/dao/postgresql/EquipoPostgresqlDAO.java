package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import red.dao.EquipoDAO;
import red.dao.TipoEquipoDAO;
import red.dao.TipoPuertoDAO;
import red.dao.UbicacionDAO;
import red.factory.BDConexion;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;

public class EquipoPostgresqlDAO implements EquipoDAO {
	private Map<String, TipoEquipo> tipoEquipo;
	private Map<String, Ubicacion> ubicaciones;
	private Map<String, TipoPuerto> tipoPuerto;

	public EquipoPostgresqlDAO() throws FileNotFoundException {
		tipoEquipo = cargarTipoEquipo();
		ubicaciones = cargarUbicaciones();
		tipoPuerto = cargarTipoPuerto();
	}

	@Override
	public void insertar(Equipo equipo) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = BDConexion.getConnection();
			con.setAutoCommit(false); // Desactivar el autocommit

			String sql = "";
			sql += "INSERT INTO poo2024.equipos_palma (codigo, descripcion, marca, modelo, tipo_equipo, ubicacion, estado, info_puertos) ";
			sql += "VALUES(?,?,?,?,?,?,?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, equipo.getCodigo());
			pstm.setString(2, equipo.getDescripcion());
			pstm.setString(3, equipo.getMarca());
			pstm.setString(4, equipo.getModelo());
			pstm.setString(5, equipo.getTipoEquipo().getCodigo());
			pstm.setString(6, equipo.getUbicacion().getCodigo());
			pstm.setString(7, Boolean.toString(equipo.isEstado()));
			pstm.setString(8, equipo.getPuertosInfo());
			pstm.executeUpdate();
			pstm.close();

			String sqlIp = "INSERT INTO poo2024.direcciones_ip_palma (equipo_codigo, direccion_ip) VALUES(?,?)";
			pstm = con.prepareStatement(sqlIp);
			for (String ip : equipo.getDireccionesIp()) {
				pstm.setString(1, equipo.getCodigo());
				pstm.setString(2, ip);
				pstm.executeUpdate();
			}
			pstm.close();

			con.commit(); // Confirmar la transacción
		} catch (SQLException ex) {
			if (con != null) {
				try {
					con.rollback(); // Revertir en caso de error
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	public void actualizar(Equipo equipo) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = BDConexion.getConnection();
			con.setAutoCommit(false); // Desactivar el autocommit

			String sql = "UPDATE poo2024.equipos_palma ";
			sql += "SET descripcion = ?, marca = ?, modelo = ?, tipo_equipo = ?, ubicacion = ?, estado = ?, info_puertos = ? ";
			sql += "WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, equipo.getDescripcion());
			pstm.setString(2, equipo.getMarca());
			pstm.setString(3, equipo.getModelo());
			pstm.setString(4, equipo.getTipoEquipo().getCodigo());
			pstm.setString(5, equipo.getUbicacion().getCodigo());
			pstm.setString(6, Boolean.toString(equipo.isEstado()));
			pstm.setString(7, equipo.getPuertosInfo());
			pstm.setString(8, equipo.getCodigo());

			pstm.executeUpdate();

			// Eliminar direcciones IP existentes
			String sqlDeleteIp = "DELETE FROM poo2024.direcciones_ip_palma WHERE equipo_codigo = ?";
			try (PreparedStatement pstmDeleteIp = con.prepareStatement(sqlDeleteIp)) {
				pstmDeleteIp.setString(1, equipo.getCodigo());
				pstmDeleteIp.executeUpdate();
			}

			// Insertar nuevas direcciones IP
			String sqlInsertIp = "INSERT INTO poo2024.direcciones_ip_palma (equipo_codigo, direccion_ip) VALUES(?, ?)";
			try (PreparedStatement pstmInsertIp = con.prepareStatement(sqlInsertIp)) {
				for (String ip : equipo.getDireccionesIp()) {
					pstmInsertIp.setString(1, equipo.getCodigo());
					pstmInsertIp.setString(2, ip);
					pstmInsertIp.executeUpdate();
				}
			}
			con.commit(); // Confirmar la transacción
		} catch (SQLException ex) {
			if (con != null) {
				try {
					con.rollback(); // Revertir en caso de error
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	public void borrar(Equipo equipo) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			con.setAutoCommit(false); // Desactivar el autocommit

			String sql = "";
			sql += "DELETE FROM poo2024.equipos_palma WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, equipo.getCodigo());
			pstm.executeUpdate();

			String sqlDeleteIp = "DELETE FROM poo2024.direcciones_ip_palma WHERE equipo_codigo = ?";
			try (PreparedStatement pstmDeleteIp = con.prepareStatement(sqlDeleteIp)) {
				pstmDeleteIp.setString(1, equipo.getCodigo());
				pstmDeleteIp.executeUpdate();
			}

			con.commit();

		} catch (SQLException ex) {
			if (con != null) {
				try {
					con.rollback(); // Revertir en caso de error
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	public List<Equipo> buscarTodos() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, descripcion, marca, modelo, tipo_equipo, ubicacion, estado, info_puertos FROM poo2024.equipos_palma ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Equipo> ret = new ArrayList<Equipo>();
			while (rs.next()) {
				String infoPuertos = rs.getString("info_puertos");
				String[] puertoParts = infoPuertos.split("[,;]");
				String tipoPuertoCodigo = puertoParts[0];
				int cantPuertos = Integer.parseInt(puertoParts[1]);
				
				Equipo eq = new Equipo(rs.getString("codigo"), rs.getString("modelo"), rs.getString("marca"),
						rs.getString("descripcion"), ubicaciones.get(rs.getString("ubicacion")),
						tipoEquipo.get(rs.getString("tipo_equipo")), cantPuertos, tipoPuerto.get(tipoPuertoCodigo),
						Boolean.parseBoolean(rs.getString("estado")));
				
				for (int i = 2; i < puertoParts.length; i += 2) {
					// comienza desde el indice 2 ya que ya agregamos el
					// primer puerto (cantidad, tipo) al momento de
					// instanciar Equipo
					tipoPuertoCodigo = puertoParts[i];
					cantPuertos = Integer.parseInt(puertoParts[i + 1]);
					TipoPuerto tipoPuerto = this.tipoPuerto.get(tipoPuertoCodigo);
					eq.agregarPuerto(cantPuertos, tipoPuerto);
				}
				ret.add(eq);
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

	private Map<String, TipoEquipo> cargarTipoEquipo() throws FileNotFoundException {
		Map<String, TipoEquipo> tipoEquipos = new HashMap<String, TipoEquipo>();
		TipoEquipoDAO tipoEquiposDAO = new TipoEquipoPostgresqlDAO();
		List<TipoEquipo> tEq = tipoEquiposDAO.buscarTodos();
		for (TipoEquipo t : tEq) {
			tipoEquipos.put(t.getCodigo(), t);
		}

		return tipoEquipos;
	}

	private Map<String, Ubicacion> cargarUbicaciones() throws FileNotFoundException {
		Map<String, Ubicacion> ubicaciones = new HashMap<String, Ubicacion>();
		UbicacionDAO ubicacionesDAO = new UbicacionesPostgresqlDAO();
		List<Ubicacion> ub = ubicacionesDAO.buscarTodos();
		for (Ubicacion u : ub) {
			ubicaciones.put(u.getCodigo(), u);
		}

		return ubicaciones;
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
