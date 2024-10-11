package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.TipoPuerto;

/**
 * Interfaz para el servicio de gestión de tipos de puerto.
 * Define los métodos que deben implementarse para manejar las operaciones CRUD sobre los tipos de puerto.
 */
public interface TipoPuertoService {

    /**
     * Inserta un nuevo tipo de puerto en el sistema.
     * 
     * @param tipoPuerto El tipo de puerto a insertar.
     */
    void insertar(TipoPuerto tipoPuerto);

    /**
     * Actualiza la información de un tipo de puerto existente.
     * 
     * @param tipoPuerto El tipo de puerto a actualizar.
     */
    void actualizar(TipoPuerto tipoPuerto);

    /**
     * Elimina un tipo de puerto del sistema.
     * 
     * @param tipoPuerto El tipo de puerto a eliminar.
     */
    void borrar(TipoPuerto tipoPuerto);

    /**
     * Busca y retorna todos los tipos de puerto almacenados.
     * 
     * @return Lista de todos los tipos de puerto.
     * @throws FileNotFoundException Si no se puede acceder a la fuente de datos.
     */
    List<TipoPuerto> buscarTodos() throws FileNotFoundException;
}
