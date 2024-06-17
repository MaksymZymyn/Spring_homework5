package homework5.service;

import homework5.dao.AccountRepository;
import homework5.dao.CustomerRepository;
import homework5.dao.EmployerRepository;
import homework5.domain.bank.Account;
import homework5.domain.bank.Currency;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private DefaultCustomerService customerService;

    @Test
    void testFindAll() {
        Customer firstCustomer = new Customer();
        Long firstCustomerId = 100L;
        firstCustomer.setId(firstCustomerId);
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
        Long secondCustomerId = 200L;
        secondCustomer.setId(secondCustomerId);
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

        List<Customer> customers = Arrays.asList(firstCustomer, secondCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers);

        Pageable pageable = PageRequest.of(0, 10);
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> returnedCustomers = customerService.findAll(pageable);

        assertNotNull(returnedCustomers);
        assertEquals(2, returnedCustomers.getTotalElements());

        Customer returnedCustomer1 = returnedCustomers.getContent().get(0);
        assertEquals(100L, returnedCustomer1.getId());
        assertEquals(firstCustomerEmail, returnedCustomer1.getEmail());

        Customer returnedCustomer2 = returnedCustomers.getContent().get(1);
        assertEquals(200L, returnedCustomer2.getId());
        assertEquals(secondCustomerName, returnedCustomer2.getName());
    }

    @Test
    void testSave() {
        Customer customer = new Customer();
        Long customerId = 100L;
        customer.setId(customerId);
        String customerName = "first";
        customer.setName(customerName);
        String customerEmail = "ava@example.com";
        customer.setEmail(customerEmail);
        int customerAge = 38;
        customer.setAge(customerAge);
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
        customer.setAccounts(Set.of(firstAccount, secondAccount));

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
        customer.setEmployers(Set.of(firstEmployer, secondEmployer));

        String customerPhoneNumber = "789654322";
        customer.setPhoneNumber(customerPhoneNumber);
        String customerPassword = "passworD75";
        customer.setPassword(customerPassword);

        List<Customer> customers = Arrays.asList(customer);
        Page<Customer> customerPage = new PageImpl<>(customers);

        Pageable pageable = PageRequest.of(0, 10);
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> returnedCustomers = customerService.findAll(pageable);

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.save(customer);

        assertNotNull(savedCustomer);
        assertEquals(100L, savedCustomer.getId());
        assertEquals(customerEmail, savedCustomer.getEmail());
        assertEquals(1, returnedCustomers.getTotalElements());
    }

    @Test
    void testSaveAll() {
        Customer firstCustomer = new Customer();
        Long firstCustomerId = 100L;
        firstCustomer.setId(firstCustomerId);
        firstCustomer.setName("first");
        firstCustomer.setEmail("ava@example.com");
        firstCustomer.setAge(38);
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

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
        firstEmployer.setName("first");
        firstEmployer.setAddress("firstAddress");

        Employer secondEmployer = new Employer();
        secondEmployer.setId(2000L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        firstCustomer.setEmployers(Set.of(firstEmployer, secondEmployer));

        Customer secondCustomer = new Customer();
        Long secondCustomerId = 200L;
        secondCustomer.setId(secondCustomerId);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

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
        thirdEmployer.setName("third");
        thirdEmployer.setAddress("thirdAddress");

        Employer fourthEmployer = new Employer();
        fourthEmployer.setId(4000L);
        fourthEmployer.setName("fourth");
        fourthEmployer.setAddress("fourthAddress");

        secondCustomer.setEmployers(Set.of(thirdEmployer, fourthEmployer));

        // List of customers to save
        List<Customer> customersToSave = Arrays.asList(firstCustomer, secondCustomer);

        // Mock saveAll to return the customers
        when(customerRepository.saveAll(customersToSave)).thenReturn(customersToSave);

        // Call saveAll on the service
        customerService.saveAll(customersToSave);

        // Assertions
        assertEquals(2, customersToSave.size());

        Customer savedCustomer1 = customersToSave.get(0);
        assertEquals(100L, savedCustomer1.getId());
        assertEquals("first", savedCustomer1.getName());

        Customer savedCustomer2 = customersToSave.get(1);
        assertEquals(200L, savedCustomer2.getId());
        assertEquals("second", savedCustomer2.getName());
    }

    @Test
    void testDelete() {
        Customer firstCustomer = new Customer();
        Long firstCustomerId = 100L;
        firstCustomer.setId(firstCustomerId);
        firstCustomer.setName("first");
        firstCustomer.setEmail("ava@example.com");
        firstCustomer.setAge(38);
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

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

        Customer secondCustomer = new Customer();
        Long secondCustomerId = 200L;
        secondCustomer.setId(secondCustomerId);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

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

        List<Customer> customers = new ArrayList<>(Arrays.asList(firstCustomer, secondCustomer));
        Page<Customer> customerPage = new PageImpl<>(customers);

        Pageable pageable = PageRequest.of(0, 10);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> returnedCustomers = customerService.findAll(pageable);
        assertEquals(2, returnedCustomers.getTotalElements());
        assertTrue(returnedCustomers.getContent().contains(firstCustomer));
        assertTrue(returnedCustomers.getContent().contains(secondCustomer));

        doAnswer(invocation -> {
            customers.remove(firstCustomer);
            return customers;
        }).when(customerRepository).delete(firstCustomer);

        customerService.delete(firstCustomer);

        Page<Customer> updatedCustomerPage = new PageImpl<>(customers);
        when(customerRepository.findAll(pageable)).thenReturn(updatedCustomerPage);

        Page<Customer> updatedCustomers = customerService.findAll(pageable);

        assertEquals(1, updatedCustomers.getTotalElements());
        assertFalse(updatedCustomers.getContent().contains(firstCustomer));
        assertTrue(updatedCustomers.getContent().contains(secondCustomer));
    }

    @Test
    void testDeleteAll() {
        Customer firstCustomer = new Customer();
        firstCustomer.setId(100L);
        firstCustomer.setName("first");
        firstCustomer.setEmail("ava@example.com");
        firstCustomer.setAge(38);
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

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

        Customer secondCustomer = new Customer();
        secondCustomer.setId(200L);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

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

        List<Customer> customers = new ArrayList<>(Arrays.asList(firstCustomer, secondCustomer));
        Page<Customer> customerPage = new PageImpl<>(customers);

        Pageable pageable = PageRequest.of(0, 10);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> returnedCustomers = customerService.findAll(pageable);
        assertEquals(2, returnedCustomers.getTotalElements());
        assertTrue(returnedCustomers.getContent().contains(firstCustomer));
        assertTrue(returnedCustomers.getContent().contains(secondCustomer));

        doAnswer(invocation -> {
            customers.clear();
            return customers;
        }).when(customerRepository).deleteAll(customers);

        customerService.deleteAll(customers);

        Page<Customer> updatedCustomerPage = new PageImpl<>(customers);
        when(customerRepository.findAll(pageable)).thenReturn(updatedCustomerPage);

        Page<Customer> updatedCustomers = customerService.findAll(pageable);

        assertEquals(0, updatedCustomers.getTotalElements());
        assertFalse(updatedCustomers.getContent().contains(firstCustomer));
        assertFalse(updatedCustomers.getContent().contains(secondCustomer));
    }

    @Test
    void testDeleteById() {
        Customer firstCustomer = new Customer();
        Long firstCustomerId = 100L;
        firstCustomer.setId(firstCustomerId);
        firstCustomer.setName("first");
        firstCustomer.setEmail("ava@example.com");
        firstCustomer.setAge(38);
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

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

        Customer secondCustomer = new Customer();
        Long secondCustomerId = 200L;
        secondCustomer.setId(secondCustomerId);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

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

        List<Customer> customers = new ArrayList<>(Arrays.asList(firstCustomer, secondCustomer));
        Page<Customer> customerPage = new PageImpl<>(customers);

        Pageable pageable = PageRequest.of(0, 10);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> returnedCustomers = customerService.findAll(pageable);
        assertEquals(2, returnedCustomers.getTotalElements());
        assertTrue(returnedCustomers.getContent().contains(firstCustomer));
        assertTrue(returnedCustomers.getContent().contains(secondCustomer));

        doAnswer(invocation -> {
            customers.removeIf(c -> c.getId().equals(firstCustomerId));
            return customers;
        }).when(customerRepository).deleteById(firstCustomerId);

        customerService.deleteById(firstCustomerId);

        Page<Customer> updatedCustomerPage = new PageImpl<>(customers);
        when(customerRepository.findAll(pageable)).thenReturn(updatedCustomerPage);

        Page<Customer> updatedCustomers = customerService.findAll(pageable);

        assertEquals(1, updatedCustomers.getTotalElements());
        assertFalse(updatedCustomers.getContent().contains(firstCustomer));
        assertTrue(updatedCustomers.getContent().contains(secondCustomer));
    }

    @Test
    void testGetById() {
        Customer firstCustomer = new Customer();
        Long firstCustomerId = 100L;
        firstCustomer.setId(firstCustomerId);
        String firstCustomerName = "first";
        firstCustomer.setName(firstCustomerName);
        String firstCustomerEmail = "ava@example.com";
        firstCustomer.setEmail(firstCustomerEmail);
        int firstCustomerAge = 38;
        firstCustomer.setAge(firstCustomerAge);
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

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

        Customer secondCustomer = new Customer();
        Long secondCustomerId = 200L;
        secondCustomer.setId(secondCustomerId);
        secondCustomer.setName("second");
        secondCustomer.setEmail("jane@example.com");
        secondCustomer.setAge(35);
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

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

        when(customerRepository.findById(100L)).thenReturn(Optional.of(firstCustomer));

        Customer returnedCustomer = customerService.getById(100L);
        assertNotNull(returnedCustomer);
        assertEquals(100L, returnedCustomer.getId());
        assertEquals(firstCustomerEmail, returnedCustomer.getEmail());
    }

    @Test
    void testGetByEmail() {
        Customer firstCustomer = new Customer();
        Long firstCustomerId = 100L;
        firstCustomer.setId(firstCustomerId);
        String firstCustomerName = "first";
        firstCustomer.setName(firstCustomerName);
        String firstCustomerEmail = "ava@example.com";
        firstCustomer.setEmail(firstCustomerEmail);
        int firstCustomerAge = 38;
        firstCustomer.setAge(firstCustomerAge);
        firstCustomer.setPhoneNumber("789654322");
        firstCustomer.setPassword("passworD75");

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

        Customer secondCustomer = new Customer();
        Long secondCustomerId = 200L;
        secondCustomer.setId(secondCustomerId);
        secondCustomer.setName("second");
        String secondCustomerEmail = "jane@example.com";
        secondCustomer.setEmail(secondCustomerEmail);
        secondCustomer.setAge(35);
        secondCustomer.setPhoneNumber("789654333");
        secondCustomer.setPassword("passworD55");

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

        when(customerRepository.getByEmail(firstCustomerEmail)).thenReturn(firstCustomer);

        Customer returnedCustomer = customerService.getByEmail(firstCustomerEmail);
        assertNotNull(returnedCustomer);
        assertEquals(firstCustomerEmail, returnedCustomer.getEmail());
        assertEquals(firstCustomerId, returnedCustomer.getId());
    }

    @Test
    void testUpdateCustomer() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(100L);
        existingCustomer.setName("Old Name");
        existingCustomer.setEmail("old.email@example.com");
        existingCustomer.setAge(30);
        existingCustomer.setPhoneNumber("123456789");
        existingCustomer.setPassword("oldPassword");
        existingCustomer.setLastModifiedDate(LocalDateTime.now().minusDays(1));

        Customer newCustomer = new Customer();
        newCustomer.setId(100L);
        newCustomer.setName("New Name");
        newCustomer.setEmail("new.email@example.com");
        newCustomer.setAge(35);
        newCustomer.setPhoneNumber("987654321");
        newCustomer.setPassword("newPassword");
        newCustomer.setLastModifiedDate(LocalDateTime.now());

        when(customerRepository.findById(existingCustomer.getId())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        Customer updatedCustomer = customerService.update(newCustomer);

        assertEquals("New Name", updatedCustomer.getName());
        assertEquals("new.email@example.com", updatedCustomer.getEmail());
        assertEquals(35, updatedCustomer.getAge());
        assertEquals("987654321", updatedCustomer.getPhoneNumber());
        assertEquals("newPassword", updatedCustomer.getPassword());
        assertEquals(newCustomer.getLastModifiedDate(), updatedCustomer.getLastModifiedDate());
    }

    @Test
    void testCreateAccount() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setAccounts(new HashSet<>());

        Account account = new Account();
        account.setId(1L);
        account.setNumber(UUID.randomUUID());
        account.setBalance(100.0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.createAccount(customerId, account);

        assert(customer.getAccounts().contains(account));
    }

    @Test
    void testDeleteAccount() {
        Long customerId = 1L;
        UUID accountNumber = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setId(customerId);
        Account accountToDelete = new Account();
        accountToDelete.setId(1L);
        accountToDelete.setNumber(accountNumber);
        Set<Account> accounts = new HashSet<>();
        accounts.add(accountToDelete);
        customer.setAccounts(accounts);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(accountRepository).delete(accountToDelete);
        assert(customer.getAccounts().contains(accountToDelete));

        customerService.deleteAccount(customerId, accountNumber);
        assert(!customer.getAccounts().contains(accountToDelete));
    }

    @Test
    void testAddEmployer() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmployers(new HashSet<>());

        Employer employer = new Employer();
        employer.setId(1L);
        employer.setName("Employer");
        employer.setAddress("Employer Address");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.addEmployer(customerId, employer);
        assert(customer.getEmployers().contains(employer));
    }

    @Test
    void testDeleteEmployerSuccess() {
        Long customerId = 1L;
        Long employerId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);
        Set<Employer> employers = new HashSet<>();
        Employer employerToDelete = new Employer();
        employerToDelete.setId(employerId);
        employerToDelete.setName("Employer");
        employerToDelete.setAddress("Employer Address");
        employers.add(employerToDelete);
        customer.setEmployers(employers);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(employerRepository.findById(employerId)).thenReturn(Optional.of(employerToDelete));
        when(employerRepository.save(any(Employer.class))).thenReturn(employerToDelete);
        assert(customer.getEmployers().contains(employerToDelete));

        customerService.deleteEmployer(customerId, employerId);
        assert(!customer.getEmployers().contains(employerToDelete));
    }
}
