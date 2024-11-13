package red.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;
import red.negocio.Calculo;
import red.negocio.Red;
import red.controlador.Configuracion;

public class VentanaEquipos extends JFrame {

	private Red red;
	private Calculo calculo;
	private JTable equiposTable;
	private DefaultTableModel equipoTableModel;
	private List<TipoEquipo> listTipoEquipo; // Lista de TipoEquipo
	private List<TipoPuerto> listTipoPuerto; // Lista de TipoPuerto
	private List<Ubicacion> listUbicaciones; // Lista de Ubicaciones

	public VentanaEquipos(Calculo calculo, Red red) {
		setTitle(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_titulo"));
		setSize(1200, 400); // Ajustar el tamaño para ver todas las columnas
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.red = red;
		this.calculo = calculo;

		try {
			listTipoEquipo = red.getTipoEquipoService().buscarTodos();
			listTipoPuerto = red.getTipoPuertoService().buscarTodos();
			listUbicaciones = red.getUbicaciones();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_error_cargar_datos"),
					Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
		}

		inicializarComponentes();
	}

	private void inicializarComponentes() {

		String[] equipoColumnNames = { Configuracion.getConfiguracion().getRb().getString("Equipo_codigo"),
				Configuracion.getConfiguracion().getRb().getString("Equipo_descripcion"),
				Configuracion.getConfiguracion().getRb().getString("Equipo_marca"),
				Configuracion.getConfiguracion().getRb().getString("Equipo_modelo"),
				Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_equipo"),
				Configuracion.getConfiguracion().getRb().getString("Equipo_ubicacion"),
				Configuracion.getConfiguracion().getRb().getString("Equipo_estado"),
				Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_info_puertos"),
				Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_direccion_ip"),
				Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_acciones"),
				Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_modificar") };
		equipoTableModel = new DefaultTableModel(equipoColumnNames, 0);
		equiposTable = new JTable(equipoTableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 9 || column == 10;
			}
		};

		JScrollPane scrollEquipos = new JScrollPane(equiposTable);
		add(scrollEquipos, BorderLayout.CENTER);

		JButton agregarEquipoButton = new JButton(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_agregar_equipo"));
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
			// Convertir booleano a texto legible
			String estadoTexto = equipo.isEstado() ? Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_activo")
					: Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_inactivo");
			equipoTableModel.addRow(new Object[] { equipo.getCodigo(), equipo.getDescripcion(), equipo.getMarca(),
					equipo.getModelo(), equipo.getTipoEquipo().getDescripcion(),

					equipo.getUbicacion().getDescripcion(),

					estadoTexto, equipo.getPuertosInfo(),
					
					equipo.getDireccionesIp().toString(),

					Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar"),
					
					Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar") });
		}

		// Configurar los botones de "Eliminar"
		equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_acciones"))
				.setCellRenderer(new ButtonRenderer(Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar")));
		equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_acciones"))
				.setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable, Configuracion.getConfiguracion().getRb().getString("Modelo_equipo"), red, calculo));

		equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar"))
				.setCellRenderer(new ButtonRenderer(Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar_minuscula")));
		equiposTable.getColumn(Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar"))
				.setCellEditor(new ButtonEditor(new JCheckBox(), equiposTable,
						Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar_minuscula"), red, calculo));

	}

	private String generarIPAleatoria(Calculo calculo) {
		List<String> ipsExistentes = calculo.obtenerTodasLasIPs();
		Random random = new Random();
		int tercerOcteto = 16; // Inicialmente 192.168.16.x
		int cuartoOcteto;

		while (true) {
			cuartoOcteto = random.nextInt(256); // Genera un número entre 0 y 255
			String ipGenerada = "192.168." + tercerOcteto + "." + cuartoOcteto;

			if (!ipsExistentes.contains(ipGenerada)) {
				return ipGenerada; // Devuelve la IP si no está en la lista de existentes
			}

			// Si todas las IPs posibles en 192.168.16.x están asignadas, pasa al siguiente
			// octeto
			if (cuartoOcteto == 255 && !ipsExistentes.contains("192.168." + (tercerOcteto + 1) + ".0")) {
				tercerOcteto++;
			}
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
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_codigo_opcion")));
		panel.add(codigoField);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_modelo_opcion")));
		panel.add(modeloField);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_marca_opcion")));
		panel.add(marcaField);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_descripcion_opcion")));
		panel.add(descripcionField);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_cantidad_puertos_opcion")));
		panel.add(cantPuertosField);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_equipo_opcion")));
		panel.add(tipoEquipoComboBox);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_puerto_opcion")));
		panel.add(tipoPuertoComboBox);
		panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_ubicacion_opcion")));
		panel.add(ubicacionComboBox);
		int result = JOptionPane.showConfirmDialog(this, panel, Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_agregar_equipo"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
				
				// asignar una ip aleatoria al equipo
				String ipAsignada = generarIPAleatoria(calculo);
				equipo.agregarIp(ipAsignada);
				
				calculo.agregarEquipoAlGrafo(equipo);
				red.agregarEquipo(equipo);
				mostrarEquiposEnTabla(); // Refrescar la tabla después de la inserción
				JOptionPane.showMessageDialog(this, Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_equipo_anadido_correctamente"));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						String.format(Configuracion.getConfiguracion().getRb().getString("VentanaEquipos_error_agregar_equipo"), e.getMessage()),
						Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
