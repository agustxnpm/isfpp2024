package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.TipoCableDAO;
import red.modelo.TipoCable;

/**
 * Clase que implementa el acceso a datos para los tipos de cables de forma secuencial.
 * Utiliza archivos para leer y escribir los datos de los tipos de cables.
 */
public class TipoCableSecuencialDAO implements TipoCableDAO {

    // Lista para almacenar los tipos de cables.
    private List<TipoCable> list;
    private boolean actualizar; // Indica si es necesario actualizar la lista de tipos de cables.
    private String name; // Nombre del archivo que almacena los tipos de cables.

    /**
     * Constructor que carga el nombre del archivo desde un archivo de configuración.
     */
    public TipoCableSecuencialDAO() {
        // Cargar el nombre del archivo desde config.properties
        ResourceBundle rb = ResourceBundle.getBundle("config");
        name = rb.getString("tipoCable");
        actualizar = true; // Indica que se debe leer la lista de tipos de cables desde el archivo.
    }

    /**
     * Lee los tipos de cables desde un archivo y los carga en una lista.
     * @param fileName El nombre del archivo desde donde leer.
     * @return Lista de tipos de cables leídos.
     */
    private List<TipoCable> readFromFile(String fileName) throws FileNotFoundException {
        List<TipoCable> tipoCables = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fileName));

        // Leer cada línea del archivo y convertirla en un objeto TipoCable.
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            // Leer los datos de la línea y crear un objeto de tipo TipoCable.
            if (lineScanner.hasNext()) {
                String codigo = lineScanner.next();
                String descripcion = lineScanner.next();
                int velocidad = Integer.parseInt(lineScanner.next());
                tipoCables.add(new TipoCable(codigo, descripcion, velocidad));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return tipoCables;
    }

    /**
     * Escribe los tipos de cables en un archivo de forma secuencial.
     * @param list La lista de tipos de cables a escribir.
     * @param file El nombre del archivo en el que escribir.
     */
    private void writeToFile(List<TipoCable> list, String file) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(file);
            // Escribir cada tipo de cable en una línea del archivo.
            for (TipoCable e : list) {
                outFile.format("%s;%s;%s;\n", 
                        e.getCodigo(),            // Código del tipo de cable.
                        e.getDescripcion(),       // Descripción del tipo de cable.
                        e.getVelocidad());        // Velocidad del tipo de cable.
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
     * Inserta un nuevo tipo de cable en la lista y actualiza el archivo.
     * @param tipoCable El tipo de cable a insertar.
     */
    @Override
    public void insertar(TipoCable tipoCable) {
        list.add(tipoCable);
        writeToFile(list, name);
        actualizar = true; // Marcar que es necesario actualizar la lista desde el archivo.
    }

    /**
     * Actualiza un tipo de cable existente y actualiza el archivo.
     * @param tipoCable El tipo de cable a actualizar.
     */
    @Override
    public void actualizar(TipoCable tipoCable) {
        int pos = list.indexOf(tipoCable);
        list.set(pos, tipoCable);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Borra un tipo de cable de la lista y actualiza el archivo.
     * @param tipoCable El tipo de cable a borrar.
     */
    @Override
    public void borrar(TipoCable tipoCable) {
        list.remove(tipoCable);
        writeToFile(list, name);
        actualizar = true;
    }

    /**
     * Obtiene todos los tipos de cables. Si hay cambios, lee desde el archivo.
     * @return Lista de todos los tipos de cables.
     */
    @Override
    public List<TipoCable> buscarTodos() throws FileNotFoundException {
        if (actualizar) {
            list = readFromFile(name);
            actualizar = false; // Marcar que ya está actualizado.
        }
        return list;
    }
}
