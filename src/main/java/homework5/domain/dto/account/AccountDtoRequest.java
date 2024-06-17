package homework5.domain.dto.account;

import homework5.domain.bank.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoRequest {

    @NotNull(message = "Currency must be provided")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull(message = "Balance must be provided, it can be negative")
    @Digits(integer = 12, message = "Balance must be a valid monetary amount with up to 12 integer digits", fraction = 0)
    private double balance;
}
