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

import red.dao.ConexionDAO;
import red.dao.EquipoDAO;
import red.dao.TipoCableDAO;
import red.dao.TipoPuertoDAO;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoCable;
import red.modelo.TipoPuerto;
import red.factory.BDConexion;

/**
 * Implementación de la interfaz ConexionDAO para manejar las operaciones CRUD
 * de la entidad Conexion utilizando una base de datos PostgreSQL.
 */
public class ConexionPostgresqlDAO implements ConexionDAO {

    private Map<String, Equipo> equipos;
    private Map<String, TipoCable> tipoCable;
    private Map<String, TipoPuerto> tipoPuerto;

    /**
     * Constructor que inicializa los mapas de equipos, tipos de cables y tipos de
     * puertos.
     * 
     * @throws FileNotFoundException si ocurre un error al cargar los equipos, cables o puertos.
     */
    public ConexionPostgresqlDAO() throws FileNotFoundException {
        equipos = cargarEquipos();
        tipoCable = cargarTipoCable();
        tipoPuerto = cargarTipoPuerto();
    }

    /**
     * Inserta una nueva conexión en la base de datos.
     * 
     * @param conexion La conexión a insertar.
     */
    @Override
    public void insertar(Conexion conexion) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "INSERT INTO poo2024.conexion_palma (equipo1, equipo2, tipo_cable, tipo_puerto1, tipo_puerto2) VALUES(?,?,?,?,?)";
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

    /**
     * Actualiza una conexión existente en la base de datos.
     * 
     * @param conexion La conexión a actualizar.
     */
    @Override
    public void actualizar(Conexion conexion) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "UPDATE poo2024.conexion_palma SET tipo_cable = ?, tipo_puerto1 = ?, tipo_puerto2 = ? WHERE equipo1 = ? AND equipo2 = ?";
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

    /**
     * Elimina una conexión de la base de datos.
     * 
     * @param conexion La conexión a eliminar.
     */
    @Override
    public void borrar(Conexion conexion) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            con.setAutoCommit(false); // Desactivar el autocommit

            // Imprimir para ver si se está intentando eliminar la conexión correcta
            System.out.println("Eliminando conexión entre equipo1: " + conexion.getEquipo1().getCodigo()
                    + " y equipo2: " + conexion.getEquipo2().getCodigo());

            // SQL para eliminar la conexión de la base de datos
            String sql = "DELETE FROM poo2024.conexion_palma WHERE equipo1 = ? AND equipo2 = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, conexion.getEquipo1().getCodigo());
            pstm.setString(2, conexion.getEquipo2().getCodigo());
            pstm.executeUpdate();

            // Confirmar la transacción
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
            }
        }
    }

    /**
     * Busca y devuelve todas las conexiones de la base de datos.
     * 
     * @return Una lista de todas las conexiones.
     * @throws FileNotFoundException si ocurre un error al cargar equipos, cables o puertos.
     */
    @Override
    public List<Conexion> buscarTodos() throws FileNotFoundException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConexion.getConnection();
            String sql = "SELECT equipo1, equipo2, tipo_cable, tipo_puerto1, tipo_puerto2 FROM poo2024.conexion_palma";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            List<Conexion> ret = new ArrayList<>();
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

    /**
     * Carga los equipos desde la base de datos.
     * 
     * @return Un mapa de equipos donde la clave es el código del equipo.
     * @throws FileNotFoundException si ocurre un error al cargar los equipos.
     */
    private Map<String, Equipo> cargarEquipos() throws FileNotFoundException {
        Map<String, Equipo> equipos = new HashMap<>();
        EquipoDAO equipoDAO = new EquipoPostgresqlDAO();
        List<Equipo> eq = equipoDAO.buscarTodos();
        for (Equipo e : eq) {
            equipos.put(e.getCodigo(), e);
        }
        return equipos;
    }

    /**
     * Carga los tipos de cables desde la base de datos.
     * 
     * @return Un mapa de tipos de cables donde la clave es el código del cable.
     * @throws FileNotFoundException si ocurre un error al cargar los tipos de cables.
     */
    private Map<String, TipoCable> cargarTipoCable() throws FileNotFoundException {
        Map<String, TipoCable> cables = new HashMap<>();
        TipoCableDAO cablesDAO = new TipoCablePostgresqlDAO();
        List<TipoCable> cbls = cablesDAO.buscarTodos();
        for (TipoCable c : cbls) {
            cables.put(c.getCodigo(), c);
        }
        return cables;
    }

    /**
     * Carga los tipos de puertos desde la base de datos.
     * 
     * @return Un mapa de tipos de puertos donde la clave es el código del puerto.
     * @throws FileNotFoundException si ocurre un error al cargar los tipos de puertos.
     */
    private Map<String, TipoPuerto> cargarTipoPuerto() throws FileNotFoundException {
        Map<String, TipoPuerto> puertos = new HashMap<>();
        TipoPuertoDAO puertosDAO = new TipoPuertoPostgresqlDAO();
        List<TipoPuerto> prts = puertosDAO.buscarTodos();
        for (TipoPuerto p : prts) {
            puertos.put(p.getCodigo(), p);
        }
        return puertos;
    }
}
