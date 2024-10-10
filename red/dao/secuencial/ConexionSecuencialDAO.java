package red.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import red.dao.ConexionDAO;
import red.dao.EquipoDAO;
import red.modelo.Conexion;
import red.modelo.Equipo;
import red.modelo.TipoCable;
import red.modelo.TipoPuerto;
import red.dao.TipoCableDAO;
import red.dao.TipoPuertoDAO;

public class ConexionSecuencialDAO implements ConexionDAO {

	private Map<String, Equipo> equipos;
	private Map<String, TipoCable> tipoCable;
	private Map<String, TipoPuerto> tipoPuerto;
	private List<Conexion> list;
	private boolean actualizar;
	private String name;

	public ConexionSecuencialDAO() throws FileNotFoundException {
		equipos = cargarEquipos();
		tipoCable = cargarTipoCable();
		tipoPuerto = cargarTipoPuerto();

		ResourceBundle rb = ResourceBundle.getBundle("config");
		name = rb.getString("conexion");
		actualizar = true;
	}

	private List<Conexion> readFromFile(String fileName) throws FileNotFoundException {

		List<Conexion> conexiones = new ArrayList<Conexion>();
		Scanner fileScanner = new Scanner(new File(fileName));

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				Equipo equipo1 = equipos.get(lineScanner.next());
				TipoPuerto tipoPuerto1 = tipoPuerto.get(lineScanner.next());
				Equipo equipo2 = equipos.get(lineScanner.next());
				TipoPuerto tipoPuerto2 = tipoPuerto.get(lineScanner.next());
				TipoCable tipoCableConexion = tipoCable.get(lineScanner.next());
				conexiones.add(new Conexion(equipo1, equipo2, tipoCableConexion, tipoPuerto1, tipoPuerto2));
			}
			lineScanner.close();
		}
		fileScanner.close();
		return conexiones;
	}

	private void writeToFile(List<Conexion> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Conexion e : list) {
				outFile.format("%s;%s;%s;%s;%s;\n",
						e.getEquipo1().getCodigo(),
						e.getTipoPuerto1().getCodigo(),
						e.getEquipo2().getCodigo(),
						e.getTipoPuerto2().getCodigo(),
						e.getTipoCable().getCodigo());
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

	private Map<String, Equipo> cargarEquipos() throws FileNotFoundException {
		Map<String, Equipo> equipos = new HashMap<String, Equipo>();
		EquipoDAO equipoDAO = new EquipoSecuencialDAO();
		List<Equipo> eq = equipoDAO.buscarTodos();
		for (Equipo e : eq) {
			equipos.put(e.getCodigo(), e);
		}

		return equipos;
	}

	private Map<String, TipoCable> cargarTipoCable() throws FileNotFoundException {
		Map<String, TipoCable> cables = new HashMap<String, TipoCable>();
		TipoCableDAO cablesDAO = new TipoCableSecuencialDAO();
		List<TipoCable> cbls = cablesDAO.buscarTodos();
		for (TipoCable c : cbls) {
			cables.put(c.getCodigo(), c);
		}

		return cables;
	}

	private Map<String, TipoPuerto> cargarTipoPuerto() throws FileNotFoundException {
		Map<String, TipoPuerto> puertos = new HashMap<String, TipoPuerto>();
		TipoPuertoDAO puertosDAO = new TipoPuertoSecuencialDAO();
		List<TipoPuerto> prts = puertosDAO.buscarTodos();
		for (TipoPuerto p : prts) {
			puertos.put(p.getCodigo(), p);
		}

		return puertos;
	}

	@Override
	public void insertar(Conexion conexion) {
		list.add(conexion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Conexion conexion) {
		int pos = list.indexOf(conexion);
		list.set(pos, conexion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void borrar(Conexion conexion) {
		list.remove(conexion);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public List<Conexion> buscarTodos() throws FileNotFoundException {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

}
