package red.modelo;

import java.util.Objects;

/**
 * Clase que representa un tipo de puerto de red.
 * Incluye atributos para el código, la descripción y la velocidad de transmisión.
 */
public class TipoPuerto {

    private String codigo; // Código identificador del tipo de puerto.
    private String descripcion; // Descripción del tipo de puerto.
    private int velocidad; // Velocidad máxima del puerto en Mbps.

    /**
     * Constructor que inicializa un TipoPuerto con los valores proporcionados.
     * 
     * @param codigo El código del tipo de puerto.
     * @param descripcion La descripción del tipo de puerto.
     * @param velocidad La velocidad de transmisión del puerto en Mbps.
     */
    public TipoPuerto(String codigo, String descripcion, int velocidad) {
        super();
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.velocidad = velocidad;
    }

    // Métodos de acceso (getters y setters).
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    // Sobrescribir hashCode para uso en colecciones como HashSet.
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    // Sobrescribir equals para comparar objetos TipoPuerto por su código.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TipoPuerto other = (TipoPuerto) obj;
        return Objects.equals(codigo, other.codigo);
    }

    // Método para representar el TipoPuerto como una cadena de texto.
    @Override
    public String toString() {
        return "TipoPuerto [codigo=" + codigo + ", descripcion=" + descripcion + ", velocidad=" + velocidad + "]";
    }
}
