package red.negocio;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import net.datastructures.Graph;
import net.datastructures.Vertex;
import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.TreeMap;
import red.excepciones.ConexionNoConectadaException;
import red.excepciones.ConexionRepetidaException;
import red.excepciones.EquipoNoConectadoException;
import red.excepciones.EquipoRepetidoException;
import red.excepciones.DireccionIpNoEncontradaException;
import red.modelo.*;
import red.controlador.Configuracion;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Clase que representa las operaciones y cálculos sobre la red de equipos y
 * conexiones.
 */
public class Calculo {

    private TreeMap<String, Vertex<Equipo>> vertices; // Mapa de equipos a sus vértices en el grafo.
    private Graph<Equipo, Conexion> red; // Grafo que representa la red de equipos y sus conexiones.

    /**
     * Constructor de la clase Calculo. Inicializa el objeto sin cargar datos.
     */
    public Calculo() {
        // Constructor vacío para inicializar la clase.
    }

    /**
     * Cargar los datos de equipos y conexiones en el grafo.
     *
     * @param eq Lista de equipos.
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
        for (Entry<String, Equipo> e : equipos.entrySet()) // Insertar cada equipo como un vértice en el grafo.
        {
            vertices.put(e.getKey(), red.insertVertex(e.getValue()));
        }

        // Insertar conexiones como aristas entre los vértices del grafo.
        for (Conexion c : conex) {
            red.insertEdge(vertices.get(c.getEquipo1().getCodigo()), vertices.get(c.getEquipo2().getCodigo()), c);
        }
    }

    /**
     * Obtiene los equipos conectados transitivamente a partir de un equipo.
     *
     * @param equipoInicial El equipo de partida.
     * @return Un conjunto de equipos conectados transitivamente.
     */
    public Set<Equipo> obtenerEquiposConectadosTransitivamente(Equipo equipoInicial) {
        Set<Equipo> equiposConectados = new HashSet<>();
        Queue<Equipo> cola = new LinkedList<>();
        Set<Equipo> visitados = new HashSet<>();

        cola.add(equipoInicial);
        visitados.add(equipoInicial);

        while (!cola.isEmpty()) {
            Equipo actual = cola.poll();
            equiposConectados.add(actual);

            // Recorremos todas las conexiones del equipo actual
            for (Edge<Conexion> conexion : red.incomingEdges(vertices.get(actual.getCodigo()))) {
                Equipo vecino = conexion.getElement().getEquipo1().equals(actual) ? conexion.getElement().getEquipo2()
                        : conexion.getElement().getEquipo1();
                if (!visitados.contains(vecino)) {
                    cola.add(vecino);
                    visitados.add(vecino);
                }
            }
        }
        return equiposConectados;
    }

    /**
     * Obtiene todas las direcciones IP de los equipos en la red.
     *
     * @return Una lista de todas las direcciones IP.
     */
    public List<String> obtenerTodasLasIPs() {
        List<String> ips = new ArrayList<>();
        for (Vertex<Equipo> vertice : vertices.values()) {
            Equipo equipo = vertice.getElement();
            ips.addAll(equipo.getDireccionesIp()); // Suponiendo que el método getDireccionesIp() devuelve una lista de
            // IPs.
        }
        return ips;
    }

    /**
     * Agregar una conexion al grafo
     *
     * @param conexion
     * @throws ConexionRepetidaException si la conexion ya ha sido insertada en
     * el grafo anteriormente
     */
    public void agregarConexionAlGrafo(Conexion conexion) throws ConexionRepetidaException {

        for (Edge<Conexion> edge : red.edges()) {
            if (edge.getElement().equals(conexion)) {
                throw new ConexionRepetidaException(
                        Configuracion.getConfiguracion().getRb().getString("Calculo_conexion_ya_existe"));
            }
        }

        Equipo equipo1 = conexion.getEquipo1();
        Equipo equipo2 = conexion.getEquipo2();

        red.insertEdge(vertices.get(equipo1.getCodigo()), vertices.get(equipo2.getCodigo()), conexion);
    }

