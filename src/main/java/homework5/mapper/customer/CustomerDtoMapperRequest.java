package homework5.mapper.customer;

import homework5.domain.bank.Customer;
import homework5.domain.dto.customer.CustomerDtoRequest;
import homework5.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class CustomerDtoMapperRequest extends DtoMapperFacade<Customer, CustomerDtoRequest> {

    public CustomerDtoMapperRequest() {
        super(Customer.class, CustomerDtoRequest.class);
    }
}
