package homework5.mapper.customer;

import homework5.domain.bank.Customer;
import homework5.domain.dto.customer.CustomerDtoRequest;
import homework5.service.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerDtoMapperRequestTest {

    @Autowired
    private CustomerDtoMapperRequest customerDtoMapperRequest;

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
        // Setup test data
        CustomerDtoRequest dto = new CustomerDtoRequest(
                "John Doe",
                "john.doe@example.com",
                30,
                "+1234567890",
                "SecurePassword123!"
        );

        Customer expectedCustomer = new Customer();
        expectedCustomer.setName("John Doe");
        expectedCustomer.setEmail("john.doe@example.com");
        expectedCustomer.setAge(30);
        expectedCustomer.setPhoneNumber("+1234567890");
        expectedCustomer.setPassword("SecurePassword123!");

        when(modelMapper.map(dto, Customer.class)).thenReturn(expectedCustomer);

        Customer actualCustomer = customerDtoMapperRequest.convertToEntity(dto);

        assertThat(actualCustomer).isEqualTo(expectedCustomer);
        assertThat(actualCustomer.getName()).isEqualTo("John Doe");
        assertThat(actualCustomer.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(actualCustomer.getAge()).isEqualTo(30);
        assertThat(actualCustomer.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(actualCustomer.getPassword()).isEqualTo("SecurePassword123!");
    }
}
