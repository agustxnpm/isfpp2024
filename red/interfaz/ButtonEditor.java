package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import red.servicio.EquipoService;
import red.servicio.ConexionService;
import red.modelo.Equipo;
import red.modelo.Conexion;

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private String actionType; // Nueva variable para determinar el tipo de acción ("equipo" o "conexion")
    private EquipoService equipoService;
    private ConexionService conexionService;

    // Constructor para el manejo de eliminaciones, especificando el tipo de acción
    public ButtonEditor(JCheckBox checkBox, JTable table, String actionType, EquipoService equipoService,
            ConexionService conexionService) {
        super(checkBox);
        this.table = table;
        this.actionType = actionType; // Define si es para equipos o conexiones
        this.equipoService = equipoService;
        this.conexionService = conexionService;

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "Eliminar" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = table.getSelectedRow();

            // Verificación para evitar el índice -1 (sin fila seleccionada)
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida antes de eliminar.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return label;
            }

            // Dependiendo del tipo de acción, se llama al método correspondiente
            if ("equipo".equals(actionType)) {
                eliminarEquipo(selectedRow);
            } else if ("conexion".equals(actionType)) {
                eliminarConexion(selectedRow);
            }
        }
        isPushed = false;
        return label;
    }

    // Método para eliminar un equipo
    private void eliminarEquipo(int selectedRow) {
        String equipoCodigo = (String) table.getValueAt(selectedRow, 0); // Código del equipo
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar este equipo?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Equipo equipoAEliminar = equipoService.buscarPorCodigo(equipoCodigo);
                equipoService.borrar(equipoAEliminar);

                // Detener la edición antes de eliminar la fila
                stopCellEditing();

                // Eliminar la fila del modelo de la tabla
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);

                JOptionPane.showMessageDialog(null, "Equipo eliminado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar el equipo: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para eliminar una conexión
    private void eliminarConexion(int selectedRow) {
        String equipo1Codigo = (String) table.getValueAt(selectedRow, 0); // Código del primer equipo
        String equipo2Codigo = (String) table.getValueAt(selectedRow, 1); // Código del segundo equipo
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar esta conexión?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Conexion conexionAEliminar = conexionService.buscarPorCodigo(equipo1Codigo, equipo2Codigo);
                conexionService.borrar(conexionAEliminar);

                // Detener la edición antes de eliminar la fila
                stopCellEditing();

                // Eliminar la fila del modelo de la tabla
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);

                JOptionPane.showMessageDialog(null, "Conexión eliminada correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar la conexión: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
