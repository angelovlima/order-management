package br.com.fiap.customer_management.api.customer.controller;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final FindAllCustomersUseCase findAllCustomersUseCase;

    @PostMapping
    @Operation(
            summary = "Registrar Cliente",
            description = "Registra um novo cliente no sistema com suas informações pessoais.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de Registro de Cliente",
                                            summary = "Exemplo de dados para registrar um cliente",
                                            value = "{ \"name\": \"Ângelo Lima\", \"address\": \"Rua Exemplo, 123\", \"phone\": \"(12) 98765-4321\", \"email\": \"emailexemplo@hotmail.com\" }"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = createCustomerUseCase.execute(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Cliente",
            description = "Atualiza as informações de um cliente existente no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de Atualização de Cliente",
                                            summary = "Exemplo de dados para atualizar um cliente",
                                            value = "{ \"name\": \"Giovanna Esposito\", \"address\": \"Rua Nova, 456\", \"phone\": \"(11) 91234-5678\", \"email\": \"giovanna_esposito@gmail.com\" }"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer
    ) {
        Customer updatedCustomer = updateCustomerUseCase.execute(id, customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Cliente",
            description = "Remove um cliente existente do sistema com base no ID fornecido."
    )
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        deleteCustomerUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Cliente por ID",
            description = "Retorna as informações de um cliente com base no ID fornecido."
    )
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = findCustomerByIdUseCase.execute(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    @Operation(
            summary = "Listar Todos os Clientes",
            description = "Retorna uma lista com todos os clientes cadastrados no sistema."
    )
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = findAllCustomersUseCase.execute();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verifica se um cliente existe pelo ID")
    public ResponseEntity<Void> checkCustomerExists(@PathVariable Long id) {
        if (findCustomerByIdUseCase.execute(id) != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
