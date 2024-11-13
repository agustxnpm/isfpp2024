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

/**
 * Implementación de la interfaz TipoCableDAO para gestionar las operaciones CRUD
 * de la entidad TipoCable utilizando una base de datos PostgreSQL.
 */
public class TipoCablePostgresqlDAO implements TipoCableDAO {

    /**
     * Inserta un nuevo tipo de cable en la base de datos.
     * 
     * @param tipoCable El tipo de cable a insertar.
     */
    @Override
    public void insertar(TipoCable tipoCable) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "INSERT INTO poo2024.tipo_cable_palma (codigo, descripcion, velocidad) VALUES(?, ?, ?)";
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
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Actualiza un tipo de cable existente en la base de datos.
     * 
     * @param tipoCable El tipo de cable a actualizar.
     */
    @Override
    public void actualizar(TipoCable tipoCable) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "UPDATE poo2024.tipo_cable_palma SET descripcion = ?, velocidad = ? WHERE codigo = ?";
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
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Borra un tipo de cable de la base de datos.
     * 
     * @param tipoCable El tipo de cable a borrar.
     */
    @Override
    public void borrar(TipoCable tipoCable) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "DELETE FROM poo2024.tipo_cable_palma WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, tipoCable.getCodigo());
            pstm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Busca y devuelve todos los tipos de cable en la base de datos.
     * 
     * @return Una lista de todos los tipos de cable.
     * @throws FileNotFoundException si ocurre un error al cargar los tipos de cable.
     */
    @Override
    public List<TipoCable> buscarTodos() throws FileNotFoundException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "SELECT codigo, descripcion, velocidad FROM poo2024.tipo_cable_palma";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            List<TipoCable> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(new TipoCable(rs.getString("codigo"), rs.getString("descripcion"),
                        Integer.parseInt(rs.getString("velocidad"))));
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }
}