    /**
     * Agregar un equipo al grafo
     *
     * @param equipo
     * @throws EquipoRepetidoException si el equipo ya existe en el grafo (red)
     * se lanza una excepcion
     */
    public void agregarEquipoAlGrafo(Equipo equipo) throws EquipoRepetidoException {

        for (Vertex<Equipo> vertex : vertices.values()) {
            if (vertex.getElement().equals(equipo)) {
                throw new EquipoRepetidoException(
                        Configuracion.getConfiguracion().getRb().getString("Calculo_equipo_ya_existe"));
            }
        }

        red.insertVertex(equipo);

    }

    /**
     * Modifica un equipo existente en el grafo.
     *
     * @param equipo El equipo modificado.
     */
    public void modificarEquipoEnElGrafo(Equipo equipo) {

        Iterable<Edge<Conexion>> edges = red.outgoingEdges(vertices.get(equipo.getCodigo()));

        for (Vertex<Equipo> vertex : red.vertices()) {
            if (vertex.equals(vertices.get(equipo.getCodigo()))) {
                red.removeVertex(vertex);
                break;
            }
        }

        Vertex<Equipo> nuevoVertice = red.insertVertex(equipo);
        vertices.put(equipo.getCodigo(), nuevoVertice);

        for (Edge<Conexion> e : edges) {
            red.insertEdge(vertices.get(e.getElement().getEquipo1().getCodigo()),
                    vertices.get(e.getElement().getEquipo2().getCodigo()), e.getElement());
        }
    }

    /**
     * Encuentra la ruta entre dos equipos utilizando el algoritmo BFS.
     *
     * @param equipoInicio Equipo desde donde comienza la búsqueda.
     * @param equipoFin Equipo destino al que se quiere llegar.
     * @return Una lista de equipos que representa la ruta más corta entre
     * equipoInicio y equipoFin. Si no se encuentra una ruta, retorna null.
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

            // Recorrer todas las conexiones del equipo actual con los vecinos.
            for (Equipo vecino : vecinos(actual)) // Si el vecino no ha sido visitado, lo agregamos a la cola.
            {
                if (!visitados.contains(vecino)) {
                    predecesores.put(vecino, actual);
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        } // Fin while
        return null; // No se encontró una ruta.
    }

    /**
     * Método auxiliar para obtener a todos los vecinos de un equipo dado
     *
     * @param eq Equipo cuyos vecinos buscamos
     * @returns Conjunto de vecinos de dicho equipo
     */
    private Set<Equipo> vecinos(Equipo eq) {
        Set<Equipo> vecinos = new HashSet<>(); // conjunto de equipos vecinos de nuestro equipo
        for (Edge<Conexion> conexion : red.edges()) {
            if (conexion.getElement().getEquipo1().equals(eq)) {
                vecinos.add(conexion.getElement().getEquipo2()); // si eq es el equipo 1 de la conexión, agrega al
            }// equipo 2
            else if (conexion.getElement().getEquipo2().equals(eq)) {
                vecinos.add(conexion.getElement().getEquipo1()); // si eq es el equipo 2 de la conexión, agrega al
            }																	// equipo 1

        }
        return vecinos;
    }

    /**
     * Método auxiliar para reconstruir la ruta desde el equipo de inicio hasta
     * el equipo de fin.
     *
     * @param predecesores Mapa que contiene los predecesores de cada equipo.
     * @param equipoInicio Equipo inicial.
     * @param equipoFin Equipo destino.
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
     * Calcula la velocidad máxima de una ruta, limitada por la conexión más
     * lenta.
     *
     * @param ruta Lista de equipos que forman la ruta.
     * @return La velocidad máxima en Mbps, limitada por el cable o puertos más
     * lentos.
     */
    public int calcularVelocidadMaxima(List<Equipo> ruta) throws ConexionNoConectadaException {
        int velocidadMaxima = Integer.MAX_VALUE;

        if (ruta.size() == 1) {
            velocidadMaxima = 0;
            return velocidadMaxima;
        }
        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);

