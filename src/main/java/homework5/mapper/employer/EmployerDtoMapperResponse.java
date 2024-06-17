package homework5.mapper.employer;

import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.employer.EmployerDtoResponse;
import homework5.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerDtoMapperResponse extends DtoMapperFacade<Employer, EmployerDtoResponse> {

    public EmployerDtoMapperResponse() {
        super(Employer.class, EmployerDtoResponse.class);
    }

    @Override
    protected void decorateDto(EmployerDtoResponse dto, Employer entity) {
        List<String> customerNames = entity.getCustomers().stream()
                .map(Customer::getName)
                .toList();
        dto.setCustomersNames(customerNames);
    }
}