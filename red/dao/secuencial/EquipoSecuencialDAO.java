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

import red.dao.EquipoDAO;
import red.dao.TipoPuertoDAO;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;
import red.dao.TipoEquipoDAO;
import red.dao.UbicacionDAO;

/**
 * Clase que implementa el acceso a datos para los equipos de forma secuencial.
 * Utiliza archivos para leer y escribir los equipos.
 */
public class EquipoSecuencialDAO implements EquipoDAO {

    // Mapas para almacenar tipos de equipos, ubicaciones, tipos de puertos, y lista de equipos.
    private Map<String, TipoEquipo> tipoEquipo;
    private Map<String, Ubicacion> ubicaciones;
    private Map<String, TipoPuerto> tipoPuerto;
    private List<Equipo> list;
    private boolean actualizar; // Indica si es necesario actualizar la lista de equipos.
    private String name; // Nombre del archivo que almacena los equipos.

    /**
     * Constructor que carga los tipos de equipos, ubicaciones y tipos de puertos.
     * También obtiene el nombre del archivo de equipos desde un archivo de configuración.
     */
    public EquipoSecuencialDAO() throws FileNotFoundException {
        tipoEquipo = cargarTipoEquipo();
        ubicaciones = cargarUbicaciones();
        tipoPuerto = cargarTipoPuerto();

        // Cargar el nombre del archivo desde config.properties
        ResourceBundle rb = ResourceBundle.getBundle("config");
        name = rb.getString("equipo");
        actualizar = true; // Indica que se debe leer la lista de equipos del archivo.
    }

