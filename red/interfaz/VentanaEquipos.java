package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;
import red.servicio.EquipoService;
import red.servicio.EquipoServiceImp;
import red.servicio.TipoEquipoService;
import red.servicio.TipoEquipoServiceImp;
import red.servicio.TipoPuertoService;
import red.servicio.TipoPuertoServiceImp;
import red.servicio.UbicacionService;
import red.servicio.UbicacionServiceImp;

public class VentanaEquipos extends JFrame implements ActionListener {

    private EquipoService equipoService;
    private TipoEquipoService teS; // Servicio para TipoEquipo
    private TipoPuertoService tpS; // Servicio para TipoPuerto
    private UbicacionService uS; // Servicio para Ubicación

    private JTable equiposTable;
    private DefaultTableModel equipoTableModel;
    private List<TipoEquipo> listTipoEquipo; // Lista de TipoEquipo
    private List<TipoPuerto> listTipoPuerto; // Lista de TipoPuerto
    private List<Ubicacion> listUbicaciones; // Lista de Ubicaciones

    public VentanaEquipos() {
        setTitle("Gestión de Equipos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            equipoService = new EquipoServiceImp();
            teS = new TipoEquipoServiceImp(); // Inicializar servicio de TipoEquipo
            tpS = new TipoPuertoServiceImp(); // Inicializar servicio de TipoPuerto
            uS = new UbicacionServiceImp(); // Inicializar servicio de Ubicación

            listTipoEquipo = teS.buscarTodos();
            listTipoPuerto = tpS.buscarTodos();
            listUbicaciones = uS.buscarTodos();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de los equipos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String[] equipoColumnNames = { "Código", "Modelo", "Marca", "Descripción", "Acciones" };
        equipoTableModel = new DefaultTableModel(equipoColumnNames, 0);
        equiposTable = new JTable(equipoTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo la columna de "Acciones" es editable
            }
        };

        JScrollPane scrollEquipos = new JScrollPane(equiposTable);
        add(scrollEquipos, BorderLayout.CENTER);

        JButton agregarEquipoButton = new JButton("Agregar Equipo");
        agregarEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarEquipo();
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.add(agregarEquipoButton);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarEquiposEnTabla(); // Mostrar equipos al iniciar
    }

	private void mostrarEquiposEnTabla() {
		try {
			List<Equipo> equipos = equipoService.buscarTodos();
			equipoTableModel.setRowCount(0); // Limpiar la tabla
			for (Equipo equipo : equipos) {
				equipoTableModel.addRow(new Object[] { equipo.getCodigo(), equipo.getModelo(), equipo.getMarca(),
						equipo.getDescripcion(), "Eliminar" });
			}
	
			equiposTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
			equiposTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable, "equipo", equipoService, null));
	
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar los equipos.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	


	private void eliminarEquipo(Equipo equipo) {
		int confirmacion = JOptionPane.showConfirmDialog(this, 
			"¿Estás seguro de que quieres eliminar este equipo?",
			"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
	
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				// Verificar si el equipo realmente existe antes de eliminarlo
				Equipo equipoExistente = equipoService.buscarPorCodigo(equipo.getCodigo());
				if (equipoExistente != null) {
					System.out.println("Eliminando equipo con código: " + equipo.getCodigo());
					equipoService.borrar(equipo); // Llamada para eliminar el equipo en la base de datos
					JOptionPane.showMessageDialog(this, 
						"Equipo eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					mostrarEquiposEnTabla(); // Refrescar la tabla para actualizar la lista de equipos
				} else {
					JOptionPane.showMessageDialog(this, 
						"El equipo no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, 
					"Error al eliminar el equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int row = equiposTable.getSelectedRow();
		String codigoEquipo = (String) equipoTableModel.getValueAt(row, 0); // Asumiendo que la columna 0 tiene el código
		
		try {
			// Aquí está la impresión que genera el log
			System.out.println("Botón 'Eliminaaaaaaar' clickeado para: " + codigoEquipo + " y " + equipoTableModel.getValueAt(row, 1));
	
			// Aquí buscarías el equipo por el código para eliminarlo
			Equipo equipoAEliminar = equipoService.buscarPorCodigo(codigoEquipo);
			
			eliminarEquipo(equipoAEliminar);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al buscar el equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
		
    private void agregarEquipo() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField codigoField = new JTextField();
        JTextField modeloField = new JTextField();
        JTextField marcaField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField cantPuertosField = new JTextField();

        // Convertir listas a arrays para los ComboBox
        String[] tipoEquipoArray = listTipoEquipo.stream().map(TipoEquipo::getCodigo).toArray(String[]::new);
        String[] tipoPuertoArray = listTipoPuerto.stream().map(TipoPuerto::getCodigo).toArray(String[]::new);
        String[] ubicacionArray = listUbicaciones.stream().map(Ubicacion::getCodigo).toArray(String[]::new);

        // ComboBoxes para TipoEquipo, TipoPuerto, y Ubicacion
        JComboBox<String> tipoEquipoComboBox = new JComboBox<>(tipoEquipoArray);
        JComboBox<String> tipoPuertoComboBox = new JComboBox<>(tipoPuertoArray);
        JComboBox<String> ubicacionComboBox = new JComboBox<>(ubicacionArray);

        // Agregar componentes al panel
        panel.add(new JLabel("Código:"));
        panel.add(codigoField);
        panel.add(new JLabel("Modelo:"));
        panel.add(modeloField);
        panel.add(new JLabel("Marca:"));
        panel.add(marcaField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descripcionField);
        panel.add(new JLabel("Cantidad de Puertos:"));
        panel.add(cantPuertosField);
        panel.add(new JLabel("Tipo de Equipo:"));
        panel.add(tipoEquipoComboBox);
        panel.add(new JLabel("Tipo de Puerto:"));
        panel.add(tipoPuertoComboBox);
        panel.add(new JLabel("Ubicacion:"));
        panel.add(ubicacionComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Equipo", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int cantPuertos = Integer.parseInt(cantPuertosField.getText());
                String tipoEquipo = (String) tipoEquipoComboBox.getSelectedItem();
                String tipoPuerto = (String) tipoPuertoComboBox.getSelectedItem();
                String ubicacion = (String) ubicacionComboBox.getSelectedItem();

                // Obtener los objetos seleccionados
                TipoPuerto selectedPuerto = listTipoPuerto.stream().filter(tp -> tp.getCodigo().equals(tipoPuerto))
                        .findFirst().orElse(null);
                Ubicacion selectedUbicacion = listUbicaciones.stream().filter(u -> u.getCodigo().equals(ubicacion))
                        .findFirst().orElse(null);

                // Crear nuevo equipo con los valores seleccionados
                Equipo equipo = new Equipo(codigoField.getText(), modeloField.getText(), marcaField.getText(),
                        descripcionField.getText(), selectedUbicacion, new TipoEquipo(tipoEquipo, ""), cantPuertos,
                        selectedPuerto, true);

                equipoService.insertar(equipo);
                mostrarEquiposEnTabla(); // Refrescar la tabla después de la inserción
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al agregar el equipo: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
