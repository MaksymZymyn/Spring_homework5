package homework5.mapper.customer;

import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.customer.CustomerDtoResponse;
import homework5.service.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerDtoMapperResponseTest {

    @Autowired
    private CustomerDtoMapperResponse customerDtoMapperResponse;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertToDto() {
        Set<Account> accounts = new HashSet<>();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Jane Doe");
        customer.setEmail("jane.doe@example.com");
        customer.setAge(28);
        customer.setPhoneNumber("+9876543210");

        Account account1 = new Account(Currency.USD, customer);
        account1.setBalance(1000.0);
        account1.setNumber(UUID.randomUUID());

        Account account2 = new Account(Currency.EUR, customer);
        account1.setBalance(2000.0);
        account1.setNumber(UUID.randomUUID());

        accounts.add(account1);
        accounts.add(account2);

        Set<Employer> employers = new HashSet<>();
        employers.add(new Employer("Company A", "Address A"));
        employers.add(new Employer("Company B", "Address B"));

        customer.setAccounts(accounts);
        customer.setEmployers(employers);
        customer.setCreationDate(LocalDateTime.of(2023, 1, 1, 12, 0));
        customer.setLastModifiedDate(LocalDateTime.of(2024, 6, 1, 12, 0));

        CustomerDtoResponse dtoResponse = customerDtoMapperResponse.convertToDto(customer);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(1L);
        assertThat(dtoResponse.getName()).isEqualTo("Jane Doe");
        assertThat(dtoResponse.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(dtoResponse.getAge()).isEqualTo(28);
        assertThat(dtoResponse.getPhoneNumber()).isEqualTo("+9876543210");
        assertThat(dtoResponse.getAccountNumbers()).containsExactlyInAnyOrder(
                accounts.stream().map(Account::getNumber).toArray(UUID[]::new)
        );
        assertThat(dtoResponse.getEmployerNames()).containsExactlyInAnyOrder(
                employers.stream().map(Employer::getName).toArray(String[]::new)
        );
        assertThat(dtoResponse.getCreationDate()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(dtoResponse.getLastModifiedDate()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 0));
    }
}
