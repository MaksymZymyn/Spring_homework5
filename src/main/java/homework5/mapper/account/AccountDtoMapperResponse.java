package homework5.mapper.account;

import homework5.domain.bank.Account;
import homework5.domain.bank.Customer;
import homework5.domain.dto.account.AccountDtoResponse;
import homework5.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class AccountDtoMapperResponse extends DtoMapperFacade<Account, AccountDtoResponse> {

    public AccountDtoMapperResponse() {
        super(Account.class, AccountDtoResponse.class);
    }

    @Override
    protected void decorateDto(AccountDtoResponse dto, Account entity) {
        Customer customer = entity.getCustomer();
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        dto.setCustomerName(customer.getName());
    }
}
