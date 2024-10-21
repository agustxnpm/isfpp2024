package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import red.dao.UbicacionDAO;
import red.factory.BDConexion;
import red.modelo.Ubicacion;

public class UbicacionesPostgresqlDAO implements UbicacionDAO {

	@Override
	public void insertar(Ubicacion ubicacion) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "INSERT INTO poo2024.ubicaciones_palma (codigo, descripcion) ";
			sql += "VALUES(?, ?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, ubicacion.getCodigo());
			pstm.setString(2, ubicacion.getDescripcion());
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
	public void actualizar(Ubicacion ubicacion) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "UPDATE poo2024.ubicaciones_palma ";
			sql += "SET descripcion = ?";
			sql += "WHERE codigo = ?";
			
			pstm = con.prepareStatement(sql);
			pstm.setString(1, ubicacion.getDescripcion());
			pstm.setString(2, ubicacion.getCodigo());
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
	public void borrar(Ubicacion ubicacion) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "DELETE FROM poo2024.ubicaciones_palma WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, ubicacion.getCodigo());
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
	public List<Ubicacion> buscarTodos() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, descripcion FROM poo2024.ubicaciones_palma ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Ubicacion> ret = new ArrayList<Ubicacion>();
			while (rs.next()) {
				ret.add(new Ubicacion(rs.getString("codigo"), rs.getString("descripcion")));
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
