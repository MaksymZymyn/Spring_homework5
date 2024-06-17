package homework5.mapper.account;

import homework5.domain.bank.Account;
import homework5.domain.dto.account.AccountDtoRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.*;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class AccountDtoMapperRequestConfig {

    @Bean
    public ModelMapper accountDtoRequestMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(AccountDtoRequest.class, Account.class)
                .addMapping(AccountDtoRequest::getCurrency, Account::setCurrency)
                .addMapping(AccountDtoRequest::getBalance, Account::setBalance);

        return mapper;
    }
}
