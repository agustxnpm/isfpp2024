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

public class VentanaEquipos extends JFrame {

	private EquipoService equipoService;
	private JTable equiposTable;
	private DefaultTableModel equipoTableModel;

	public VentanaEquipos() {
		setTitle("Gestión de Equipos");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		try {
			equipoService = new EquipoServiceImp();
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

			// Asignar el renderer y editor a la columna de "Acciones"
			equiposTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
			equiposTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox()));

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar los equipos.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void eliminarEquipo(Equipo equipo) {
		int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este equipo?",
				"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			equipoService.borrar(equipo); // Lógica para borrar el equipo
			mostrarEquiposEnTabla(); // Refrescar la tabla después de eliminar
		}
	}
	private void agregarEquipo() {
		JPanel panel = new JPanel(new GridLayout(0, 2));
	
		JTextField codigoField = new JTextField();
		JTextField modeloField = new JTextField();
		JTextField marcaField = new JTextField();
		JTextField descripcionField = new JTextField();
		
		// ComboBox for TipoEquipo
		JComboBox<String> tipoEquipoComboBox = new JComboBox<>(new String[]{"AP", "COM", "RT", "SW"});
		// ComboBox for TipoPuerto
		JComboBox<String> tipoPuertoComboBox = new JComboBox<>(new String[]{"C5", "C5E", "C6", "100M", "1G"});
		// ComboBox for Ubicacion
		JComboBox<String> ubicacionComboBox = new JComboBox<>(new String[]{"A01", "A03", "A06", "BT", "FOT", "GRE", "L00", "L02", "L05", "OFI", "RAM"});
	
		JTextField cantPuertosField = new JTextField();
	
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
	
		int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Equipo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				int cantPuertos = Integer.parseInt(cantPuertosField.getText());
				String tipoEquipo = (String) tipoEquipoComboBox.getSelectedItem();
				String tipoPuerto = (String) tipoPuertoComboBox.getSelectedItem();
				String ubicacion = (String) ubicacionComboBox.getSelectedItem();
	
				// Set velocity based on selected TipoPuerto
				int velocidad = 0;
				switch (tipoPuerto) {
					case "C5":
					case "100M":
						velocidad = 100;
						break;
					case "C5E":
					case "C6":
					case "1G":
						velocidad = 1000;
						break;
					default:
						velocidad = 0;
						break;
				}
	
				TipoPuerto puerto = new TipoPuerto(tipoPuerto, "Descripción del puerto", velocidad);
	
				// Create Equipo object with selected values
				Equipo equipo = new Equipo(codigoField.getText(), modeloField.getText(), marcaField.getText(),
						descripcionField.getText(), new Ubicacion(ubicacion, ""), new TipoEquipo(tipoEquipo, ""), 
						cantPuertos, puerto, true);
	
				equipoService.insertar(equipo);
				mostrarEquiposEnTabla(); // Refresh the table after insertion
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al agregar el equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}	