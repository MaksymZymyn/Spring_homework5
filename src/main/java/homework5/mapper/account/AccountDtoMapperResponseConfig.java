package homework5.mapper.account;

import homework5.domain.SysUser;
import homework5.domain.bank.Account;
import homework5.domain.dto.account.AccountDtoResponse;
import homework5.security.JwtAuthentication;
import homework5.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
@RequiredArgsConstructor
public class AccountDtoMapperResponseConfig {

    private final AuthService authService;

    public SysUser getUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return authInfo.getPrincipal();
    }

    @Bean
    public ModelMapper accountDtoResponseMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(Account.class, AccountDtoResponse.class);

        mapper.typeMap(Account.class, AccountDtoResponse.class)
                .addMapping(Account::getId, AccountDtoResponse::setId)
                .addMapping(Account::getNumber, AccountDtoResponse::setNumber)
                .addMapping(Account::getCurrency, AccountDtoResponse::setCurrency)
                .addMapping(Account::getBalance, AccountDtoResponse::setBalance)
                .addMapping(src -> src.getCustomer().getName(), AccountDtoResponse::setCustomerName)
                .addMapping(Account::getCreationDate, AccountDtoResponse::setCreationDate)
                .addMapping(Account::getLastModifiedDate, AccountDtoResponse::setLastModifiedDate)
                .addMapping(src -> src.getCreatedBy() != null ? src.getCreatedBy().getUserName() : null, AccountDtoResponse::setCreationByUserName)
                .addMapping(src -> src.getLastModifiedBy() != null ? src.getLastModifiedBy().getUserName() : null, AccountDtoResponse::setLastModifiedByUserName);
        return mapper;
    }
}
