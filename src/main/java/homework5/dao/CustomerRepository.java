package homework5.dao;

import homework5.domain.bank.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Override
    @EntityGraph("customerWithEmployersAndAccounts")
    Page<Customer> findAll(Pageable pageable);

    @Override
    @EntityGraph("customerWithEmployersAndAccounts")
    Optional<Customer> findById(Long id);

    @EntityGraph("customerWithEmployersAndAccounts")
    Customer getByEmail(String email);

    @EntityGraph("customerWithEmployersAndAccounts")
    @Query("SELECT c FROM Customer c JOIN c.employers e WHERE e.id = :employerId")
    List<Customer> findByEmployerId(Long employerId);

    @Override
    @EntityGraph("customerWithEmployersAndAccounts")
    void deleteById(Long id);
}
