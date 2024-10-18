package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.TipoPuertoDAO;
import red.modelo.TipoPuerto;

/**
 * Clase que implementa el acceso a datos para los tipos de puertos de forma secuencial.
 * Utiliza archivos para leer y escribir los datos de los tipos de puertos.
 */
public class TipoPuertoSecuencialDAO implements TipoPuertoDAO {

    // Lista para almacenar los tipos de puertos.
    private List<TipoPuerto> list;
    private boolean actualizar; // Indica si es necesario actualizar la lista de tipos de puertos.
    private String name; // Nombre del archivo que almacena los tipos de puertos.

    /**
     * Constructor que carga el nombre del archivo desde un archivo de configuración.
     */
    public TipoPuertoSecuencialDAO() {
        // Cargar el nombre del archivo desde config.properties
        ResourceBundle rb = ResourceBundle.getBundle("config");
        name = rb.getString("tipoPuerto");
        actualizar = true; // Indica que se debe leer la lista de tipos de puertos desde el archivo.
    }

    /**
     * Lee los tipos de puertos desde un archivo y los carga en una lista.
     * @param fileName El nombre del archivo desde donde leer.
     * @return Lista de tipos de puertos leídos.
     */
    private List<TipoPuerto> readFromFile(String fileName) throws FileNotFoundException {
        List<TipoPuerto> tipoPuerto = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fileName));

        // Leer cada línea del archivo y convertirla en un objeto TipoPuerto.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            // Leer los datos de la línea y crear un objeto de tipo TipoPuerto.
            if (lineScanner.hasNext()) {
                String codigo = lineScanner.next();
                String descripcion = lineScanner.next();
                int velocidad = Integer.parseInt(lineScanner.next());
                tipoPuerto.add(new TipoPuerto(codigo, descripcion, velocidad));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return tipoPuerto;
    }

    /**
     * Escribe los tipos de puertos en un archivo de forma secuencial.
     * @param list La lista de tipos de puertos a escribir.
     * @param file El nombre del archivo en el que escribir.
     */
    private void writeToFile(List<TipoPuerto> list, String file) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(file);
            // Escribir cada tipo de puerto en una línea del archivo.
            for (TipoPuerto e : list) {
                outFile.format("%s;%s;%s;\n",
                        e.getCodigo(),        // Código del tipo de puerto.
                        e.getDescripcion(),   // Descripción del tipo de puerto.
                        e.getVelocidad());    // Velocidad del tipo de puerto.
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
     * Inserta un nuevo tipo de puerto en la lista y actualiza el archivo.
     * @param tipoPuerto El tipo de puerto a insertar.
     */
    @Override
    public void insertar(TipoPuerto tipoPuerto) {
        list.add(tipoPuerto);
        writeToFile(list, name);
        actualizar = true; // Marcar que es necesario actualizar la lista desde el archivo.
    }

    /**
     * Actualiza un tipo de puerto existente y actualiza el archivo.
     * @param tipoPuerto El tipo de puerto a actualizar.
     */
    @Override
    public void actualizar(TipoPuerto tipoPuerto) {
        int pos = list.indexOf(tipoPuerto);
        list.set(pos, tipoPuerto);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Borra un tipo de puerto de la lista y actualiza el archivo.
     * @param tipoPuerto El tipo de puerto a borrar.
     */
    @Override
    public void borrar(TipoPuerto tipoPuerto) {
        list.remove(tipoPuerto);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Obtiene todos los tipos de puertos. Si hay cambios, lee desde el archivo.
     * @return Lista de todos los tipos de puertos.
     */
    @Override
    public List<TipoPuerto> buscarTodos() throws FileNotFoundException {
        if (actualizar) {
            list = readFromFile(name);
            actualizar = false; // Marcar que ya está actualizado.
        }
        return list;
    }
}
