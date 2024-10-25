package red.interfaz;

import javax.swing.*;
import javax.swing.JOptionPane;
import red.controlador.Constantes;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestión de Red - Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear menú
        JMenuBar menuBar = new JMenuBar();

        JMenu mnPrograma = new JMenu("Programa");
        JMenu mnOpciones = new JMenu("Opciones");
        menuBar.add(mnPrograma);
        menuBar.add(mnOpciones);
        
        JMenuItem menuCreditos = new JMenuItem("Créditos");
        JMenuItem menuSalir = new JMenuItem("Salir");
        mnPrograma.add(menuCreditos);
        mnPrograma.add(menuSalir);

        JMenuItem menuEquipos = new JMenuItem("Gestionar Equipos");
        JMenuItem menuConexiones = new JMenuItem("Gestionar Conexiones");
        mnOpciones.add(menuEquipos);
        mnOpciones.add(menuConexiones);

        setJMenuBar(menuBar);
        
        menuCreditos.addActionListener(e -> JOptionPane.showMessageDialog(this, Constantes.CREDITOS, "Créditos", JOptionPane.PLAIN_MESSAGE));
        
        // Acción para cerrar la ventana, enviar mensaje de despedida y finalizar el programa
        menuSalir.addActionListener(e -> {
        	int salir = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que querés salir?",
                    "Salida", JOptionPane.YES_NO_OPTION);
            if (salir == JOptionPane.YES_OPTION) {
                dispose();
                JOptionPane.showMessageDialog(this, "¡Gracias por usar el programa!", "Despedida", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        });

        // Acción para abrir la ventana de equipos
        menuEquipos.addActionListener(e -> {
            VentanaEquipos ventanaEquipos = new VentanaEquipos();
            ventanaEquipos.setVisible(true);  // Mostrar la ventana de equipos
        });

        // Acción para abrir la ventana de conexiones
        menuConexiones.addActionListener(e -> {
            VentanaConexiones ventanaConexiones = new VentanaConexiones();
            ventanaConexiones.setVisible(true);  // Mostrar la ventana de conexiones
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
