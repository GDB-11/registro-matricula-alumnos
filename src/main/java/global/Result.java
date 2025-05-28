package global;

/**
 * Clase que implementa el patrón de resultados para manejar operaciones que pueden
 * fallar sin usar excepciones. Permite encapsular tanto el resultado exitoso
 * como el error en un solo objeto.
 *
 * @param <T> El tipo del valor en caso de éxito
 */
public class Result<T> {
    private final T value;
    private final String error;
    private final Exception exception;
    private final boolean success;

    private Result(T value) {
        this.value = value;
        this.error = null;
        this.exception = null;
        this.success = true;
    }

    private Result(String error) {
        this.value = null;
        this.error = error;
        this.exception = null;
        this.success = false;
    }

    private Result(String error, Exception exception) {
        this.value = null;
        this.error = error;
        this.exception = exception;
        this.success = false;
    }

    /**
     * Crea un Result exitoso con el valor proporcionado
     * @param <T> El tipo del valor
     * @param value El valor del resultado
     * @return Un Result exitoso
     */
    public static <T> Result<T> success(T value) {
        return new Result<>(value);
    }

    /**
     * Crea un Result exitoso sin valor (útil para operaciones que no devuelven nada)
     * @return Un Result exitoso sin valor
     */
    public static Result<Void> success() {
        return new Result<>((Void) null);
    }

    /**
     * Crea un Result con error usando solo un mensaje
     * @param <T> El tipo del valor que se esperaba
     * @param error El mensaje de error
     * @return Un Result con error
     */
    public static <T> Result<T> error(String error) {
        return new Result<>(error);
    }

    /**
     * Crea un Result con error incluyendo una excepción
     * @param <T> El tipo del valor que se esperaba
     * @param error El mensaje de error
     * @param exception La excepción que causó el error
     * @return Un Result con error
     */
    public static <T> Result<T> error(String error, Exception exception) {
        return new Result<>(error, exception);
    }

    /**
     * Verifica si el resultado es exitoso
     * @return true si el resultado es exitoso, false en caso contrario
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Verifica si el resultado contiene un error
     * @return true si el resultado contiene un error, false en caso contrario
     */
    public boolean isError() {
        return !success;
    }

    /**
     * Obtiene el valor del resultado exitoso
     * @return El valor si el resultado es exitoso
     * @throws IllegalStateException si el resultado contiene un error
     */
    public T getValue() {
        if (!success) {
            throw new IllegalStateException("No se puede obtener el valor de un resultado con error");
        }
        return value;
    }

    /**
     * Obtiene el mensaje de error
     * @return El mensaje de error si el resultado contiene un error
     * @throws IllegalStateException si el resultado es exitoso
     */
    public String getError() {
        if (success) {
            throw new IllegalStateException("No se puede obtener el error de un resultado exitoso");
        }
        return error;
    }

    /**
     * Obtiene la excepción asociada al error (si existe)
     * @return La excepción si existe, null en caso contrario
     * @throws IllegalStateException si el resultado es exitoso
     */
    public Exception getException() {
        if (success) {
            throw new IllegalStateException("No se puede obtener la excepción de un resultado exitoso");
        }
        return exception;
    }

    /**
     * Verifica si el resultado tiene una excepción asociada
     * @return true si hay una excepción, false en caso contrario
     */
    public boolean hasException() {
        return !success && exception != null;
    }

    /**
     * Obtiene el valor si es exitoso, o devuelve el valor por defecto si hay error
     * @param defaultValue El valor por defecto a devolver en caso de error
     * @return El valor del resultado o el valor por defecto
     */
    public T orElse(T defaultValue) {
        return success ? value : defaultValue;
    }
}