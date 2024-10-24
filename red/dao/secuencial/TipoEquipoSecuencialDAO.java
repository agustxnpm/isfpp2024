package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.TipoEquipoDAO;
import red.modelo.TipoEquipo;

/**
 * Clase que implementa el acceso a datos para los tipos de equipos de forma secuencial.
 * Utiliza archivos para leer y escribir los datos de los tipos de equipos.
 */
public class TipoEquipoSecuencialDAO implements TipoEquipoDAO {

    // Lista para almacenar los tipos de equipos.
    private List<TipoEquipo> list;
    private boolean actualizar; // Indica si es necesario actualizar la lista de tipos de equipos.
    private String name; // Nombre del archivo que almacena los tipos de equipos.

    /**
     * Constructor que carga el nombre del archivo desde un archivo de configuración.
     */
    public TipoEquipoSecuencialDAO() {
        // Cargar el nombre del archivo desde config.properties
        ResourceBundle rb = ResourceBundle.getBundle("config");
        name = rb.getString("tipoEquipo");
        actualizar = true; // Indica que se debe leer la lista de tipos de equipos desde el archivo.
    }

    /**
     * Lee los tipos de equipos desde un archivo y los carga en una lista.
     * @param fileName El nombre del archivo desde donde leer.
     * @return Lista de tipos de equipos leídos.
     */
    private List<TipoEquipo> readFromFile(String fileName) throws FileNotFoundException {
        List<TipoEquipo> tipoEquipo = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fileName));

        // Leer cada línea del archivo y convertirla en un objeto TipoEquipo.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            // Leer los datos de la línea y crear un objeto de tipo TipoEquipo.
            if (lineScanner.hasNext()) {
                String codigo = lineScanner.next();
                String descripcion = lineScanner.next();
                tipoEquipo.add(new TipoEquipo(codigo, descripcion));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return tipoEquipo;
    }

    /**
     * Escribe los tipos de equipos en un archivo de forma secuencial.
     * @param list La lista de tipos de equipos a escribir.
     * @param file El nombre del archivo en el que escribir.
     */
    private void writeToFile(List<TipoEquipo> list, String file) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(file);
            // Escribir cada tipo de equipo en una línea del archivo.
            for (TipoEquipo e : list) {
                outFile.format("%s;%s;\n",
                        e.getCodigo(),        // Código del tipo de equipo.
                        e.getDescripcion());  // Descripción del tipo de equipo.
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
     * Inserta un nuevo tipo de equipo en la lista y actualiza el archivo.
     * @param tipoEquipo El tipo de equipo a insertar.
     */
    @Override
    public void insertar(TipoEquipo tipoEquipo) {
        list.add(tipoEquipo);
        writeToFile(list, name);
        actualizar = true; // Marcar que es necesario actualizar la lista desde el archivo.
    }

    /**
     * Actualiza un tipo de equipo existente y actualiza el archivo.
     * @param tipoEquipo El tipo de equipo a actualizar.
     */
    @Override
    public void actualizar(TipoEquipo tipoEquipo) {
        int pos = list.indexOf(tipoEquipo);
        list.set(pos, tipoEquipo);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Borra un tipo de equipo de la lista y actualiza el archivo.
     * @param tipoEquipo El tipo de equipo a borrar.
     */
    @Override
    public void borrar(TipoEquipo tipoEquipo) {
        list.remove(tipoEquipo);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Obtiene todos los tipos de equipos. Si hay cambios, lee desde el archivo.
     * @return Lista de todos los tipos de equipos.
     */
    @Override
    public List<TipoEquipo> buscarTodos() throws FileNotFoundException {
        if (actualizar) {
            list = readFromFile(name);
            actualizar = false; // Marcar que ya está actualizado.
        }
        return list;
    }
}
