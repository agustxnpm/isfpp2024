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

import red.dao.EquipoDAO;
import red.dao.TipoPuertoDAO;
import red.modelo.Equipo;
import red.modelo.TipoEquipo;
import red.modelo.TipoPuerto;
import red.modelo.Ubicacion;
import red.dao.TipoEquipoDAO;
import red.dao.UbicacionDAO;


public class EquipoSecuencialDAO implements EquipoDAO {

	private Map<String, TipoEquipo> tipoEquipo;
	private Map<String, Ubicacion> ubicaciones;
	private Map<String, TipoPuerto> tipoPuerto;
	private List<Equipo> list;
	private boolean actualizar;
	private String name;
	
	public EquipoSecuencialDAO() throws FileNotFoundException {
		tipoEquipo = cargarTipoEquipo();
		ubicaciones = cargarUbicaciones();
		tipoPuerto = cargarTipoPuerto();

		ResourceBundle rb = ResourceBundle.getBundle("config");
		name = rb.getString("equipo");
		actualizar = true;
	}
	
	private List<Equipo> readFromFile(String fileName) throws FileNotFoundException{
		List<Equipo> equipos = new ArrayList<Equipo>();

		Scanner fileScanner = new Scanner(new File(fileName));
		String codigo, modelo, marca, descripcion;
		Ubicacion ubicacion;
		TipoEquipo tipoEquipo;
		int cantPuertos;
		TipoPuerto tipoPuerto;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				codigo = lineScanner.next();
				descripcion = lineScanner.hasNext() ? lineScanner.next() : "";
				marca = lineScanner.hasNext() ? lineScanner.next() : "";
				modelo = lineScanner.hasNext() ? lineScanner.next() : "";
				tipoEquipo = this.tipoEquipo.get(lineScanner.next());
				String ubicacionCodigo = lineScanner.next();
				ubicacion = ubicaciones.get(ubicacionCodigo);

				String puertosInfo = lineScanner.next();
				String[] puertoParts = puertosInfo.split(","); // Separar los tipos y cantidades de puerto

				// guardamos los datos del primer puerto en el archivo para poder instanciar Equipo 
				String tipoPuertoCodigo = puertoParts[0];
				cantPuertos = Integer.parseInt(puertoParts[1]);

				// Crear una instancia del objeto Equipo
				Equipo equipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos,
						this.tipoPuerto.get(tipoPuertoCodigo), false);

				for (int i = 2; i < puertoParts.length; i += 2) {
					// comienza desde el indice 2 ya que ya agregamos el
					// primer puerto (cantidad, tipo) al momento de
					// instanciar Equipo
					tipoPuertoCodigo = puertoParts[i];
					cantPuertos = Integer.parseInt(puertoParts[i + 1]);
					tipoPuerto = this.tipoPuerto.get(tipoPuertoCodigo);
					equipo.agregarPuerto(cantPuertos, tipoPuerto);
				}

				if (lineScanner.hasNext()) {
					String ipInfo = lineScanner.next();
					String[] direccionesIpArray = ipInfo.split(",");
					for (String ip : direccionesIpArray) {
						if (!ip.isEmpty()) {
							equipo.agregarIp(ip);
						}
					}
				}

				boolean estado = Boolean.parseBoolean(lineScanner.next());
				equipo.setEstado(estado);

				equipos.add(equipo);
			}
			lineScanner.close();
		}
		fileScanner.close();
		return equipos;
	
	}
	
	private void writeToFile(List<Equipo> list, String file) {
		Formatter outFile = null;
	    try {
	        outFile = new Formatter(file);
	        for (Equipo e : list) {
	            // Formateo de los datos del equipo en el archivo
	            StringBuilder ipAddresses = new StringBuilder();
	            List<String> ips = e.getDireccionesIp();
	            for (int i = 0; i < ips.size(); i++) {
	                ipAddresses.append(ips.get(i));
	                if (i < ips.size() - 1) {
	                    ipAddresses.append(",");
	                }
	            }

	            outFile.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;\n",
	                e.getCodigo(),                       
	                e.getDescripcion() != null ? e.getDescripcion() : "",   
	                e.getMarca() != null ? e.getMarca() : "",             
	                e.getModelo() != null ? e.getModelo() : "",        
	                e.getTipoEquipo().getCodigo(),          
	                e.getUbicacion().getCodigo(),         
	                e.getPuertosInfo(),                        
	                ipAddresses.toString(),                    
	                e.isEstado()                               
	            );
	        }
	    } catch (FileNotFoundException fileNotFoundException) {
	        System.err.println("Error creating file.");
	    } catch (FormatterClosedException formatterClosedException) {
	        System.err.println("Error writing to file.");
	    } finally {
	        if (outFile != null) {
	            outFile.close();
	        }
	    }
	}

	private Map<String, TipoEquipo> cargarTipoEquipo() throws FileNotFoundException {
		Map<String, TipoEquipo> tipoEquipos = new HashMap<String, TipoEquipo>();
		TipoEquipoDAO tipoEquiposDAO = new TipoEquipoSecuencialDAO();
		List<TipoEquipo> tEq = tipoEquiposDAO.buscarTodos();
		for (TipoEquipo t : tEq) {
			tipoEquipos.put(t.getCodigo(), t);
		}

		return tipoEquipos;
	}
	
	private Map<String, Ubicacion> cargarUbicaciones() throws FileNotFoundException {
		Map<String, Ubicacion> ubicaciones = new HashMap<String, Ubicacion>();
		UbicacionDAO ubicacionesDAO = new UbicacionesSecuencialDAO();
		List<Ubicacion> ub = ubicacionesDAO.buscarTodos();
		for (Ubicacion u : ub) {
			ubicaciones.put(u.getCodigo(), u);
		}

		return ubicaciones;
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
	public void insertar(Equipo equipo) {
		list.add(equipo);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void actualizar(Equipo equipo) {
		int pos = list.indexOf(equipo);
		list.set(pos, equipo);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public void borrar(Equipo equipo) {
		list.remove(equipo);
		writeToFile(list, name);
		actualizar = true;		
	}

	@Override
	public List<Equipo> buscarTodos() throws FileNotFoundException {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}
	

}
