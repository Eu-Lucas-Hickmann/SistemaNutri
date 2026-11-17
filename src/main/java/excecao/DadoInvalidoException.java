package excecao;

public class DadoInvalidoException extends RuntimeException {

    public DadoInvalidoException(String message) {
        super(message);
    }

    public DadoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
