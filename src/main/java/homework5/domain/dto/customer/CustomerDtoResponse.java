package homework5.domain.dto.customer;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDtoResponse {

    @JsonView({Views.Summary.class, Views.Detail.class})
    private Long id;

    @JsonView({Views.Summary.class, Views.Detail.class})
    private String name;

    @JsonView(Views.Detail.class)
    private String email;

    @JsonView(Views.Detail.class)
    private Integer age;

    @JsonView({Views.Summary.class, Views.Detail.class})
    private Set<UUID> accountNumbers;

    @JsonView(Views.Detail.class)
    private List<String> employerNames;

    @JsonView(Views.Detail.class)
    private String phoneNumber;

    @JsonView(Views.Detail.class)
    private String creationByUserName;

    @JsonView(Views.Detail.class)
    private LocalDateTime creationDate;

    @JsonView(Views.Detail.class)
    private String lastModifiedByUserName;

    @JsonView(Views.Detail.class)
    private LocalDateTime lastModifiedDate;
}
