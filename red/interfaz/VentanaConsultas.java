package red.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            calculo = new Calculo(); // Initialize Calculo



        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de las conexiones.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

     
        JButton calcularVelocidadButton = new JButton("Calcular Velocidad Máxima");
        calcularVelocidadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularVelocidadMaxima();
            }
        });

        JButton pingEquipoButton = new JButton("Realizar Ping a Equipo");
        pingEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarPingAEquipo();
            }
        });

        JButton detectarProblemasButton = new JButton("Detectar Problemas de Conectividad");
        detectarProblemasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detectarProblemasConectividad();
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(calcularVelocidadButton);
        panel.add(pingEquipoButton);
        panel.add(detectarProblemasButton);

        add(panel, BorderLayout.CENTER);
    }

    // Method to calculate maximum speed based on selected equipment
    private void calcularVelocidadMaxima() {
        try {
            // Fetch the list of equipment and connections
            List<Equipo> equipos = equipoService.buscarTodos();
            List<Conexion> conexiones = conexionService.buscarTodos();
            calculo.cargarDatos(equipos, conexiones); // Load data into the graph

            // Perform calculation between two selected teams
            Equipo equipoInicio = equipoService.buscarPorCodigo("EQUIPO1"); // Example code
            Equipo equipoFin = equipoService.buscarPorCodigo("EQUIPO2"); // Example code

            List<Equipo> ruta = calculo.buscarRuta(equipoInicio, equipoFin);

            if (ruta != null) {
                int velocidadMaxima = calculo.calcularVelocidadMaxima(ruta);
                JOptionPane.showMessageDialog(this, "Velocidad máxima de transmisión: " + velocidadMaxima + " Mbps");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró una ruta entre los equipos seleccionados.");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to perform ping on a selected team
    private void realizarPingAEquipo() {
        String direccionIp = JOptionPane.showInputDialog(this, "Ingrese la dirección IP del equipo:");
        boolean respuestaPing = calculo.realizarPingAEquipo(direccionIp);

        if (respuestaPing) {
            JOptionPane.showMessageDialog(this, "Ping exitoso al equipo con IP: " + direccionIp);
        } else {
            JOptionPane.showMessageDialog(this, "Ping fallido o equipo no encontrado.");
        }
    }

    private void detectarProblemasConectividad() {
        try {
            Equipo equipo = equipoService.buscarPorCodigo("EQUIPO1"); // Example code
            Equipo gateway = equipoService.buscarPorCodigo("GATEWAY"); // Example code

            calculo.verificarConectividad(equipo, gateway); // Perform connectivity check
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar el equipo o Gateway.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
