package homework5.mapper.employer;

import homework5.domain.bank.Employer;
import homework5.domain.dto.employer.EmployerDtoRequest;
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
public class EmployerDtoMapperRequestTest {

    @Autowired
    private EmployerDtoMapperRequest employerDtoMapperRequest;

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
        EmployerDtoRequest dtoRequest = new EmployerDtoRequest("Company XYZ", "123 Main St");

        Employer expectedEntity = new Employer();
        expectedEntity.setName("Company XYZ");
        expectedEntity.setAddress("123 Main St");

        when(modelMapper.map(dtoRequest, Employer.class)).thenReturn(expectedEntity);

        Employer entity = employerDtoMapperRequest.convertToEntity(dtoRequest);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Company XYZ");
        assertThat(entity.getAddress()).isEqualTo("123 Main St");
    }
}
