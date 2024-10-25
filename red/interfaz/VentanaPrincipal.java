package red.interfaz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestión de Red - Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Opciones");
        menuBar.add(menu);

        JMenuItem menuEquipos = new JMenuItem("Gestionar Equipos");
        JMenuItem menuConexiones = new JMenuItem("Gestionar Conexiones");
        JMenuItem menuConsultas = new JMenuItem("Consultas de Red"); // Nueva opción para VentanaConsultas
        menu.add(menuEquipos);
        menu.add(menuConexiones);
        menu.add(menuConsultas); // Agregar el nuevo ítem al menú

        setJMenuBar(menuBar);

        // Acción para abrir la ventana de equipos
        menuEquipos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaEquipos ventanaEquipos = new VentanaEquipos();
                ventanaEquipos.setVisible(true);  // Mostrar la ventana de equipos
            }
        });

        // Acción para abrir la ventana de conexiones
        menuConexiones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaConexiones ventanaConexiones = new VentanaConexiones();
                ventanaConexiones.setVisible(true);  // Mostrar la ventana de conexiones
            }
        });

        // Acción para abrir la ventana de consultas
        menuConsultas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaConsultas ventanaConsultas = new VentanaConsultas();
                ventanaConsultas.setVisible(true);  // Mostrar la ventana de consultas
            }
        });
    }

    // Método para lanzar la ventana principal
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
