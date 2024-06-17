package homework5.dao;

import homework5.domain.bank.Employer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

    @EntityGraph("employerWithCustomersAndAccountsAndOtherEmployers")
    List<Employer> findAll();

    @EntityGraph("employerWithCustomersAndAccountsAndOtherEmployers")
    Optional<Employer> findById(Long id);

    @EntityGraph("employerWithCustomersAndAccountsAndOtherEmployers")
    Employer getByAddress(String address);
}
