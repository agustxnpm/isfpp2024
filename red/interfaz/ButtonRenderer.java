package red.interfaz;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import red.controlador.Configuracion;

/**
 * Clase que extiende JButton e implementa TableCellRenderer para personalizar la visualización
 * de botones en celdas de una tabla.
 */
class ButtonRenderer extends JButton implements TableCellRenderer {
    private String actionType; // Tipo de acción que realizará el botón ("eliminar" o "modificar").

    /**
     * Constructor de la clase ButtonRenderer.
     * 
     * @param actionType Tipo de acción que el botón debe representar ("eliminar" o "modificar").
     */
    public ButtonRenderer(String actionType) {
        setOpaque(true);
        this.actionType = actionType;
    }

    /**
     * Método que configura el componente de celda de la tabla.
     * 
     * @param table La tabla que contiene la celda.
     * @param value El valor de la celda.
     * @param isSelected True si la celda está seleccionada.
     * @param hasFocus True si la celda tiene el foco.
     * @param row El índice de la fila de la celda.
     * @param column El índice de la columna de la celda.
     * @return El componente configurado para ser renderizado en la celda de la tabla.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? (actionType.equals(Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar_minuscula"))
                ? Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar")
                : Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar")) : value.toString());
        return this;
    }
}
