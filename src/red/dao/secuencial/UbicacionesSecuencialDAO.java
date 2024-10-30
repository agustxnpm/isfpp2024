package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.UbicacionDAO;
import red.modelo.Ubicacion;

/**
 * Clase que implementa el acceso a datos para las ubicaciones de forma secuencial.
 * Utiliza archivos para leer y escribir los datos de las ubicaciones.
 */
public class UbicacionesSecuencialDAO implements UbicacionDAO {

    // Lista para almacenar las ubicaciones.
    private List<Ubicacion> list;
    private boolean actualizar; // Indica si es necesario actualizar la lista de ubicaciones.
    private String name; // Nombre del archivo que almacena las ubicaciones.

    /**
     * Constructor que carga el nombre del archivo desde un archivo de configuración.
     */
    public UbicacionesSecuencialDAO() {
        // Cargar el nombre del archivo desde config.properties
        ResourceBundle rb = ResourceBundle.getBundle("config");
        name = rb.getString("ubicacion");
        actualizar = true; // Indica que se debe leer la lista de ubicaciones desde el archivo.
    }

    /**
     * Lee las ubicaciones desde un archivo y las carga en una lista.
     * @param fileName El nombre del archivo desde donde leer.
     * @return Lista de ubicaciones leídas.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    private List<Ubicacion> readFromFile(String fileName) throws FileNotFoundException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fileName));

        // Leer cada línea del archivo y convertirla en un objeto Ubicacion.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            // Leer los datos de la línea y crear un objeto de tipo Ubicacion.
            if (lineScanner.hasNext()) {
                String codigo = lineScanner.next();
                String descripcion = lineScanner.next();
                ubicaciones.add(new Ubicacion(codigo, descripcion));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return ubicaciones;
    }

    /**
     * Escribe las ubicaciones en un archivo de forma secuencial.
     * @param list La lista de ubicaciones a escribir.
     * @param file El nombre del archivo en el que escribir.
     */
    private void writeToFile(List<Ubicacion> list, String file) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(file);
            // Escribir cada ubicación en una línea del archivo.
            for (Ubicacion e : list) {
                outFile.format("%s;%s;\n", 
                        e.getCodigo(),        // Código de la ubicación.
                        e.getDescripcion());  // Descripción de la ubicación.
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
     * Inserta una nueva ubicación en la lista y actualiza el archivo.
     * @param ubicacion La ubicación a insertar.
     */
    @Override
    public void insertar(Ubicacion ubicacion) {
        list.add(ubicacion);
        writeToFile(list, name);
        actualizar = true; // Marcar que es necesario actualizar la lista desde el archivo.
    }

    /**
     * Actualiza una ubicación existente y actualiza el archivo.
     * @param ubicacion La ubicación a actualizar.
     */
    @Override
    public void actualizar(Ubicacion ubicacion) {
        int pos = list.indexOf(ubicacion);
        list.set(pos, ubicacion);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Borra una ubicación de la lista y actualiza el archivo.
     * @param ubicacion La ubicación a borrar.
     */
    @Override
    public void borrar(Ubicacion ubicacion) {
        list.remove(ubicacion);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Obtiene todas las ubicaciones. Si hay cambios, lee desde el archivo.
     * @return Lista de todas las ubicaciones.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    @Override
    public List<Ubicacion> buscarTodos() throws FileNotFoundException {
        if (actualizar) {
            list = readFromFile(name);
            actualizar = false; // Marcar que ya está actualizado.
        }
        return list;
    }
}
