package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.DuplicateResourceException;
import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public Customer execute(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com o id " + id + " não encontrado."));

        if (!existingCustomer.getEmail().equals(updatedCustomer.getEmail())
                && customerGateway.existsByEmail(updatedCustomer.getEmail())) {
            throw new DuplicateResourceException("Usuário com o email " + updatedCustomer.getEmail() + " já existe.");
        }

        existingCustomer.update(
                updatedCustomer.getName(),
                updatedCustomer.getAddress(),
                updatedCustomer.getPhone(),
                updatedCustomer.getEmail()
        );

        return customerGateway.save(existingCustomer);
    }
}
