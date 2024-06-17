package homework5.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework5.domain.SysUser;
import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.account.AccountDtoRequest;
import homework5.domain.dto.customer.CustomerDtoRequest;
import homework5.domain.dto.customer.CustomerDtoResponse;
import homework5.domain.dto.employer.EmployerDtoRequest;
import homework5.exceptions.CustomerNotFoundException;
import homework5.mapper.account.AccountDtoMapperRequest;
import homework5.mapper.customer.CustomerDtoMapperRequest;
import homework5.mapper.customer.CustomerDtoMapperResponse;
import homework5.mapper.employer.EmployerDtoMapperRequest;
import homework5.service.CustomerService;
import homework5.service.jwt.JwtProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerDtoMapperResponse customerDtoMapperResponse;

    @MockBean
    private AccountDtoMapperRequest accountDtoMapperRequest;

    @MockBean
    private EmployerDtoMapperRequest employerDtoMapperRequest;

    @MockBean
    private CustomerDtoMapperRequest customerDtoMapperRequest;

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
        reset(customerService, customerDtoMapperResponse, customerDtoMapperRequest, jwtProvider);
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

        List<Customer> customers = new ArrayList<>();

        Customer firstCustomer = new Customer();
        firstCustomer.setId(100L);
        String firstCustomerName = "first";
        firstCustomer.setName(firstCustomerName);
        String firstCustomerEmail = "ava@example.com";
        firstCustomer.setEmail(firstCustomerEmail);
        int firstCustomerAge = 38;
        firstCustomer.setAge(firstCustomerAge);
        Account firstAccount = new Account();
        firstAccount.setId(10L);
        firstAccount.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        firstAccount.setNumber(firstAccountNumber);
        Account secondAccount = new Account();
        secondAccount.setId(20L);
        secondAccount.setCurrency(Currency.EUR);
        final UUID secondAccountNumber = UUID.randomUUID();
        secondAccount.setNumber(secondAccountNumber);
        firstCustomer.setAccounts(Set.of(firstAccount, secondAccount));

        Employer firstEmployer = new Employer();
        firstEmployer.setId(1000L);
        String firstEmployerName = "first";
        firstEmployer.setName(firstEmployerName);
        String firstEmployerAddress = "firstAddress";
        firstEmployer.setAddress(firstEmployerAddress);
        Employer secondEmployer = new Employer();
        secondEmployer.setId(2000L);
        String secondEmployerName = "second";
        secondEmployer.setName(secondEmployerName);
        String secondEmployerAddress = "secondAddress";
        secondEmployer.setAddress(secondEmployerAddress);
        firstCustomer.setEmployers(Set.of(firstEmployer, secondEmployer));

        String firstCustomerPhoneNumber = "789654322";
        firstCustomer.setPhoneNumber(firstCustomerPhoneNumber);
        String firstCustomerPassword = "passworD75";
        firstCustomer.setPassword(firstCustomerPassword);

        Customer secondCustomer = new Customer();
        secondCustomer.setId(200L);
        String secondCustomerName = "second";
        secondCustomer.setName(secondCustomerName);
        String secondCustomerEmail = "jane@example.com";
        secondCustomer.setEmail(secondCustomerEmail);
        int secondCustomerAge = 35;
        secondCustomer.setAge(secondCustomerAge);
        Account thirdAccount = new Account();
        thirdAccount.setId(30L);
        thirdAccount.setCurrency(Currency.USD);
        final UUID thirdAccountNumber = UUID.randomUUID();
        thirdAccount.setNumber(thirdAccountNumber);
        Account fourthAccount = new Account();
        fourthAccount.setId(40L);
        fourthAccount.setCurrency(Currency.EUR);
        final UUID fourthAccountNumber = UUID.randomUUID();
        fourthAccount.setNumber(fourthAccountNumber);
        secondCustomer.setAccounts(Set.of(thirdAccount, fourthAccount));

        Employer thirdEmployer = new Employer();
        thirdEmployer.setId(3000L);
        String thirdEmployerName = "third";
        thirdEmployer.setName(thirdEmployerName);
        String thirdEmployerAddress = "thirdAddress";
        thirdEmployer.setAddress(thirdEmployerAddress);
        Employer fourthEmployer = new Employer();
        fourthEmployer.setId(4000L);
        String fourthEmployerName = "fourth";
        fourthEmployer.setName(fourthEmployerName);
        String fourthEmployerAddress = "fourthAddress";
        fourthEmployer.setAddress(fourthEmployerAddress);
        secondCustomer.setEmployers(Set.of(thirdEmployer, fourthEmployer));

        String secondCustomerPhoneNumber = "789654333";
        secondCustomer.setPhoneNumber(secondCustomerPhoneNumber);
        String secondCustomerPassword = "passworD55";
        secondCustomer.setPassword(secondCustomerPassword);

        customers.add(firstCustomer);
        customers.add(secondCustomer);

        LocalDateTime now = LocalDateTime.now();

        CustomerDtoResponse dtoFirst = new CustomerDtoResponse(100L, firstCustomerName, firstCustomerEmail,
                firstCustomerAge, Set.of(firstAccount.getNumber(), secondAccount.getNumber()),
                List.of(firstEmployerName, secondEmployerName), firstCustomerPhoneNumber,
                creatorName, now, modifierName, now);

        CustomerDtoResponse dtoSecond = new CustomerDtoResponse(200L, secondCustomerName, secondCustomerEmail,
                secondCustomerAge, Set.of(thirdAccount.getNumber(), fourthAccount.getNumber()),
                List.of(thirdEmployerName, fourthEmployerName), secondCustomerPhoneNumber,
                creatorName, now, modifierName, now);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());

        when(customerService.findAll(any(Pageable.class))).thenReturn(customerPage);
        when(customerDtoMapperResponse.convertToDto(firstCustomer)).thenReturn(dtoFirst);
        when(customerDtoMapperResponse.convertToDto(secondCustomer)).thenReturn(dtoSecond);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(dtoFirst.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(dtoFirst.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumbers", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumbers", Matchers.containsInAnyOrder(
                        firstAccountNumber.toString(), secondAccountNumber.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(dtoSecond.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is(dtoSecond.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountNumbers", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountNumbers", Matchers.containsInAnyOrder(
                        thirdAccountNumber.toString(), fourthAccountNumber.toString())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetById() throws Exception {
        Customer customer = new Customer();
        customer.setId(100L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);
        Account account = new Account();
        account.setId(1L);
        account.setCurrency(Currency.USD);
        account.setNumber(UUID.randomUUID());
        customer.setAccounts(Set.of(account));

        Employer employer = new Employer();
        employer.setId(1000L);
        employer.setName("Acme Corporation");
        employer.setAddress("123 Main St");
        customer.setEmployers(Set.of(employer));

        LocalDateTime now = LocalDateTime.now();
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        CustomerDtoResponse dtoResponse = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(account.getNumber()),
                List.of(employer.getName()),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerService.getById(100L)).thenReturn(customer);
        when(customerDtoMapperResponse.convertToDto(any(Customer.class))).thenReturn(dtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", 100L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(dtoResponse.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(dtoResponse.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(dtoResponse.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(dtoResponse.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumbers", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumbers[0]", Matchers.is(account.getNumber().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employerNames", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employerNames[0]", Matchers.is(employer.getName())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testCreateCustomer() throws Exception {
        CustomerDtoRequest customerDtoRequest = new CustomerDtoRequest();
        String customerName = "John Doe";
        customerDtoRequest.setName(customerName);
        String customerEmail = "john.doe@example.com";
        customerDtoRequest.setEmail(customerEmail);
        int customerAge = 30;
        customerDtoRequest.setAge(customerAge);
        String customerPhoneNumber = "+1234567891";
        customerDtoRequest.setPhoneNumber(customerPhoneNumber);
        String customerPassword = "passworD55";
        customerDtoRequest.setPassword(customerPassword);

        Customer customer = new Customer();
        customer.setId(100L);
        customer.setName(customerName);
        customer.setEmail(customerEmail);
        customer.setAge(customerAge);
        customer.setPhoneNumber(customerPhoneNumber);
        customer.setPassword(customerPassword);

        Account account = new Account();
        account.setId(1L);
        account.setCurrency(Currency.USD);
        account.setNumber(UUID.randomUUID());
        customer.setAccounts(Set.of(account));

        Employer employer = new Employer();
        employer.setId(1000L);
        employer.setName("Acme Corporation");
        employer.setAddress("123 Main St");
        customer.setEmployers(Set.of(employer));

        LocalDateTime now = LocalDateTime.now();
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        CustomerDtoResponse dtoResponse = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(account.getNumber()),
                List.of(employer.getName()),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperRequest.convertToEntity(customerDtoRequest)).thenReturn(customer);
        when(customerService.save(customer)).thenReturn(customer);
        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(dtoResponse);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(customerName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(customerAge))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumbers", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumbers[0]", Matchers.is(account.getNumber().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employerNames", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employerNames[0]", Matchers.is(employer.getName())));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1L;

        CustomerDtoRequest customerDtoRequest = new CustomerDtoRequest(
                "John Doe",
                "john.doe@example.com",
                30,
                "+1234567890",
                "securePassword123"
        );

        Customer customer = new Customer(
                "John Doe",
                "john.doe@example.com",
                30,
                "+1234567890",
                "securePassword123"
        );
        customer.setId(customerId);

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse(
                customerId,
                "John Doe",
                "john.doe@example.com",
                30,
                new HashSet<>(List.of(UUID.randomUUID())),
                List.of("Acme Corp"),
                "+1234567890",
                "admin",
                LocalDateTime.now(),
                "admin",
                LocalDateTime.now()
        );

        when(customerDtoMapperRequest.convertToEntity(any(CustomerDtoRequest.class))).thenReturn(customer);
        when(customerService.update(any(Customer.class))).thenReturn(customer);
        when(customerDtoMapperResponse.convertToDto(any(Customer.class))).thenReturn(customerDtoResponse);

        mockMvc.perform(put("/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(jsonPath("$.creationByUserName").value("admin"))
                .andExpect(jsonPath("$.lastModifiedByUserName").value("admin"));
    }

    @Test
    void testDeleteById() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName("creatorUser");

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName("modifierUser");

        List<Customer> customers = new ArrayList<>();

        Customer firstCustomer = new Customer();
        firstCustomer.setId(100L);
        firstCustomer.setName("first");
        firstCustomer.setEmail("ava@example.com");
        firstCustomer.setAge(38);
        firstCustomer.setAccounts(Set.of(createAccount(10L, Currency.USD), createAccount(20L, Currency.EUR)));
        firstCustomer.setEmployers(Set.of(createEmployer(1000L, "first", "firstAddress"), createEmployer(2000L, "second", "secondAddress")));
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

        Customer secondCustomer = new Customer();
        secondCustomer.setId(200L);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);
        secondCustomer.setAccounts(Set.of(createAccount(30L, Currency.USD), createAccount(40L, Currency.EUR)));
        secondCustomer.setEmployers(Set.of(createEmployer(3000L, "third", "thirdAddress"), createEmployer(4000L, "fourth", "fourthAddress")));
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

        customers.add(firstCustomer);
        customers.add(secondCustomer);

        when(customerService.getById(firstCustomer.getId())).thenReturn(firstCustomer);
        doNothing().when(customerService).deleteById(firstCustomer.getId());
        when(customerService.getById(secondCustomer.getId())).thenReturn(secondCustomer);

        mockMvc.perform(delete("/customers/{customerId}", firstCustomer.getId()))
                .andExpect(status().isOk());

        when(customerService.getById(firstCustomer.getId())).thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", firstCustomer.getId()))
                .andExpect(status().isBadRequest());

        when(customerService.getById(200L)).thenReturn(secondCustomer);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", secondCustomer.getId()))
                .andExpect(status().isOk());
    }

    private Account createAccount(Long id, Currency currency) {
        Account account = new Account();
        account.setId(id);
        account.setCurrency(currency);
        account.setNumber(UUID.randomUUID());
        return account;
    }

    private Employer createEmployer(Long id, String name, String address) {
        Employer employer = new Employer();
        employer.setId(id);
        employer.setName(name);
        employer.setAddress(address);
        return employer;
    }

    @Test
    @WithMockUser(username = "testUser")
    void testCreateAccount() throws Exception {
        Long customerId = 100L;

        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName("creatorUser");

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName("modifierUser");

        LocalDateTime now = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);

        Set<Account> accounts = new HashSet<>();
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        existingAccount.setNumber(firstAccountNumber);
        accounts.add(existingAccount);

        customer.setAccounts(accounts);

        Employer employer = new Employer();
        employer.setId(1000L);
        employer.setName("Acme Corporation");
        employer.setAddress("123 Main St");
        customer.setEmployers(Set.of(employer));

        when(customerService.getById(customerId)).thenReturn(customer);

        AccountDtoRequest accountDtoRequest = new AccountDtoRequest(Currency.USD, 1000);
        Account newAccount = new Account();
        newAccount.setId(2L);
        newAccount.setCurrency(Currency.USD);
        final UUID secondAccountNumber = UUID.randomUUID();
        newAccount.setNumber(secondAccountNumber);
        when(accountDtoMapperRequest.convertToEntity(accountDtoRequest)).thenReturn(newAccount);

        doAnswer(invocation -> {
            accounts.add(newAccount);
            return customer;
        }).when(customerService).createAccount(eq(customerId), any(Account.class));

        Set<UUID> accountNumbers = new HashSet<>();
        accountNumbers.add(firstAccountNumber);
        accountNumbers.add(secondAccountNumber);

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                accountNumbers,
                List.of(employer.getName()),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(customerDtoResponse);

        mockMvc.perform(post("/customers/{customerId}/accounts", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountDtoRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.accountNumbers").isArray())
                .andExpect(jsonPath("$.accountNumbers", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumbers", Matchers.containsInAnyOrder(
                        firstAccountNumber.toString(), secondAccountNumber.toString())));
    }

    @Test
    void testDeleteAccount() throws Exception {
        Long customerId = 100L;

        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName("creatorUser");

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName("modifierUser");

        LocalDateTime now = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);

        Set<Account> accounts = new HashSet<>();
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        existingAccount.setNumber(firstAccountNumber);
        accounts.add(existingAccount);

        Account newAccount = new Account();
        newAccount.setId(2L);
        newAccount.setCurrency(Currency.USD);
        final UUID secondAccountNumber = UUID.randomUUID();
        newAccount.setNumber(secondAccountNumber);
        accounts.add(newAccount);

        customer.setAccounts(accounts);

        Employer employer = new Employer();
        employer.setId(1000L);
        employer.setName("Acme Corporation");
        employer.setAddress("123 Main St");
        customer.setEmployers(Set.of(employer));

        when(customerService.getById(customerId)).thenReturn(customer);

        AccountDtoRequest firstAccountDtoRequest = new AccountDtoRequest(Currency.USD, 1000);
        AccountDtoRequest secondAccountDtoRequest = new AccountDtoRequest(Currency.USD, 2000);

        when(accountDtoMapperRequest.convertToEntity(firstAccountDtoRequest)).thenReturn(existingAccount);
        when(accountDtoMapperRequest.convertToEntity(secondAccountDtoRequest)).thenReturn(newAccount);

        CustomerDtoResponse customerDtoResponseBefore = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(firstAccountNumber, secondAccountNumber),
                List.of(employer.getName()),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(customerDtoResponseBefore);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumbers", hasSize(2)));

        doAnswer(invocation -> {
            accounts.remove(existingAccount);
            return accounts;
        }).when(customerService).deleteAccount(customerId, secondAccountNumber);

        CustomerDtoResponse customerDtoResponseAfter = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(secondAccountNumber),
                List.of(employer.getName()),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(customerDtoResponseAfter);

        mockMvc.perform(delete("/customers/{customerId}/accounts/{accountNumber}", customerId, firstAccountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.accountNumbers", hasSize(1)))
                .andExpect(jsonPath("$.accountNumbers[0]").value(secondAccountNumber.toString()));
    }

    @Test
    void testAddEmployer() throws Exception {
        Long customerId = 100L;

        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName("creatorUser");

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName("modifierUser");

        LocalDateTime now = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);

        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setCurrency(Currency.USD);
        UUID firstAccountNumber = UUID.randomUUID();
        existingAccount.setNumber(firstAccountNumber);
        customer.setAccounts(Set.of(existingAccount));

        Set<Employer> employers = new HashSet<>();
        Employer employer = new Employer();
        employer.setId(1000L);
        String firstEmployerName = "Acme Corporation";
        employer.setName(firstEmployerName);
        employer.setAddress("123 Main St");
        employers.add(employer);
        customer.setEmployers(employers);

        when(customerService.getById(customerId)).thenReturn(customer);

        String employerName = "firstEmployer";
        String employerAddress = "1 Right St";
        EmployerDtoRequest employerDtoRequest = new EmployerDtoRequest(employerName, employerAddress);
        Employer newEmployer = new Employer();
        newEmployer.setId(2000L);
        newEmployer.setName(employerName);
        newEmployer.setAddress(employerAddress);
        when(employerDtoMapperRequest.convertToEntity(employerDtoRequest)).thenReturn(newEmployer);

        doAnswer(invocation -> {
            employers.add(newEmployer);
            return employers;
        }).when(customerService).addEmployer(customerId, newEmployer);

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(firstAccountNumber),
                List.of(firstEmployerName, employerName),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(customerDtoResponse);

        mockMvc.perform(post("/customers/{customerId}/employers", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employerDtoRequest)));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employerNames", hasSize(2)))
                .andExpect(jsonPath("$.employerNames[0]").value(firstEmployerName))
                .andExpect(jsonPath("$.employerNames[1]").value(employerName));
    }

    @Test
    void testDeleteEmployer() throws Exception {
        Long customerId = 100L;

        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName("creatorUser");

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName("modifierUser");

        LocalDateTime now = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);

        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        existingAccount.setNumber(firstAccountNumber);

        customer.setAccounts(Set.of(existingAccount));

        Set<Employer> employers = new HashSet<>();
        Employer employer = new Employer();
        employer.setId(1000L);
        String employerName = "Acme Corporation";
        employer.setName(employerName);
        String employerAddress = "123 Main St";
        employer.setAddress(employerAddress);
        employers.add(employer);

        Employer newEmployer = new Employer();
        newEmployer.setId(2000L);
        String newEmployerName = "Acme Corporation Plus";
        newEmployer.setName(newEmployerName);
        String newEmployerAddress = "1 Right St";
        newEmployer.setAddress(newEmployerAddress);
        employers.add(newEmployer);

        customer.setEmployers(employers);

        when(customerService.getById(customerId)).thenReturn(customer);

        EmployerDtoRequest firstEmployerDtoRequest = new EmployerDtoRequest(employerName, employerAddress);
        EmployerDtoRequest secondEmployerDtoRequest = new EmployerDtoRequest(newEmployerName, newEmployerAddress);

        when(employerDtoMapperRequest.convertToEntity(firstEmployerDtoRequest)).thenReturn(employer);
        when(employerDtoMapperRequest.convertToEntity(secondEmployerDtoRequest)).thenReturn(newEmployer);

        CustomerDtoResponse customerDtoResponseBefore = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(firstAccountNumber),
                List.of(employerName, newEmployerName),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(customerDtoResponseBefore);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employerNames", hasSize(2)));

        doAnswer(invocation -> {
            employers.remove(employer);
            return employers;
        }).when(customerService).deleteEmployer(customerId, employer.getId());

        CustomerDtoResponse customerDtoResponseAfter = new CustomerDtoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                Set.of(firstAccountNumber),
                List.of(newEmployerName),
                customer.getPhoneNumber(),
                creator.getUserName(),
                now,
                modifier.getUserName(),
                now
        );

        when(customerDtoMapperResponse.convertToDto(customer)).thenReturn(customerDtoResponseAfter);

        mockMvc.perform(delete("/customers/{customerId}/employers/{employerId}", customerId, employer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.employerNames", hasSize(1)))
                .andExpect(jsonPath("$.employerNames[0]").value(newEmployerName));
    }
}
