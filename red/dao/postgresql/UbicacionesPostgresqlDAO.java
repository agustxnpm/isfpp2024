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

/**
 * Implementación de la interfaz UbicacionDAO para gestionar las operaciones
 * CRUD de la entidad Ubicacion en una base de datos PostgreSQL.
 */
public class UbicacionesPostgresqlDAO implements UbicacionDAO {

    /**
     * Inserta una nueva ubicación en la base de datos.
     * 
     * @param ubicacion La ubicación a insertar.
     */
    @Override
    public void insertar(Ubicacion ubicacion) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "INSERT INTO poo2024.ubicaciones_palma (codigo, descripcion) VALUES(?, ?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ubicacion.getCodigo());
            pstm.setString(2, ubicacion.getDescripcion());
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
     * Actualiza una ubicación existente en la base de datos.
     * 
     * @param ubicacion La ubicación a actualizar.
     */
    @Override
    public void actualizar(Ubicacion ubicacion) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "UPDATE poo2024.ubicaciones_palma SET descripcion = ? WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ubicacion.getDescripcion());
            pstm.setString(2, ubicacion.getCodigo());
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
     * Elimina una ubicación de la base de datos.
     * 
     * @param ubicacion La ubicación a eliminar.
     */
    @Override
    public void borrar(Ubicacion ubicacion) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "DELETE FROM poo2024.ubicaciones_palma WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, ubicacion.getCodigo());
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
     * Busca y devuelve todas las ubicaciones de la base de datos.
     * 
     * @return Una lista con todas las ubicaciones.
     * @throws FileNotFoundException si ocurre un error al cargar las ubicaciones.
     */
    @Override
    public List<Ubicacion> buscarTodos() throws FileNotFoundException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "SELECT codigo, descripcion FROM poo2024.ubicaciones_palma";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            List<Ubicacion> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(new Ubicacion(rs.getString("codigo"), rs.getString("descripcion")));
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
