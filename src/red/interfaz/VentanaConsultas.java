package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Equipo;
import red.modelo.Conexion;
import red.negocio.Calculo;
import red.servicio.EquipoService;
import red.servicio.ConexionService;
import red.servicio.EquipoServiceImp;
import red.servicio.TipoCableServiceImp;
import red.servicio.ConexionServiceImp;

public class VentanaConsultas extends JFrame {

    private EquipoService equipoService;
    private ConexionService conexionService;
    private Calculo calculo;

    public VentanaConsultas() {
        setTitle("Consultas de la Red");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            // Inicializar los servicios antes de utilizarlos
            equipoService = new EquipoServiceImp();
            conexionService = new ConexionServiceImp();
            calculo = new Calculo();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de las conexiones.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

     
        JButton calcularVelocidadButton = new JButton("Calcular Velocidad Máxima");
        calcularVelocidadButton.addActionListener(e -> calcularVelocidadMaxima());

        JButton pingEquipoButton = new JButton("Realizar Ping a Equipo");
        pingEquipoButton.addActionListener(e -> realizarPingAEquipo());

        JButton detectarProblemasButton = new JButton("Detectar Problemas de Conectividad");
        detectarProblemasButton.addActionListener(e -> detectarProblemasConectividad());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(calcularVelocidadButton);
        panel.add(pingEquipoButton);
        panel.add(detectarProblemasButton);

        add(panel, BorderLayout.CENTER);
    }

    // Método para calcular velocidad máxima basado en dos equipos seleccionados
    private void calcularVelocidadMaxima() {
        try {
            // Busca la lista de equipos y conexiones y los carga en el grafo de Cálculo
            List<Equipo> equipos = equipoService.buscarTodos();
            List<Conexion> conexiones = conexionService.buscarTodos();
            calculo.cargarDatos(equipos, conexiones);

            // Pide al usuario que ingrese los códigos de ambos equipos y los busca, si existen
            String inicio = JOptionPane.showInputDialog("Ingrese el código del equipo de inicio:");
            Equipo equipoInicio = equipoService.buscarPorCodigo(inicio);
            if (equipoInicio == null) {
            	JOptionPane.showMessageDialog(this, "No se encontró un equipo de inicio con el código " + inicio + ". Parece que dicho equipo no existe\no hubo algún error.",
            			"Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }
            String fin = JOptionPane.showInputDialog("Ingrese el código del equipo de fin:");
            Equipo equipoFin = equipoService.buscarPorCodigo(fin); // Código de ejemplo
            if (equipoFin == null) {
            	JOptionPane.showMessageDialog(this, "No se encontró un equipo de fin con el código " + fin + ". Parece que dicho equipo no existe\no hubo algún error.",
            			"Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }

            // Realiza el cálculo entre ambos equipos
            List<Equipo> ruta = calculo.buscarRuta(equipoInicio, equipoFin);

            if (ruta != null) {
                int velocidadMaxima = calculo.calcularVelocidadMaxima(ruta);
                JOptionPane.showMessageDialog(this, "Velocidad máxima de transmisión: " + velocidadMaxima + " Mbps");
            } else JOptionPane.showMessageDialog(this, "No se encontró una ruta entre los equipos seleccionados.");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para realizar un ping en un equipo seleccionado
    private void realizarPingAEquipo() {
        String direccionIp = JOptionPane.showInputDialog(this, "Ingrese la dirección IP del equipo:");
        boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);

        if (respuestaPing)
            JOptionPane.showMessageDialog(this, "Ping exitoso al equipo con IP: " + direccionIp);
        else JOptionPane.showMessageDialog(this, "Ping fallido o equipo no encontrado.");
    }

    // Revisar la conectividad entre dos equipos dados
    private void detectarProblemasConectividad() {
        try {
        	String codigo = JOptionPane.showInputDialog("Ingrese el código del equipo de inicio:");
        	Equipo equipo = equipoService.buscarPorCodigo(codigo);
        	if (equipo == null) {
            	JOptionPane.showMessageDialog(this, "No se encontró un equipo de inicio con el código " + codigo + ". Parece que dicho equipo no existe\no hubo algún error.",
            			"Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }
        	String codigoGateway = JOptionPane.showInputDialog("Ingrese el código del equipo de Gateway:");
            Equipo gateway = equipoService.buscarPorCodigo(codigoGateway);
            if (gateway == null) {
            	JOptionPane.showMessageDialog(this, "No se encontró un Gateway con el código " + codigoGateway + ". Parece que dicho equipo no existe\no hubo algún error.",
            			"Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }

            calculo.verificarConectividad(equipo, gateway); // Revisar la conectividad
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar el equipo o Gateway.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
