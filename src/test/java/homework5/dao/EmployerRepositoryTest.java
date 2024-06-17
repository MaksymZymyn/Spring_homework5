package homework5.dao;

import homework5.domain.bank.Employer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployerRepositoryTest {

    @Autowired
    private EmployerRepository employerRepository;

    @Test
    void testFindAll() {
        List<Employer> employers = employerRepository.findAll();
        assertEquals(3, employers.size());

        assertTrue(employers.stream().anyMatch(employer ->
                employer.getName().equals("Company A") &&
                        employer.getCustomers().size() == 7));
    }

    @Test
    void testFindById() {
        Optional<Employer> employerOpt = employerRepository.findById(1L);
        assertTrue(employerOpt.isPresent());
        Employer employer = employerOpt.get();

        assertEquals("Company A", employer.getName());
        assertEquals(7, employer.getCustomers().size());
    }

    @Test
    void testGetByAddress() {
        Employer employer = employerRepository.getByAddress("Address C");
        assertNotNull(employer);
        assertEquals("Company C", employer.getName());
        assertEquals(6, employer.getCustomers().size());
    }
}

