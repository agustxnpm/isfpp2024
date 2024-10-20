package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import red.dao.TipoEquipoDAO;
import red.factory.BDConexion;
import red.modelo.TipoCable;
import red.modelo.TipoEquipo;

public class TipoEquipoPostgresqlDAO implements TipoEquipoDAO {

	@Override
	public void insertar(TipoEquipo tipoEquipo) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "INSERT INTO poo2024.tipo_equipo_palma (codigo, descripcion) ";
			sql += "VALUES(?, ?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoEquipo.getCodigo());
			pstm.setString(2, tipoEquipo.getDescripcion());
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
	public void actualizar(TipoEquipo tipoEquipo) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "UPDATE poo2024.tipo_equipo_palma ";
			sql += "SET descripcion = ?";
			sql += "WHERE codigo = ?";
			
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoEquipo.getDescripcion());
			pstm.setString(2, tipoEquipo.getCodigo());
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
	public void borrar(TipoEquipo tipoEquipo) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "DELETE FROM poo2024.tipo_equipo_palma WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoEquipo.getCodigo());
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
	public List<TipoEquipo> buscarTodos() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, descripcion FROM poo2024.tipo_equipo_palma ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<TipoEquipo> ret = new ArrayList<TipoEquipo>();
			while (rs.next()) {
				ret.add(new TipoEquipo(rs.getString("codigo"), rs.getString("descripcion")));
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
