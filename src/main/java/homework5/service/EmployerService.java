package homework5.service;

import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import java.util.List;

public interface EmployerService {

    Employer save(Employer employer);

    void delete(Employer employer);

    void deleteAll(List<Employer> employers);

    void saveAll(List<Employer> employers);

    List<Employer> findAll();

    void deleteById(Long id);

    Employer getById(Long id);

    Employer getByAddress(String address);

    Employer update(Employer employer);

    void addCustomer(Long employerId, Customer customer);

    void deleteCustomer(Long employerId, Long customerId);
}
