package br.com.fiap.customer_management.api.customer.gateway.impl;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import br.com.fiap.customer_management.api.config.db.entity.CustomerEntity;
import br.com.fiap.customer_management.api.config.db.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerGatewayImpl implements CustomerGateway {

    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity savedEntity = customerRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(customer.getId(), customer.getName(), customer.getAddress(), customer.getPhone(), customer.getEmail());
    }

    private Customer toDomain(CustomerEntity entity) {
        return new Customer(entity.getId(), entity.getName(), entity.getAddress(), entity.getPhone(), entity.getEmail());
    }
}
