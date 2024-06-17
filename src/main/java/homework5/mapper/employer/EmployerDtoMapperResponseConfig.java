package homework5.mapper.employer;

import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.employer.EmployerDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class EmployerDtoMapperResponseConfig {

    @Bean
    public ModelMapper employerDtoResponseMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(Employer.class, EmployerDtoResponse.class)
                .addMapping(Employer::getId, EmployerDtoResponse::setId)
                .addMapping(Employer::getName, EmployerDtoResponse::setName)
                .addMapping(Employer::getAddress, EmployerDtoResponse::setAddress)
                .addMapping(src -> mapCustomerNames(src), EmployerDtoResponse::setCustomersNames)
                .addMapping(Employer::getCreationDate, EmployerDtoResponse::setCreationDate)
                .addMapping(Employer::getLastModifiedDate, EmployerDtoResponse::setLastModifiedDate)
                .addMapping(src -> src.getCreatedBy() != null ? src.getCreatedBy().getUserName() : null, EmployerDtoResponse::setCreationByUserName)
                .addMapping(src -> src.getLastModifiedBy() != null ? src.getLastModifiedBy().getUserName() : null, EmployerDtoResponse::setLastModifiedByUserName);

        return mapper;
    }

    private List<String> mapCustomerNames(Employer employer) {
        return employer.getCustomers() != null
                ? employer.getCustomers().stream().map(Customer::getName).toList()
                : Collections.emptyList();
    }
}