            Conexion conexion = buscarConexion(equipo1, equipo2);
            if (conexion == null) {
                throw new ConexionNoConectadaException(String.format(
                        Configuracion.getConfiguracion().getRb().getString("Calculo_no_existe_conexion_entre"),
                        equipo1.getCodigo(),
                        equipo2.getCodigo()));
            }

            int velocidadCable = conexion.getTipoCable().getVelocidad();
            int velocidadEquipo1 = equipo1.getVelocidadMaxima();
            int velocidadEquipo2 = equipo2.getVelocidadMaxima();

            velocidadMaxima = Math.min(velocidadMaxima,
                    Math.min(velocidadCable, Math.min(velocidadEquipo1, velocidadEquipo2)));
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
     * @param equipoOrigen Equipo de origen
     * @param internetGateway Equipo que representa el Gateway.
     */
    public String verificarConectividad(Equipo equipoOrigen, Equipo internetGateway)
            throws EquipoNoConectadoException, ConexionNoConectadaException {

        /*
		 * Encuentra la ruta entre equipos. La propia existencia de la ruta implica la
		 * existencia de conexiones entre cada
		 * par de equipos consecutivos en la misma.
         */
        List<Equipo> ruta = buscarRuta(equipoOrigen, internetGateway);

        // Verificar si no existe una ruta
        if (ruta == null || ruta.isEmpty()) {
            throw new ConexionNoConectadaException(String.format(
                    Configuracion.getConfiguracion().getRb()
                            .getString("Calculo_no_se_encontro_conexion_desde_equipo_hasta_gateway"),
                    equipoOrigen.getCodigo()));
        }

        // Verificar cada equipo y conexión en la ruta
        for (int i = 0; i < ruta.size() - 1; i++) {
            Equipo equipo1 = ruta.get(i);
            Equipo equipo2 = ruta.get(i + 1);
            Conexion conexion = buscarConexion(equipo1, equipo2);

            boolean equipo1Activo = equipo1.realizarPing();
            boolean equipo2Activo = equipo2.realizarPing();
            boolean conexionFuncionando = conexion.getTipoCable().getVelocidad() > 0;

            // Lanzar excepción si algún equipo está inactivo
            if (!equipo1Activo) {
                throw new EquipoNoConectadoException(String.format(
                        Configuracion.getConfiguracion().getRb()
                                .getString("Calculo_equipo_inactivo_se_pierde_conectividad_aqui"),
                        equipo1.getCodigo()));
            }

            if (!equipo2Activo) {
                throw new EquipoNoConectadoException(String.format(
                        Configuracion.getConfiguracion().getRb()
                                .getString("Calculo_equipo_inactivo_se_pierde_conectividad_aqui"),
                        equipo2.getCodigo()));
            }

            // Lanzar excepción si el cable está defectuoso
            if (!conexionFuncionando) {
                throw new ConexionNoConectadaException(
                        String.format(Configuracion.getConfiguracion().getRb().getString("Calculo_problema_cable"),
                                equipo1.getCodigo(),
                                equipo2.getCodigo()));
            }

        }
        return Configuracion.getConfiguracion().getRb().getString("Calculo_equipo_posee_conectividad");
    }

    /**
     * Realiza un ping a un equipo específico por su dirección IP.
     *
     * @param direccionIp Dirección IP del equipo.
     * @return true si el ping fue exitoso, false en caso contrario.
     */
    public boolean realizarPingAEquipo(String direccionIp) throws DireccionIpNoEncontradaException {
        direccionIp = direccionIp.trim();
        for (Vertex<Equipo> equipo : vertices.values()) {
            if (equipo.getElement().getDireccionesIp().contains(direccionIp)) {
                return equipo.getElement().realizarPing();
            }
        }

        throw new DireccionIpNoEncontradaException(
                String.format(Configuracion.getConfiguracion().getRb().getString("Calculo_ip_no_se_encuentra_en_red"),
                        direccionIp));
    }

