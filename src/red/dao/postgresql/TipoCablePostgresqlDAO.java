package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import red.dao.TipoCableDAO;
import red.factory.BDConexion;
import red.modelo.TipoCable;

public class TipoCablePostgresqlDAO implements TipoCableDAO{

	@Override
	public void insertar(TipoCable tipoCable) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "INSERT INTO poo2024.tipo_cable_palma (codigo, descripcion, velocidad) ";
			sql += "VALUES(?, ?, ?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoCable.getCodigo());
			pstm.setString(2, tipoCable.getDescripcion());
			pstm.setInt(3, tipoCable.getVelocidad());
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
	public void actualizar(TipoCable tipoCable) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "UPDATE poo2024.tipo_cable_palma ";
			sql += "SET descripcion = ?, velocidad = ?";
			sql += "WHERE codigo = ?";
			
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoCable.getDescripcion());
			pstm.setString(2, Integer.toString(tipoCable.getVelocidad()));
			pstm.setString(3, tipoCable.getCodigo());
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
	public void borrar(TipoCable tipoCable) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "";
			sql += "DELETE FROM poo2024.tipo_cable_palma WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tipoCable.getCodigo());
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
	public List<TipoCable> buscarTodos() throws FileNotFoundException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, descripcion, velocidad FROM poo2024.tipo_cable_palma ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<TipoCable> ret = new ArrayList<TipoCable>();
			while (rs.next()) {
				ret.add(new TipoCable(rs.getString("codigo"), rs.getString("descripcion"), Integer.parseInt(rs.getString("velocidad"))));
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
