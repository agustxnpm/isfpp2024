	package red.interfaz;

	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.io.FileNotFoundException;
	import java.util.List;

	import red.modelo.Equipo;
	import red.modelo.TipoPuerto;
	import red.modelo.Ubicacion;
	import red.negocio.Calculo;
	import red.negocio.Red;
	import red.modelo.Conexion;

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
			label = (value == null) ? actionType.equals("eliminar") ? "Eliminar" : "Modificar" : value.toString();
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
				} else if ("modificar".equals(actionType)) {
					modificarEquipo(selectedRow); // Método para modificar
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
					Equipo equipoAEliminar = red.buscarEquipoPorCodigo(equipoCodigo);
					red.borrarEquipo(equipoAEliminar);
					// Detener la edición antes de eliminar la fila
					stopCellEditing();

					// Eliminar la fila del modelo de la tabla
					((DefaultTableModel) table.getModel()).removeRow(selectedRow);

					JOptionPane.showMessageDialog(null, "Equipo eliminado correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al eliminar el equipo: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
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
				JCheckBox estadoCheckBox = new JCheckBox("Activo");
		
				JComboBox<String> ubicacionComboBox = new JComboBox<>(ubicacionArray);
				JComboBox<String> tipoPuertoComboBox = new JComboBox<>(tipoPuertoArray);
		
				// Agregar componentes al panel
				panel.add(new JLabel("Codigo:"));
				codigoField.setEditable(false);
				panel.add(codigoField);
				panel.add(new JLabel("Descripción:"));
				panel.add(descripcionField);
				panel.add(new JLabel("Marca:"));
				panel.add(marcaField);
				panel.add(new JLabel("Modelo:"));
				panel.add(modeloField);
				panel.add(new JLabel("Tipo Equipo:"));
				tipoEquipoField.setEditable(false);
				panel.add(tipoEquipoField);
				panel.add(new JLabel("Ubicación:"));
				panel.add(ubicacionComboBox);
				panel.add(new JLabel("Estado:"));
				panel.add(estadoCheckBox); // Añadir el checkbox de estado
				panel.add(new JLabel("Cantidad de Puertos:"));
				panel.add(cantPuertosField);
				panel.add(new JLabel("Tipo de Puerto:"));
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
				int result = JOptionPane.showConfirmDialog(null, panel, "Modificar Equipo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
				if (result == JOptionPane.OK_OPTION) {
					// Obtener valores y asignar al equipo antes de actualizar
					equipoAModificar.setModelo(modeloField.getText());
					equipoAModificar.setMarca(marcaField.getText());
					equipoAModificar.setDescripcion(descripcionField.getText());
					equipoAModificar.setEstado(estadoCheckBox.isSelected()); // Asignar el estado del checkbox
					String ubicacionSeleccionada = (String) ubicacionComboBox.getSelectedItem();
					String tipoPuertoSeleccionado = (String) tipoPuertoComboBox.getSelectedItem();
		
					for (Ubicacion ub : listUbicaciones) {
						if (ub.getDescripcion().equals(ubicacionSeleccionada)) {
							equipoAModificar.setUbicacion(ub);
						}
					}
					if (!cantPuertosField.getText().isBlank()) {
						for (TipoPuerto p : listTipoPuerto) {
							if (p.getCodigo().equals(tipoPuertoSeleccionado)) {
								equipoAModificar.agregarPuerto(Integer.parseInt(cantPuertosField.getText()), p);
							}
						}
					}
		
					// Actualizar el equipo en el servicio
					red.modificarEquipo(equipoAModificar);
		
					// Mensaje de confirmación y refrescar tabla
					JOptionPane.showMessageDialog(null, "Equipo modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getModelo(), selectedRow, 3);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getMarca(), selectedRow, 2);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getDescripcion(), selectedRow, 1);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getUbicacion().getDescripcion(), selectedRow, 5);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.isEstado() ? "Activo" : "Inactivo", selectedRow, 6);
					((DefaultTableModel) table.getModel()).setValueAt(equipoAModificar.getPuertosInfo(), selectedRow, 7);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error al modificar el equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
					Conexion conexionAEliminar = red.buscarConexionPorCodigo(equipo1Codigo, equipo2Codigo);
				//	calculo.borrarConexionDelGrafo(conexionAEliminar);
					red.borrarConexion(conexionAEliminar);

					// Detener la edición antes de eliminar la fila
					stopCellEditing();

					// Eliminar la fila del modelo de la tabla
					((DefaultTableModel) table.getModel()).removeRow(selectedRow);

					JOptionPane.showMessageDialog(null, "Conexión eliminada correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
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
			try {
				super.fireEditingStopped();
			} catch (Exception e) {
				// solo atrapar la excepcion (no afecta al funcionamiento)
			}
		}
	}
