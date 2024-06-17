package homework5.service;

import homework5.dao.CustomerRepository;
import homework5.dao.EmployerRepository;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultEmployerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private DefaultEmployerService employerService;

    @Test
    void testFindAll() {
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

        when(employerRepository.findAll()).thenReturn(employers);

        List<Employer> returnedEmployers = employerService.findAll();

        assertNotNull(returnedEmployers);
        assertEquals(2, returnedEmployers.size());

        Employer returnedEmployer1 = returnedEmployers.get(0);
        assertEquals(10L, returnedEmployer1.getId());
        assertEquals(firstEmployerName, returnedEmployer1.getName());
        assertEquals(firstEmployerAddress, returnedEmployer1.getAddress());

        Employer returnedAccount2 = returnedEmployers.get(1);
        assertEquals(20L, returnedAccount2.getId());
        assertEquals(secondEmployerName, returnedAccount2.getName());
        assertEquals(secondEmployerAddress, returnedAccount2.getAddress());
    }

    @Test
    void testSave() {
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
        employers.add(firstEmployer);

        when(employerRepository.save(firstEmployer)).thenReturn(firstEmployer);

        Employer savedEmployer = employerService.save(firstEmployer);

        assertNotNull(firstEmployer);
        assertEquals(10L, savedEmployer.getId());
        assertEquals(firstEmployerName, savedEmployer.getName());
        assertEquals(firstEmployerAddress, savedEmployer.getAddress());
    }

    @Test
    void testSaveAll() {
        List<Employer> employers = new ArrayList<>();

        Employer firstEmployer = new Employer();
        firstEmployer.setId(10L);
        firstEmployer.setName("first");
        firstEmployer.setAddress("firstAddress");

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerRepository.saveAll(employers)).thenReturn(employers);
        when(employerRepository.findAll()).thenReturn(employers);
        when(employerRepository.findById(20L)).thenReturn(Optional.of(secondEmployer));

        employerService.saveAll(employers);

        assertEquals(2, employerService.findAll().size());

        assertEquals(secondEmployer, employerService.getById(20L));
    }

    @Test
    void testDelete() {
        List<Employer> employers = new ArrayList<>();

        Employer firstEmployer = new Employer();
        firstEmployer.setId(10L);
        firstEmployer.setName("first");
        firstEmployer.setAddress("firstAddress");

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerRepository.findAll()).thenReturn(employers);

        List<Employer> returnedAccounts = employerService.findAll();
        assertNotNull(returnedAccounts);
        assertEquals(2, returnedAccounts.size());

        doAnswer(invocation -> {
            employers.remove(firstEmployer);
            return employers;
        }).when(employerRepository).delete(firstEmployer);

        employerService.delete(firstEmployer);

        List<Employer> updatedEmployers = employerService.findAll();

        assertEquals(1, updatedEmployers.size());

        assertFalse(updatedEmployers.contains(firstEmployer));
        assertTrue(updatedEmployers.contains(secondEmployer));
    }

    @Test
    void testDeleteAll() {
        List<Employer> employers = new ArrayList<>();

        Employer firstEmployer = new Employer();
        firstEmployer.setId(10L);
        firstEmployer.setName("first");
        firstEmployer.setAddress("firstAddress");

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerRepository.saveAll(employers)).thenReturn(employers);
        when(employerRepository.findAll()).thenReturn(employers);

        employerService.saveAll(employers);

        assertEquals(2, employerService.findAll().size());

        doAnswer(invocation -> {
            employers.clear();
            return employers;
        }).when(employerRepository).deleteAll(employers);

        employerService.deleteAll(employers);

        List<Employer> updatedEmployers = employerService.findAll();

        assertEquals(0, updatedEmployers.size());
        assertFalse(updatedEmployers.contains(firstEmployer));
        assertFalse(updatedEmployers.contains(secondEmployer));
    }

    @Test
    void testDeleteById() {
        Long firstEmployerId = 10L;
        Employer firstEmployer = new Employer();
        firstEmployer.setId(firstEmployerId);
        firstEmployer.setName("Test Employer");
        firstEmployer.setAddress("Test Address");

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        List<Employer> employers = new ArrayList<>();
        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerRepository.findAll()).thenReturn(employers);
        List<Employer> returnedEmployers = employerService.findAll();
        assertNotNull(returnedEmployers);
        assertEquals(2, returnedEmployers.size());
        assertTrue(returnedEmployers.contains(firstEmployer));
        assertTrue(returnedEmployers.contains(secondEmployer));

        doAnswer(invocation -> {
            employers.removeIf(e -> e.getId().equals(firstEmployerId));
            return employers;
        }).when(employerRepository).deleteById(firstEmployerId);

        employerService.deleteById(firstEmployerId);

        List<Employer> updatedEmployers = employerService.findAll();
        assertFalse(updatedEmployers.contains(firstEmployer));
        assertTrue(updatedEmployers.contains(secondEmployer));
        assertEquals(1, updatedEmployers.size());
    }

    @Test
    void testGetById() {
        Long firstEmployerId = 10L;
        Employer firstEmployer = new Employer();
        firstEmployer.setId(firstEmployerId);
        String firstEmployerName = "Test Employer";
        firstEmployer.setName(firstEmployerName);
        String firstEmployerAddress = "Test Address";
        firstEmployer.setAddress(firstEmployerAddress);

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        List<Employer> employers = new ArrayList<>();
        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerRepository.findById(firstEmployerId)).thenReturn(Optional.of(firstEmployer));

        Employer returnedEmployer = employerService.getById(firstEmployerId);
        assertNotNull(returnedEmployer);
        assertEquals(10L, returnedEmployer.getId());
        assertEquals(firstEmployerName, returnedEmployer.getName());
        assertEquals(firstEmployerAddress, returnedEmployer.getAddress());
    }

    @Test
    void testGetByAddress() {
        Long firstEmployerId = 10L;
        Employer firstEmployer = new Employer();
        firstEmployer.setId(firstEmployerId);
        String firstEmployerName = "Test Employer";
        firstEmployer.setName(firstEmployerName);
        String firstEmployerAddress = "Test Address";
        firstEmployer.setAddress(firstEmployerAddress);

        Employer secondEmployer = new Employer();
        secondEmployer.setId(20L);
        secondEmployer.setName("second");
        secondEmployer.setAddress("secondAddress");

        List<Employer> employers = new ArrayList<>();
        employers.add(firstEmployer);
        employers.add(secondEmployer);

        when(employerRepository.getByAddress(firstEmployerAddress)).thenReturn(firstEmployer);

        Employer returnedEmployer = employerService.getByAddress(firstEmployerAddress);
        assertNotNull(returnedEmployer);
        assertEquals(firstEmployerAddress, returnedEmployer.getAddress());
        assertEquals(10L, returnedEmployer.getId());
        assertEquals(firstEmployerName, returnedEmployer.getName());
    }

    @Test
    void testUpdate() {
        Long employerId = 1L;
        String originalName = "Original Employer";
        String updatedName = "Updated Employer";
        String originalAddress = "Original Address";
        String updatedAddress = "Updated Address";
        LocalDateTime originalTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now();

        Employer existingEmployer = new Employer();
        existingEmployer.setId(employerId);
        existingEmployer.setName(originalName);
        existingEmployer.setAddress(originalAddress);
        existingEmployer.setLastModifiedDate(originalTime);

        Employer newEmployer = new Employer();
        newEmployer.setId(employerId);
        newEmployer.setName(updatedName);
        newEmployer.setAddress(updatedAddress);
        newEmployer.setLastModifiedDate(updatedTime);

        when(employerRepository.findById(employerId)).thenReturn(Optional.of(existingEmployer));
        when(employerRepository.save(any(Employer.class))).thenReturn(existingEmployer);

        Employer updatedEmployer = employerService.update(newEmployer);

        assertEquals(updatedName, updatedEmployer.getName());
        assertEquals(updatedAddress, updatedEmployer.getAddress());
        assertEquals(newEmployer.getLastModifiedDate(), updatedEmployer.getLastModifiedDate());
    }

    @Test
    void testAddCustomer() {
        Long employerId = 1L;
        Employer employer = new Employer();
        employer.setId(employerId);
        employer.setName("Employer");

        Customer customerToAdd = new Customer();
        customerToAdd.setId(10L);
        customerToAdd.setName("Customer");
        customerToAdd.setEmail("customer@example.com");
        customerToAdd.setAge(30);

        when(employerRepository.findById(employerId)).thenReturn(Optional.of(employer));
        when(customerRepository.getByEmail(customerToAdd.getEmail())).thenReturn(null);
        when(customerRepository.save(customerToAdd)).thenReturn(customerToAdd);
        when(customerRepository.findByEmployerId(employerId)).thenReturn(new ArrayList<>());

        employerService.addCustomer(employerId, customerToAdd);

        assertTrue(employer.getCustomers().contains(customerToAdd));
        assertTrue(customerToAdd.getEmployers().contains(employer));
    }

    @Test
    void testDeleteCustomer() {
        Long employerId = 1L;
        Long customerId = 10L;

        Employer employer = new Employer();
        employer.setId(employerId);
        employer.setName("Employer");

        Customer customerToDelete = new Customer();
        customerToDelete.setId(customerId);
        customerToDelete.setName("Customer");
        customerToDelete.setEmail("customer@example.com");

        employer.getCustomers().add(customerToDelete);
        customerToDelete.getEmployers().add(employer);

        when(employerRepository.findById(employerId)).thenReturn(Optional.of(employer));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerToDelete));

        employerService.deleteCustomer(employerId, customerId);

        assertFalse(employer.getCustomers().contains(customerToDelete));
        assertFalse(customerToDelete.getEmployers().contains(employer));
    }
}
