package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.UbicacionDAO;
import red.modelo.Ubicacion;

public class UbicacionesSecuencialDAO implements UbicacionDAO {
	private List<Ubicacion> list;
	private boolean actualizar;
	private String name;

	public UbicacionesSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		name = rb.getString("ubicacion");
		actualizar = true;
	}

	private List<Ubicacion> readFromFile(String fileName) throws FileNotFoundException {

		List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
		Scanner fileScanner = new Scanner(new File(fileName));

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				String codigo = lineScanner.next();
				String descripcion = lineScanner.next();
				ubicaciones.add(new Ubicacion(codigo, descripcion));
			}
			lineScanner.close();
		}
		fileScanner.close();
		return ubicaciones;
	}

	private void writeToFile(List<Ubicacion> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Ubicacion e : list) {
				outFile.format("%s;%s;\n", 
						e.getCodigo(),
						e.getDescripcion());
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error creating file.");
		} catch (FormatterClosedException formatterClosedException) {
			System.err.println("Error writing to file.");
		} finally {
			if (outFile != null)
				outFile.close();
		}
	}

	@Override
	public void insertar(Ubicacion ubicacion) {
		list.add(ubicacion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Ubicacion ubicacion) {
		int pos = list.indexOf(ubicacion);
		list.set(pos, ubicacion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void borrar(Ubicacion ubicacion) {
		list.remove(ubicacion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public List<Ubicacion> buscarTodos() throws FileNotFoundException {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

}
