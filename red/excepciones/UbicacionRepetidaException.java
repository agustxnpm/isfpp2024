package red.excepciones;

/**
 * Excepción personalizada para manejar situaciones donde se intenta agregar
 * una ubicación que ya está registrada en el sistema.
 */
public class UbicacionRepetidaException extends RuntimeException {

    /**
     * Constructor sin argumentos que invoca el constructor de la superclase.
     */
    public UbicacionRepetidaException() {
        super();
    }

    /**
     * Constructor que permite personalizar el mensaje, la causa y el control
     * sobre la supresión y la escritura del stack trace.
     * 
     * @param message El mensaje de la excepción.
     * @param cause La causa de la excepción.
     * @param enableSuppression Habilitar o deshabilitar la supresión.
     * @param writableStackTrace Permitir o no la escritura del stack trace.
     */
    public UbicacionRepetidaException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructor que permite especificar un mensaje y una causa.
     * 
     * @param message El mensaje de la excepción.
     * @param cause La causa de la excepción.
     */
    public UbicacionRepetidaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor que permite especificar solo un mensaje.
     * 
     * @param message El mensaje de la excepción.
     */
    public UbicacionRepetidaException(String message) {
        super(message);
    }

    /**
     * Constructor que permite especificar solo una causa.
     * 
     * @param cause La causa de la excepción.
     */
    public UbicacionRepetidaException(Throwable cause) {
        super(cause);
    }
}
