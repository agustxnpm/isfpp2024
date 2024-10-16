package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.TipoCableDAO;
import red.modelo.TipoCable;

public class TipoCableSecuencialDAO implements TipoCableDAO {

	private List<TipoCable> list;
	private boolean actualizar;
	private String name;
	
	public TipoCableSecuencialDAO() {
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		name = rb.getString("tipoCable");
		actualizar = true;
	}
	
	private List<TipoCable> readFromFile(String fileName) throws FileNotFoundException {

		List<TipoCable> tipoCables = new ArrayList<TipoCable>();
		Scanner fileScanner = new Scanner(new File(fileName));

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");

            if (lineScanner.hasNext()) {
                String codigo = lineScanner.next();
                String descripcion = lineScanner.next();
                int velocidad = Integer.parseInt(lineScanner.next());
                tipoCables.add(new TipoCable(codigo, descripcion, velocidad));
            }
            lineScanner.close();
        }
        fileScanner.close();
        return tipoCables;
	}
	
	private void writeToFile(List<TipoCable> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (TipoCable e : list) {
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
	public void insertar(TipoCable tipoCable) {
		list.add(tipoCable);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void actualizar(TipoCable tipoCable) {
		int pos = list.indexOf(tipoCable);
		list.set(pos, tipoCable);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void borrar(TipoCable tipoCable) {
		list.remove(tipoCable);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public List<TipoCable> buscarTodos() throws FileNotFoundException {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

}
