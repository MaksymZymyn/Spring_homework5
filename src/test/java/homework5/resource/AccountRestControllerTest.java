package homework5.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework5.domain.SysUser;
import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.bank.Customer;
import homework5.domain.dto.account.AccountDtoResponse;
import homework5.exceptions.AccountNotFoundException;
import homework5.mapper.account.AccountDtoMapperResponse;
import homework5.service.AccountService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountDtoMapperResponse accountDtoMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() {
        reset(accountService, accountDtoMapper, jwtProvider);
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

        List<Account> accounts = new ArrayList<>();

        Account firstAccount = new Account();
        firstAccount.setId(10L);
        firstAccount.setCurrency(Currency.USD);
        UUID firstAccountNumber = UUID.fromString("77cf9522-5d7e-4928-a6b5-50c1160e9bd3");
        firstAccount.setNumber(firstAccountNumber);
        String customerNameFirstAccount = "first";
        Customer customer1 = new Customer(customerNameFirstAccount, "ava@example.com", 38, "789654321", "password12");
        firstAccount.setCustomer(customer1);

        Account secondAccount = new Account();
        secondAccount.setId(20L);
        secondAccount.setCurrency(Currency.EUR);
        UUID secondAccountNumber = UUID.fromString("77cf9522-5d7e-4928-a6b5-50c1160e9bd3");
        firstAccount.setNumber(secondAccountNumber);
        String customerNameSecondAccount = "second";
        Customer customer2 = new Customer(customerNameSecondAccount, "john@example.com", 35, "123454321", "password45");
        secondAccount.setCustomer(customer2);

        accounts.add(firstAccount);
        accounts.add(secondAccount);

        AccountDtoResponse dtoFirst = new AccountDtoResponse(10L, firstAccountNumber, Currency.USD, 1000.0,
                customerNameFirstAccount, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());
        AccountDtoResponse dtoSecond = new AccountDtoResponse(20L, secondAccountNumber, Currency.EUR, 2000.0,
                customerNameSecondAccount, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());

        when(accountService.findAll()).thenReturn(accounts);
        when(accountDtoMapper.convertToDto(firstAccount)).thenReturn(dtoFirst);
        when(accountDtoMapper.convertToDto(secondAccount)).thenReturn(dtoSecond);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(dtoFirst.getId().intValue())))
                .andExpect(jsonPath("$[0].number", Matchers.is(dtoFirst.getNumber().toString())))
                .andExpect(jsonPath("$[0].customerName", Matchers.is(dtoFirst.getCustomerName())))
                .andExpect(jsonPath("$[0].balance", Matchers.is(dtoFirst.getBalance())))
                .andExpect(jsonPath("$[0].currency", Matchers.is(dtoFirst.getCurrency().toString())))
                .andExpect(jsonPath("$[1].id", Matchers.is(dtoSecond.getId().intValue())))
                .andExpect(jsonPath("$[1].number", Matchers.is(dtoSecond.getNumber().toString())))
                .andExpect(jsonPath("$[1].customerName", Matchers.is(dtoSecond.getCustomerName())))
                .andExpect(jsonPath("$[1].balance", Matchers.is(dtoSecond.getBalance())))
                .andExpect(jsonPath("$[1].currency", Matchers.is(dtoSecond.getCurrency().toString())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetByNumber_Success() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        Account account = new Account();
        account.setId(10L);
        UUID accountNumber = UUID.randomUUID();
        account.setNumber(accountNumber);
        String customerName = "first";
        Customer customer = new Customer(customerName, "ava@example.com", 38, "789654321", "password12");
        account.setCustomer(customer);

        AccountDtoResponse dtoFirst = new AccountDtoResponse(10L, accountNumber, Currency.USD, 1000.0,
                customerName, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());

        when(accountService.getByNumber(anyString())).thenReturn(account);
        when(accountDtoMapper.convertToDto(account)).thenReturn(dtoFirst);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{number}", accountNumber.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetByNumber_AccountNotFound() throws Exception {
        UUID number = UUID.randomUUID();
        when(accountService.getByNumber(anyString())).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{number}", number))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDeposit() throws Exception {
        Account account = new Account();
        account.setId(10L);
        UUID accountNumber = UUID.fromString("937819b2-7359-4730-bf28-df4ec227a063");
        account.setNumber(accountNumber);
        account.setCurrency(Currency.USD);
        account.setBalance(1000);
        String customerName = "first";
        Customer customer = new Customer(customerName, "ava@example.com", 38, "789654321", "password12");
        account.setCustomer(customer);

        String parametersJson = String.format("{\"accountNumber\": \"%s\", \"amount\": 150.00}", accountNumber);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNodeAccountNumber = mapper.readTree(parametersJson);
        String accountNumberJson = nameNodeAccountNumber.get("accountNumber").asText();
        Double amount = Double.parseDouble(nameNodeAccountNumber.get("amount").asText());

        AccountDtoResponse dto = new AccountDtoResponse(
                10L,
                accountNumber,
                Currency.USD,
                1150.0,
                customerName,
                "creatorName",
                LocalDateTime.now(),
                "modifierName",
                LocalDateTime.now()
        );

        when(accountService.deposit(accountNumberJson, amount)).thenReturn(account);
        when(accountDtoMapper.convertToDto(account)).thenReturn(dto);

        mockMvc.perform(put("/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parametersJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(dto.getId().intValue())))
                .andExpect(jsonPath("$.customerName", Matchers.is(dto.getCustomerName())))
                .andExpect(jsonPath("$.balance", Matchers.is(dto.getBalance())))
                .andExpect(jsonPath("$.currency", Matchers.is(dto.getCurrency().toString())))
                .andExpect(jsonPath("$.number", Matchers.is(dto.getNumber().toString())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testWithdraw() throws Exception {
        Account account = new Account();
        account.setId(10L);
        UUID accountNumber = UUID.fromString("937819b2-7359-4730-bf28-df4ec227a063");
        account.setNumber(accountNumber);
        account.setCurrency(Currency.USD);
        account.setBalance(1000);
        String customerName = "first";
        Customer customer = new Customer(customerName, "ava@example.com", 38, "789654321", "password12");
        account.setCustomer(customer);

        String parametersJson = String.format("{\"accountNumber\": \"%s\", \"amount\": 150.00}", accountNumber);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNodeAccountNumber = mapper.readTree(parametersJson);
        String accountNumberJson = nameNodeAccountNumber.get("accountNumber").asText();
        Double amount = Double.parseDouble(nameNodeAccountNumber.get("amount").asText());

        AccountDtoResponse dto = new AccountDtoResponse(
                10L,
                accountNumber,
                Currency.USD,
                850.0,
                customerName,
                "creatorName",
                LocalDateTime.now(),
                "modifierName",
                LocalDateTime.now()
        );

        when(accountService.withdraw(accountNumberJson, amount)).thenReturn(account);
        when(accountDtoMapper.convertToDto(account)).thenReturn(dto);

        mockMvc.perform(put("/accounts/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parametersJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(dto.getId().intValue())))
                .andExpect(jsonPath("$.customerName", Matchers.is(dto.getCustomerName())))
                .andExpect(jsonPath("$.balance", Matchers.is(dto.getBalance())))
                .andExpect(jsonPath("$.currency", Matchers.is(dto.getCurrency().toString())))
                .andExpect(jsonPath("$.number", Matchers.is(dto.getNumber().toString())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testTransfer() throws Exception {
        Account fromAccount = createTestAccount(
                10L,
                UUID.fromString("937819b2-7359-4730-bf28-df4ec227a063"),
                "first",
                "ava@example.com",
                2000.0);

        Account toAccount = createTestAccount(
                20L,
                UUID.fromString("123e4567-e89b-12d3-a456-426614174002"),
                "second",
                "john@example.com",
                500.0);

        String parametersJson = String.format("{\"accountNumberSender\": \"%s\", " +
                        "\"accountNumberReceiver\": \"%s\", " +
                        "\"amount\": 150.00}",
                fromAccount.getNumber(),
                toAccount.getNumber());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(parametersJson);
        String fromAccountNumberJson = jsonNode.get("accountNumberSender").asText();
        String toAccountNumberJson = jsonNode.get("accountNumberReceiver").asText();
        Double amountJson = jsonNode.get("amount").asDouble();

        doAnswer(invocation -> {
            Account from = accountService.getByNumber(invocation.getArgument(0));
            Account to = accountService.getByNumber(invocation.getArgument(1));
            double amount = invocation.getArgument(2);
            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);
            return null;
        }).when(accountService).transfer(fromAccountNumberJson, toAccountNumberJson, amountJson);

        when(accountService.getByNumber(fromAccountNumberJson)).thenReturn(fromAccount);
        when(accountService.getByNumber(toAccountNumberJson)).thenReturn(toAccount);

        MvcResult result = mockMvc.perform(put("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parametersJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        JsonNode responseJsonNode = mapper.readTree(responseJson);

        assertEquals(1850.0, responseJsonNode.get("fromAccountBalance").asDouble(), 0.0);
        assertEquals(650.0, responseJsonNode.get("toAccountBalance").asDouble(), 0.0);
    }

    private Account createTestAccount(Long id, UUID accountNumber, String customerName, String email, double balance) {
        Account account = new Account();
        account.setId(id);
        account.setNumber(accountNumber);
        account.setBalance(balance);
        Customer customer = new Customer(customerName, email, 38, "789654321", "password12");
        account.setCustomer(customer);
        return account;
    }
}
