package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;
import red.negocio.Calculo;
import red.negocio.Red;

public class VentanaEquipos extends JFrame {

	private Red red;
	private Calculo calculo;

	private JTable equiposTable;
	private DefaultTableModel equipoTableModel;
	private List<TipoEquipo> listTipoEquipo; // Lista de TipoEquipo
	private List<TipoPuerto> listTipoPuerto; // Lista de TipoPuerto
	private List<Ubicacion> listUbicaciones; // Lista de Ubicaciones

	public VentanaEquipos(Calculo calculo, Red red) {
		setTitle("Gestión de Equipos");
		setSize(900, 400); // Ajustar el tamaño para ver todas las columnas
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.red = red;
		this.calculo = calculo;

		try {
			listTipoEquipo = red.getTipoEquipoService().buscarTodos();
			listTipoPuerto = red.getTipoPuertoService().buscarTodos();
			listUbicaciones = red.getUbicaciones();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar los datos de los equipos.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		inicializarComponentes();
	}

	private void inicializarComponentes() {
		
		String[] equipoColumnNames = { "Código", "Descripción", "Marca", "Modelo", "Tipo Equipo", "Ubicación", "Estado",
				"Info Puertos", "Acciones", "Modificar" };
		equipoTableModel = new DefaultTableModel(equipoColumnNames, 0);
		equiposTable = new JTable(equipoTableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 8 || column == 9; 
			}
		};

		JScrollPane scrollEquipos = new JScrollPane(equiposTable);
		add(scrollEquipos, BorderLayout.CENTER);

		JButton agregarEquipoButton = new JButton("Agregar Equipo");
		agregarEquipoButton.addActionListener(e -> agregarEquipo());

		JPanel panelInferior = new JPanel();
		panelInferior.add(agregarEquipoButton);
		add(panelInferior, BorderLayout.SOUTH);

		mostrarEquiposEnTabla(); // Mostrar equipos al iniciar
	}

	private void mostrarEquiposEnTabla() {
		List<Equipo> equipos = red.getEquipos();
		equipoTableModel.setRowCount(0); // Limpiar la tabla
		// Añadir filas a la tabla con todos los datos del equipo
		for (Equipo equipo : equipos) {
			String estadoTexto = equipo.isEstado() ? "Activo" : "Inactivo"; // Convertir booleano a texto legible
			equipoTableModel.addRow(new Object[] { equipo.getCodigo(), equipo.getDescripcion(), equipo.getMarca(),
					equipo.getModelo(), equipo.getTipoEquipo().getDescripcion(),

					equipo.getUbicacion().getDescripcion(),

					estadoTexto, equipo.getPuertosInfo(),

					"Eliminar", "Modificar" });
		}

		// Configurar los botones de "Eliminar"
		equiposTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer("eliminar"));
		equiposTable.getColumn("Acciones")
				.setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable, "equipo", red, calculo));

		equiposTable.getColumn("Modificar").setCellRenderer(new ButtonRenderer("modificar"));
		equiposTable.getColumn("Modificar")
				.setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable, "modificar", red, calculo));

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
				calculo.agregarEquipoAlGrafo(equipo);
				red.agregarEquipo(equipo);
				mostrarEquiposEnTabla(); // Refrescar la tabla después de la inserción
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al agregar el equipo: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
