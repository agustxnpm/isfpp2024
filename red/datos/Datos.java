package datos;

import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import modelo.*;

public class Datos {

	private static Map<String, Equipo> equipos;
	private static Map<String, Ubicacion> ubicaciones;
	private static List<Conexion> conexiones;
	private static Map<String, TipoCable> tipoCable;
	private static Map<String, TipoPuerto> tipoPuerto;
	private static Map<String, TipoEquipo> tipoEquipo;

	/**
	 * Cargar un mapa con instancias de Equipo las cuales estan cargadas en un
	 * archivo de texto
	 * 
	 * @param fileName
	 * @return Mapa con los equipos que fueron cargados
	 * @throws FileNotFoundException si no se encuentra el archivo
	 */
	public static Map<String, Equipo> cargarEquipos(String fileName) throws FileNotFoundException {

		equipos = new HashMap<String, Equipo>();

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
				tipoEquipo = Datos.tipoEquipo.get(lineScanner.next());
				ubicacion = Datos.ubicaciones.get(lineScanner.next());

				// Procesar los puertos
				String puertosInfo = lineScanner.next();
				String[] puertoParts = puertosInfo.split(","); // Separar los tipos y cantidades de puerto
				
				// guardamos los datos del primer puerto en el archivo para poder instanciar Equipo
				String tipoPuertoCodigo = puertoParts[0];
				cantPuertos = Integer.parseInt(puertoParts[1]);

				// Crear una instancia del objeto Equipo
				Equipo equipo = new Equipo(codigo, modelo, marca, descripcion, ubicacion, tipoEquipo, cantPuertos, Datos.tipoPuerto.get(tipoPuertoCodigo), false);

				for (int i = 2; i < puertoParts.length; i += 2) { // comienza desde el indice 2 ya que ya agregamos el
																	// primer puerto (cantidad, tipo) al momento de
																	// instanciar Equipo
					
					tipoPuertoCodigo = puertoParts[i]; // campo que contiene el codigo del puerto
					cantPuertos = Integer.parseInt(puertoParts[i + 1]); // campo que contiene la cantidad de dicho
																		// puerto
					tipoPuerto = Datos.tipoPuerto.get(tipoPuertoCodigo); // Obtener TipoPuerto según el codigo

					equipo.agregarPuerto(cantPuertos, tipoPuerto);
				}

				if (lineScanner.hasNext()) {
					String ipInfo = lineScanner.next();
					String[] direccionesIpArray = ipInfo.split(",");
					for (String ip : direccionesIpArray) {
						if (!ip.isEmpty())
							equipo.agregarIp(ip);
					}
				}

				boolean estado = Boolean.parseBoolean(lineScanner.next());
				equipo.setEstado(estado);
				
				equipos.put(codigo, equipo);
			}

			lineScanner.close();
		}

		fileScanner.close();
		return equipos;
	}

	/**
	 * Cargar un mapa con todos los tipos de equipo que pueden existir en la red
	 * 
	 * @param fileName archivo que contiene los datos de los tipos de equipo
	 * @return Mapa con instancias de TipoEquipo
	 * @throws FileNotFoundException
	 */
	public static Map<String, TipoEquipo> cargarTipoEquipo(String fileName) throws FileNotFoundException {
		tipoEquipo = new HashMap<String, TipoEquipo>();

		Scanner fileScanner = new Scanner(new File(fileName));

		String codigo, descripcion;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				codigo = lineScanner.next();
				descripcion = lineScanner.next();
				tipoEquipo.put(codigo, new TipoEquipo(codigo, descripcion));
			}

			lineScanner.close();
		}

		fileScanner.close();
		return tipoEquipo;

	}

	/**
	 * Cargar un mapa con todos los tipos de cable que pueden haber en la red
	 * 
	 * @param fileName archivo que contiene los datos de los tipos de cable
	 * @return Mapa con las instancias de TipoCable
	 * @throws FileNotFoundException
	 */
	public static Map<String, TipoCable> cargarTipoCable(String fileName) throws FileNotFoundException {
		tipoCable = new HashMap<String, TipoCable>();

		Scanner fileScanner = new Scanner(new File(fileName));

		String codigo, descripcion;
		int velocidad;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				codigo = lineScanner.next();
				descripcion = lineScanner.next();
				velocidad = Integer.parseInt(lineScanner.next());
				tipoCable.put(codigo, new TipoCable(codigo, descripcion, velocidad));
			}

			lineScanner.close();
		}

		fileScanner.close();
		return tipoCable;

	}

	/**
	 * Cargar un mapa con todos los tipos de puerto que pueden tener los equipos
	 * 
	 * @param fileName archivo con los datos de los tipos de puerto
	 * @return Map que contiene las instancias de TipoPuerto
	 * @throws FileNotFoundException
	 */
	public static Map<String, TipoPuerto> cargarTipoPuerto(String fileName) throws FileNotFoundException {
		tipoPuerto = new HashMap<String, TipoPuerto>();

		Scanner fileScanner = new Scanner(new File(fileName));

		String codigo, descripcion;
		int velocidad;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				codigo = lineScanner.next();
				descripcion = lineScanner.next();
				velocidad = Integer.parseInt(lineScanner.next());
				tipoPuerto.put(codigo, new TipoPuerto(codigo, descripcion, velocidad));
			}

			lineScanner.close();
		}

		fileScanner.close();
		return tipoPuerto;

	}

	/**
	 * Cargar un mapa con las ubicaciones dentro de la red
	 * 
	 * @param fileName archivo que contiene los datos de las ubicaciones
	 * @return Map que contiene las instancias de Ubicacion
	 * @throws FileNotFoundException
	 */
	public static Map<String, Ubicacion> cargarUbicaciones(String fileName) throws FileNotFoundException {
		ubicaciones = new HashMap<String, Ubicacion>();

		Scanner fileScanner = new Scanner(new File(fileName));

		String codigo, descripcion;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				codigo = lineScanner.next();
				descripcion = lineScanner.next();
				ubicaciones.put(codigo, new Ubicacion(codigo, descripcion));
			}

			lineScanner.close();
		}

		fileScanner.close();
		return ubicaciones;

	}

	/**
	 * Cargar las conexiones entre equipos dentro de la red
	 * 
	 * @param fileName archivo que contiene los datos de las conexiones
	 * @return Lista con todas las conexiones de la red
	 * @throws FileNotFoundException
	 */
	public static List<Conexion> cargarConexiones(String fileName) throws FileNotFoundException {
		conexiones = new ArrayList<Conexion>();

		Scanner fileScanner = new Scanner(new File(fileName));
		Equipo equipo1, equipo2;
		TipoCable tipoCableConexion;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(";");

			if (lineScanner.hasNext()) {
				equipo1 = equipos.get(lineScanner.next());
				equipo2 = equipos.get(lineScanner.next());
				tipoCableConexion = tipoCable.get(lineScanner.next());
				conexiones.add(new Conexion(equipo1, equipo2, tipoCableConexion));
			}

			lineScanner.close();
		}

		fileScanner.close();
		return conexiones;

	}

}
