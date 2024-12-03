package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.DuplicateResourceException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public Customer execute(Customer customer) {
        if (customerGateway.existsByEmail(customer.getEmail())) {
            throw new DuplicateResourceException("Usuário com o email " + customer.getEmail() + " já existe.");
        }
        return customerGateway.save(customer);
    }
}
