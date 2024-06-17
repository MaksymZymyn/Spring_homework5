package homework5.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework5.domain.bank.Account;
import homework5.domain.dto.account.AccountDtoResponse;
import homework5.exceptions.*;
import homework5.mapper.account.AccountDtoMapperResponse;
import homework5.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")         /* http://localhost:9000/accounts */
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AccountRestController {

    private final AccountService accountService;
    private final AccountDtoMapperResponse accountDtoMapperResponse;

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))})
    })
    @GetMapping
    public List<AccountDtoResponse> getAll() {
        log.info("Request received to fetch all accounts.");
        List<Account> accounts = accountService.findAll();
        List<AccountDtoResponse> dtos = accounts.stream()
                .map(accountDtoMapperResponse::convertToDto)
                .toList();
        log.info("Returning {} accounts.", dtos.size());
        return dtos;
    }

    @Operation(summary = "Get an account by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content)
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getByNumber(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getByNumber(accountNumber);
            AccountDtoResponse accountDto = accountDtoMapperResponse.convertToDto(account);
            log.info("Account with number {} found and returned.", accountNumber);
            return ResponseEntity.ok(accountDto);
        } catch (IllegalArgumentException e) {
            log.error("Invalid account number format: {}", accountNumber, e);
            return ResponseEntity.badRequest().body("Invalid account number format: " + accountNumber);
        } catch (AccountNotFoundException e) {
            log.error("Account with number {} not found", accountNumber, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with number " + accountNumber + " not found");
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving account with number {}", accountNumber, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Deposit an amount to an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody String parametersJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNodeAccountNumber = mapper.readTree(parametersJson);
        String accountNumber = nameNodeAccountNumber.get("accountNumber").asText();
        Double amount = Double.parseDouble(nameNodeAccountNumber.get("amount").asText());
        log.info("Deposit request received for account number: {}, amount: {}", accountNumber, amount);

        try {
            if (amount <= 0) {
                log.error("Amount for deposit must be greater than 0");
                return ResponseEntity.badRequest().body("Amount for deposit must be greater than 0");
            }
            log.info("Operation with account: Deposit");

            Account updatedAccount = accountService.deposit(accountNumber, amount);
            AccountDtoResponse dto = accountDtoMapperResponse.convertToDto(updatedAccount);
            log.info("Amount {} deposited successfully into account {}", amount, accountNumber);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            log.error("Invalid account number format: {}", accountNumber, e);
            return ResponseEntity.badRequest().body("Invalid account number format: " + accountNumber);
        } catch (AccountNotFoundException e) {
            log.error("Account with number {} not found", accountNumber, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with number " + accountNumber + " not found");
        } catch (Exception e) {
            log.error("Unexpected error during deposit", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /*
    {
        "accountNumber": "937819b2-7359-4730-bf28-df4ec227a063",
        "amount": "100.50"
    }
     */

    @Operation(summary = "Withdraw an amount from an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PutMapping("/withdrawal")
    public ResponseEntity<?> withdraw(@RequestBody String parametersJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNodeAccountNumber = mapper.readTree(parametersJson);
        String accountNumber = nameNodeAccountNumber.get("accountNumber").asText();
        Double amount = Double.parseDouble(nameNodeAccountNumber.get("amount").asText());
        log.info("Withdrawal request received for account number: {}, amount: {}", accountNumber, amount);

        try {
            if (amount <= 0) {
                log.error("Amount for withdrawal must be greater than 0");
                return ResponseEntity.badRequest().body(Map.of("message", "Amount for withdrawal must be greater than 0"));
            }
            Account updatedAccount = accountService.withdraw(accountNumber, amount);
            AccountDtoResponse accountDto = accountDtoMapperResponse.convertToDto(updatedAccount);
            log.info("Amount {} withdrawn successfully from account {}", amount, accountNumber);
            return ResponseEntity.ok(accountDto);
        } catch (IllegalArgumentException e) {
            log.error("Invalid account number format: {}", accountNumber, e);
            return ResponseEntity.badRequest().body("Invalid account number format: " + accountNumber);
        } catch (AccountNotFoundException e) {
            log.error("Account with number {} not found", accountNumber, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with number " + accountNumber + " not found");
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during withdrawal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Transfer an amount from one account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PutMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody String parametersJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNodeAccountNumber = mapper.readTree(parametersJson);
        String fromAccountNumber = nameNodeAccountNumber.get("accountNumberSender").asText();
        String toAccountNumber = nameNodeAccountNumber.get("accountNumberReceiver").asText();
        Double amount = Double.parseDouble(nameNodeAccountNumber.get("amount").asText());
        log.info("Transfer request received from account: {} to account: {}, amount: {}", fromAccountNumber, toAccountNumber, amount);

        try {
            if (amount <= 0) {
                throw new InvalidTransferAmountException("Transfer amount must be greater than 0");
            }

            accountService.transfer(fromAccountNumber, toAccountNumber, amount);

            double fromAccountBalance = accountService.getByNumber(fromAccountNumber).getBalance();
            double toAccountBalance = accountService.getByNumber(toAccountNumber).getBalance();

            log.info("Transfer of amount {} from account {} to account {} completed successfully", amount, fromAccountNumber, toAccountNumber);

            Map<String, Double> balances = new HashMap<>();
            balances.put("fromAccountBalance", fromAccountBalance);
            balances.put("toAccountBalance", toAccountBalance);
            return ResponseEntity.ok(balances);
        } catch (IllegalArgumentException e) {
            log.error("Invalid account number format", e);
            return ResponseEntity.badRequest().body("Invalid account number format");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SameAccountException | InvalidTransferAmountException | InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error transferring amount: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /*
    {
        "accountNumberSender": "123e4567-e89b-12d3-a456-426614174001",
        "accountNumberReceiver": "123e4567-e89b-12d3-a456-426614174002",
        "amount": 150.75
    }
     */

}
