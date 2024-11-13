	package red.interfaz;

	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.io.FileNotFoundException;
	import java.util.List;

	import red.modelo.Equipo;
	import red.modelo.TipoPuerto;
	import red.modelo.Ubicacion;
	import red.negocio.Calculo;
	import red.negocio.Red;
	import red.modelo.Conexion;
	import red.controlador.Configuracion;

	class ButtonEditor extends DefaultCellEditor {
		
		private Red red;
		private Calculo calculo;

		protected JButton button;
		
		private String label;
		private boolean isPushed;
		private JTable table;
		private String actionType; // Nueva variable para determinar el tipo de acción ("equipo" o "conexion")
		
		

		// Constructor para el manejo de eliminaciones, especificando el tipo de acción
		public ButtonEditor(JCheckBox checkBox, JTable table, String actionType, Red red, Calculo calculo) {
			
			super(checkBox);
			this.table = table;
			this.actionType = actionType; // Define si es para equipos o conexiones
			this.red = red;
			this.calculo = calculo;


			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(e -> fireEditingStopped());
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
			label = (value == null) ? actionType.equals(Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar_minuscula"))
					? Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar")
					: Configuracion.getConfiguracion().getRb().getString("Interfaz_modificar") : value.toString();
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
					JOptionPane.showMessageDialog(null, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_selecciona_fila_valida"),
							Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
					return label;
				}

				// Dependiendo del tipo de acción, se llama al método correspondiente
				if (Configuracion.getConfiguracion().getRb().getString("Modelo_equipo").equals(actionType))
					eliminarEquipo(selectedRow);
				else if (Configuracion.getConfiguracion().getRb().getString("Modelo_conexion").equals(actionType))
					eliminarConexion(selectedRow);
				else if (Configuracion.getConfiguracion().getRb().getString("Interfaz_eliminar_minuscula").equals(actionType))
					modificarEquipo(selectedRow); // Método para modificar
			}
			isPushed = false;
			return label;
		}

		// Método para eliminar un equipo
		private void eliminarEquipo(int selectedRow) {
			String equipoCodigo = (String) table.getValueAt(selectedRow, 0); // Código del equipo
			int confirmacion = JOptionPane.showConfirmDialog(null, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_seguro_eliminar_equipo"),
					Configuracion.getConfiguracion().getRb().getString("ButtonEditor_confirmar_eliminacion"), JOptionPane.YES_NO_OPTION);
			if (confirmacion == JOptionPane.YES_OPTION) {
				try {
					Equipo equipoAEliminar = red.buscarEquipoPorCodigo(equipoCodigo);
					red.borrarEquipo(equipoAEliminar);
					// Detener la edición antes de eliminar la fila
					stopCellEditing();

					// Eliminar la fila del modelo de la tabla
					((DefaultTableModel) table.getModel()).removeRow(selectedRow);

					JOptionPane.showMessageDialog(null, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_equipo_eliminado_correctamente"),
							Configuracion.getConfiguracion().getRb().getString("ButtonEditor_exito"),
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, String.format(Configuracion.getConfiguracion().getRb()
																				.getString("ButtonEditor_error_eliminar_equipo"), e.getMessage()),
							Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		private void modificarEquipo(int selectedRow) {
			String equipoCodigo = (String) table.getValueAt(selectedRow, 0);
			Equipo equipoAModificar;
		
			List<Ubicacion> listUbicaciones;
			List<TipoPuerto> listTipoPuerto;
		
			try {
				listUbicaciones = red.getUbicaciones();
				listTipoPuerto = red.getTipoPuertoService().buscarTodos();
				String[] ubicacionArray = listUbicaciones.stream().map(Ubicacion::getDescripcion).toArray(String[]::new);
				String[] tipoPuertoArray = listTipoPuerto.stream().map(TipoPuerto::getCodigo).toArray(String[]::new);
		
				// Crear un panel para el diálogo de modificación
				JPanel panel = new JPanel(new GridLayout(0, 2));
				JTextField modeloField = new JTextField();
				JTextField marcaField = new JTextField();
				JTextField codigoField = new JTextField();
				JTextField tipoEquipoField = new JTextField();
				JTextField descripcionField = new JTextField();
				JTextField cantPuertosField = new JTextField();
				JCheckBox estadoCheckBox = new JCheckBox(Configuracion.getConfiguracion().getRb().getString("ButtonEditor_activo"));
		
				JComboBox<String> ubicacionComboBox = new JComboBox<>(ubicacionArray);
				JComboBox<String> tipoPuertoComboBox = new JComboBox<>(tipoPuertoArray);
		
				// Agregar componentes al panel
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_codigo_opcion")));
				codigoField.setEditable(false);
				panel.add(codigoField);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_descripcion_opcion")));
				panel.add(descripcionField);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_marca_opcion")));
				panel.add(marcaField);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_modelo_opcion")));
				panel.add(modeloField);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_equipo_opcion")));
				tipoEquipoField.setEditable(false);
				panel.add(tipoEquipoField);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_ubicacion_opcion")));
				panel.add(ubicacionComboBox);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_estado_opcion")));
				panel.add(estadoCheckBox); // Añadir el checkbox de estado
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_cantidad_puertos_opcion")));
				panel.add(cantPuertosField);
				panel.add(new JLabel(Configuracion.getConfiguracion().getRb().getString("Equipo_tipo_puerto_opcion")));
				panel.add(tipoPuertoComboBox);
		
				// Buscar el equipo y mostrar el diálogo de modificación
				equipoAModificar = red.buscarEquipoPorCodigo(equipoCodigo);
				codigoField.setText(equipoAModificar.getCodigo());
				tipoEquipoField.setText(equipoAModificar.getTipoEquipo().getDescripcion());
				estadoCheckBox.setSelected(equipoAModificar.isEstado()); // Inicializar el estado actual
				modeloField.setText(equipoAModificar.getModelo());
				marcaField.setText(equipoAModificar.getMarca());
				descripcionField.setText(equipoAModificar.getDescripcion());
				ubicacionComboBox.setSelectedItem(equipoAModificar.getUbicacion().getCodigo());
		
				// Mostrar el diálogo de entrada
				int result = JOptionPane.showConfirmDialog(null, panel, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_modificar_equipo"),
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
				if (result == JOptionPane.OK_OPTION) {
					// Obtener valores y asignar al equipo antes de actualizar
					equipoAModificar.setModelo(modeloField.getText());
					equipoAModificar.setMarca(marcaField.getText());
					equipoAModificar.setDescripcion(descripcionField.getText());
					equipoAModificar.setEstado(estadoCheckBox.isSelected()); // Asignar el estado del checkbox
					String ubicacionSeleccionada = (String) ubicacionComboBox.getSelectedItem();
					String tipoPuertoSeleccionado = (String) tipoPuertoComboBox.getSelectedItem();
		
					for (Ubicacion ub : listUbicaciones)
						if (ub.getDescripcion().equals(ubicacionSeleccionada))
							equipoAModificar.setUbicacion(ub);

					if (!cantPuertosField.getText().isBlank())
						for (TipoPuerto p : listTipoPuerto)
							if (p.getCodigo().equals(tipoPuertoSeleccionado))
								equipoAModificar.agregarPuerto(Integer.parseInt(cantPuertosField.getText()), p);
		
					// Actualizar el equipo en el servicio
					red.modificarEquipo(equipoAModificar);
		
					// Mensaje de confirmación y refrescar tabla
					JOptionPane.showMessageDialog(null, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_equipo_modificado_correctamente"),
							Configuracion.getConfiguracion().getRb().getString("ButtonEditor_exito"), JOptionPane.INFORMATION_MESSAGE);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getModelo(), selectedRow, 3);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getMarca(), selectedRow, 2);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getDescripcion(), selectedRow, 1);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getUbicacion().getDescripcion(), selectedRow, 5);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.isEstado()
							? Configuracion.getConfiguracion().getRb().getString("ButtonEditor_activo")
							: Configuracion.getConfiguracion().getRb().getString("ButtonEditor_inactivo"), selectedRow, 6);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getPuertosInfo(), selectedRow, 7);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, String.format(Configuracion.getConfiguracion().getRb().getString("ButtonEditor_error_modificar_equipo"),
						e.getMessage()), Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// Método para eliminar una conexión
		private void eliminarConexion(int selectedRow) {
			String equipo1Codigo = (String) table.getValueAt(selectedRow, 0); // Código del primer equipo
			String equipo2Codigo = (String) table.getValueAt(selectedRow, 1); // Código del segundo equipo
			int confirmacion = JOptionPane.showConfirmDialog(null, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_seguro_eliminar_conexion"),
					Configuracion.getConfiguracion().getRb().getString("ButtonEditor_confirmar_eliminacion"), JOptionPane.YES_NO_OPTION);
			if (confirmacion == JOptionPane.YES_OPTION) {
				try {
					Conexion conexionAEliminar = red.buscarConexionPorCodigo(equipo1Codigo, equipo2Codigo);
				//	calculo.borrarConexionDelGrafo(conexionAEliminar);
					red.borrarConexion(conexionAEliminar);

					// Detener la edición antes de eliminar la fila
					stopCellEditing();

					// Eliminar la fila del modelo de la tabla
					((DefaultTableModel) table.getModel()).removeRow(selectedRow);

					JOptionPane.showMessageDialog(null, Configuracion.getConfiguracion().getRb().getString("ButtonEditor_conexion_eliminada_correctamente"), 
							Configuracion.getConfiguracion().getRb().getString("ButtonEditor_exito"), JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, String.format(Configuracion.getConfiguracion().getRb().getString("ButtonEditor_error_eliminar_conexion"),
							e.getMessage()), Configuracion.getConfiguracion().getRb().getString("Interfaz_error"), JOptionPane.ERROR_MESSAGE);
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
			try {
				super.fireEditingStopped();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), Configuracion.getConfiguracion().getRb().getString("Interfaz_error"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
