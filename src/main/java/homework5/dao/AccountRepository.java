package homework5.dao;

import homework5.domain.bank.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph("accountWithCustomerAndCustomerEmployers")
    List<Account> findAll();

    @EntityGraph("accountWithCustomerAndCustomerEmployers")
    Optional<Account> findById(Long id);

    @EntityGraph("accountWithCustomerAndCustomerEmployers")
    Optional<Account> findByNumber(UUID accountNumber);
}
