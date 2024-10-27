package red.modelo;

import java.util.Objects;

/**
 * Clase que representa una ubicación física en una red.
 * Incluye atributos para el código y la descripción.
 */
public class Ubicacion {

    private String codigo; // Código identificador de la ubicación.
    private String descripcion; // Descripción de la ubicación.

    /**
     * Constructor que inicializa una Ubicacion con los valores proporcionados.
     * 
     * @param codigo El código de la ubicación.
     * @param descripcion La descripción de la ubicación.
     */
    public Ubicacion(String codigo, String descripcion) {
        super();
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    // Sobrescribir hashCode para uso en colecciones como HashSet.
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    // Sobrescribir equals para comparar objetos Ubicacion por su código.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Ubicacion other = (Ubicacion) obj;
        return Objects.equals(codigo, other.codigo);
    }

    // Método para representar la Ubicacion como una cadena de texto.
    @Override
    public String toString() {
        return "Ubicacion [codigo=" + codigo + ", descripcion=" + descripcion + "]";
    }
}
