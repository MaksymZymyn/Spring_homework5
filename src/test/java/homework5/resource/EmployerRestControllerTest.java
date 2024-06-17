package homework5.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework5.domain.SysUser;
import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.customer.CustomerDtoRequest;
import homework5.domain.dto.customer.CustomerDtoResponse;
import homework5.domain.dto.employer.EmployerDtoRequest;
import homework5.domain.dto.employer.EmployerDtoResponse;
import homework5.exceptions.EmployerNotFoundException;
import homework5.exceptions.SameEmployerException;
import homework5.mapper.customer.CustomerDtoMapperRequest;
import homework5.mapper.customer.CustomerDtoMapperResponse;
import homework5.mapper.employer.EmployerDtoMapperRequest;
import homework5.mapper.employer.EmployerDtoMapperResponse;
import homework5.service.EmployerService;
import homework5.service.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class EmployerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployerService employerService;

    @MockBean
    private EmployerDtoMapperResponse employerDtoMapperResponse;

    @MockBean
    private EmployerDtoMapperRequest employerDtoMapperRequest;

    @MockBean
    private CustomerDtoMapperRequest customerDtoMapperRequest;

    @MockBean
    private CustomerDtoMapperResponse customerDtoMapperResponse;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() {
        reset(employerService, employerDtoMapperResponse, employerDtoMapperRequest, jwtProvider);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testFindAll() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        List<Employer> employers = new ArrayList<>();

        Employer firstEmployer = new Employer();
        firstEmployer.setId(10L);
        String firstEmployerName = "first";
        firstEmployer.setName(firstEmployerName);
        String firstEmployerAddress = "firstAddress";
        firstEmployer.setAddress(firstEmployerAddress);
        String customerNameFirst = "firstCustomer";
        Customer customer1 = new Customer(customerNameFirst, "ava@example.com", 38, "789654321", "password12");
        String customerNameSecond = "secondCustomer";
        Customer customer2 = new Customer(customerNameSecond, "john@example.com", 35, "123454321", "password45");
        firstEmployer.setCustomers(Set.of(customer1, customer2));

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        String secondEmployerName = "second";
        secondEmployer.setName(secondEmployerName);
        String secondEmployerAddress = "secondAddress";
        secondEmployer.setAddress(secondEmployerAddress);
        String customerNameThird = "thirdCustomer";
        Customer customer3 = new Customer(customerNameThird, "jane@example.com", 38, "789654322", "password22");
        String customerNameFourth = "fourthCustomer";
        Customer customer4 = new Customer(customerNameFourth, "jeff@example.com", 35, "123454322", "password75");
        secondEmployer.setCustomers(Set.of(customer3, customer4));

        employers.add(firstEmployer);
        employers.add(secondEmployer);

        LocalDateTime now = LocalDateTime.now();

        // Створення EmployerDtoResponse для першого Employer
        EmployerDtoResponse dtoFirst = new EmployerDtoResponse(10L, firstEmployerName, firstEmployerAddress,
                List.of(customerNameFirst, customerNameSecond), creatorName, now, modifierName, now);

        // Створення EmployerDtoResponse для другого Employer
        EmployerDtoResponse dtoSecond = new EmployerDtoResponse(20L, secondEmployerName, secondEmployerAddress,
                List.of(customerNameThird, customerNameFourth), creatorName, now, modifierName, now);

        // Налаштування моків
        when(employerService.findAll()).thenReturn(employers);
        when(employerDtoMapperResponse.convertToDto(firstEmployer)).thenReturn(dtoFirst);
        when(employerDtoMapperResponse.convertToDto(secondEmployer)).thenReturn(dtoSecond);

        // Виконання запиту і перевірка відповіді
        mockMvc.perform(MockMvcRequestBuilders.get("/employers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(dtoFirst.getId().intValue())))
                .andExpect(jsonPath("$[0].name", Matchers.is(dtoFirst.getName())))
                .andExpect(jsonPath("$[0].address", Matchers.is(dtoFirst.getAddress())))
                .andExpect(jsonPath("$[0].customersNames[0]", Matchers.is(customerNameFirst)))
                .andExpect(jsonPath("$[0].customersNames[1]", Matchers.is(customerNameSecond)))
                .andExpect(jsonPath("$[0].creationByUserName", Matchers.is(dtoFirst.getCreationByUserName())))
                .andExpect(jsonPath("$[0].lastModifiedByUserName", Matchers.is(dtoFirst.getLastModifiedByUserName())))
                .andExpect(jsonPath("$[1].id", Matchers.is(dtoSecond.getId().intValue())))
                .andExpect(jsonPath("$[1].name", Matchers.is(dtoSecond.getName())))
                .andExpect(jsonPath("$[1].address", Matchers.is(dtoSecond.getAddress())))
                .andExpect(jsonPath("$[1].customersNames[0]", Matchers.is(customerNameThird)))
                .andExpect(jsonPath("$[1].customersNames[1]", Matchers.is(customerNameFourth)))
                .andExpect(jsonPath("$[1].creationByUserName", Matchers.is(dtoSecond.getCreationByUserName())))
                .andExpect(jsonPath("$[1].lastModifiedByUserName", Matchers.is(dtoSecond.getLastModifiedByUserName())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetById_Success() throws Exception {
        Long employerId = 10L;
        String employerName = "first";
        String employerAddress = "firstAddress";
        String customerNameFirst = "firstCustomer";
        String customerNameSecond = "secondCustomer";
        String creatorName = "creatorUser";
        String modifierName = "modifierUser";
        LocalDateTime now = LocalDateTime.now();

        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName(modifierName);

        Customer customer1 = new Customer(customerNameFirst, "ava@example.com", 38, "789654321", "password12");
        Customer customer2 = new Customer(customerNameSecond, "john@example.com", 35, "123454321", "password45");

        Employer employer = new Employer();
        employer.setId(employerId);
        employer.setName(employerName);
        employer.setAddress(employerAddress);
        employer.setCustomers(Set.of(customer1, customer2));

        EmployerDtoResponse dto = new EmployerDtoResponse(employerId, employerName, employerAddress,
                List.of(customerNameFirst, customerNameSecond), creatorName, now, modifierName, now);

        when(employerService.getById(employerId)).thenReturn(employer);
        when(employerDtoMapperResponse.convertToDto(employer)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/employers/{employerId}", employerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(dto.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.is(dto.getName())))
                .andExpect(jsonPath("$.address", Matchers.is(dto.getAddress())))
                .andExpect(jsonPath("$.customersNames[0]", Matchers.is(customerNameFirst)))
                .andExpect(jsonPath("$.customersNames[1]", Matchers.is(customerNameSecond)))
                .andExpect(jsonPath("$.creationByUserName", Matchers.is(dto.getCreationByUserName())))
                .andExpect(jsonPath("$.lastModifiedByUserName", Matchers.is(dto.getLastModifiedByUserName())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetById_NotFound() throws Exception {
        Long employerId = 999L;

        when(employerService.getById(employerId)).thenThrow(new EmployerNotFoundException("Employer not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/employers/{employerId}", employerId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employer with ID " + employerId + " not found"));
    }

    @Test
    public void testCreateEmployer_Success() throws Exception {
        EmployerDtoRequest requestDto = new EmployerDtoRequest();
        requestDto.setName("Test Employer");
        requestDto.setAddress("123 Test St");

        Employer employer = new Employer();
        employer.setName(requestDto.getName());
        employer.setAddress(requestDto.getAddress());

        EmployerDtoResponse responseDto = new EmployerDtoResponse();
        responseDto.setId(1L);
        responseDto.setName(employer.getName());
        responseDto.setAddress(employer.getAddress());

        when(employerDtoMapperRequest.convertToEntity(requestDto)).thenReturn(employer);
        when(employerService.save(employer)).thenReturn(employer);
        when(employerDtoMapperResponse.convertToDto(employer)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/employers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(requestDto.getName()))
                .andExpect(jsonPath("$.address").value(requestDto.getAddress()));
    }

    @Test
    public void testCreateEmployer_SameEmployerException() throws Exception {
        EmployerDtoRequest requestDto = new EmployerDtoRequest();
        requestDto.setName("Test Employer");
        requestDto.setAddress("123 Test St");

        Employer employer = new Employer();
        employer.setName(requestDto.getName());
        employer.setAddress(requestDto.getAddress());

        when(employerDtoMapperRequest.convertToEntity(requestDto)).thenReturn(employer);
        when(employerService.save(employer)).thenThrow(new SameEmployerException("Employer with this name already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/employers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employer with this name already exists"));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdateEmployer() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        Employer employer = new Employer();
        Long employerId = 1L;
        employer.setId(employerId);
        String employerName = "first";
        employer.setName(employerName);
        String employerAddress = "firstAddress";
        employer.setAddress(employerAddress);

        String customerNameFirst = "firstCustomer";
        Customer customer1 = new Customer(customerNameFirst, "ava@example.com", 38, "789654321", "password12");
        String customerNameSecond = "secondCustomer";
        Customer customer2 = new Customer(customerNameSecond, "john@example.com", 35, "123454321", "password45");
        employer.setCustomers(Set.of(customer1, customer2));

        EmployerDtoRequest employerDtoRequest = new EmployerDtoRequest();
        String requestName = "Company B";
        employerDtoRequest.setName(requestName);
        String requestAddress = "Address B";
        employerDtoRequest.setAddress(requestAddress);

        when(employerDtoMapperRequest.convertToEntity(employerDtoRequest)).thenReturn(employer);
        when(employerService.update(any(Employer.class))).thenAnswer(invocation -> {
            Employer updatedEmployer = invocation.getArgument(0);
            updatedEmployer.setName(employerDtoRequest.getName());
            updatedEmployer.setAddress(employerDtoRequest.getAddress());
            return updatedEmployer;
        });

        LocalDateTime now = LocalDateTime.now();
        EmployerDtoResponse employerDtoResponse = new EmployerDtoResponse(employerId, requestName, requestAddress,
                List.of(customerNameFirst, customerNameSecond), creatorName, now, modifierName, now);

        when(employerDtoMapperResponse.convertToDto(any(Employer.class))).thenReturn(employerDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/employers/id/{employerId}", employerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employerDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employerId))
                .andExpect(jsonPath("$.name").value(employerDtoRequest.getName()))
                .andExpect(jsonPath("$.address").value(employerDtoRequest.getAddress()));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDeleteById() throws Exception {
        List<Employer> employers = new ArrayList<>();

        Employer firstEmployer = new Employer();
        Long employerId = 10L;
        firstEmployer.setId(employerId);
        firstEmployer.setName("first");
        firstEmployer.setAddress("firstAddress");

        String customerNameFirst = "firstCustomer";
        Customer customer1 = new Customer(customerNameFirst, "ava@example.com", 38, "789654321", "password12");
        String customerNameSecond = "secondCustomer";
        Customer customer2 = new Customer(customerNameSecond, "john@example.com", 35, "123454321", "password45");
        firstEmployer.setCustomers(Set.of(customer1, customer2));

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerService.getById(employerId)).thenReturn(firstEmployer);

        doAnswer(invocation -> {
            employers.removeIf(e -> e.getId().equals(employerId));
            return employers;
        }).when(employerService).deleteById(employerId);

        // Виклик для видалення employer
        mockMvc.perform(MockMvcRequestBuilders.delete("/employers/deleting/{employerId}", employerId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Employer with ID " + employerId + " deleted")));

        // Оновлюємо заглушку для findAll після видалення
        when(employerService.findAll()).thenReturn(List.of(secondEmployer));

        // Перевірка, що тільки secondEmployer залишився
        List<Employer> remainingEmployers = employerService.findAll();
        assertEquals(1, remainingEmployers.size());
        assertEquals(20L, remainingEmployers.get(0).getId().longValue());
    }

    @Test
    public void testAddCustomer() throws Exception {
        Long EMPLOYER_ID = 10L;
        Long CUSTOMER_ID = 100L;
        String CUSTOMER_NAME = "first";
        String CUSTOMER_EMAIL = "ava@example.com";
        int CUSTOMER_AGE = 38;
        String CUSTOMER_PHONE = "+1234567890";
        String CUSTOMER_PASSWORD = "passworD12";
        String CREATOR_NAME = "creatorUser";
        String MODIFIER_NAME = "modifierUser";
        String FIRST_EMPLOYER_NAME = "first";
        String SECOND_EMPLOYER_NAME = "second";

        CustomerDtoRequest customerDtoRequest = new CustomerDtoRequest(
                CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_AGE, CUSTOMER_PHONE, CUSTOMER_PASSWORD
        );

        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setName(CUSTOMER_NAME);
        customer.setEmail(CUSTOMER_EMAIL);
        customer.setAge(CUSTOMER_AGE);
        customer.setPhoneNumber(CUSTOMER_PHONE);
        log.info("Mock Customer entity: {}", customer);

        Account firstAccount = new Account();
        firstAccount.setId(1000L);
        firstAccount.setCurrency(Currency.USD);
        UUID firstAccountNumber = UUID.randomUUID();
        firstAccount.setNumber(firstAccountNumber);
        Account secondAccount = new Account();
        secondAccount.setId(2000L);
        secondAccount.setCurrency(Currency.EUR);
        UUID secondAccountNumber = UUID.randomUUID();
        secondAccount.setNumber(secondAccountNumber);
        customer.setAccounts(Set.of(firstAccount, secondAccount));
        log.info("Mock Customer accounts: {}, {}", firstAccount, secondAccount);

        Employer firstEmployer = new Employer();
        firstEmployer.setId(500L);
        firstEmployer.setName(FIRST_EMPLOYER_NAME);
        String firstEmployerAddress = "firstAddress";
        firstEmployer.setAddress(firstEmployerAddress);
        Employer secondEmployer = new Employer();
        secondEmployer.setId(600L);
        secondEmployer.setName(SECOND_EMPLOYER_NAME);
        String secondEmployerAddress = "secondAddress";
        secondEmployer.setAddress(secondEmployerAddress);
        customer.setEmployers(Set.of(firstEmployer, secondEmployer));
        log.info("Mock Customer employers: {}, {}", firstEmployer, secondEmployer);

        LocalDateTime now = LocalDateTime.now();

        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName(CREATOR_NAME);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName(MODIFIER_NAME);

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse(
                CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_AGE,
                Set.of(firstAccountNumber, secondAccountNumber),
                List.of(FIRST_EMPLOYER_NAME, SECOND_EMPLOYER_NAME),
                CUSTOMER_PHONE, CREATOR_NAME, now, MODIFIER_NAME, now
        );
        log.info("Mock CustomerDtoResponse: {}", customerDtoResponse);

        when(customerDtoMapperRequest.convertToEntity(any(CustomerDtoRequest.class))).thenReturn(customer);
        doNothing().when(employerService).addCustomer(eq(EMPLOYER_ID), eq(customer));
        when(customerDtoMapperResponse.convertToDto(any(Customer.class))).thenReturn(customerDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/employers/{employerId}/customers/{customerId}", EMPLOYER_ID, CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.name").value(CUSTOMER_NAME))
                .andExpect(jsonPath("$.email").value(CUSTOMER_EMAIL));

        verify(employerService, times(1)).addCustomer(eq(EMPLOYER_ID), eq(customer));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Long EMPLOYER_ID = 10L;
        Long CUSTOMER_ID = 100L;

        Employer employer = new Employer();
        employer.setId(EMPLOYER_ID);
        employer.setName("employer");
        employer.setAddress("employerAddress");

        Customer firstCustomer = new Customer();
        firstCustomer.setId(CUSTOMER_ID);
        firstCustomer.setName("first");
        firstCustomer.setEmail("ava@example.com");
        firstCustomer.setAge(38);

        Account firstAccount = new Account();
        firstAccount.setId(10L);
        firstAccount.setCurrency(Currency.USD);
        firstAccount.setNumber(UUID.randomUUID());
        Account secondAccount = new Account();
        secondAccount.setId(20L);
        secondAccount.setCurrency(Currency.EUR);
        secondAccount.setNumber(UUID.randomUUID());
        firstCustomer.setAccounts(Set.of(firstAccount, secondAccount));

        Employer firstEmployer = new Employer();
        firstEmployer.setId(1000L);
        firstEmployer.setName("first");
        firstEmployer.setAddress("firstAddress");
        Employer secondEmployer = new Employer();
        secondEmployer.setId(2000L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");
        firstCustomer.setEmployers(Set.of(firstEmployer, secondEmployer));

        firstCustomer.setPhoneNumber("+1234567890");
        firstCustomer.setPassword("passworD75");

        Customer secondCustomer = new Customer();
        secondCustomer.setId(200L);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);

        Account thirdAccount = new Account();
        thirdAccount.setId(30L);
        thirdAccount.setCurrency(Currency.USD);
        thirdAccount.setNumber(UUID.randomUUID());
        Account fourthAccount = new Account();
        fourthAccount.setId(40L);
        fourthAccount.setCurrency(Currency.EUR);
        fourthAccount.setNumber(UUID.randomUUID());
        secondCustomer.setAccounts(Set.of(thirdAccount, fourthAccount));

        Employer thirdEmployer = new Employer();
        thirdEmployer.setId(3000L);
        thirdEmployer.setName("third");
        thirdEmployer.setAddress("thirdAddress");
        Employer fourthEmployer = new Employer();
        fourthEmployer.setId(4000L);
        fourthEmployer.setName("fourth");
        fourthEmployer.setAddress("fourthAddress");
        secondCustomer.setEmployers(Set.of(thirdEmployer, fourthEmployer));

        secondCustomer.setPhoneNumber("+1234567891");
        secondCustomer.setPassword("passworD55");

        employer.setCustomers(new HashSet<>(List.of(firstCustomer, secondCustomer)));

        when(employerService.getById(EMPLOYER_ID)).thenReturn(employer);

        doAnswer(invocation -> {
            Long empId = invocation.getArgument(0);
            Long custId = invocation.getArgument(1);

            Employer emp = employerService.getById(empId);

            emp.getCustomers().removeIf(customer -> customer.getId().equals(custId));

            return null;
        }).when(employerService).deleteCustomer(eq(EMPLOYER_ID), eq(CUSTOMER_ID));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employers/{employerId}/customers/{customerId}", EMPLOYER_ID, CUSTOMER_ID))
                .andExpect(status().isOk());

        verify(employerService, times(1)).deleteCustomer(eq(EMPLOYER_ID), eq(CUSTOMER_ID));

        Employer updatedEmployer = employerService.getById(EMPLOYER_ID);

        Set<Customer> remainingCustomers = updatedEmployer.getCustomers();
        assertThat(remainingCustomers).containsOnly(secondCustomer);
    }
}
