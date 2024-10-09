package logica;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import excepciones.ConexionRepetidaException;
import excepciones.EquipoRepetidoException;
import excepciones.UbicacionRepetidaException;
import modelo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Red {

    private Graph<Equipo, DefaultEdge> grafo;  // Grafo que representa la red


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
         // Grafo no dirigido y con peso (usamos SimpleWeightedGraph para poder asignar pesos)
         grafo = new SimpleWeightedGraph<>(DefaultEdge.class);
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
			TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto, boolean estado) throws EquipoRepetidoException {
		Equipo nuevoEquipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
				tipoPuerto, estado);

		// verificar que no se añadan equipos con un mismo codigo (equals de Equipo)
		if (equipos.contains(nuevoEquipo))
			throw new EquipoRepetidoException("El equipo ya existe en la red");

		equipos.add(nuevoEquipo);
        grafo.addVertex(nuevoEquipo);
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
        grafo.addVertex(equipo);
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
    // Agrega una conexión (arista) al grafo, con un peso que representa la velocidad
    public void agregarConexionAlGrafo(Equipo equipo1, Equipo equipo2, double velocidad) {
        grafo.addEdge(equipo1, equipo2);  // Creamos la arista entre los equipos
        grafo.setEdgeWeight(grafo.getEdge(equipo1, equipo2), velocidad);  // Asignamos un peso a la arista (velocidad del cable)
    }




    
    /**
     * Método para encontrar la ruta entre dos equipos utilizando una búsqueda en anchura (BFS).
     * @param equipoInicio Equipo desde donde comienza la búsqueda.
     * @param equipoFin Equipo destino al que se quiere llegar.
     * @return Una lista de equipos que representa la ruta más corta entre equipoInicio y equipoFin.
     * Si no se encuentra una ruta, retorna null.
     */
    public List<Equipo> buscarRuta(Equipo equipoInicio, Equipo equipoFin) {
        Map<Equipo, Equipo> predecesores = new HashMap<>();  // Almacena los predecesores para reconstruir la ruta
        Queue<Equipo> cola = new LinkedList<>();  // Cola para el algoritmo BFS
        Set<Equipo> visitados = new HashSet<>();  // Conjunto de equipos ya visitados
        
        cola.add(equipoInicio);
        visitados.add(equipoInicio);

        // Búsqueda en anchura
        while (!cola.isEmpty()) {
            Equipo actual = cola.poll();

            // Si hemos alcanzado el equipo destino, reconstruimos la ruta
            if (actual.equals(equipoFin)) {
                return reconstruirRuta(predecesores, equipoInicio, equipoFin);
            }

            // Recorremos todas las conexiones del equipo actual
            for (Conexion conexion : conexiones) {
                Equipo vecino = conexion.getEquipo1().equals(actual) ? conexion.getEquipo2() : conexion.getEquipo1();
                // Si el equipo vecino no ha sido visitado, lo agregamos a la cola
                if (!visitados.contains(vecino)) {
                    predecesores.put(vecino, actual);
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return null; // No se encontró una ruta
    }

    /**
     * Método auxiliar para reconstruir la ruta desde el equipo de inicio hasta el equipo de fin.
     * @param predecesores Mapa que contiene los predecesores de cada equipo visitado.
     * @param equipoInicio Equipo inicial.
     * @param equipoFin Equipo destino.
     * @return Lista de equipos que representa la ruta reconstruida.
     */
    private List<Equipo> reconstruirRuta(Map<Equipo, Equipo> predecesores, Equipo equipoInicio, Equipo equipoFin) {
        List<Equipo> ruta = new ArrayList<>();
        Equipo actual = equipoFin;
        
        // Reconstrucción de la ruta en orden inverso
        while (actual != null) {
            ruta.add(0, actual);  // Se agrega al inicio de la lista
            actual = predecesores.get(actual);  // Obtenemos el predecesor del equipo actual
        }
        return ruta;
    }

    /**
     * Método para calcular la velocidad máxima de una ruta en función del tipo de cable y puertos.
     * La velocidad está limitada por la conexión más lenta (cable o puerto).
     * @param ruta Lista de equipos que forman la ruta.
     * @return La velocidad máxima en Mbps, limitada por el cable o puertos más lentos.
     */
    public int calcularVelocidadMaxima(List<Equipo> ruta) {
        int velocidadMaxima = Integer.MAX_VALUE;  // Inicialmente la velocidad es muy alta
        
        // Iteramos sobre la ruta para analizar cada conexión entre equipos
        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);

            Conexion conexion = buscarConexion(equipo1, equipo2);  // Buscamos la conexión entre dos equipos
            if (conexion != null) {
                int velocidadCable = conexion.getTipoCable().getVelocidad();
                int velocidadEquipo1 = equipo1.getVelocidadMaxima();  // Velocidad máxima del puerto del equipo 1
                int velocidadEquipo2 = equipo2.getVelocidadMaxima();  // Velocidad máxima del puerto del equipo 2

                // Limitamos la velocidad a la conexión más lenta (cable o equipo)
                velocidadMaxima = Math.min(velocidadMaxima, Math.min(velocidadCable, Math.min(velocidadEquipo1, velocidadEquipo2)));
            }
        }
        return velocidadMaxima;
    }

    /**
     * Busca la conexión entre dos equipos en la red.
     * @param equipo1 Primer equipo.
     * @param equipo2 Segundo equipo.
     * @return La conexión entre equipo1 y equipo2, o null si no existe.
     */
    private Conexion buscarConexion(Equipo equipo1, Equipo equipo2) {
        for (Conexion conexion : conexiones) {
            if ((conexion.getEquipo1().equals(equipo1) && conexion.getEquipo2().equals(equipo2)) ||
                (conexion.getEquipo1().equals(equipo2) && conexion.getEquipo2().equals(equipo1))) {
                return conexion;  // Retornamos la conexión si existe
            }
        }
        return null;  // No se encontró conexión entre los dos equipos
    }

    /**
     * Verifica la conectividad desde un equipo hasta el Gateway, comprobando cada equipo y conexión en la ruta.
     * Si algún equipo o conexión falla, se detiene e informa el punto de fallo.
     * @param equipoOrigen Equipo de origen.
     * @param internetGateway Equipo que representa el Gateway (puerta de enlace a internet).
     */
    public void verificarConectividad(Equipo equipoOrigen, Equipo internetGateway) {
        List<Equipo> ruta = buscarRuta(equipoOrigen, internetGateway);  // Buscamos la ruta hasta el Gateway
        
        if (ruta == null || ruta.isEmpty()) {
            System.out.println("No se encontró una ruta desde el equipo " + equipoOrigen.getCodigo() + " hasta el Gateway.");
            return;
        }

        System.out.println("Verificando conectividad desde el equipo " + equipoOrigen.getCodigo() + " hasta el Gateway...");

        // Verificamos cada conexión en la ruta
        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);
            Conexion conexion = buscarConexion(equipo1, equipo2);

            if (conexion != null) {
                boolean equipo1Activo = equipo1.realizarPing();
                boolean equipo2Activo = equipo2.realizarPing();
                boolean conexionFuncionando = conexion.getTipoCable().getVelocidad() > 0; // Verificamos que el cable esté en buen estado

                // Si algún equipo está inactivo o el cable está dañado, se informa el fallo
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

    /**
     * Realiza un ping a un equipo específico por su dirección IP.
     * @param direccionIp Dirección IP del equipo.
     * @return true si el ping fue exitoso, false en caso contrario.
     */
    public boolean realizarPingAEquipo(String direccionIp) {
        for (Equipo equipo : equipos) {
            if (equipo.getDireccionesIp().contains(direccionIp)) {
                boolean respuestaPing = equipo.realizarPing();
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

    /**
     * Realiza ping a todos los equipos cuyas IPs estén dentro de un rango.
     * @param inicioIp IP inicial del rango.
     * @param finIp IP final del rango.
     */
    public void realizarPingARango(String inicioIp, String finIp) {
        for (Equipo equipo : equipos) {
            for (String ip : equipo.getDireccionesIp()) {
                if (estaDentroDelRango(ip, inicioIp, finIp)) {
                    realizarPingAEquipo(ip);
                }
            }
        }
    }

    /**
     * Verifica si una dirección IP está dentro de un rango de IPs.
     * @param ip Dirección IP a verificar.
     * @param inicioIp IP inicial del rango.
     * @param finIp IP final del rango.
     * @return true si la IP está dentro del rango, false en caso contrario.
     */
    private boolean estaDentroDelRango(String ip, String inicioIp, String finIp) {
        return compararIp(ip, inicioIp) >= 0 && compararIp(ip, finIp) <= 0;
    }

    /**
     * Compara dos direcciones IP para ver cuál es mayor o menor.
     * @param ip1 Primera IP.
     * @param ip2 Segunda IP.
     * @return Un valor negativo si ip1 < ip2, positivo si ip1 > ip2, o 0 si son iguales.
     */
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

    /**
     * Muestra el estado actual de todos los equipos conectados a la red.
     */
    public void mostrarMapaDeEstado() {
        System.out.println("Mapa del estado actual de la red:");
        for (Equipo equipo : equipos) {
            System.out.println(equipo.getCodigo() + " - IPs: " + equipo.getDireccionesIp() + " - Estado: " + equipo.isEstado());
        }
    }

}