    /**
     * Realiza ping a todos los equipos cuyas IPs estén dentro de un rango.
     * Funciona unicamente para el modo simulacion de la aplicacion
     *
     * @param inicioIp IP inicial del rango.
     * @param finIp IP final del rango.
     * @return Una lista de mensajes con el resultado de cada ping.
     */
    public List<String> realizarPingARango(String inicioIp, String finIp) {
        List<String> resultados = new ArrayList<>();
        boolean pingExitoso = false;

        for (Vertex<Equipo> equipo : vertices.values()) {
            for (String ip : equipo.getElement().getDireccionesIp()) {
                if (estaDentroDelRango(ip, inicioIp, finIp)) {
                    boolean respuesta = realizarPingAEquipo(ip);
                    String resultado = respuesta
                            ? Configuracion.getConfiguracion().getRb().getString("Calculo_ping_exitoso")
                            : Configuracion.getConfiguracion().getRb().getString("Calculo_ping fallido");
                    String mensaje = String.format(
                            Configuracion.getConfiguracion().getRb().getString("Calculo_al_equipo_con_ip"), resultado,
                            ip);
                    resultados.add(mensaje);
                    pingExitoso = true;
                }
            }
        }

        if (!pingExitoso) {
            String mensajeSinResultados = Configuracion.getConfiguracion().getRb()
                    .getString("Calculo_no_resultados_dentro_rango_especificado");
            resultados.add(mensajeSinResultados);
        }

        return resultados;
    }

    /**
     * Realiza un ping a un rango de IP Funciona para el modo real de la
     * aplicacion
     *
     * @param hostInicio direccion ip inicio
     * @param hostFinal direccion ip final
     * @param cantPings cantidad de pings
     */
    public void pingRango(String hostInicio, String hostFinal, int cantPings, JTextArea textArea, boolean[] detener,
            JProgressBar progressBar) throws IllegalArgumentException, UnknownHostException {
        long ipInicio, ipFinal;
        try {
            // Convertir hostInicio a entero
            ipInicio = ipToLong(InetAddress.getByName(hostInicio));
        } catch (UnknownHostException e) {
            textArea.append(
                    String.format(Configuracion.getConfiguracion().getRb().getString("Calculo_error"), e.getMessage()));
            throw new UnknownHostException(String.format(
                    Configuracion.getConfiguracion().getRb().getString("Calculo_ip_no_se_encuentra_en_red"),
                    hostInicio));
        }

        try {
            // Convertir hostFinal a entero
            ipFinal = ipToLong(InetAddress.getByName(hostFinal));
        } catch (UnknownHostException e) {
            textArea.append(
                    String.format(Configuracion.getConfiguracion().getRb().getString("Calculo_error"), e.getMessage()));
            throw new UnknownHostException(String.format(
                    Configuracion.getConfiguracion().getRb().getString("Calculo_ip_no_se_encuentra_en_red"),
                    hostFinal));
        }

        // Validar que hostInicio sea menor o igual a hostFinal
        if (ipInicio > ipFinal) {
            throw new IllegalArgumentException(
                    Configuracion.getConfiguracion().getRb().getString("Calculo_host_inicial_menor_igual_host_final"));
        }

        // Calcular el total de IPs en el rango
        int totalPings = (int) (ipFinal - ipInicio + 1);
        progressBar.setMaximum(totalPings);

        // Recorrer el rango de direcciones IP y hacer ping
        for (long i = ipInicio; i <= ipFinal; i++) {
            if (detener[0]) {
                textArea.append(
                        Configuracion.getConfiguracion().getRb().getString("Calculo_ping_detenido_por_usuario"));
                break;
            }

            String ipActual = longToIp(i);
            ping(ipActual, cantPings, textArea, detener);
            // Actualizar el progreso en la barra
            int progresoActual = (int) (i - ipInicio + 1);
            progressBar.setValue(progresoActual);
        }
    }

