package homework5.domain.dto.employer;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDtoRequest {
    @NotBlank(message = "Employer name must not be blank")
    @Size(min = 3, max = 100, message = "Employer name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Address must not be blank")
    @Size(min = 3, max = 100, message = "Address must be between 3 and 100 characters")
    private String address;
}
