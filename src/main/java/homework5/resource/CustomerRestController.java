package homework5.resource;

import com.fasterxml.jackson.annotation.JsonView;
import homework5.domain.bank.Account;
import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.account.AccountDtoRequest;
import homework5.domain.dto.customer.CustomerDtoRequest;
import homework5.domain.dto.customer.CustomerDtoResponse;
import homework5.domain.dto.customer.Views;
import homework5.domain.dto.employer.EmployerDtoRequest;
import homework5.exceptions.*;
import homework5.mapper.account.AccountDtoMapperRequest;
import homework5.mapper.customer.CustomerDtoMapperRequest;
import homework5.mapper.customer.CustomerDtoMapperResponse;
import homework5.mapper.employer.EmployerDtoMapperRequest;
import homework5.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")          /* http://localhost:9000/customers */
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CustomerRestController {

    private final CustomerService customerService;
    private final CustomerDtoMapperRequest customerDtoMapperRequest;
    private final CustomerDtoMapperResponse customerDtoMapperResponse;
    private final EmployerDtoMapperRequest employerDtoMapperRequest;
    private final AccountDtoMapperRequest accountDtoMapperRequest;

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all customers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoMapperResponse.class))})
    })
    @JsonView(Views.Summary.class)
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            if (pageable.getPageSize() == Integer.MAX_VALUE) {
                pageable = PageRequest.of(pageable.getPageNumber(), 10);
            }

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

            List<CustomerDtoResponse> customers = customerService.findAll(pageable)
                    .stream()
                    .map(customerDtoMapperResponse::convertToDto)
                    .toList();

            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Get a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoMapperResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @JsonView(Views.Detail.class)
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId) {
        try {
            return ResponseEntity.ok(customerDtoMapperResponse
                    .convertToDto(customerService.getById(customerId)));
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving customer with ID {}", customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoMapperResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid customer data supplied",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CustomerDtoRequest customerDtoRequest) {
        Customer customer = customerDtoMapperRequest.convertToEntity(customerDtoRequest);
        try {
            customerService.save(customer);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(customer));
        } catch (SameCustomerException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoMapperResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @PutMapping("/{customerId}")
    public ResponseEntity<?> update(@PathVariable Long customerId,
                                    @Valid @RequestBody CustomerDtoRequest customerDtoRequest) {
        try {
            Customer updatedCustomer = customerDtoMapperRequest.convertToEntity(customerDtoRequest);
            updatedCustomer.setId(customerId);

            Customer updatedEntity = customerService.update(updatedCustomer);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(updatedEntity));
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID {}", customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (SameCustomerException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while updating the customer with ID {}", customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the customer");
        }
    }

    @Operation(summary = "Delete a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteById(@PathVariable Long customerId) {
        try {
            Customer customer = customerService.getById(customerId);
            customerService.deleteById(customer.getId());
            return ResponseEntity.ok().body("Customer with ID " + customerId + " deleted");
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (Exception e) {
            log.error("An error occurred while deleting the customer with id " + customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the customer");
        }
    }

    @Operation(summary = "Create an account for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoMapperResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @PostMapping("/{customerId}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable Long customerId,
                                           @Valid @RequestBody AccountDtoRequest accountDtoRequest) {
        try {
            Customer customer = customerService.getById(customerId);
            Account account = accountDtoMapperRequest.convertToEntity(accountDtoRequest);
            account.setNumber(UUID.randomUUID());
            Customer updatedCustomer = customerService.createAccount(customer.getId(), account);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(updatedCustomer));
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (AccountNotFoundException e) {
            log.error("Account cannot be null");
            return ResponseEntity.badRequest().body("Account cannot be null");
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while adding the account to the customer with id " + customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the account to the customer");
        }
    }

    @Operation(summary = "Delete an account from a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID or account ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account or customer not found",
                    content = @Content)
    })
    @DeleteMapping("/{customerId}/accounts/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long customerId,
                                           @PathVariable UUID accountNumber) {
        try {
            customerService.deleteAccount(customerId, accountNumber);
            return ResponseEntity.ok("Account successfully deleted");
        } catch (IllegalArgumentException e) {
            log.error("Invalid account number format: {}", accountNumber, e);
            return ResponseEntity.badRequest().body("Invalid account number format: " + accountNumber);
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (AccountNotFoundException e) {
            log.error("Account with number " + accountNumber + " not found for customer with id " + customerId);
            return ResponseEntity.badRequest().body("Account with number " + accountNumber + " not found for customer with id " + customerId);
        } catch (Exception e) {
            log.error("An error occurred while deleting the account of the customer with id " + customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the account of the customer");
        }
    }

    @Operation(summary = "Add an employer to a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employer added to customer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoMapperResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID or employer data supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @PostMapping("/{customerId}/employers")
    public ResponseEntity<?> addEmployer(@PathVariable Long customerId,
                                         @Valid @RequestBody EmployerDtoRequest employerDtoRequest) {
        try {
            Customer customer = customerService.getById(customerId);
            Employer employer = employerDtoMapperRequest.convertToEntity(employerDtoRequest);
            Customer updatedCustomer = customerService.addEmployer(customer.getId(), employer);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(updatedCustomer));
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (EmployerNotFoundException e) {
            log.error("Employer cannot be null");
            return ResponseEntity.badRequest().body("Employer cannot be null");
        } catch (SameEmployerException e) {
            log.error("Address of the employer already exists");
            throw new SameEmployerException("Address of the employer already exists");
        } catch (Exception e) {
            log.error("Error adding employer to customer with id: " + customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding employer to customer with id: " + customerId);
        }
    }

    @Operation(summary = "Delete an employer from a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employer deleted from customer",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID or employer ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer or employer not found",
                    content = @Content)
    })
    @DeleteMapping("/{customerId}/employers/{employerId}")
    public ResponseEntity<?> deleteEmployer(@PathVariable Long customerId,
                                            @PathVariable Long employerId) {
        try {
            Customer customer = customerService.getById(customerId);
            customerService.deleteEmployer(customerId, employerId);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(customer));
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with ID " + customerId + " not found");
        } catch (EmployerNotFoundException e) {
            log.error("Employer not found with ID " + employerId, e);
            return ResponseEntity.badRequest().body("Employer with ID " + employerId + " not found");
        } catch (EmployerForCustomerNotFoundException e) {
            log.error("Employer is not associated with customer");
            return ResponseEntity.badRequest().body("Employer is not associated with customer");
        } catch (Exception e) {
            log.error("Error deleting employer to customer with id: " + customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting employer to customer with id: " + customerId);
        }
    }
}