    /**
     * Lee los equipos desde un archivo y los carga en una lista.
     * @param fileName El nombre del archivo desde donde leer.
     * @return Lista de equipos leídos.
     */
    private List<Equipo> readFromFile(String fileName) throws FileNotFoundException {
        List<Equipo> equipos = new ArrayList<>();

        Scanner fileScanner = new Scanner(new File(fileName));
        String codigo, modelo, marca, descripcion;
        Ubicacion ubicacion;
        TipoEquipo tipoEquipo;
        int cantPuertos;
        TipoPuerto tipoPuerto;

        // Leer cada línea del archivo y convertirla en un objeto Equipo.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            // Leer los datos de la línea y crear un objeto de tipo Equipo.
            if (lineScanner.hasNext()) {
                codigo = lineScanner.next();
                descripcion = lineScanner.hasNext() ? lineScanner.next() : "";
                marca = lineScanner.hasNext() ? lineScanner.next() : "";
                modelo = lineScanner.hasNext() ? lineScanner.next() : "";
                tipoEquipo = this.tipoEquipo.get(lineScanner.next());
                String ubicacionCodigo = lineScanner.next();
                ubicacion = ubicaciones.get(ubicacionCodigo);

                // Leer la información de los puertos y sus cantidades.
                String puertosInfo = lineScanner.next();
                String[] puertoParts = puertosInfo.split(",");

                // Guardar el primer puerto para instanciar el objeto Equipo.
                String tipoPuertoCodigo = puertoParts[0];
                cantPuertos = Integer.parseInt(puertoParts[1]);

                // Crear una instancia del objeto Equipo.
                Equipo equipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
                        this.tipoPuerto.get(tipoPuertoCodigo), false);

                // Agregar los puertos adicionales al equipo.
                for (int i = 2; i < puertoParts.length; i += 2) {
                    tipoPuertoCodigo = puertoParts[i];
                    cantPuertos = Integer.parseInt(puertoParts[i + 1]);
                    tipoPuerto = this.tipoPuerto.get(tipoPuertoCodigo);
                    equipo.agregarPuerto(cantPuertos, tipoPuerto);
                }

                // Leer las direcciones IP si existen y agregarlas al equipo.
                if (lineScanner.hasNext()) {
                    String ipInfo = lineScanner.next();
                    String[] direccionesIpArray = ipInfo.split(",");
                    for (String ip : direccionesIpArray) {
                        if (!ip.isEmpty()) {
                            equipo.agregarIp(ip);
                        }
                    }
                }

                // Leer el estado del equipo (true/false) y asignarlo.
                boolean estado = Boolean.parseBoolean(lineScanner.next());
                equipo.setEstado(estado);

                equipos.add(equipo); // Agregar el equipo a la lista.
            }
            lineScanner.close();
        }
        fileScanner.close();
        return equipos;
    }

    /**
     * Escribe los equipos en un archivo de forma secuencial.
     * @param list La lista de equipos a escribir.
     * @param file El nombre del archivo en el que escribir.
     */
    private void writeToFile(List<Equipo> list, String file) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(file);
            for (Equipo e : list) {
                // Formateo de los datos del equipo en el archivo.
                StringBuilder ipAddresses = new StringBuilder();
                List<String> ips = e.getDireccionesIp();
                for (int i = 0; i < ips.size(); i++) {
                    ipAddresses.append(ips.get(i));
                    if (i < ips.size() - 1) {
                        ipAddresses.append(",");
                    }
                }

                outFile.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;\n",
                    e.getCodigo(),                       // Código del equipo.
                    e.getDescripcion() != null ? e.getDescripcion() : "",   
                    e.getMarca() != null ? e.getMarca() : "",             
                    e.getModelo() != null ? e.getModelo() : "",        
                    e.getTipoEquipo().getCodigo(),          
                    e.getUbicacion().getCodigo(),         
                    e.getPuertosInfo(),                        
                    ipAddresses.toString(),                    
                    e.isEstado()                               
                );
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error al crear el archivo.");
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error al escribir en el archivo.");
        } finally {
            if (outFile != null) {
                outFile.close(); // Cerrar el archivo para liberar recursos.
            }
        }
    }

    /**
     * Carga todos los tipos de equipos desde el DAO y los almacena en un mapa.
     * @return Mapa de tipos de equipos.
     */
    private Map<String, TipoEquipo> cargarTipoEquipo() throws FileNotFoundException {
        Map<String, TipoEquipo> tipoEquipos = new HashMap<>();
        TipoEquipoDAO tipoEquiposDAO = new TipoEquipoSecuencialDAO();
        List<TipoEquipo> tEq = tipoEquiposDAO.buscarTodos();
        for (TipoEquipo t : tEq) {
            tipoEquipos.put(t.getCodigo(), t);
        }
        return tipoEquipos;
    }

    /**
     * Carga todas las ubicaciones desde el DAO y las almacena en un mapa.
     * @return Mapa de ubicaciones.
     */
    private Map<String, Ubicacion> cargarUbicaciones() throws FileNotFoundException {
        Map<String, Ubicacion> ubicaciones = new HashMap<>();
        UbicacionDAO ubicacionesDAO = new UbicacionesSecuencialDAO();
        List<Ubicacion> ub = ubicacionesDAO.buscarTodos();
        for (Ubicacion u : ub) {
            ubicaciones.put(u.getCodigo(), u);
        }
        return ubicaciones;
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
     * Inserta un nuevo equipo en la lista y actualiza el archivo.
     * @param equipo El equipo a insertar.
     */
    @Override
    public void insertar(Equipo equipo) {
        list.add(equipo);
        writeToFile(list, name);
        actualizar = true; // Marcar que es necesario actualizar la lista desde el archivo.
    }

    /**
     * Actualiza un equipo existente y actualiza el archivo.
     * @param equipo El equipo a actualizar.
     */
    @Override
    public void actualizar(Equipo equipo) {
        int pos = list.indexOf(equipo);
        list.set(pos, equipo);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Borra un equipo de la lista y actualiza el archivo.
     * @param equipo El equipo a borrar.
     */
    @Override
    public void borrar(Equipo equipo) {
        list.remove(equipo);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Obtiene todos los equipos. Si hay cambios, lee desde el archivo.
     * @return Lista de todos los equipos.
     */
    @Override
    public List<Equipo> buscarTodos() throws FileNotFoundException {
        if (actualizar) {
            list = readFromFile(name);
            actualizar = false; // Marcar que ya está actualizado.
        }
        return list;
    }
}
