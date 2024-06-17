package homework5.dao;

import homework5.TestSecurityConfig;
import homework5.domain.bank.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestSecurityConfig.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testFindAll() {
        Page<Customer> page = customerRepository.findAll(PageRequest.of(0, 10));
        List<Customer> customers = page.getContent();
        assertEquals(10, customers.size());
    }

    @Test
    void testFindById() {
        Optional<Customer> customer = customerRepository.findById(1L);
        assertTrue(customer.isPresent());
        assertEquals("John Doe", customer.get().getName());
    }

    @Test
    void testGetByEmail() {
        Customer customer = customerRepository.getByEmail("john@example.com");
        assertNotNull(customer);
        assertEquals("John Doe", customer.getName());
    }

    @Test
    void testFindByEmployerId() {
        List<Customer> customers = customerRepository.findByEmployerId(1L);
        assertEquals(7, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
    }

    @Test
    void testDeleteById() {
        customerRepository.deleteById(1L);
        Optional<Customer> customer = customerRepository.findById(1L);
        assertFalse(customer.isPresent());
    }
}

