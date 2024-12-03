package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase {

    private final CustomerGateway customerGateway;

    public void execute(Long id) {
        if (!customerGateway.existsById(id)) {
            throw new ResourceNotFoundException("Cliente com o id " + id + " não encontrado.");
        }
        customerGateway.deleteById(id);
    }
}