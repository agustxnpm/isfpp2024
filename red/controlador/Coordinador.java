package red.controlador;

import java.util.List;

import red.excepciones.EquipoRepetidoException;
import red.interfaz.Interfaz;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.Ubicacion;
import red.negocio.Calculo;
import red.negocio.Red;

/**
 * Clase que actúa como coordinador entre la capa de negocio y la capa de interfaz.
 * Facilita la interacción entre los objetos de la red y la interfaz de usuario.
 */
public class Coordinador {

    // Atributos que representan la red, la interfaz y las operaciones de cálculo.
    private Red red;
    private Interfaz interfaz;
    private Calculo calculo;

    /**
     * Obtiene la instancia de la red.
     * @return La red gestionada por el coordinador.
     */
    public Red getRed() {
        return red;
    }

    /**
     * Establece la red que gestionará el coordinador.
     * @param red La red a establecer.
     */
    public void setRed(Red red) {
        this.red = red;
    }

    /**
     * Obtiene la interfaz asociada al coordinador.
     * @return La interfaz de usuario.
     */
    public Interfaz getInterfaz() {
        return interfaz;
    }

    /**
     * Establece la interfaz que gestionará el coordinador.
     * @param interfaz La interfaz a establecer.
     */
    public void setInterfaz(Interfaz interfaz) {
        this.interfaz = interfaz;
    }

    /**
     * Obtiene el objeto de cálculos.
     * @return El objeto de cálculos.
     */
    public Calculo getCalculo() {
        return calculo;
    }

    /**
     * Establece el objeto de cálculos.
     * @param calculo El objeto de cálculos a establecer.
     */
    public void setCalculo(Calculo calculo) {
        this.calculo = calculo;
    }

    /**
     * Busca un equipo en la red.
     * @param equipo El equipo a buscar.
     * @return El equipo encontrado o null si no existe.
     */
    public Equipo buscarEquipo(Equipo equipo) {
        return red.buscarEquipo(equipo);
    }

    /**
     * Busca un equipo en la red por su código.
     * @param codigo El código del equipo.
     * @return El equipo encontrado o null si no existe.
     */
    public Equipo buscarEquipo(String codigo) {
        for (Equipo e : red.getEquipos()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Agrega un nuevo equipo a la red.
     * @param equipo El equipo a agregar.
     * @throws EquipoRepetidoException Si el equipo ya existe en la red.
     */
    public void agregarEquipo(Equipo equipo) throws EquipoRepetidoException {
        red.agregarEquipo(equipo);
    }

    /**
     * Lista todos los equipos de la red.
     * @return Una lista de todos los equipos.
     */
    public List<Equipo> listarEquipos() {
        return red.getEquipos();
    }

    /**
     * Lista todas las conexiones de la red.
     * @return Una lista de todas las conexiones.
     */
    public List<Conexion> listarConexiones() {
        return red.getConexiones();
    }

    /**
     * Lista todas las ubicaciones de la red.
     * @return Una lista de todas las ubicaciones.
     */
    public List<Ubicacion> listarUbicaciones() {
        return red.getUbicaciones();
    }
}
