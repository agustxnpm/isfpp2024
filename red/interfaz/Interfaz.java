package red.interfaz;

import java.util.List;
import java.util.Scanner;

import net.datastructures.Vertex;
import red.controlador.Coordinador;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;

/**
 * Clase que representa la interfaz de usuario para interactuar con el sistema.
 * Permite mostrar información y solicitar datos para gestionar la red.
 */
public class Interfaz {

    private Coordinador coordinador; // Coordinador para manejar la lógica de negocio.
    private Scanner scanner = new Scanner(System.in); // Scanner para leer entradas del usuario.

    /**
     * Asigna el coordinador para manejar las operaciones de la red.
     * @param coordinador El coordinador a utilizar.
     */
    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    /**
     * Muestra la lista de equipos en la consola.
     * @param equipos Lista de equipos a mostrar.
     */
    public void mostrarEquipos(List<Equipo> equipos) {
        for (Equipo equipo : equipos) {
            System.out.println(equipo);
        }
    }

    /**
     * Muestra la lista de conexiones en la consola.
     * @param conexiones Lista de conexiones a mostrar.
     */
    public void mostrarConexiones(List<Conexion> conexiones) {
        for (Conexion conexion : conexiones) {
            System.out.println(conexion);
        }
    }

    /**
     * Muestra la lista de ubicaciones en la consola.
     * @param ubicaciones Lista de ubicaciones a mostrar.
     */
    public void mostrarUbicaciones(List<Ubicacion> ubicaciones) {
        for (Ubicacion ubicacion : ubicaciones) {
            System.out.println(ubicacion);
        }
    }

    /**
     * Muestra las ubicaciones de los equipos.
     * @param eq Lista de equipos.
     */
    public void mostrarUbicacionesPorEquipo(List<Equipo> eq) {
        for (Equipo e : eq) {
            System.out.println("Equipo: " + e.getDescripcion() + " -> Ubicacion: " + e.getUbicacion().getDescripcion());
        }
    }

    /**
     * Solicita los datos de un nuevo equipo al usuario y crea una instancia de Equipo.
     * @return Un nuevo objeto Equipo con los datos proporcionados.
     */
    public Equipo solicitarDatosEquipo() {
        System.out.println("Ingrese los datos del nuevo equipo:");

        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Marca: ");
        String marca = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ubicacion codigo: ");
        String ubiCod = scanner.nextLine();

        System.out.print("Ubicacion descripcion: ");
        String ubiDesc = scanner.nextLine();

        System.out.print("TipoEquipo codigo: ");
        String teCod = scanner.nextLine();

        System.out.print("TipoEquipo descripcion: ");
        String teDesc = scanner.nextLine();

        System.out.print("TipoPuerto codigo: ");
        String tpCod = scanner.nextLine();

        System.out.print("TipoPuerto descripcion: ");
        String tpDesc = scanner.nextLine();

        System.out.print("TipoPuerto velocidad: ");
        String tpVel = scanner.nextLine();

        System.out.print("Cantidad de puertos: ");
        String cantPuerto = scanner.nextLine();

        // Crear instancias de Ubicacion, TipoEquipo y TipoPuerto
        Ubicacion ubicacion = new Ubicacion(ubiCod, ubiDesc);
        TipoEquipo tipoEquipo = new TipoEquipo(teCod, teDesc);
        TipoPuerto tipoPuerto = new TipoPuerto(tpCod, tpDesc, Integer.parseInt(tpVel));
        boolean estado = true; // Asignar un estado inicial al equipo.

        // Crear y retornar la instancia de Equipo.
        return new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, Integer.parseInt(cantPuerto),
                tipoPuerto, estado);
    }

    /**
     * Muestra el estado actual de todos los equipos conectados a la red.
     * @param eq Lista de equipos.
     */
    public void mostrarMapaDeEstado(List<Equipo> eq) {
        System.out.println("Mapa del estado actual de la red:");
        for (Equipo equipo : eq) {
            System.out.println(
                    equipo.getCodigo() + " - IPs: " + equipo.getDireccionesIp() + " - Estado: " + equipo.isEstado());
        }
    }

    /**
     * Muestra un mensaje de error en la consola.
     * @param mensaje El mensaje de error.
     */
    public void mostrarError(String mensaje) {
        System.err.println("Error: " + mensaje);
    }

    /**
     * Muestra un mensaje de éxito en la consola.
     * @param mensaje El mensaje de éxito.
     */
    public void mostrarExito(String mensaje) {
        System.out.println("Éxito: " + mensaje);
    }

    /**
     * Solicita el código de un equipo al usuario.
     * @return El código ingresado por el usuario.
     */
    public String solicitarCodigoEquipo() {
        System.out.print("Ingrese el código del equipo: ");
        return scanner.nextLine();
    }

    /**
     * Solicita una opción al usuario.
     * @return La opción ingresada por el usuario.
     */
    public String solicitarOpcion() {
        System.out.print("Ingrese su opción: ");
        return scanner.nextLine();
    }
}
