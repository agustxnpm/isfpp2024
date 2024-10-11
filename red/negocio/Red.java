package red.negocio;

import java.util.List;
import red.excepciones.EquipoRepetidoException;
import red.modelo.*;
import red.servicio.ConexionService;
import red.servicio.ConexionServiceImp;
import red.servicio.EquipoService;
import red.servicio.EquipoServiceImp;
import red.servicio.UbicacionService;
import red.servicio.UbicacionServiceImp;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Clase que representa una red de equipos.
 * Utiliza el patrón Singleton para asegurar que solo haya una instancia de Red.
 * Gestiona la lista de equipos, conexiones y ubicaciones en la red.
 */
public class Red {

    private static Red red = null; // Instancia única de Red (Singleton).

    private String nombre; // Nombre de la red.
    private List<Equipo> equipos; // Lista de equipos de la red.
    private EquipoService equipoService; // Servicio para gestionar los equipos.
    private List<Conexion> conexiones; // Lista de conexiones de la red.
    private ConexionService conexionService; // Servicio para gestionar las conexiones.
    private List<Ubicacion> ubicaciones; // Lista de ubicaciones de la red.
    private UbicacionService ubicacionService; // Servicio para gestionar las ubicaciones.

    /**
     * Método para obtener la instancia única de la red.
     * 
     * @return La instancia única de Red.
     * @throws FileNotFoundException Si no se pueden cargar los datos desde el almacenamiento.
     */
    public static Red getRed() throws FileNotFoundException {
        if (red == null) {
            red = new Red();
        }
        return red;
    }

    /**
     * Constructor privado para inicializar la red.
     * Carga los equipos, conexiones y ubicaciones desde los servicios.
     * 
     * @throws FileNotFoundException Si no se encuentran los archivos de datos.
     */
    private Red() throws FileNotFoundException {
        super();
        equipos = new ArrayList<>();
        equipoService = new EquipoServiceImp();
        equipos.addAll(equipoService.buscarTodos());
        
        conexiones = new ArrayList<>();
        conexionService = new ConexionServiceImp();
        conexiones.addAll(conexionService.buscarTodos());
        
        ubicaciones = new ArrayList<>();
        ubicacionService = new UbicacionServiceImp();
        ubicaciones.addAll(ubicacionService.buscarTodos());
    }

    // Métodos de acceso (getters y setters).
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Conexion> getConexiones() {
        return conexiones;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public List<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    @Override
    public String toString() {
        return "Red [nombre=" + nombre + "]";
    }

    /**
     * Agrega un nuevo equipo a la red.
     * 
     * @param codigo        Código del equipo.
     * @param modelo        Modelo del equipo.
     * @param marca         Marca del equipo.
     * @param descripcion   Descripción del equipo.
     * @param ubicacion     Ubicación del equipo.
     * @param tipoEquipo    Tipo de equipo.
     * @param cantPuertos   Cantidad de puertos.
     * @param tipoPuerto    Tipo de puerto.
     * @param estado        Estado del equipo (activo/inactivo).
     * @return La instancia del equipo creado.
     * @throws EquipoRepetidoException Si ya existe un equipo con el mismo código.
     */
    public Equipo agregarEquipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
            TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto, boolean estado) throws EquipoRepetidoException {
        Equipo nuevoEquipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
                tipoPuerto, estado);

        // Verificar que no se añadan equipos con un código ya existente.
        if (equipos.contains(nuevoEquipo))
            throw new EquipoRepetidoException("El equipo ya existe en la red");

        // Agregar el nuevo equipo a la lista y persistir en el servicio.
        equipos.add(nuevoEquipo);
        equipoService.insertar(nuevoEquipo);
        return nuevoEquipo;
    }

    /**
     * Agrega un equipo existente a la red.
     * 
     * @param equipo El equipo a agregar.
     * @throws EquipoRepetidoException Si ya existe un equipo con el mismo código.
     */
    public void agregarEquipo(Equipo equipo) throws EquipoRepetidoException {
        // Verificar que no se añadan equipos con un código ya existente.
        if (equipos.contains(equipo))
            throw new EquipoRepetidoException("El equipo ya existe en la red");

        // Agregar el equipo a la lista y persistir en el servicio.
        equipos.add(equipo);
        equipoService.insertar(equipo);
    }

    /**
     * Modifica un equipo existente en la red.
     * 
     * @param equipo El equipo a modificar.
     */
    public void modificarEquipo(Equipo equipo) {
        int pos = equipos.indexOf(equipo);
        equipos.set(pos, equipo);
        equipoService.actualizar(equipo);
    }

    /**
     * Borra un equipo de la red.
     * 
     * @param equipo El equipo a borrar.
     */
    public void borrarEquipo(Equipo equipo) {
        equipos.remove(buscarEquipo(equipo));
        equipoService.borrar(equipo);
    }

    /**
     * Busca un equipo en la red.
     * 
     * @param equipo El equipo a buscar.
     * @return El equipo encontrado o null si no existe.
     */
    public Equipo buscarEquipo(Equipo equipo) {
        int pos = equipos.indexOf(equipo);
        if (pos == -1)
            return null;
        return equipos.get(pos);
    }
}
