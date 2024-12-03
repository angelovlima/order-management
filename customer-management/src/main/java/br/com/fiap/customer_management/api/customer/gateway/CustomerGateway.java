package br.com.fiap.customer_management.api.customer.gateway;

import br.com.fiap.customer_management.api.customer.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerGateway {
    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    void deleteById(Long id);
}
