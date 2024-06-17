package homework5.service;

import homework5.dao.AccountRepository;
import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultAccountService accountService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new DefaultAccountService(accountRepository, simpMessagingTemplate);
    }

    @Test
    void testFindAll() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);

        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> returnedAccounts = accountService.findAll();

        assertNotNull(returnedAccounts);
        assertEquals(2, returnedAccounts.size());

        Account returnedAccount1 = returnedAccounts.get(0);
        assertEquals(1L, returnedAccount1.getId());
        assertEquals(Currency.USD, returnedAccount1.getCurrency());

        Account returnedAccount2 = returnedAccounts.get(1);
        assertEquals(2L, returnedAccount2.getId());
        assertEquals(Currency.EUR, returnedAccount2.getCurrency());
    }

    @Test
    void testSave() {
        Account accountToSave = new Account();
        accountToSave.setId(1L);
        accountToSave.setCurrency(Currency.USD);
        List<Account> accounts = Arrays.asList(accountToSave);
        when(accountRepository.save(accountToSave)).thenReturn(accountToSave);
        when(accountRepository.findAll()).thenReturn(accounts);
        Account savedAccount = accountService.save(accountToSave);

        assertNotNull(savedAccount);
        assertEquals(1L, savedAccount.getId());
        assertEquals(Currency.USD, savedAccount.getCurrency());
        assertEquals(accountService.findAll().size(), 1);
    }

    @Test
    void testSaveAll() {
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.saveAll(accounts)).thenReturn(accounts);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));

        accountService.saveAll(accounts);

        assertEquals(2, accountService.findAll().size());

        assertEquals(account1, accountService.getById(1L));
    }

    @Test
    void testDelete() {
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> returnedAccounts = accountService.findAll();
        assertNotNull(returnedAccounts);
        assertEquals(2, returnedAccounts.size());

        doAnswer(invocation -> {
            accounts.remove(account1);
            return accounts;
        }).when(accountRepository).delete(account1);

        accountService.delete(account1);

        List<Account> updatedAccounts = accountService.findAll();

        assertEquals(1, updatedAccounts.size());

        assertFalse(updatedAccounts.contains(account1));
    }

    @Test
    void testDeleteAll() {
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.saveAll(accounts)).thenReturn(accounts);
        when(accountRepository.findAll()).thenReturn(accounts);

        accountService.saveAll(accounts);

        assertEquals(2, accountService.findAll().size());

        doAnswer(invocation -> {
            accounts.clear();
            return accounts;
        }).when(accountRepository).deleteAll(accounts);

        accountService.deleteAll(accounts);

        List<Account> updatedAccounts = accountService.findAll();

        assertEquals(0, updatedAccounts.size());
        assertFalse(updatedAccounts.contains(account1));
        assertFalse(updatedAccounts.contains(account2));
    }

    @Test
    void testDeleteById() {
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.findAll()).thenReturn(accounts);
        List<Account> returnedAccounts = accountService.findAll();
        assertNotNull(returnedAccounts);
        assertEquals(2, returnedAccounts.size());

        doAnswer(invocation -> {
            accounts.remove(account1);
            return accounts;
        }).when(accountRepository).deleteById(1L);

        accountService.deleteById(1L);

        List<Account> updatedAccounts = accountService.findAll();

        assertEquals(1, updatedAccounts.size());

        assertFalse(updatedAccounts.contains(account1));
    }

    @Test
    void testGetById() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));

        Account returnedAccount = accountService.getById(1L);
        assertNotNull(returnedAccount);
        assertEquals(1L, returnedAccount.getId());
        assertEquals(Currency.USD, returnedAccount.getCurrency());
    }

    @Test
    void testGetByNumber() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);

        when(accountRepository.findByNumber(firstAccountNumber)).thenReturn(Optional.of(account1));

        Account returnedAccount = accountService.getByNumber(firstAccountNumber.toString());
        assertNotNull(returnedAccount);
        assertEquals(firstAccountNumber, returnedAccount.getNumber());
        assertEquals(Currency.USD, returnedAccount.getCurrency());
    }

    @Test
    void testDeposit() {
        List<Account> accounts = new ArrayList<>();

        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        account1.setBalance(1000.0);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        account2.setBalance(500.0);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.findByNumber(firstAccountNumber)).thenReturn(Optional.of(account1));

        when(accountRepository.findAll()).thenReturn(accounts);
        List<Account> returnedAccounts = accountService.findAll();
        assertEquals(2, returnedAccounts.size());

        Account updatedAccount = accountService.deposit(firstAccountNumber.toString(), 200.0);

        assertNotNull(updatedAccount);
        assertEquals(1200.0, updatedAccount.getBalance());
    }

    @Test
    void testWithdraw() {
        List<Account> accounts = new ArrayList<>();

        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        account1.setBalance(1000.0);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        account2.setBalance(500.0);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.findByNumber(firstAccountNumber)).thenReturn(Optional.of(account1));

        when(accountRepository.findAll()).thenReturn(accounts);
        List<Account> returnedAccounts = accountService.findAll();
        assertEquals(2, returnedAccounts.size());

        Account updatedAccount = accountService.withdraw(firstAccountNumber.toString(), 200.0);

        assertNotNull(updatedAccount);
        assertEquals(800.0, updatedAccount.getBalance());
    }

    @Test
    void testTransfer() {
        List<Account> accounts = new ArrayList<>();

        Account account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(Currency.USD);
        account1.setBalance(1000.0);
        final UUID firstAccountNumber = UUID.randomUUID();
        account1.setNumber(firstAccountNumber);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(Currency.EUR);
        account2.setBalance(500.0);
        final UUID secondAccountNumber = UUID.randomUUID();
        account2.setNumber(secondAccountNumber);
        accounts.add(account2);

        when(accountRepository.findByNumber(firstAccountNumber)).thenReturn(Optional.of(account1));

        when(accountRepository.findAll()).thenReturn(accounts);
        List<Account> returnedAccounts = accountService.findAll();
        assertEquals(2, returnedAccounts.size());

        when(accountRepository.findByNumber(firstAccountNumber)).thenReturn(Optional.of(account1));
        when(accountRepository.findByNumber(secondAccountNumber)).thenReturn(Optional.of(account2));
        when(accountRepository.save(any(Account.class))).thenReturn(account1).thenReturn(account2);

        accountService.transfer(firstAccountNumber.toString(), secondAccountNumber.toString(), 200.0);

        assertEquals(800.0, account1.getBalance());
        assertEquals(700.0, account2.getBalance());
    }
}
