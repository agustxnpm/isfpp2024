package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import red.dao.TipoPuertoDAO;
import red.factory.BDConexion;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;

public class TipoPuertoPostgresqlDAO implements TipoPuertoDAO {

	@Override
	public void insertar(TipoPuerto tipoPuerto) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "INSERT INTO poo2024.tipo_puerto_palma (codigo, descripcion, velocidad) ";
			sql += "VALUES(?, ?, ?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoPuerto.getCodigo());
			pstm.setString(2, tipoPuerto.getDescripcion());
			pstm.setInt(3, tipoPuerto.getVelocidad());
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
	public void actualizar(TipoPuerto tipoPuerto) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "UPDATE poo2024.tipo_puerto_palma ";
			sql += "SET descripcion = ?, velocidad = ?";
			sql += "WHERE codigo = ?";

			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoPuerto.getDescripcion());
			pstm.setString(2, Integer.toString(tipoPuerto.getVelocidad()));
			pstm.setString(3, tipoPuerto.getCodigo());
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
	public void borrar(TipoPuerto tipoPuerto) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "DELETE FROM poo2024.tipo_puerto_palma WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoPuerto.getCodigo());
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
	public List<TipoPuerto> buscarTodos() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, descripcion, velocidad FROM poo2024.tipo_puerto_palma ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<TipoPuerto> ret = new ArrayList<TipoPuerto>();
			while (rs.next()) {
				ret.add(new TipoPuerto(rs.getString("codigo"), rs.getString("descripcion"),
						Integer.parseInt(rs.getString("velocidad"))));
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
}
