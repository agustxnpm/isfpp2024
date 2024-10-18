package red.modelo;

import java.util.Objects;

/**
 * Clase que representa una conexión entre dos equipos en una red.
 * Cada conexión incluye dos equipos, un tipo de cable y dos tipos de puertos.
 */
public class Conexion {

    private Equipo equipo1; // Primer equipo de la conexión.
    private Equipo equipo2; // Segundo equipo de la conexión.
    private TipoCable tipoCable; // Tipo de cable utilizado en la conexión.
    private TipoPuerto tipoPuerto1; // Tipo de puerto del primer equipo.
    private TipoPuerto tipoPuerto2; // Tipo de puerto del segundo equipo.

    /**
     * Constructor que inicializa una conexión entre dos equipos.
     * 
     * @param equipo1 El primer equipo de la conexión.
     * @param equipo2 El segundo equipo de la conexión.
     * @param tipoCable El tipo de cable utilizado.
     * @param tipoPuerto1 El tipo de puerto en el primer equipo.
     * @param tipoPuerto2 El tipo de puerto en el segundo equipo.
     * @throws IllegalArgumentException Si ambos equipos son iguales.
     */
    public Conexion(Equipo equipo1, Equipo equipo2, TipoCable tipoCable, TipoPuerto tipoPuerto1,
                    TipoPuerto tipoPuerto2) {
        super();
        // Verificar que los equipos sean diferentes.
        if (equipo1.equals(equipo2)) {
            throw new IllegalArgumentException("Los equipos en una conexión deben ser diferentes.");
        }
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.tipoCable = tipoCable;
        this.tipoPuerto1 = tipoPuerto1;
        this.tipoPuerto2 = tipoPuerto2;
    }

    // Métodos de acceso (getters y setters).
    public TipoPuerto getTipoPuerto1() {
        return tipoPuerto1;
    }

    public void setTipoPuerto1(TipoPuerto tipoPuerto1) {
        this.tipoPuerto1 = tipoPuerto1;
    }

    public TipoPuerto getTipoPuerto2() {
        return tipoPuerto2;
    }

    public void setTipoPuerto2(TipoPuerto tipoPuerto2) {
        this.tipoPuerto2 = tipoPuerto2;
    }

    public Equipo getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }

    public TipoCable getTipoCable() {
        return tipoCable;
    }

    public void setTipoCable(TipoCable tipoCable) {
        this.tipoCable = tipoCable;
    }

    // Métodos sobrescritos para igualdad y representación en cadena.
    @Override
    public int hashCode() {
        return Objects.hash(equipo1, equipo2, tipoCable);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Conexion other = (Conexion) obj;
        // Las conexiones son iguales si tienen los mismos equipos y tipo de cable.
        return Objects.equals(equipo1, other.equipo1) && Objects.equals(equipo2, other.equipo2)
                && Objects.equals(tipoCable, other.tipoCable);
    }

    @Override
    public String toString() {
        return "Conexion: " + equipo1.getCodigo() + " <-> " + equipo2.getCodigo() + " (Cable: "
                + tipoCable.getDescripcion() + ")";
    }
}
