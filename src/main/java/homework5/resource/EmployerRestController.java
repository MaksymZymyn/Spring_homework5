package homework5.resource;

import homework5.domain.bank.Customer;
import homework5.domain.bank.Employer;
import homework5.domain.dto.customer.CustomerDtoRequest;
import homework5.domain.dto.customer.CustomerDtoResponse;
import homework5.domain.dto.employer.EmployerDtoRequest;
import homework5.domain.dto.employer.EmployerDtoResponse;
import homework5.exceptions.*;
import homework5.mapper.customer.CustomerDtoMapperRequest;
import homework5.mapper.customer.CustomerDtoMapperResponse;
import homework5.mapper.employer.EmployerDtoMapperRequest;
import homework5.mapper.employer.EmployerDtoMapperResponse;
import homework5.service.CustomerService;
import homework5.service.EmployerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employers")         /* http://localhost:9000/employers */
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class EmployerRestController {

    private final EmployerService employerService;
    private final CustomerService customerService;
    private final EmployerDtoMapperRequest employerDtoMapperRequest;
    private final EmployerDtoMapperResponse employerDtoMapperResponse;
    private final CustomerDtoMapperRequest customerDtoMapperRequest;
    private final CustomerDtoMapperResponse customerDtoMapperResponse;

    @Operation(summary = "Get all employers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all employers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployerDtoResponse.class))})
    })
    @GetMapping
    public List<EmployerDtoResponse> getAll() {
        return employerService.findAll().stream().map(employerDtoMapperResponse::convertToDto).toList();
    }

    @Operation(summary = "Get an employer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the employer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployerDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employer not found",
                    content = @Content)
    })
    @GetMapping("/{employerId}")
    public ResponseEntity<?> getById(@PathVariable Long employerId) {
        try {
            return ResponseEntity.ok(employerDtoMapperResponse.convertToDto(employerService.getById(employerId)));
        } catch (EmployerNotFoundException e) {
            log.error("Employer with ID " + employerId + " not found", e);
            return ResponseEntity.badRequest().body("Employer with ID " + employerId + " not found");
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving employer with ID {}", employerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Create a new employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employer created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployerDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid employer data supplied",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EmployerDtoRequest employerDtoRequest) {
        Employer employer = employerDtoMapperRequest.convertToEntity(employerDtoRequest);
        try {
            employerService.save(employer);
            return ResponseEntity.ok(employerDtoMapperResponse.convertToDto(employer));
        } catch (SameEmployerException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update an employer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employer updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployerDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employer not found",
                    content = @Content)
    })
    @PutMapping("/id/{employerId}")
    public ResponseEntity<?> update(@PathVariable Long employerId,
                                    @Valid @RequestBody EmployerDtoRequest employerDtoRequest) {
        try {
            Employer updatedEmployer = employerDtoMapperRequest.convertToEntity(employerDtoRequest);
            updatedEmployer.setId(employerId);

            Employer updatedEntity = employerService.update(updatedEmployer);
            log.info("Updated employer");
            return ResponseEntity.ok(employerDtoMapperResponse.convertToDto(updatedEntity));
        } catch (EmployerNotFoundException e) {
            log.error("Employer not found with ID " + employerId, e);
            return ResponseEntity.badRequest().body("Employer with id " + employerId + " not found");
        } catch (SameEmployerException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while updating the employer with id " + employerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the employer");
        }
    }

    @Operation(summary = "Delete an employer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employer deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employer not found",
                    content = @Content)
    })
    @DeleteMapping("/deleting/{employerId}")
    public ResponseEntity<?> deleteById(@PathVariable Long employerId) {
        try {
            Employer employer =employerService.getById(employerId);
            employerService.deleteById(employer.getId());
            return ResponseEntity.ok().body("Employer with ID " + employerId + " deleted");
        } catch (EmployerNotFoundException e) {
            log.error("Employer not found with ID " + employerId, e);
            return ResponseEntity.badRequest().body("Employer with id " + employerId + " not found");
        } catch (Exception e) {
            log.error("An error occurred while deleting the employer with id " + employerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the employer");
        }
    }

    @Operation(summary = "Add a customer to an employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid customer data supplied",
                    content = @Content)
    })
    @PostMapping("/{employerId}/customers/{customerId}")
    public ResponseEntity<?> addCustomer(@PathVariable Long employerId,
                                         @Valid @RequestBody CustomerDtoRequest customerDtoRequest) {
        try {
            Customer customer = customerDtoMapperRequest.convertToEntity(customerDtoRequest);
            employerService.addCustomer(employerId, customer);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(customer));
        } catch (EmployerNotFoundException e) {
            log.error("Employer not found with ID " + employerId, e);
            return ResponseEntity.badRequest().body("Employer with id " + employerId + " not found");
        } catch (CustomerNotFoundException e) {
            log.error("Customer cannot be null", e);
            return ResponseEntity.badRequest().body("Customer id cannot be null");
        } catch (SameCustomerException e) {
            log.error("Customer is already associated with employer", e);
            return ResponseEntity.badRequest().body("Customer is already associated with employer");
        } catch (Exception e) {
            log.error("An error occurred while updating the employer with id " + employerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the employer");
        }
    }
    /*
    In Swagger:
    {
      "name": "string",
      "email": "string@example.com",
      "age": 18,
      "phoneNumber": "+1234567890",
      "password": "stringsT12"
    }
     */

    @Operation(summary = "Delete the employee from the bank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @DeleteMapping("/{employerId}/customers/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long employerId, @PathVariable Long customerId) {
        try {
            employerService.deleteCustomer(employerId, customerId);
            return ResponseEntity.ok().build();
        } catch (EmployerNotFoundException e) {
            log.error("Employer not found with ID " + employerId, e);
            return ResponseEntity.badRequest().body("Employer with id " + employerId + " not found");
        } catch (CustomerNotFoundException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.badRequest().body("Customer with id " + customerId + " not found");
        } catch (CustomerForEmployerNotFoundException e) {
            log.error("Customer {} is not associated with employer {}", customerId, employerId);
            return ResponseEntity.badRequest().body("Customer " + customerId + " is not associated with employer " + employerId);
        } catch (Exception e) {
            log.error("An error occurred while updating the employer with id " + employerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
