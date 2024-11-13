package red.dao.postgresql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import red.dao.TipoPuertoDAO;
import red.factory.BDConexion;
import red.modelo.TipoPuerto;

/**
 * Implementación de la interfaz TipoPuertoDAO para gestionar las operaciones
 * CRUD de la entidad TipoPuerto en una base de datos PostgreSQL.
 */
public class TipoPuertoPostgresqlDAO implements TipoPuertoDAO {

    /**
     * Inserta un nuevo tipo de puerto en la base de datos.
     * 
     * @param tipoPuerto El tipo de puerto a insertar.
     */
    @Override
    public void insertar(TipoPuerto tipoPuerto) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "INSERT INTO poo2024.tipo_puerto_palma (codigo, descripcion, velocidad) VALUES(?, ?, ?)";
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
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Actualiza un tipo de puerto existente en la base de datos.
     * 
     * @param tipoPuerto El tipo de puerto a actualizar.
     */
    @Override
    public void actualizar(TipoPuerto tipoPuerto) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "UPDATE poo2024.tipo_puerto_palma SET descripcion = ?, velocidad = ? WHERE codigo = ?";
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
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Elimina un tipo de puerto de la base de datos.
     * 
     * @param tipoPuerto El tipo de puerto a eliminar.
     */
    @Override
    public void borrar(TipoPuerto tipoPuerto) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "DELETE FROM poo2024.tipo_puerto_palma WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, tipoPuerto.getCodigo());
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
     * Busca y devuelve todos los tipos de puerto de la base de datos.
     * 
     * @return Una lista con todos los tipos de puerto.
     * @throws FileNotFoundException si ocurre un error al cargar los tipos de
     *                               puerto.
     */
    @Override
    public List<TipoPuerto> buscarTodos() throws FileNotFoundException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "SELECT codigo, descripcion, velocidad FROM poo2024.tipo_puerto_palma";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            List<TipoPuerto> ret = new ArrayList<>();
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
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }
}
