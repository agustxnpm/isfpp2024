package red.interfaz;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import red.controlador.Constantes;
import red.controlador.Configuracion;
import red.negocio.Calculo;
import red.negocio.Red;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

/**
 * Clase que representa la ventana principal de la aplicación.
 * Permite navegar entre distintas funcionalidades de la aplicación como la gestión de equipos,
 * conexiones y consultas sobre la red.
 */
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Calculo calculo;
    private Red red;
    private Handler manejador;

    private JMenuBar menuBar;
    private JMenu mnPrograma;
    private JMenu mnOpciones;
    private JMenu mnModo;
    private JMenu mnIdiomas;
    private JMenuItem mntmCreditos;
    private JMenuItem mntmSalir;
    private JMenuItem mntmEquipos;
    private JMenuItem mntmConexiones;
    private JMenuItem mntmConsultas;
    private JMenuItem modoSimulacion;
    private JMenuItem modoReal;
    private JMenuItem mntmEspanolAR;
    private JMenuItem mntmEspanolES;
    private JMenuItem mntmInglesEU;
    private boolean modo; // true = modo simulación, false = modo real
    private JLabel lblModoActual; // Etiqueta para mostrar el modo actual
    private JLabel lblISFPP;
    private JLabel lblPOO;
    private JLabel lblGestion;

    /**
     * Constructor de la clase VentanaPrincipal.
     * Inicializa la ventana y carga los servicios necesarios.
     */
    public VentanaPrincipal() {
        try {
            calculo = new Calculo();
            red = Red.getRed();
            calculo.cargarDatos(red.getEquipos(), red.getConexiones());
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
            System.exit(ERROR);
        }

        setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_titulo"));
        setSize(628, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    /**
     * Inicializa los componentes gráficos y configura las acciones de los menús.
     */
    private void inicializarComponentes() {
        // Crear menú
        menuBar = new JMenuBar();
        mnPrograma = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa"));
        mnOpciones = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones"));
        mnModo = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo"));
        mnIdiomas = new JMenu(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas"));

        menuBar.add(mnPrograma);
        menuBar.add(mnOpciones);
        menuBar.add(mnModo);
        menuBar.add(mnIdiomas);

        modoSimulacion = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo_simulacion"));
        modoReal = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo_real"));
        mnModo.add(modoSimulacion);
        mnModo.add(modoReal);

        mntmCreditos = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_creditos"));
        mntmSalir = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_salir"));
        mnPrograma.add(mntmCreditos);
        mnPrograma.add(mntmSalir);

        mntmEquipos = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_gestionar_equipos"));
        mntmConexiones = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_gestionar_conexiones"));
        mntmConsultas = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_consultas_red"));
        mnOpciones.add(mntmEquipos);
        mnOpciones.add(mntmConexiones);
        mnOpciones.add(mntmConsultas);

        mntmEspanolAR = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_espanol_ar"));
        mntmEspanolES = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_espanol_es"));
        mntmInglesEU = new JMenuItem(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_ingles_eu"));
        mnIdiomas.add(mntmEspanolAR);
        mnIdiomas.add(mntmEspanolES);
        mnIdiomas.add(mntmInglesEU);

        setJMenuBar(menuBar);

        manejador = new Handler();

        // Acciones de los menús
        mntmCreditos.addActionListener(
                e -> JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_creditos_texto"),
                        Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_creditos_texto"), JOptionPane.PLAIN_MESSAGE));

        mntmSalir.addActionListener(e -> {
            int salir = JOptionPane.showConfirmDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_seguro_salir"),
                    Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_salir"), JOptionPane.YES_NO_OPTION);
            if (salir == JOptionPane.YES_OPTION) {
                dispose();
                JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_gracias_usar_programa"),
                        Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_despedida"), JOptionPane.PLAIN_MESSAGE);
                System.exit(NORMAL);
            }
        });

        mntmEquipos.addActionListener(manejador);
        mntmConexiones.addActionListener(manejador);
        mntmConsultas.addActionListener(manejador);

        lblModoActual = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_simulacion"));
        lblModoActual.setFont(new Font("Dialog", Font.BOLD, 14));
        cambiarModo(Configuracion.getConfiguracion().isSimulacion());

        modoSimulacion.addActionListener(manejador);
        modoReal.addActionListener(manejador);

        mntmEspanolAR.addActionListener(manejador);
        mntmEspanolES.addActionListener(manejador);
        mntmInglesEU.addActionListener(manejador);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // Componentes estéticos
        lblISFPP = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_isfpp"));
        lblISFPP.setHorizontalAlignment(SwingConstants.CENTER);
        lblISFPP.setFont(new Font("Dialog", Font.BOLD, 18));

        lblPOO = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_poo"));
        lblPOO.setHorizontalAlignment(SwingConstants.CENTER);
        lblPOO.setFont(new Font("Dialog", Font.BOLD, 15));

        lblGestion = new JLabel(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_gestion"));
        lblGestion.setHorizontalAlignment(SwingConstants.CENTER);
        lblGestion.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));

        JLabel lblFotoRedes = new JLabel("");
        lblFotoRedes.setIcon(new ImageIcon(VentanaPrincipal.class.getResource(Constantes.RUTA_REDES)));

        JLabel lblUNPSJB = new JLabel("");
        lblUNPSJB.setIcon(new ImageIcon(VentanaPrincipal.class.getResource(Constantes.RUTA_UNPSJB)));

        // Layout del contentPane
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(20)
                            .addComponent(lblModoActual))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(35)
                            .addComponent(lblFotoRedes)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(lblUNPSJB))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(47)
                            .addComponent(lblISFPP))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(179)
                            .addComponent(lblPOO))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(209)
                            .addComponent(lblGestion)))
                    .addContainerGap(109, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addGap(27)
                    .addComponent(lblISFPP)
                    .addGap(5)
                    .addComponent(lblPOO)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblGestion)
                    .addGap(6)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(99)
                            .addComponent(lblFotoRedes))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(69)
                            .addComponent(lblUNPSJB)))
                    .addPreferredGap(ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                    .addComponent(lblModoActual)
                    .addContainerGap())
        );
        contentPane.setLayout(gl_contentPane);
    }

    /**
     * Cambia el modo de la aplicación y actualiza la etiqueta lblModoActual.
     * @param esSimulacion true para modo simulación, false para modo real.
     */
    private void cambiarModo(boolean esSimulacion) {
        this.modo = esSimulacion;
        if (modo)
            lblModoActual.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_simulacion"));
        else
            lblModoActual.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_real"));
    }

    /**
     * Clase interna para manejar las acciones de los menús.
     */
    private class Handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(mntmEquipos)) {
                VentanaEquipos ventanaEquipos = new VentanaEquipos(calculo, red);
                ventanaEquipos.setVisible(true);
            }

            if (e.getSource().equals(mntmConexiones)) {
                VentanaConexiones ventanaConexiones = new VentanaConexiones(calculo, red);
                ventanaConexiones.setVisible(true);
            }

            if (e.getSource().equals(mntmConsultas)) {
                VentanaConsultas ventanaConsultas = new VentanaConsultas(calculo, red, modo);
                ventanaConsultas.setVisible(true);
            }

            if (e.getSource().equals(modoReal))
                cambiarModo(false);

            if (e.getSource().equals(modoSimulacion))
                cambiarModo(true);

            if (e.getSource().equals(mntmEspanolES)) {
                Configuracion.getConfiguracion().establecerIdiomaYPais(Constantes.ESPANOL, Constantes.ESPANA);
                restablecer();
            }

            if (e.getSource().equals(mntmEspanolAR)) {
                Configuracion.getConfiguracion().establecerIdiomaYPais(Constantes.ESPANOL, Constantes.ARGENTINA);
                restablecer();
            }

            if (e.getSource().equals(mntmInglesEU)) {
                Configuracion.getConfiguracion().establecerIdiomaYPais(Constantes.INGLES, Constantes.ESTADOS_UNIDOS);
                restablecer();
            }
        }
    }

    /**
     * Restablece los textos de las etiquetas al cambiar de idioma.
     */
    private void restablecer() {
        setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_titulo"));
        mnPrograma.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa"));
        mnOpciones.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones"));
        mnModo.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo"));
        mnIdiomas.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas"));
        mntmCreditos.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_creditos"));
        mntmSalir.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_programa_salir"));
        mntmEquipos.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_gestionar_equipos"));
        mntmConexiones.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_gestionar_conexiones"));
        mntmConsultas.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_opciones_consultas_red"));
        modoSimulacion.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo_simulacion"));
        modoReal.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_modo_real"));
        mntmEspanolAR.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_espanol_ar"));
        mntmEspanolES.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_espanol_es"));
        mntmInglesEU.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_idiomas_ingles_eu"));
        cambiarModo(modo);
        lblISFPP.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_isfpp"));
        lblPOO.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_poo"));
        lblGestion.setText(Configuracion.getConfiguracion().getRb().getString("VentanaPrincipal_gestion"));
    }

}
