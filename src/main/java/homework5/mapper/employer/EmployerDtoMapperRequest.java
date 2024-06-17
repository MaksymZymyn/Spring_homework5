package homework5.mapper.employer;

import homework5.domain.bank.Employer;
import homework5.domain.dto.employer.EmployerDtoRequest;
import homework5.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class EmployerDtoMapperRequest extends DtoMapperFacade<Employer, EmployerDtoRequest> {

    public EmployerDtoMapperRequest() {
        super(Employer.class, EmployerDtoRequest.class);
    }
}
