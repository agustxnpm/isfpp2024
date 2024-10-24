package red.modelo;

import java.util.Objects;

/**
 * Clase que representa un tipo de cable de red.
 * Incluye atributos para el código, descripción y velocidad de transmisión.
 */
public class TipoCable {

    private String codigo; // Código identificador del tipo de cable.
    private String descripcion; // Descripción del tipo de cable.
    private int velocidad; // Velocidad máxima del cable en Mbps.

    /**
     * Constructor que inicializa un TipoCable con los valores proporcionados.
     * 
     * @param codigo El código del tipo de cable.
     * @param descripcion La descripción del tipo de cable.
     * @param velocidad La velocidad de transmisión del cable en Mbps.
     */
    public TipoCable(String codigo, String descripcion, int velocidad) {
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

    // Sobrescribir equals para comparar objetos TipoCable por su código.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TipoCable other = (TipoCable) obj;
        return Objects.equals(codigo, other.codigo);
    }

    // Método para representar el TipoCable como una cadena de texto.
    @Override
    public String toString() {
        return "TipoCable [codigo=" + codigo + ", descripcion=" + descripcion + ", velocidad=" + velocidad + "]";
    }
}
