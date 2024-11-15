package red.factory;

import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * Clase Factory que implementa el patrón de diseño Factory para instanciar objetos de forma dinámica
 * y mantener una única instancia de cada uno en una tabla hash.
 */
public class Factory {
    // Tabla hash para almacenar instancias únicas de los objetos creados.
    private static Hashtable<String, Object> instancias = new Hashtable<String, Object>();

    /**
     * Obtiene una instancia de un objeto a partir de su nombre. Si el objeto ya existe
     * en la tabla hash, se devuelve la instancia existente; de lo contrario, se crea
     * una nueva instancia y se almacena.
     * 
     * @param objName El nombre del objeto a instanciar.
     * @return La instancia del objeto solicitada.
     * @throws RuntimeException si ocurre algún error durante la instanciación del objeto.
     */
    public static Object getInstancia(String objName) {
        try {
            // Verificar si ya existe una instancia del objeto en la tabla hash.
            Object obj = instancias.get(objName);
            // Si no existe, se instancia y se agrega a la tabla.
            if (obj == null) {
                ResourceBundle rb = ResourceBundle.getBundle("factory");
                String sClassname = rb.getString(objName);
                obj = Class.forName(sClassname).getDeclaredConstructor().newInstance();
                // Agregar la nueva instancia a la tabla hash.
                instancias.put(objName, obj);
            }
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
