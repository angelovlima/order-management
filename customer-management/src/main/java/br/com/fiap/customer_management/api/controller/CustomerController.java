package br.com.fiap.customer_management.api.controller;

import br.com.fiap.customer_management.api.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.model.dto.CustomerDTO;
import br.com.fiap.customer_management.api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(
            summary = "Registrar Cliente",
            description = "Registra um novo cliente no sistema com suas informações pessoais.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class),
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
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO newCustomer = customerService.save(customerDTO);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Cliente",
            description = "Atualiza as informações de um cliente existente no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class),
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
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDTO customerDTO
    ) {
        CustomerDTO updatedCustomer = customerService.update(id, customerDTO);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Cliente",
            description = "Remove um cliente existente do sistema com base no ID fornecido."
    )
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Cliente por ID",
            description = "Retorna as informações de um cliente com base no ID fornecido."
    )
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    @Operation(
            summary = "Listar Todos os Clientes",
            description = "Retorna uma lista com todos os clientes cadastrados no sistema."
    )
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> checkCustomerExists(@PathVariable Long id) {
        try {
            customerService.findById(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
