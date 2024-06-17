package homework5.domain.bank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Перевірка довжини пароля
        if (password.length() < 8) {
            return false;
        }

        // Перевірка наявності принаймні однієї великої літери
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        boolean containsUppercase = uppercaseMatcher.find();

        // Перевірка наявності принаймні однієї малої літери
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
        boolean containsLowercase = lowercaseMatcher.find();

        return containsUppercase && containsLowercase;
    }
}
