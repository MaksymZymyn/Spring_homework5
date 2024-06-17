package homework5.mapper.employer;

import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.employer.EmployerDtoResponse;
import homework5.service.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployerDtoMapperResponseTest {

    @Autowired
    private EmployerDtoMapperResponse employerDtoMapperResponse;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertToDto() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");

        Employer employer = new Employer();
        employer.setId(1L);
        employer.setName("Company XYZ");
        employer.setAddress("123 Main St");
        employer.setCustomers(Set.of(customer1, customer2));
        employer.setCreationDate(LocalDateTime.of(2023, 6, 10, 10, 0));
        employer.setLastModifiedDate(LocalDateTime.of(2023, 6, 11, 12, 0));

        EmployerDtoResponse expectedDto = new EmployerDtoResponse();
        expectedDto.setId(1L);
        expectedDto.setName("Company XYZ");
        expectedDto.setAddress("123 Main St");
        expectedDto.setCustomersNames(List.of("John Doe", "Jane Smith"));
        expectedDto.setCreationDate(LocalDateTime.of(2023, 6, 10, 10, 0));
        expectedDto.setLastModifiedDate(LocalDateTime.of(2023, 6, 11, 12, 0));
        EmployerDtoResponse dto = employerDtoMapperResponse.convertToDto(employer);

        assertNotNull(dto);
        assertEquals(expectedDto.getId(), dto.getId());
        assertEquals(expectedDto.getName(), dto.getName());
        assertEquals(expectedDto.getAddress(), dto.getAddress());
        assertEquals(expectedDto.getCustomersNames(), dto.getCustomersNames());
        assertEquals(expectedDto.getCreationByUserName(), dto.getCreationByUserName());
        assertEquals(expectedDto.getCreationDate(), dto.getCreationDate());
        assertEquals(expectedDto.getLastModifiedByUserName(), dto.getLastModifiedByUserName());
        assertEquals(expectedDto.getLastModifiedDate(), dto.getLastModifiedDate());
    }
}
