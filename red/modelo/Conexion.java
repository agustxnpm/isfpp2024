package modelo;

import java.util.Objects;

public class Conexion {

	private Equipo equipo1;
	private Equipo equipo2;
	private TipoCable tipoCable;
	public Conexion(Equipo equipo1, Equipo equipo2, TipoCable tipoCable) {
		super();
		if (equipo1.equals(equipo2)) {
            throw new IllegalArgumentException("Los equipos en una conexión deben ser diferentes.");
        }
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.tipoCable = tipoCable;
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
		return Objects.equals(equipo1, other.equipo1) && Objects.equals(equipo2, other.equipo2)
				&& Objects.equals(tipoCable, other.tipoCable);
	}
	@Override
    public String toString() {
        return "Conexion: " + equipo1.getCodigo() + " <-> " + equipo2.getCodigo() + " (Cable: " + tipoCable.getDescripcion() + ")";
    }

	

}
