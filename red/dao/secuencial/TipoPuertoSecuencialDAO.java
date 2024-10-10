package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.TipoPuertoDAO;

import red.modelo.TipoPuerto;

public class TipoPuertoSecuencialDAO implements TipoPuertoDAO{

	private List<TipoPuerto> list;
	private boolean actualizar;
	private String name;
	
	public TipoPuertoSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		name = rb.getString("tipoPuerto");
		actualizar = true;
	}
	
	private List<TipoPuerto> readFromFile(String fileName) throws FileNotFoundException {

		List<TipoPuerto> tipoPuerto = new ArrayList<TipoPuerto>();
	        Scanner fileScanner = new Scanner(new File(fileName));

	        while (fileScanner.hasNextLine()) {
	            String line = fileScanner.nextLine();
	            Scanner lineScanner = new Scanner(line);
	            lineScanner.useDelimiter(";");

	            if (lineScanner.hasNext()) {
	                String codigo = lineScanner.next();
	                String descripcion = lineScanner.next();
	                int velocidad = Integer.parseInt(lineScanner.next());
	                tipoPuerto.add(new TipoPuerto(codigo, descripcion, velocidad));
	            }
	            lineScanner.close();
	        }
	        fileScanner.close();
	        return tipoPuerto;
	}
	
	private void writeToFile(List<TipoPuerto> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (TipoPuerto e : list) {
				outFile.format("%s;%s;%s;\n",
						e.getCodigo(),
						e.getDescripcion(),
						e.getVelocidad());
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
	public void insertar(TipoPuerto tipoPuerto) {
		list.add(tipoPuerto);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void actualizar(TipoPuerto tipoPuerto) {
		int pos = list.indexOf(tipoPuerto);
		list.set(pos, tipoPuerto);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void borrar(TipoPuerto tipoPuerto) {
		list.remove(tipoPuerto);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public List<TipoPuerto> buscarTodos() throws FileNotFoundException {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

}
