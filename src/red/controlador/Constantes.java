package red.controlador;
/* Clase que contiene valores constantes numéricos o Strings, de clase y finales
 * Estas constantes son invocadas por las otras clases del programa para su uso
 * Ayuda a facilitar la utilización y escalabilidad del programa, evadiendo el
 * hardcodeo de números y Strings
*/
public class Constantes {
	/* Constante para que la clase red.controlador.AplicaciónConsultas escriba en
	 * la consola las opciones que puede realizar el usuario
	*/
	public static final String OPCIONES = "Seleccione una opción:\n"
			+ "1. Listar Equipos\n"
			+ "2. Listar Conexiones\n"
			+ "3. Listar Ubicaciones\n"
			+ "4. Agregar Nuevo Equipo\n"
			+ "5. Buscar Equipo por Código\n"
			+ "6. Ruta entre dos equipos\n"
			+ "7. Realizar ping a un rango de IP\n"
			+ "8. Mostrar mapa de estado de la red\n"
			+ "9. Verificar conectividad\n"
			+ "10. Salir";
	
	/**
	 * Constante para que la GUI escriba en un diálogo a los miembros del equipo de desarrollo,
	 * los docentes de la materia y los detalles del trabajo
	 */
	public static final String CREDITOS = "Instancia Supervisada de Formación Práctica Profesional\n\n"
			+ "Materia: Programación Orientada a Objetos\n"
			+ "Carreras: Analista Programador Universitario, Licenciatura en Informática\n"
			+ "Universidad Nacional de la Patagonia San Juan Bosco, sede Puerto Madryn\n"
			+ "Miembros del grupo:\n"
			+ " - Gabriel Sorrentino\n"
			+ " - Agustín Palma\n"
			+ " - Agustín Lohse\n\n"
			+ "Profesores:\n"
			+ " - Renato Mazzanti\n"
			+ " - Gustavo Samec\n"
			+ " - Débora Pollicelli\n"
			+ " - Alejandro Solá Leiva (auxiliar alumno)\n\n"
			+ "Fecha de entrega: 25 de octubre de 2024";
}
