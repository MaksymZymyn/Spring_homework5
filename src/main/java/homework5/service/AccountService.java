package homework5.service;

import homework5.domain.bank.Account;
import java.util.List;

public interface AccountService {
    Account save(Account account);

    void delete(Account account);

    void deleteAll(List<Account> accounts);

    void saveAll(List<Account> accounts);

    List<Account> findAll();

    void deleteById(Long id);

    Account getById(Long id);

    Account getByNumber(String accountNumber);

    Account deposit(String number, double amount);

    Account withdraw(String accountNumber, double amount);

    void transfer(String from, String to, double amount);
}
