package red.modelo;

import java.util.Objects;

/**
 * Clase que representa un tipo de equipo en una red.
 * Incluye atributos para el código y la descripción.
 */
public class TipoEquipo {

    private String codigo; // Código identificador del tipo de equipo.
    private String descripcion; // Descripción del tipo de equipo.

    /**
     * Constructor que inicializa un TipoEquipo con los valores proporcionados.
     * 
     * @param codigo El código del tipo de equipo.
     * @param descripcion La descripción del tipo de equipo.
     */
    public TipoEquipo(String codigo, String descripcion) {
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

    // Sobrescribir equals para comparar objetos TipoEquipo por su código.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TipoEquipo other = (TipoEquipo) obj;
        return Objects.equals(codigo, other.codigo);
    }

    // Método para representar el TipoEquipo como una cadena de texto.
    @Override
    public String toString() {
        return "TipoEquipo [codigo=" + codigo + ", descripcion=" + descripcion + "]";
    }
}
