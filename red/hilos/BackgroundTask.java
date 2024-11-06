package red.hilos;

import javax.swing.*;

public abstract class BackgroundTask<T> extends SwingWorker<T, Void> {
    
    private JFrame parentFrame;

    /**
     * Constructor para crear una tarea en segundo plano.
     * 
     * @param parentFrame La ventana principal o componente padre donde se mostrará el mensaje de error si ocurre alguna excepción.
     */
    public BackgroundTask(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    /**
     * Método llamado al finalizar la ejecución de la tarea en segundo plano.
     * Se encarga de manejar el resultado o cualquier excepción que haya ocurrido.
     */
    @Override
    protected void done() {
        try {
            // Obtener el resultado de la tarea
            T result = get();
            // Llamar al método onSuccess si la tarea fue exitosa
            onSuccess(result);
        } catch (Exception e) {
            // Manejar cualquier error que haya ocurrido en la tarea
            onFailure(e);
        }
    }

    /**
     * Método que debe ser implementado para manejar el éxito de la tarea.
     * 
     * @param result El resultado de la tarea en segundo plano.
     */
    protected abstract void onSuccess(T result);

    /**
     * Método que maneja las excepciones en caso de fallo en la tarea.
     * 
     * @param e La excepción que ocurrió.
     */
    protected void onFailure(Exception e) {
        JOptionPane.showMessageDialog(parentFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
