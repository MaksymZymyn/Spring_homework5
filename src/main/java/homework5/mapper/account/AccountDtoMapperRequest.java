package homework5.mapper.account;

import homework5.domain.bank.*;
import homework5.domain.dto.account.AccountDtoRequest;
import homework5.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class AccountDtoMapperRequest extends DtoMapperFacade<Account, AccountDtoRequest> {
    public AccountDtoMapperRequest() {
        super(Account.class, AccountDtoRequest.class);
    }
}
