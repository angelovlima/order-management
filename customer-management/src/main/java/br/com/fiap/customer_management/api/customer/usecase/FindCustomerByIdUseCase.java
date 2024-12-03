package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindCustomerByIdUseCase {

    private final CustomerGateway customerGateway;

    public Customer execute(Long id) {
        return customerGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com o id " + id + " não encontrado."));
    }
}
