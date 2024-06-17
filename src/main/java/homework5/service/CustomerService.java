package homework5.service;

import homework5.domain.bank.Account;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

public interface CustomerService {

    Customer save(Customer customer);

    void delete(Customer customer);

    void deleteAll(List<Customer> customers);

    void saveAll(List<Customer> customers);

    Page<Customer> findAll(Pageable pageable);

    void deleteById(Long id);

    Customer getById(Long id);

    Customer getByEmail(String email);

    Customer update(Customer customer);

    Customer createAccount(Long customerId, Account account);

    void deleteAccount(Long customerId, UUID accountNumber);

    Customer addEmployer(Long customerId, Employer employer);

    void deleteEmployer(Long customerId, Long employerId);
}
