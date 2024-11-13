package red.interfaz;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import red.controlador.Configuracion;

class ButtonRenderer extends JButton implements TableCellRenderer {
    private String actionType;

    public ButtonRenderer(String actionType) {
        setOpaque(true);
        this.actionType = actionType;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? (actionType.equals(Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar_minuscula"))
        		? Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar")
        		: Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar")): value.toString());
        return this;
    }
}