package homework5.mapper.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.dto.account.AccountDtoRequest;
import homework5.service.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AccountDtoMapperRequestTest {

    @Autowired
    private AccountDtoMapperRequest accountDtoMapperRequest;

    @Mock
    private ModelMapper modelMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertToEntity() {
        AccountDtoRequest accountDtoRequest = new AccountDtoRequest();
        accountDtoRequest.setCurrency(Currency.USD);
        accountDtoRequest.setBalance(1000.0);

        when(modelMapper.map(accountDtoRequest, Account.class)).thenReturn(new Account(Currency.USD, null));

        Account account = accountDtoMapperRequest.convertToEntity(accountDtoRequest);

        assertThat(account).isNotNull();
        assertThat(account.getCurrency()).isEqualTo(Currency.USD);
        assertThat(account.getBalance()).isEqualTo(1000.0);
    }
}
