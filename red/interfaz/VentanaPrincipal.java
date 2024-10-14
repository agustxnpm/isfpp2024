package red.interfaz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

import red.modelo.Equipo;
import red.modelo.Conexion;
import red.servicio.EquipoService;
import red.servicio.EquipoServiceImp;
import red.servicio.ConexionService;
import red.servicio.ConexionServiceImp;

public class VentanaPrincipal extends JFrame {

    private EquipoService equipoService;
    private ConexionService conexionService;

    private JButton btnVerEquipos;
    private JButton btnAgregarEquipo;
    private JButton btnVerConexiones;
    private JTextArea textArea;

    public VentanaPrincipal() {
        // Inicializar los servicios
        try {
            equipoService = new EquipoServiceImp(); // Servicio para gestionar equipos
            conexionService = new ConexionServiceImp(); // Servicio para gestionar conexiones
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Configuración de la ventana principal
        setTitle("Gestión de la Red de Equipos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en pantalla
        setLayout(new BorderLayout());

        // Crear el área de texto donde se mostrarán resultados
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Crear un panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        // Crear botones
        btnVerEquipos = new JButton("Ver Equipos");
        btnAgregarEquipo = new JButton("Agregar Equipo");
        btnVerConexiones = new JButton("Ver Conexiones");

        // Agregar los botones al panel
        panelBotones.add(btnVerEquipos);
        panelBotones.add(btnAgregarEquipo);
        panelBotones.add(btnVerConexiones);

        // Agregar el panel de botones a la ventana
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar los eventos de los botones
        configurarEventos();
    }

    private void configurarEventos() {
        // Evento para mostrar los equipos registrados
        btnVerEquipos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarEquipos();
            }
        });

        // Evento para agregar un nuevo equipo
        btnAgregarEquipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarEquipo();
            }
        });

        // Evento para mostrar las conexiones entre equipos
        btnVerConexiones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarConexiones();
            }
        });
    }

    // Método para mostrar los equipos registrados
    private void mostrarEquipos() {
        textArea.setText("Equipos registrados:\n");
        try {
            List<Equipo> equipos = equipoService.buscarTodos(); // Cargar equipos desde la persistencia
            if (equipos.isEmpty()) {
                textArea.append("No hay equipos registrados.\n");
            } else {
                for (Equipo equipo : equipos) {
                    textArea.append(equipo.getCodigo() + ": " + equipo.getDescripcion() + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            textArea.setText("Error al cargar los equipos.\n");
        }
    }

    // Método para mostrar las conexiones registradas
    private void mostrarConexiones() {
        textArea.setText("Conexiones registradas:\n");
        try {
            List<Conexion> conexiones = conexionService.buscarTodos(); // Cargar conexiones desde la persistencia
            if (conexiones.isEmpty()) {
                textArea.append("No hay conexiones registradas.\n");
            } else {
                for (Conexion conexion : conexiones) {
                    textArea.append(conexion.getEquipo1().getCodigo() + " -> " +
                                    conexion.getEquipo2().getCodigo() + " (Cable: " +
                                    conexion.getTipoCable().getDescripcion() + ")\n");
                }
            }
        } catch (FileNotFoundException e) {
            textArea.setText("Error al cargar las conexiones.\n");
        }
    }

    // Método para agregar un equipo
    private void agregarEquipo() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del equipo:");
        String descripcion = JOptionPane.showInputDialog(this, "Ingrese la descripción del equipo:");
        // Puedes agregar más campos como modelo, marca, etc.

        // Crear un objeto Equipo (debes agregar los campos correspondientes)
        Equipo equipo = new Equipo(codigo, "modelo", "marca", descripcion, null, null, 0, null, true);
        
        equipoService.insertar(equipo);  // Guardar el equipo en la persistencia
        textArea.append("Equipo agregado: " + codigo + "\n");
    }

    // Método para lanzar la ventana
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            }
        });
    }
}
