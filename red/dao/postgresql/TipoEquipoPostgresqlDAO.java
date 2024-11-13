package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import red.dao.TipoEquipoDAO;
import red.factory.BDConexion;
import red.modelo.TipoEquipo;

/**
 * Implementación de la interfaz TipoEquipoDAO para gestionar las operaciones
 * CRUD de la entidad TipoEquipo en una base de datos PostgreSQL.
 */
public class TipoEquipoPostgresqlDAO implements TipoEquipoDAO {

    /**
     * Inserta un nuevo tipo de equipo en la base de datos.
     * 
     * @param tipoEquipo El tipo de equipo a insertar.
     */
    @Override
    public void insertar(TipoEquipo tipoEquipo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "INSERT INTO poo2024.tipo_equipo_palma (codigo, descripcion) VALUES(?, ?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, tipoEquipo.getCodigo());
            pstm.setString(2, tipoEquipo.getDescripcion());
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
     * Actualiza un tipo de equipo existente en la base de datos.
     * 
     * @param tipoEquipo El tipo de equipo a actualizar.
     */
    @Override
    public void actualizar(TipoEquipo tipoEquipo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "UPDATE poo2024.tipo_equipo_palma SET descripcion = ? WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, tipoEquipo.getDescripcion());
            pstm.setString(2, tipoEquipo.getCodigo());
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
     * Elimina un tipo de equipo de la base de datos.
     * 
     * @param tipoEquipo El tipo de equipo a eliminar.
     */
    @Override
    public void borrar(TipoEquipo tipoEquipo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "DELETE FROM poo2024.tipo_equipo_palma WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, tipoEquipo.getCodigo());
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
     * Busca y devuelve todos los tipos de equipo de la base de datos.
     * 
     * @return Una lista con todos los tipos de equipo.
     * @throws FileNotFoundException si ocurre un error al cargar los tipos de
     *                               equipo.
     */
    @Override
    public List<TipoEquipo> buscarTodos() throws FileNotFoundException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "SELECT codigo, descripcion FROM poo2024.tipo_equipo_palma";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            List<TipoEquipo> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(new TipoEquipo(rs.getString("codigo"), rs.getString("descripcion")));
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
