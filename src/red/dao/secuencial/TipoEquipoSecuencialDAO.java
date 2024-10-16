package red.dao.secuencial;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.TipoEquipoDAO;
import red.modelo.TipoEquipo;

public class TipoEquipoSecuencialDAO implements TipoEquipoDAO {

	private List<TipoEquipo> list;
	private boolean actualizar;
	private String name;
	
	public TipoEquipoSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		name = rb.getString("tipoEquipo");
		actualizar = true;
	}
	
	private List<TipoEquipo> readFromFile(String fileName) throws FileNotFoundException {

		List<TipoEquipo> tipoEquipo = new ArrayList<TipoEquipo>();
        Scanner fileScanner = new Scanner(new File(fileName));

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            if (lineScanner.hasNext()) {
                String codigo = lineScanner.next();
                String descripcion = lineScanner.next();
                tipoEquipo.add(new TipoEquipo(codigo, descripcion));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return tipoEquipo;
	}
	
	private void writeToFile(List<TipoEquipo> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (TipoEquipo e : list) {
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
	public void insertar(TipoEquipo tipoEquipo) {
		list.add(tipoEquipo);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(TipoEquipo tipoEquipo) {
		int pos = list.indexOf(tipoEquipo);
		list.set(pos, tipoEquipo);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void borrar(TipoEquipo tipoEquipo) {
		list.remove(tipoEquipo);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public List<TipoEquipo> buscarTodos() throws FileNotFoundException {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

}
