package homework5.service;

import homework5.dao.AccountRepository;
import homework5.dao.CustomerRepository;
import homework5.dao.EmployerRepository;
import homework5.domain.bank.Account;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DefaultCustomerService implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final EmployerRepository employerRepository;

    @Override
    public Customer save(Customer customer) {
        Customer existingCustomer = customerRepository.getByEmail(customer.getEmail());
        if (existingCustomer != null) {
            throw new SameCustomerException("Customer with email " + customer.getEmail() + " already exists");
        }
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteAll(List<Customer> customers) {
        customerRepository.deleteAll(customers);
    }

    @Override
    public void saveAll(List<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = NoSuchElementException.class, timeout = 1000)
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getByEmail(String email) {
        return customerRepository.getByEmail(email);
    }

    @Override
    public Customer update(Customer customer) {
        Customer existingCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customer.getId()));

        updateCustomerData(existingCustomer, customer);

        return customerRepository.save(existingCustomer);
    }

    private void updateCustomerData(Customer existingCustomer, Customer newCustomer) {
        if (existingCustomer.getName().equals(newCustomer.getName()) &&
                existingCustomer.getEmail().equals(newCustomer.getEmail()) &&
                existingCustomer.getAge().equals(newCustomer.getAge())) {

            throw new SameCustomerException("The provided customer data is identical to the existing data.");
        }

        if (customerRepository.getByEmail(newCustomer.getEmail()) != null) {
            throw new SameCustomerException("This email of the customer is already exists.");
        }

        existingCustomer.setName(newCustomer.getName());
        existingCustomer.setEmail(newCustomer.getEmail());
        existingCustomer.setAge(newCustomer.getAge());
        existingCustomer.setPhoneNumber(newCustomer.getPhoneNumber());
        existingCustomer.setPassword(newCustomer.getPassword());
        existingCustomer.setLastModifiedDate(newCustomer.getLastModifiedDate());
    }

    @Override
    public Customer createAccount(Long customerId, Account account) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customerId));

        if (account == null) {
            throw new AccountNotFoundException("Account cannot be null");
        }

        if (account.getBalance() < 1) {
            throw new InsufficientBalanceException("Amount must be greater than zero");
        }

        if (customer.getAccounts().stream().anyMatch(acc -> acc.getNumber().equals(account.getNumber()))) {
            throw new SameAccountException("Account number " + account.getNumber() + " already exists");
        }

        customer.getAccounts().add(account);
        account.setCustomer(customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return updatedCustomer;
    }

    @Override
    public void deleteAccount(Long customerId, UUID accountNumber) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customerId));

        Account accountToDelete = customer.getAccounts().stream()
                .filter(account -> account.getNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + accountNumber + " not found for customer with id " + customerId));

        if (customer.getAccounts().remove(accountToDelete)) {
            accountRepository.delete(accountToDelete);
        }
    }

    public Customer addEmployer(Long customerId, Employer employer) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customerId));

        if (employer == null) {
            throw new EmployerNotFoundException("Employer cannot be null");
        }

        Set<Employer> existingEmployers = customer.getEmployers();
        if (existingEmployers.contains(employer)) {
            throw new SameEmployerException("Employer with address " + employer.getAddress() + " already exists");
        }

        customer.getEmployers().add(employer);
        employer.getCustomers().add(customer);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Added employer with id {} for customer with id {}", employer.getId(), customerId);
        return updatedCustomer;
    }

    public void deleteEmployer(Long customerId, Long employerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customerId));

        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with id " + employerId));

        if (customer.getEmployers().remove(employer)) {
            employer.getCustomers().remove(customer);
            employerRepository.save(employer);
            log.info("Employer with id {} removed from customer with id {}", employerId, customerId);
        } else {
            log.error("Employer with id {} not associated with customer with id {}", employerId, customerId);
            throw new EmployerForCustomerNotFoundException("Employer is not associated with customer");
        }

        customerRepository.save(customer);
        log.info("Deleted employer with id {} for customer with id {}", employerId, customerId);
    }
}