    /**
     * Convierte una dirección IP en formato InetAddress a un número long.
     *
     * @param ip Dirección IP en formato InetAddress.
     * @return La representación de la dirección IP como un número long.
     */
    private static long ipToLong(InetAddress ip) {
        byte[] bytes = ip.getAddress();
        long result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }

    /**
     * Convierte un número long a una dirección IP en formato String.
     *
     * @param ip Dirección IP en formato long.
     * @return La representación de la dirección IP en formato String (e.g.,
     * "192.168.1.1").
     */
    private static String longToIp(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xFF,
                (ip >> 16) & 0xFF,
                (ip >> 8) & 0xFF,
                ip & 0xFF);
    }

    /**
     * Realiza un comando de ping al host especificado.
     *
     * @param host Dirección IP o nombre de host al que se realizará el ping.
     * @param cantPings Número de veces que se realizará el ping. Si es 0, se
     * usará el valor por defecto.
     * @param textArea Área de texto donde se mostrarán los resultados del ping.
     * @param detener Arreglo booleano para controlar si se debe detener el
     * proceso de ping.
     */
    public void ping(String host, int cantPings, JTextArea textArea, boolean[] detener) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String os = System.getProperty("os.name").toLowerCase(); // Verifica el sistema operativo

        // Construir el comando de ping según el sistema operativo
        if (os.contains("win")) // Comando de ping para Windows
        {
            if (cantPings == 0) {
                processBuilder.command("ping", host);
            } else {
                processBuilder.command("ping", "-n", Integer.toString(cantPings), host);
            }
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) // Comando de ping para Linux/Unix/Mac
        {
            if (cantPings == 0) {
                processBuilder.command("ping", host);
            } else {
                processBuilder.command("ping", "-c", Integer.toString(cantPings), host);
            }
        } else {
            textArea.append(
                    Configuracion.getConfiguracion().getRb().getString("Calculo_sistema_operativo_no_soportado"));
            return;
        }

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            // Configurar la barra de progreso

            while ((line = reader.readLine()) != null) {
                if (detener[0]) {
                    textArea.append(
                            Configuracion.getConfiguracion().getRb().getString("Calculo_ping_detenido_por_usuario"));
                    process.destroy(); // Detener el proceso de ping
                    break;
                }
                textArea.append(line + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }

            int exitCode = process.waitFor();
            if (!detener[0]) {
                textArea.append(String.format(
                        Configuracion.getConfiguracion().getRb()
                                .getString("Calculo_proceso_finalizado_con_codigo_salida"),
                        exitCode));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        } catch (IOException e) {
            textArea.append(
                    String.format(Configuracion.getConfiguracion().getRb().getString("Calculo_error"), e.getMessage()));
        } catch (InterruptedException e) {
            textArea.append(Configuracion.getConfiguracion().getRb().getString("Calculo_proceso_interrumpido"));
        }
    }

    /**
     * Realiza un comando de ping al host especificado con una barra de
     * progreso.
     *
     * @param host Dirección IP o nombre de host al que se realizará el ping.
     * @param cantPings Número de veces que se realizará el ping. Si es 0, se
     * usará el valor por defecto.
     * @param textArea Área de texto donde se mostrarán los resultados del ping.
     * @param detener Arreglo booleano para controlar si se debe detener el
     * proceso de ping.
     * @param progressBar Barra de progreso para mostrar el avance del ping.
     */
    public void ping(String host, int cantPings, JTextArea textArea, boolean[] detener, JProgressBar progressBar) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String os = System.getProperty("os.name").toLowerCase(); // Verifica el sistema operativo

        // Construir el comando de ping según el sistema operativo
        if (os.contains("win")) // Comando de ping para Windows
        {
            if (cantPings == 0) {
                processBuilder.command("ping", host);
            } else {
                processBuilder.command("ping", "-n", Integer.toString(cantPings), host);
            }
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) // Comando de ping para Linux/Unix/Mac
        {
            if (cantPings == 0) {
                processBuilder.command("ping", host);
            } else {
                processBuilder.command("ping", "-c", Integer.toString(cantPings), host);
            }
        } else {
            textArea.append(
                    Configuracion.getConfiguracion().getRb().getString("Calculo_sistema_operativo_no_soportado"));
            return;
        }

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int progresoActual = 0;
            int totalPings = cantPings > 0 ? cantPings : 4; // Usar 4 como mínimo si no se especifica cantidad

            // Configurar la barra de progreso
            progressBar.setMaximum(totalPings);
            progressBar.setValue(0);
            line = reader.readLine();
            textArea.append(line + "\n");

            while ((line = reader.readLine()) != null) {
                if (detener[0]) {
                    textArea.append(
                            Configuracion.getConfiguracion().getRb().getString("Calculo_ping_detenido_por_usuario"));
                    process.destroy(); // Detener el proceso de ping
                    break;
                }
                textArea.append(line + "\n");
                progressBar.setValue(progresoActual++);
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }

            int exitCode = process.waitFor();
            if (!detener[0]) {
                textArea.append(String.format(
                        Configuracion.getConfiguracion().getRb()
                                .getString("Calculo_proceso_finalizado_con_codigo_salida"),
                        exitCode));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        } catch (IOException e) {
            textArea.append(
                    String.format(Configuracion.getConfiguracion().getRb().getString("Calculo_error"), e.getMessage()));
        } catch (InterruptedException e) {
            textArea.append(Configuracion.getConfiguracion().getRb().getString("Calculo_proceso_interrumpido"));
        }
    }

    /**
     * Calcula el total de IPs dentro de un rango especificado.
     *
     * @param inicioIp IP de inicio del rango.
     * @param finIp IP de fin del rango.
     * @return La cantidad total de IPs en el rango.
     */
    public int calcularTotalIPsEnRango(String inicioIp, String finIp) {
        try {
            long ipInicio = ipToLong(InetAddress.getByName(inicioIp));
            long ipFin = ipToLong(InetAddress.getByName(finIp));

            // Verifica que la IP de inicio sea menor o igual a la de fin
            if (ipInicio > ipFin) {
                throw new IllegalArgumentException(
                        Configuracion.getConfiguracion().getRb().getString("Calculo_ip_inicio_menor_igual_ip_fin"));
            }

            return (int) (ipFin - ipInicio + 1); // Devuelve la cantidad total de IPs en el rango
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // Retorna 0 en caso de error
        }
    }

    /**
     * Verifica si una dirección IP está dentro de un rango de IPs.
     *
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
     *
     * @param ip1 Primera IP.
     * @param ip2 Segunda IP.
     * @return Un valor negativo si ip1 < ip2, positivo si ip1 > ip2, o 0 si son
     * iguales.
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
     * Metodo que crea el panel con una disposicion organizada
     *
     * @return panel
     */
    // Método para crear el panel con disposición organizada
    public JPanel crearMapaDeEstado() {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        // Empieza a actualizar el modelo del grafo
        graph.getModel().beginUpdate();
        Map<String, Object> vertexMap = new HashMap<>();

        try {
            // Añadir los equipos como vértices
            for (Vertex<Equipo> vertex : vertices.values()) {
                Equipo equipo = vertex.getElement();

                // Establece el color según el estado del equipo
                String color = equipo.isEstado() ? "green" : "red";

                // Crea un estilo para el equipo basado en el estado
                Map<String, Object> estilo = new HashMap<>();
                estilo.put("fillColor", color);
				estilo.put("fontColor", "black"); // El color del texto (negro)
                graph.getStylesheet().putCellStyle("EQUIPO_" + equipo.getCodigo(), estilo);

                // Inserta el vértice con el estilo correspondiente
                Object v = graph.insertVertex(parent, equipo.getCodigo(),
                        equipo.getCodigo() + "\n" + equipo.getDescripcion(), 0, 0, 80, 30,
                        "EQUIPO_" + equipo.getCodigo());
                vertexMap.put(equipo.getCodigo(), v);
            }

            // Añadir conexiones como aristas entre vértices
            for (Edge<Conexion> edge : red.edges()) {
                Conexion conexion = edge.getElement();
                Object equipo1 = vertexMap.get(conexion.getEquipo1().getCodigo());
                Object equipo2 = vertexMap.get(conexion.getEquipo2().getCodigo());
                graph.insertEdge(parent, null, conexion.getTipoCable().getDescripcion(), equipo1, equipo2);
            }
        } finally {
            graph.getModel().endUpdate();
        }

        // Configura el layout jerárquico
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.execute(graph.getDefaultParent());

        // Crear el componente del grafo y ajustarlo al panel
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(graphComponent, BorderLayout.CENTER);

        return panel;
    }

	public JPanel crearMapaDeEstadoConRuta(List<Equipo> ruta) {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
	
		// Inicia la actualización del modelo
		graph.getModel().beginUpdate();
		Map<String, Object> vertexMap = new HashMap<>();
	
		try {
			// Añadir los equipos como vértices
			for (Vertex<Equipo> vertex : vertices.values()) {
				Equipo equipo = vertex.getElement();
	
				// Verifica si el equipo está en la ruta y cambia el color del nodo si es necesario
				String estiloNodo = "defaultNode";
				if (ruta.contains(equipo)) {
					estiloNodo = "rutaNode";
					Map<String, Object> estiloRutaNodo = new HashMap<>();
					estiloRutaNodo.put("fillColor", "yellow"); // Color del nodo en la ruta
					graph.getStylesheet().putCellStyle("rutaNode", estiloRutaNodo);
				} else {
					// Estilo por defecto del nodo
					Map<String, Object> estiloDefaultNodo = new HashMap<>();
					estiloDefaultNodo.put("fillColor", equipo.isEstado() ? "white" : "red");
					graph.getStylesheet().putCellStyle("defaultNode", estiloDefaultNodo);
				}
	
				// Inserta el vértice en el grafo con el estilo correspondiente
				Object v = graph.insertVertex(parent, equipo.getCodigo(),
						equipo.getCodigo() + "\n" + equipo.getDescripcion(), 0, 0, 80, 30, estiloNodo);
				vertexMap.put(equipo.getCodigo(), v);
			}
	
			// Añadir conexiones y pintar las flechas si son parte de la ruta
			for (Edge<Conexion> edge : red.edges()) {
				Conexion conexion = edge.getElement();
				Object equipo1 = vertexMap.get(conexion.getEquipo1().getCodigo());
				Object equipo2 = vertexMap.get(conexion.getEquipo2().getCodigo());
	
				// Verifica si la conexión es parte de la ruta y cambia el color de la flecha
				String estiloArista = "defaultEdge";
				if (ruta.contains(conexion.getEquipo1()) && ruta.contains(conexion.getEquipo2())) {
					estiloArista = "rutaEdge";
					Map<String, Object> estiloRutaArista = new HashMap<>();
					estiloRutaArista.put("strokeColor", "black"); // Color de las flechas de la ruta
					estiloRutaArista.put("fontColor", "black"); // El color del texto (negro)
					estiloRutaArista.put("endArrow", "block");
					graph.getStylesheet().putCellStyle("rutaEdge", estiloRutaArista);
				}
	
				// Inserta la arista con el estilo apropiado
				graph.insertEdge(parent, null, conexion.getTipoCable().getDescripcion(), equipo1, equipo2, estiloArista);
			}
		} finally {
			graph.getModel().endUpdate();
		}
	
		// Configura el layout jerárquico
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		layout.execute(graph.getDefaultParent());
	
		// Crea el componente del grafo y ajusta el panel
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(graphComponent, BorderLayout.CENTER);
	
		return panel;
	}
	
}
