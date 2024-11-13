package red.modelo;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import red.controlador.Configuracion;
import red.excepciones.DireccionIpRepetidaException;

/**
 * Clase que representa un equipo de red con atributos como modelo, marca,
 * descripción, direcciones IP, ubicación, tipo de equipo, y puertos.
 */
public class Equipo {

    private String codigo; // Identificador único del equipo.
    private String modelo; // Modelo del equipo.
    private String marca; // Marca del equipo.
    private String descripcion; // Descripción del equipo.
    private List<String> direccionesIp; // Lista de direcciones IP asignadas al equipo.
    private Ubicacion ubicacion; // Ubicación física del equipo.
    private TipoEquipo tipoEquipo; // Tipo de equipo (por ejemplo, router, switch).
    private List<Puerto> puertos; // Lista de puertos disponibles en el equipo.
    private boolean estado; // Estado del equipo (Activo/Inactivo).

    /**
     * Constructor para crear una nueva instancia de Equipo.
     * 
     * @param codigo El código identificador del equipo.
     * @param modelo El modelo del equipo.
     * @param marca La marca del equipo.
     * @param descripcion Descripción del equipo.
     * @param ubicacion Ubicación del equipo.
     * @param tipoEquipo Tipo de equipo.
     * @param cantPuertos Número de puertos.
     * @param tipoPuerto Tipo de puerto.
     * @param estado Estado inicial del equipo.
     */
    public Equipo(String codigo, String modelo, String marca, String descripcion, Ubicacion ubicacion,
                  TipoEquipo tipoEquipo, int cantPuertos, TipoPuerto tipoPuerto, boolean estado) {
        super();
        this.codigo = codigo;
        this.modelo = (modelo == null || modelo.isEmpty()) ? Configuracion.getConfiguracion().getRb().getString("Equipo_modelo_desconocido") : modelo;
        this.marca = (marca == null || marca.isEmpty()) ? Configuracion.getConfiguracion().getRb().getString("Equipo_marca_desconocida") : marca;
        this.descripcion = (descripcion == null || descripcion.isEmpty())
        		? Configuracion.getConfiguracion().getRb().getString("Equipo_sin_descripcion") : descripcion;
        this.ubicacion = ubicacion;
        this.tipoEquipo = tipoEquipo;
        direccionesIp = new ArrayList<>();
        puertos = new ArrayList<>();
        agregarPuerto(cantPuertos, tipoPuerto); // Inicializar los puertos.
        this.estado = estado;
    }

    /**
     * Obtiene la velocidad máxima del equipo basada en los puertos disponibles.
     * La velocidad máxima está limitada por el puerto más lento.
     * 
     * @return La velocidad máxima del equipo en Mbps.
     */
    public int getVelocidadMaxima() {
        int velocidadMaxima = Integer.MAX_VALUE; // Inicializamos con un valor muy alto.
        // Iterar sobre los puertos del equipo para encontrar el puerto más lento.
        for (Puerto puerto : puertos)
            velocidadMaxima = Math.min(velocidadMaxima, puerto.getTipoPuerto().getVelocidad());
        return velocidadMaxima; // Retornar la velocidad más baja.
    }

    /**
     * Simula la respuesta del equipo a un ping. Un equipo responde al ping si está
     * en estado ACTIVO.
     * 
     * @return true si el equipo está ACTIVO, false si está INACTIVO.
     */
    public boolean realizarPing() {
        return estado; // El ping es exitoso si el equipo está ACTIVO.
    }

