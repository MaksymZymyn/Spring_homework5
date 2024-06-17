package homework5.service;

import homework5.dao.CustomerRepository;
import homework5.dao.EmployerRepository;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DefaultEmployerService implements EmployerService {

    private final EmployerRepository employerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Employer save(Employer employer) {
        Employer existingEmployer = employerRepository.getByAddress(employer.getAddress());
        if (existingEmployer != null) {
            throw new SameEmployerException("Employer with address " + employer.getAddress() + " already exists");
        }
        employerRepository.save(employer);
        return employer;
    }

    @Override
    public void delete(Employer employer) {
        employerRepository.delete(employer);
    }

    @Override
    public void deleteAll(List<Employer> employers) {
        employerRepository.deleteAll(employers);
    }

    @Override
    public void saveAll(List<Employer> employers) {
        employerRepository.saveAll(employers);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = NoSuchElementException.class, timeout = 1000)
    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        employerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Employer getById(Long id) {
        return employerRepository.findById(id)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with ID " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Employer getByAddress(String address) {
        return employerRepository.getByAddress(address);
    }

    @Override
    public Employer update(Employer employer) {
        Employer existingEmployer = employerRepository.findById(employer.getId())
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with ID " + employer.getId()));
        updateEmployerData(existingEmployer, employer);
        Employer updatedEmployer = employerRepository.save(existingEmployer);
        return updatedEmployer;
    }

    private void updateEmployerData(Employer existingEmployer, Employer newEmployer) {
        if (existingEmployer.getName().equals(newEmployer.getName()) &&
                existingEmployer.getAddress().equals(newEmployer.getAddress())) {
            throw new SameEmployerException("The provided employer data is identical to the existing data.");
        }

        if (employerRepository.getByAddress(newEmployer.getAddress()) != null) {
            throw new SameEmployerException("This address of the employer is already exists.");
        }

        existingEmployer.setName(newEmployer.getName());
        existingEmployer.setAddress(newEmployer.getAddress());
        existingEmployer.setLastModifiedDate(newEmployer.getLastModifiedDate());
    }

    @Override
    public void addCustomer(Long employerId, Customer customer) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with id " + employerId));

        if (customer == null) {
            throw new CustomerNotFoundException("Customer cannot be null");
        }

        // Перевіряємо, чи існує клієнт з таким email
        Customer existingCustomer = customerRepository.getByEmail(customer.getEmail());
        if (existingCustomer != null) {
            // Оновлюємо інформацію про існуючого клієнта
            existingCustomer.setAge(customer.getAge());
            existingCustomer.setName(customer.getName());
            customer = existingCustomer;
        } else {
            // Додаємо нового клієнта
            customerRepository.save(customer);
        }

        if (customerRepository.findByEmployerId(employerId).contains(customer)) {
            log.info("Customer {} is already associated with employer {}", customer.getId(), employerId);
            throw new SameCustomerException("Customer " + customer.getId() + " is already associated with employer " + employerId);
        }

        employer.getCustomers().add(customer);
        customer.getEmployers().add(employer);
        employerRepository.save(employer);
        log.info("Customer {} added to employer {}", customer.getId(), employerId);
    }

    @Override
    public void deleteCustomer(Long employerId, Long customerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with id " + employerId));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + customerId));

        if (!employer.getCustomers().contains(customer)) {
            log.info("Customer {} is not associated with employer {}", customerId, employerId);
            throw new CustomerForEmployerNotFoundException("Customer " + customerId + " is not associated with employer " + employerId);
        }

        employer.getCustomers().remove(customer);
        customer.getEmployers().remove(employer);
        employerRepository.save(employer);
        customerRepository.save(customer);
        log.info("Customer {} removed from employer {}", customerId, employerId);
    }
}
