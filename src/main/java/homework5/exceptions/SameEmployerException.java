package homework5.exceptions;

public class SameEmployerException extends RuntimeException {
    public SameEmployerException(String message) {
        super(message);
    }
}