    // Métodos de acceso (getters y setters).
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public TipoEquipo getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(TipoEquipo tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public List<String> getDireccionesIp() {
        return direccionesIp;
    }

	/**
     * Agrega una dirección IP al equipo, verificando que sea válida y que no esté repetida.
     * @param ip La dirección IP a agregar.
     * @throws IllegalArgumentException si la IP no tiene el formato válido
     * @throws DireccionIpRepetidaException Si la IP ya está asignada al equipo.
     */
    public void agregarIp(String ip) throws DireccionIpRepetidaException {
        // Expresión regular para validar IPv4.
        String ipv4Regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(ipv4Regex);
        Matcher matcher = pattern.matcher(ip);

        // Validar si la IP tiene formato válido.
        if (!matcher.matches())
            throw new IllegalArgumentException(Configuracion.getConfiguracion().getRb().getString("Equipo_direccion_ip_no_valida"));

        // Verificar si la IP ya existe en el equipo.
        if (direccionesIp.contains(ip))
            throw new DireccionIpRepetidaException(Configuracion.getConfiguracion().getRb().getString("Equipo_direccion_ip_ya_existe"));

        direccionesIp.add(ip);
    }

    /**
     * Agrega un tipo de puerto al equipo.
     * Si ya existe, actualiza la cantidad de puertos.
     * 
     * @param cantPuertos Cantidad de puertos.
     * @param tipoPuerto Tipo de puerto.
     * @throws IllegalArgumentException si la cantidad de puertos no es positiva o el tipo de puerto es nulo
     */
    public void agregarPuerto(int cantPuertos, TipoPuerto tipoPuerto) throws IllegalArgumentException {
        if (cantPuertos <= 0)
            throw new IllegalArgumentException(Configuracion.getConfiguracion().getRb().getString("Equipo_debe_tener_al_menos_un_puerto"));
        if (tipoPuerto == null)
        	throw new IllegalArgumentException(
        			String.format(Configuracion.getConfiguracion().getRb().getString("Equipo_puerto_ingresado_no_puede_ser_nulo."), codigo));

        Puerto puerto = new Puerto(cantPuertos, tipoPuerto);
        if (puertos.contains(puerto)) {
        	// Incrementa a la cantidad de puertos con el tipo de puerto en cuestión, la cantidad ingresada al método, y luego sale del método
            int index = puertos.indexOf(puerto);
            Puerto puertoExistente = puertos.get(index);
            puertoExistente.setCantidadPuertos(puertoExistente.getCantidadPuertos() + puerto.getCantidadPuertos());
            puertos.set(index, puertoExistente);
            return;
        }

        puertos.add(puerto); // Siendo que el equipo no contaba con el tipo de puerto ingresado, éste se agrega
    }

    /**
     * Devuelve la información de los puertos del equipo.
     * 
     * @return Una cadena con la información de los puertos.
     */
    public String getPuertosInfo() {
        StringBuilder puertosInfo = new StringBuilder();
        for (Puerto p : puertos)
        	puertosInfo.append(p.getTipoPuerto().getCodigo())   // Código del tipo de puerto
                            .append(",")
                            .append(p.getCantidadPuertos())               // Cantidad de puertos
                            .append(";");
    
        // Eliminar el último punto y coma para que no haya un separador extra
        if (puertosInfo.length() > 0)
            puertosInfo.setLength(puertosInfo.length() - 1);
    
        return puertosInfo.toString();
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Equipo other = (Equipo) obj;
        return Objects.equals(codigo, other.codigo);
    }

    @Override
    public String toString() {
        return String.format(Configuracion.getConfiguracion().getRb().getString("Equipo_to_string"), codigo, descripcion);
    }

    // Clase interna que representa un puerto.
    private class Puerto {
        private int cantidadPuertos;
        private TipoPuerto tipoPuerto;

        public Puerto(int cantidadPuertos, TipoPuerto tipoPuerto) {
        	super();
        	this.cantidadPuertos = cantidadPuertos;
            this.tipoPuerto = tipoPuerto;
        }

        public int getCantidadPuertos() {
            return cantidadPuertos;
        }

        public void setCantidadPuertos(int cantidadPuertos) {
            this.cantidadPuertos = cantidadPuertos;
        }

        public TipoPuerto getTipoPuerto() {
            return tipoPuerto;
        }

        public void setTipoPuerto(TipoPuerto tipoPuerto) {
            this.tipoPuerto = tipoPuerto;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + Objects.hash(tipoPuerto);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Puerto other = (Puerto) obj;
            return Objects.equals(tipoPuerto, other.tipoPuerto);
        }

        private Equipo getEnclosingInstance() {
            return Equipo.this;
        }
    }
}
