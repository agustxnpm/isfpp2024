package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.ConexionDAO;
import red.dao.EquipoDAO;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoCable;
import red.modelo.TipoPuerto;
import red.dao.TipoCableDAO;
import red.dao.TipoPuertoDAO;

/**
 * Clase que implementa el acceso a datos para las conexiones de forma secuencial.
 * Utiliza archivos para leer y escribir las conexiones.
 */
public class ConexionSecuencialDAO implements ConexionDAO {

    // Mapas para almacenar los equipos, tipos de cables y puertos, y lista de conexiones.
    private Map<String, Equipo> equipos;
    private Map<String, TipoCable> tipoCable;
    private Map<String, TipoPuerto> tipoPuerto;
    private List<Conexion> list;
    private boolean actualizar; // Indica si es necesario actualizar la lista de conexiones.
    private String name; // Nombre del archivo que almacena las conexiones.

    /**
     * Constructor que carga los equipos, tipos de cables y tipos de puertos.
     * También obtiene el nombre del archivo de conexiones desde un archivo de configuración.
     */
    public ConexionSecuencialDAO() throws FileNotFoundException {
        equipos = cargarEquipos();
        tipoCable = cargarTipoCable();
        tipoPuerto = cargarTipoPuerto();

        // Cargar el nombre del archivo desde config.properties
        ResourceBundle rb = ResourceBundle.getBundle("config");
        name = rb.getString("conexion");
        actualizar = true; // Indica que se debe leer la lista de conexiones del archivo.
    }

    /**
     * Lee las conexiones desde un archivo y las carga en una lista.
     * @param fileName El nombre del archivo desde donde leer.
     * @return Lista de conexiones leídas.
     */
    private List<Conexion> readFromFile(String fileName) throws FileNotFoundException {
        List<Conexion> conexiones = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fileName));

        // Leer cada línea del archivo y convertirla en una conexión.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            // Leer los datos de la línea y crear un objeto de tipo Conexion.
            if (lineScanner.hasNext()) {
                Equipo equipo1 = equipos.get(lineScanner.next());
                TipoPuerto tipoPuerto1 = tipoPuerto.get(lineScanner.next());
                Equipo equipo2 = equipos.get(lineScanner.next());
                TipoPuerto tipoPuerto2 = tipoPuerto.get(lineScanner.next());
                TipoCable tipoCableConexion = tipoCable.get(lineScanner.next());
                conexiones.add(new Conexion(equipo1, equipo2, tipoCableConexion, tipoPuerto1, tipoPuerto2));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return conexiones;
    }

    /**
     * Escribe las conexiones en un archivo de forma secuencial.
     * @param list La lista de conexiones a escribir.
     * @param file El nombre del archivo en el que escribir.
     */
    private void writeToFile(List<Conexion> list, String file) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(file);
            // Escribir cada conexión en una línea del archivo.
            for (Conexion e : list) {
                outFile.format("%s;%s;%s;%s;%s;\n",
                        e.getEquipo1().getCodigo(),
                        e.getTipoPuerto1().getCodigo(),
                        e.getEquipo2().getCodigo(),
                        e.getTipoPuerto2().getCodigo(),
                        e.getTipoCable().getCodigo());
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error creando el archivo.");
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error al escribir en el archivo.");
        } finally {
            if (outFile != null)
                outFile.close(); // Cerrar el archivo para liberar recursos.
        }
    }

    /**
     * Carga todos los equipos desde el DAO y los almacena en un mapa.
     * @return Mapa de equipos.
     */
    private Map<String, Equipo> cargarEquipos() throws FileNotFoundException {
        Map<String, Equipo> equipos = new HashMap<>();
        EquipoDAO equipoDAO = new EquipoSecuencialDAO();
        List<Equipo> eq = equipoDAO.buscarTodos();
        for (Equipo e : eq) {
            equipos.put(e.getCodigo(), e);
        }
        return equipos;
    }

    /**
     * Carga todos los tipos de cables desde el DAO y los almacena en un mapa.
     * @return Mapa de tipos de cables.
     */
    private Map<String, TipoCable> cargarTipoCable() throws FileNotFoundException {
        Map<String, TipoCable> cables = new HashMap<>();
        TipoCableDAO cablesDAO = new TipoCableSecuencialDAO();
        List<TipoCable> cbls = cablesDAO.buscarTodos();
        for (TipoCable c : cbls) {
            cables.put(c.getCodigo(), c);
        }
        return cables;
    }

    /**
     * Carga todos los tipos de puertos desde el DAO y los almacena en un mapa.
     * @return Mapa de tipos de puertos.
     */
    private Map<String, TipoPuerto> cargarTipoPuerto() throws FileNotFoundException {
        Map<String, TipoPuerto> puertos = new HashMap<>();
        TipoPuertoDAO puertosDAO = new TipoPuertoSecuencialDAO();
        List<TipoPuerto> prts = puertosDAO.buscarTodos();
        for (TipoPuerto p : prts) {
            puertos.put(p.getCodigo(), p);
        }
        return puertos;
    }

    /**
     * Inserta una nueva conexión en la lista y actualiza el archivo.
     * @param conexion La conexión a insertar.
     */
    @Override
    public void insertar(Conexion conexion) {
        list.add(conexion);
        writeToFile(list, name);
        actualizar = true; // Marcar que es necesario actualizar la lista desde el archivo.
    }

    /**
     * Actualiza una conexión existente y actualiza el archivo.
     * @param conexion La conexión a actualizar.
     */
    @Override
    public void actualizar(Conexion conexion) {
        int pos = list.indexOf(conexion);
        list.set(pos, conexion);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Borra una conexión de la lista y actualiza el archivo.
     * @param conexion La conexión a borrar.
     */
    @Override
    public void borrar(Conexion conexion) {
        list.remove(conexion);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Obtiene todas las conexiones. Si hay cambios, lee desde el archivo.
     * @return Lista de todas las conexiones.
     */
    @Override
    public List<Conexion> buscarTodos() throws FileNotFoundException {
        if (actualizar) {
            list = readFromFile(name);
            actualizar = false; // Marcar que ya está actualizado.
        }
        return list;
    }
}
