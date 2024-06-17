package homework5.mapper.account;

import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.bank.Customer;
import homework5.domain.dto.account.AccountDtoResponse;
import homework5.service.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountDtoMapperResponseTest {

    @Autowired
    private AccountDtoMapperResponse accountDtoMapperResponse;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertToDto() {
        Customer customer = new Customer("Ava Gonzalez", "ava@example.com", 38, "789654321", "password12");
        Account account = new Account(Currency.USD, customer);
        account.setId(1L);
        account.setBalance(1000.0);
        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime lastModifiedDate = LocalDateTime.now();
        account.setCreationDate(creationDate);
        account.setLastModifiedDate(lastModifiedDate);

        AccountDtoResponse dto = accountDtoMapperResponse.convertToDto(account);

        assertNotNull(dto);
        assertEquals(account.getId(), dto.getId());
        assertEquals(account.getNumber(), dto.getNumber());
        assertEquals(account.getCurrency(), dto.getCurrency());
        assertEquals(account.getBalance(), dto.getBalance());
        assertEquals(account.getCustomer().getName(), dto.getCustomerName());
        assertEquals(account.getCreationDate(), dto.getCreationDate());
        assertEquals(account.getLastModifiedDate(), dto.getLastModifiedDate());
    }
}
