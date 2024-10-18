package red.interfaz;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Eliminar" : value.toString());
        return this;
    }
}