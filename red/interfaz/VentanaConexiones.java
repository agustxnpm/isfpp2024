package red.interfaz;import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Conexion;
import red.modelo.TipoCable;
import red.servicio.ConexionService;
import red.servicio.ConexionServiceImp;

public class VentanaConexiones extends JFrame {

    private ConexionService conexionService;
    private JTable conexionesTable;
    private DefaultTableModel conexionTableModel;

    public VentanaConexiones() {
        setTitle("Gestión de Conexiones");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            conexionService = new ConexionServiceImp();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de las conexiones.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String[] conexionColumnNames = {"Equipo 1", "Equipo 2", "Tipo de Cable", "Acciones"};
        conexionTableModel = new DefaultTableModel(conexionColumnNames, 0);
        conexionesTable = new JTable(conexionTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;  // Solo la columna de "Acciones" es editable
            }
        };

        JScrollPane scrollConexiones = new JScrollPane(conexionesTable);
        add(scrollConexiones, BorderLayout.CENTER);

        JButton agregarConexionButton = new JButton("Agregar Conexión");
        agregarConexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarConexion();
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.add(agregarConexionButton);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarConexionesEnTabla();  // Mostrar conexiones al iniciar
    }

    private void mostrarConexionesEnTabla() {
        try {
            List<Conexion> conexiones = conexionService.buscarTodos();
            conexionTableModel.setRowCount(0);  // Limpiar la tabla
            for (Conexion conexion : conexiones) {
                conexionTableModel.addRow(new Object[]{conexion.getEquipo1().getCodigo(), conexion.getEquipo2().getCodigo(), conexion.getTipoCable().getDescripcion(), "Eliminar"});
            }
    
            // Asignar el renderer y editor a la columna de "Acciones"
            conexionesTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
            conexionesTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox()));
    
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las conexiones.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void eliminarConexion(Conexion conexion) {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar esta conexión?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            conexionService.borrar(conexion);  // Lógica para borrar la conexión
            mostrarConexionesEnTabla();  // Refrescar la tabla después de eliminar
        }
    }
    

    private void agregarConexion() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField equipo1Field = new JTextField();
        JTextField equipo2Field = new JTextField();
        JTextField tipoCableField = new JTextField();

        panel.add(new JLabel("Equipo 1:"));
        panel.add(equipo1Field);
        panel.add(new JLabel("Equipo 2:"));
        panel.add(equipo2Field);
        panel.add(new JLabel("Tipo de Cable:"));
        panel.add(tipoCableField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Conexión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Crear la conexión (la lógica para buscar equipos se deja para ti)
                Conexion conexion = new Conexion(null, null, new TipoCable(tipoCableField.getText(), "", 0), null, null);
                conexionService.insertar(conexion);
                mostrarConexionesEnTabla();  // Refrescar la tabla
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
