package red.excepciones;

public class TipoPuertoRepetidoException extends RuntimeException{
	
	public TipoPuertoRepetidoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public TipoPuertoRepetidoException() {
		super();
	}
	
	public TipoPuertoRepetidoException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public TipoPuertoRepetidoException(String msg) {
		super(msg);
	}
	
	public TipoPuertoRepetidoException(Throwable cause) {
		super(cause);
	}

}
