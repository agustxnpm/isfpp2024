package red.modelo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import red.excepciones.ConexionRepetidaException;
import red.excepciones.EquipoRepetidoException;
import red.excepciones.UbicacionRepetidaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Red {

	private List<Conexion> conexiones;
	private List<Equipo> equipos;
	private List<Ubicacion> ubicaciones;
	private String nombre;

	public Red(String nombre) {
		super();
		this.nombre = nombre;
		conexiones = new ArrayList<Conexion>();
		equipos = new ArrayList<Equipo>();
		ubicaciones = new ArrayList<Ubicacion>();
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(conexiones, equipos, nombre, ubicaciones);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Red other = (Red) obj;
		return Objects.equals(conexiones, other.conexiones) && Objects.equals(equipos, other.equipos)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(ubicaciones, other.ubicaciones);
	}

	/**
	 * Agrega un equipo a la red
	 * 
	 * @param codigo
	 * @param modelo
	 * @param marca
	 * @param descripcion
	 * @param ubicacion
	 * @param tipoEquipo
	 * @param cantPuertos
	 * @param tipoPuerto
	 * @return instancia de Equipo con los parametros dados
	 * @throws EquipoRepetidoException no pueden haber dos equipos con el mismo
	 *                                 codigo por ejemplo dos servidores "SE01"
	 */
	public Equipo agregarEquipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto) throws EquipoRepetidoException {
		Equipo nuevoEquipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
				tipoPuerto);

		// verificar que no se añadan equipos con un mismo codigo (equals de Equipo)
		if (equipos.contains(nuevoEquipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(nuevoEquipo);
		return nuevoEquipo;
	}

	/**
	 * Sobrecarga del metodo agregarEquipo que recibe directamente una instancia de
	 * Equipo
	 * 
	 * @param equipo
	 * @throws EquipoRepetidoException
	 */
	public void agregarEquipo(Equipo equipo) throws EquipoRepetidoException {

		if (equipos.contains(equipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(equipo);
	}

	/**
	 * Sobrecarga del metodo agregarEquipo que recibe un mapa con equipos cargados
	 * 
	 * @param equipos
	 */
	public void agregarEquipo(Map<String, Equipo> equipos) {

		for (Equipo e : equipos.values()) {
			agregarEquipo(e);
		}
	}

	/**
	 * Agrega una ubicacion a la red
	 * 
	 * @param codigo
	 * @param descripcion
	 * @return instancia de Ubicacion con los parametros dados
	 * @throws UbicacionRepetidaException no pueden existir dos ubicaciones con el
	 *                                    mismo codigo por ejemplo "SS"
	 */
	public Ubicacion agregarUbicacion(String codigo, String descripcion) throws UbicacionRepetidaException {

		Ubicacion ubi = new Ubicacion(codigo, descripcion);
		if (ubicaciones.contains(ubi))
			throw new UbicacionRepetidaException("Ubicacion con el mismo codigo ya existe");

		ubicaciones.add(ubi);
		return ubi;
	}

	/**
	 * Sobrecarga del metodo agregarUbicacion que recibe directamente una instancia
	 * de Ubicacion
	 * 
	 * @param ubicacion
	 * @throws UbicacionRepetidaException
	 */
	public void agregarUbicacion(Ubicacion ubicacion) throws UbicacionRepetidaException {
		if (ubicaciones.contains(ubicacion))
			throw new UbicacionRepetidaException("Ubicacion con el mismo codigo ya existe");

		ubicaciones.add(ubicacion);
	}

	/**
	 * Sobrecarga del metodo agregarUbicacion que recibe un mapa con ubicaciones
	 * cargadas
	 * 
	 * @param ubicaciones
	 */
	public void agregarUbicacion(Map<String, Ubicacion> ubicaciones) {
		for (Ubicacion ubi : ubicaciones.values()) {
			agregarUbicacion(ubi);
		}
	}

	/**
	 * Agrega una conexion a la red
	 * 
	 * @param equipo1 -> origen
	 * @param equipo2 -> destino
	 * @param tipoCable
	 * @return instancia de Conexion con los parametros dados
	 * @throws ConexionRepetidaException se lanza una excepcion si equipo1 ya esta
	 *                                   conectado con equipo2
	 */
	public Conexion agregarConexion(Equipo equipo1, Equipo equipo2, TipoCable tipoCable)
			throws ConexionRepetidaException {

		Conexion conex = new Conexion(equipo1, equipo2, tipoCable);
		if (conexiones.contains(conex))
			throw new ConexionRepetidaException("Ya existe una conexion entre equipo 1 y equipo 2");

		conexiones.add(conex);
		return conex;
	}

	public void agregarConexion(List<Conexion> conexiones) {
		for (Conexion c : conexiones) {
			agregarConexion(c.getEquipo1(), c.getEquipo2(), c.getTipoCable());
		}
	}

	   // Método para encontrar la ruta entre dos equipos y calcular la velocidad máxima
    public List<Equipo> buscarRuta(Equipo equipoInicio, Equipo equipoFin) {
        Map<Equipo, Equipo> predecesores = new HashMap<>();
        Queue<Equipo> cola = new LinkedList<>();
        Set<Equipo> visitados = new HashSet<>();
        cola.add(equipoInicio);
        visitados.add(equipoInicio);

        while (!cola.isEmpty()) {
            Equipo actual = cola.poll();

            if (actual.equals(equipoFin)) {
                return reconstruirRuta(predecesores, equipoInicio, equipoFin);
            }

            for (Conexion conexion : conexiones) {
                Equipo vecino = conexion.getEquipo1().equals(actual) ? conexion.getEquipo2() : conexion.getEquipo1();
                if (!visitados.contains(vecino)) {
                    predecesores.put(vecino, actual);
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return null; // No se encontró una ruta
    }

    // Método auxiliar para reconstruir la ruta desde los predecesores
    private List<Equipo> reconstruirRuta(Map<Equipo, Equipo> predecesores, Equipo equipoInicio, Equipo equipoFin) {
        List<Equipo> ruta = new ArrayList<>();
        Equipo actual = equipoFin;
        while (actual != null) {
            ruta.add(0, actual);
            actual = predecesores.get(actual);
        }
        return ruta;
    }

    // Método para calcular la velocidad máxima de la ruta
    public int calcularVelocidadMaxima(List<Equipo> ruta) {
        int velocidadMaxima = Integer.MAX_VALUE;

        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);

            Conexion conexion = buscarConexion(equipo1, equipo2);
            if (conexion != null) {
                int velocidadCable = conexion.getTipoCable().getVelocidad();
                int velocidadEquipo1 = equipo1.getVelocidadMaxima(); // Método en Equipo
                int velocidadEquipo2 = equipo2.getVelocidadMaxima(); // Método en Equipo

                // La velocidad está limitada por la menor velocidad de equipo y cable
                velocidadMaxima = Math.min(velocidadMaxima, Math.min(velocidadCable, Math.min(velocidadEquipo1, velocidadEquipo2)));
            }
        }
        return velocidadMaxima;
    }

   // Buscar una conexión entre dos equipos
    private Conexion buscarConexion(Equipo equipo1, Equipo equipo2) {
        for (Conexion conexion : conexiones) {
            if ((conexion.getEquipo1().equals(equipo1) && conexion.getEquipo2().equals(equipo2)) ||
                (conexion.getEquipo1().equals(equipo2) && conexion.getEquipo2().equals(equipo1))) {
                return conexion;
            }
        }
        return null;
    }

	// Método para verificar conectividad hacia el Gateway
public void verificarConectividad(Equipo equipoOrigen, Equipo internetGateway) {
    List<Equipo> ruta = buscarRuta(equipoOrigen, internetGateway);
    
    if (ruta == null || ruta.isEmpty()) {
        System.out.println("No se encontró una ruta desde el equipo " + equipoOrigen.getCodigo() + " hasta el Gateway.");
        return;
    }

    System.out.println("Verificando conectividad desde el equipo " + equipoOrigen.getCodigo() + " hasta el Gateway...");

    for (int i = 0; i < ruta.size() - 1; i++) {
        Equipo equipo1 = ruta.get(i);
        Equipo equipo2 = ruta.get(i + 1);
        Conexion conexion = buscarConexion(equipo1, equipo2);

        if (conexion != null) {
            boolean equipo1Activo = equipo1.realizarPing();
            boolean equipo2Activo = equipo2.realizarPing();
            boolean conexionFuncionando = conexion.getTipoCable().getVelocidad() > 0; // Cable en buen estado

            if (!equipo1Activo) {
                System.out.println("El equipo " + equipo1.getCodigo() + " está inactivo. Se pierde conectividad aquí.");
                return;
            } else if (!equipo2Activo) {
                System.out.println("El equipo " + equipo2.getCodigo() + " está inactivo. Se pierde conectividad aquí.");
                return;
            } else if (!conexionFuncionando) {
                System.out.println("Problema con el cable entre " + equipo1.getCodigo() + " y " + equipo2.getCodigo() + ". Se pierde conectividad aquí.");
                return;
            } else {
                System.out.println("Conectividad correcta entre " + equipo1.getCodigo() + " y " + equipo2.getCodigo());
            }
        }
    }
    
    System.out.println("El equipo " + equipoOrigen.getCodigo() + " tiene conectividad hasta el Gateway.");
}
	

	  // Realizar ping a un equipo por su dirección IP
	  public boolean realizarPingAEquipo(String direccionIp) {
        for (Equipo equipo : equipos) {
            if (equipo.getDireccionesIp().contains(direccionIp)) {
                boolean respuestaPing = equipo.realizarPing(); // Simular ping
                if (respuestaPing) {
                    System.out.println("Ping exitoso al equipo con IP: " + direccionIp);
                } else {
                    System.out.println("Ping fallido al equipo con IP: " + direccionIp);
                }
                return respuestaPing;
            }
        }
        System.out.println("No se encontró un equipo con la IP: " + direccionIp);
        return false;
    }

    // Realizar ping a un rango de IPs
    public void realizarPingARango(String inicioIp, String finIp) {
        for (Equipo equipo : equipos) {
            for (String ip : equipo.getDireccionesIp()) {
                if (estaDentroDelRango(ip, inicioIp, finIp)) {
                    realizarPingAEquipo(ip);
                }
            }
        }
    }

    // Verificar si una IP está dentro de un rango
    private boolean estaDentroDelRango(String ip, String inicioIp, String finIp) {
        return compararIp(ip, inicioIp) >= 0 && compararIp(ip, finIp) <= 0;
    }

    // Comparar dos direcciones IP
    private int compararIp(String ip1, String ip2) {
        String[] octetos1 = ip1.split("\\.");
        String[] octetos2 = ip2.split("\\.");
        for (int i = 0; i < 4; i++) {
            int diferencia = Integer.parseInt(octetos1[i]) - Integer.parseInt(octetos2[i]);
            if (diferencia != 0) {
                return diferencia;
            }
        }
        return 0;
    }

    // Mostrar un mapa del estado actual de los equipos conectados
    public void mostrarMapaDeEstado() {
        System.out.println("Mapa del estado actual de la red:");
        for (Equipo equipo : equipos) {
            System.out.println(equipo.getCodigo() + " - IPs: " + equipo.getDireccionesIp() + " - Estado: " + equipo.getEstado());
        }
    }

}


