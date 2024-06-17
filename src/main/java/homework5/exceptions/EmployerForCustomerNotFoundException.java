package homework5.exceptions;

public class EmployerForCustomerNotFoundException extends RuntimeException {
    public EmployerForCustomerNotFoundException(String message) {
        super(message);
    }
}