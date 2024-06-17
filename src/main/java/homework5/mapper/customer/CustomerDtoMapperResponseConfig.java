package homework5.mapper.customer;

import homework5.domain.bank.Account;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.customer.CustomerDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class CustomerDtoMapperResponseConfig {

    @Bean
    public ModelMapper customerDtoResponseMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(Customer.class, CustomerDtoResponse.class)
                .addMapping(Customer::getId, CustomerDtoResponse::setId)
                .addMapping(Customer::getName, CustomerDtoResponse::setName)
                .addMapping(Customer::getEmail, CustomerDtoResponse::setEmail)
                .addMapping(Customer::getAge, CustomerDtoResponse::setAge)
                .addMapping(Customer::getPhoneNumber, CustomerDtoResponse::setPhoneNumber)
                .addMapping(src -> mapAccountNumbers(src.getAccounts()), CustomerDtoResponse::setAccountNumbers)
                .addMapping(src -> mapEmployerNames(src.getEmployers()), CustomerDtoResponse::setEmployerNames)
                .addMapping(Customer::getCreationDate, CustomerDtoResponse::setCreationDate)
                .addMapping(Customer::getLastModifiedDate, CustomerDtoResponse::setLastModifiedDate)
                .addMapping(src -> src.getCreatedBy() != null ? src.getCreatedBy().getUserName() : null, CustomerDtoResponse::setCreationByUserName)
                .addMapping(src -> src.getLastModifiedBy() != null ? src.getLastModifiedBy().getUserName() : null, CustomerDtoResponse::setLastModifiedByUserName);

        return mapper;
    }

    private Set<UUID> mapAccountNumbers(Set<Account> accounts) {
        if (accounts == null) return Collections.emptySet();
        return accounts.stream()
                .map(Account::getNumber)
                .collect(Collectors.toSet());
    }

    private List<String> mapEmployerNames(Set<Employer> employers) {
        if (employers == null) return Collections.emptyList();
        return employers.stream()
                .map(Employer::getName)
                .toList();
    }
}
