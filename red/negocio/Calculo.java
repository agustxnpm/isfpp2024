package red.negocio;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.datastructures.Graph;
import net.datastructures.Vertex;
import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.TreeMap;
import red.controlador.Coordinador;

import red.modelo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Calculo {

    private TreeMap<String, Vertex<Equipo>> vertices; // Mapa de equipos a sus vértices en el grafo.
    private Graph<Equipo, Conexion> red; // Grafo que representa la red de equipos y sus conexiones.
    private Coordinador coordinador; // Referencia al Coordinador para interactuar con otras partes del sistema.

    public Calculo() {
        // Constructor vacío para inicializar la clase.
    }

    /**
     * Cargar los datos de equipos y conexiones en el grafo.
     * 
     * @param eq    Lista de equipos.
     * @param conex Lista de conexiones.
     */
    public void cargarDatos(List<Equipo> eq, List<Conexion> conex) {
        // Mapa para almacenar equipos por su código.
        TreeMap<String, Equipo> equipos = new TreeMap<String, Equipo>();
        for (Equipo e : eq) {
            equipos.put(e.getCodigo(), e);
        }

        // Crear un grafo no dirigido para la red.
        red = new AdjacencyMapGraph<>(false);

        // Mapa de vértices de equipos en el grafo.
        vertices = new TreeMap<String, Vertex<Equipo>>();
        for (Entry<String, Equipo> e : equipos.entrySet()) {
            // Insertar cada equipo como un vértice en el grafo.
            vertices.put(e.getKey(), red.insertVertex(e.getValue()));
        }

        // Insertar conexiones como aristas entre los vértices del grafo.
        for (Conexion c : conex) {
            red.insertEdge(vertices.get(c.getEquipo1().getCodigo()), vertices.get(c.getEquipo2().getCodigo()), c);
        }
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    /**
     * Obtener la instancia de Equipo correspondiente a un código.
     * 
     * @param codigo Código del equipo a buscar.
     * @return Equipo con el código proporcionado o null si no se encuentra.
     */
    public Equipo obtenerEquipo(String codigo) {
        for (Vertex<Equipo> e : vertices.values()) {
            if (e.getElement().getCodigo().equals(codigo)) {
                return e.getElement();
            }
        }
        return null;
    }

    /**
     * Encuentra la ruta entre dos equipos utilizando el algoritmo BFS.
     * 
     * @param equipoInicio Equipo desde donde comienza la búsqueda.
     * @param equipoFin    Equipo destino al que se quiere llegar.
     * @return Una lista de equipos que representa la ruta más corta entre
     *         equipoInicio y equipoFin. Si no se encuentra una ruta, retorna null.
     */
    public List<Equipo> buscarRuta(Equipo equipoInicio, Equipo equipoFin) {
        Map<Equipo, Equipo> predecesores = new HashMap<>(); // Almacena los predecesores para reconstruir la ruta.
        Queue<Equipo> cola = new LinkedList<>(); // Cola para el algoritmo BFS.
        Set<Equipo> visitados = new HashSet<>(); // Conjunto de equipos ya visitados.

        cola.add(equipoInicio);
        visitados.add(equipoInicio);

        // Realizar la búsqueda en anchura.
        while (!cola.isEmpty()) {
            Equipo actual = cola.poll();

            // Si alcanzamos el equipo destino, reconstruimos la ruta.
            if (actual.equals(equipoFin)) {
                return reconstruirRuta(predecesores, equipoInicio, equipoFin);
            }

            // Recorrer todas las conexiones del equipo actual.
            for (Edge<Conexion> conexion : red.edges()) {
                // Obtener el equipo vecino a través de la conexión.
                Equipo vecino = conexion.getElement().getEquipo1().equals(actual) ?
                        conexion.getElement().getEquipo2() : conexion.getElement().getEquipo1();

                // Si el vecino no ha sido visitado, lo agregamos a la cola.
                if (!visitados.contains(vecino)) {
                    predecesores.put(vecino, actual);
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return null; // No se encontró una ruta.
    }

    /**
     * Método auxiliar para reconstruir la ruta desde el equipo de inicio hasta el
     * equipo de fin.
     * 
     * @param predecesores Mapa que contiene los predecesores de cada equipo.
     * @param equipoInicio Equipo inicial.
     * @param equipoFin    Equipo destino.
     * @return Lista de equipos que representa la ruta reconstruida.
     */
    private List<Equipo> reconstruirRuta(Map<Equipo, Equipo> predecesores, Equipo equipoInicio, Equipo equipoFin) {
        List<Equipo> ruta = new ArrayList<>();
        Equipo actual = equipoFin;

        // Reconstrucción de la ruta en orden inverso.
        while (actual != null) {
            ruta.add(0, actual); // Agregar al inicio de la lista.
            actual = predecesores.get(actual);
        }
        return ruta;
    }

    /**
     * Calcula la velocidad máxima de una ruta, limitada por la conexión más lenta.
     * 
     * @param ruta Lista de equipos que forman la ruta.
     * @return La velocidad máxima en Mbps, limitada por el cable o puertos más lentos.
     */
    public int calcularVelocidadMaxima(List<Equipo> ruta) {
        int velocidadMaxima = Integer.MAX_VALUE; // Inicialmente la velocidad es muy alta.

        // Iterar sobre la ruta para analizar cada conexión entre equipos.
        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);

            Conexion conexion = buscarConexion(equipo1, equipo2); // Buscar la conexión entre dos equipos.
            if (conexion != null) {
                int velocidadCable = conexion.getTipoCable().getVelocidad();
                int velocidadEquipo1 = equipo1.getVelocidadMaxima();
                int velocidadEquipo2 = equipo2.getVelocidadMaxima();

                // Determinar la velocidad más baja entre el cable y los puertos de los equipos.
                velocidadMaxima = Math.min(velocidadMaxima,
                        Math.min(velocidadCable, Math.min(velocidadEquipo1, velocidadEquipo2)));
            }
        }
        return velocidadMaxima;
    }

    /**
     * Busca la conexión entre dos equipos en la red.
     * 
     * @param equipo1 Primer equipo.
     * @param equipo2 Segundo equipo.
     * @return La conexión entre equipo1 y equipo2, o null si no existe.
     */
    private Conexion buscarConexion(Equipo equipo1, Equipo equipo2) {
        for (Edge<Conexion> conexion : red.edges()) {
            if ((conexion.getElement().getEquipo1().equals(equipo1)
                    && conexion.getElement().getEquipo2().equals(equipo2))
                    || (conexion.getElement().getEquipo1().equals(equipo2)
                            && conexion.getElement().getEquipo2().equals(equipo1))) {
                return conexion.getElement();
            }
        }
        return null; // No se encontró conexión entre los dos equipos.
    }

    /**
     * Verifica la conectividad desde un equipo hasta el Gateway, informando si
     * algún equipo o conexión falla.
     * 
     * @param equipoOrigen    Equipo de origen.
     * @param internetGateway Equipo que representa el Gateway.
     */
    public void verificarConectividad(Equipo equipoOrigen, Equipo internetGateway) {
        List<Equipo> ruta = buscarRuta(equipoOrigen, internetGateway);

        if (ruta == null || ruta.isEmpty()) {
            System.out.println("No se encontró una ruta desde el equipo " + equipoOrigen.getCodigo() + " hasta el Gateway.");
            return;
        }

        System.out.println("Verificando conectividad desde el equipo " + equipoOrigen.getCodigo() + " hasta el Gateway...");

        // Verificar cada equipo y conexión en la ruta.
        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);
            Conexion conexion = buscarConexion(equipo1, equipo2);

            if (conexion != null) {
                boolean equipo1Activo = equipo1.realizarPing();
                boolean equipo2Activo = equipo2.realizarPing();
                boolean conexionFuncionando = conexion.getTipoCable().getVelocidad() > 0;

                // Informar si algún equipo está inactivo o el cable está dañado.
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
     * 
     * @param direccionIp Dirección IP del equipo.
     * @return true si el ping fue exitoso, false en caso contrario.
     */
    public boolean realizarPingAEquipo(String direccionIp) {
        for (Vertex<Equipo> equipo : vertices.values()) {
            if (equipo.getElement().getDireccionesIp().contains(direccionIp)) {
                boolean respuestaPing = equipo.getElement().realizarPing();
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
     * 
     * @param inicioIp IP inicial del rango.
     * @param finIp    IP final del rango.
     */
    public void realizarPingARango(String inicioIp, String finIp) {
        for (Vertex<Equipo> equipo : vertices.values()) {
            for (String ip : equipo.getElement().getDireccionesIp()) {
                if (estaDentroDelRango(ip, inicioIp, finIp)) {
                    realizarPingAEquipo(ip);
                }
            }
        }
    }

    /**
     * Verifica si una dirección IP está dentro de un rango de IPs.
     * 
     * @param ip       Dirección IP a verificar.
     * @param inicioIp IP inicial del rango.
     * @param finIp    IP final del rango.
     * @return true si la IP está dentro del rango, false en caso contrario.
     */
    private boolean estaDentroDelRango(String ip, String inicioIp, String finIp) {
        return compararIp(ip, inicioIp) >= 0 && compararIp(ip, finIp) <= 0;
    }

    /**
     * Compara dos direcciones IP para ver cuál es mayor o menor.
     * 
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
}
