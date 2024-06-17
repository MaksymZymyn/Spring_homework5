package homework5.dao;

import homework5.domain.bank.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindAll() {
        List<Account> accounts = accountRepository.findAll();
        assertEquals(21, accounts.size());

        assertTrue(accounts.stream().anyMatch(account ->
                account.getCustomer().getName().equals("John Doe") &&
                        account.getCustomer().getEmployers().size() == 2));

        assertTrue(accounts.stream().anyMatch(account ->
                account.getCustomer().getName().equals("Emily Adams") &&
                        account.getCustomer().getEmployers().size() == 1));
    }

    @Test
    void testFindById() {
        Optional<Account> accountOpt = accountRepository.findById(1L);

        assertTrue(accountOpt.isPresent());
        Account account = accountOpt.get();

        assertEquals("John Doe", account.getCustomer().getName());
        assertEquals(2, account.getCustomer().getEmployers().size());
    }

    @Test
    void testFindByNumber() {
        Optional<Account> accountOpt = accountRepository.findByNumber(accountRepository.findById(1L).get().getNumber());

        assertTrue(accountOpt.isPresent());
        Account account = accountOpt.get();

        assertEquals("John Doe", account.getCustomer().getName());
        assertEquals(2, account.getCustomer().getEmployers().size());
    }
}

