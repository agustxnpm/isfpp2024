package red.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

/**
 * Clase de utilidad para la conexión a la base de datos.
 * Implementa un patrón Singleton para mantener una única instancia de la conexión.
 */
public class BDConexion {
    private static Connection con = null;

    /**
     * Obtiene la conexión a la base de datos utilizando los datos de configuración
     * del archivo `jdbc.properties`.
     * 
     * @return La instancia de la conexión a la base de datos.
     * @throws RuntimeException si ocurre un error al crear la conexión.
     */
    public static Connection getConnection() {
        try {
            if (con == null) {
                // Añade un hook para cerrar la conexión al finalizar la JVM.
                Runtime.getRuntime().addShutdownHook(new MiShDwnHook());
                ResourceBundle rb = ResourceBundle.getBundle("jdbc");
                String driver = rb.getString("driver");
                String url = rb.getString("url");
                String usr = rb.getString("usr");
                String pwd = rb.getString("pwd");
                Class.forName(driver);
                con = DriverManager.getConnection(url, usr, pwd);
            }
            return con;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error al crear la conexion", ex);
        }
    }

    /**
     * Clase interna que extiende `Thread` para cerrar la conexión a la base de datos
     * justo antes de que finalice la JVM.
     */
    public static class MiShDwnHook extends Thread {
        /**
         * Método ejecutado por la JVM antes de finalizar, que cierra la conexión a la
         * base de datos.
         */
        public void run() {
            try {
                Connection con = BDConexion.getConnection();
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }
}
