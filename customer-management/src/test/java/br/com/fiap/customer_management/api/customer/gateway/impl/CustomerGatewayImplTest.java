package br.com.fiap.customer_management.api.customer.gateway.impl;

import br.com.fiap.customer_management.api.config.db.entity.CustomerEntity;
import br.com.fiap.customer_management.api.config.db.repository.CustomerRepository;
import br.com.fiap.customer_management.api.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerGatewayImplTest {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerGatewayImpl customerGateway;

    @BeforeEach
    void setUp() {
        customerGateway = new CustomerGatewayImpl(customerRepository);
    }

    @Test
    void shouldSaveCustomerSuccessfully() {
        Customer customer = new Customer(null, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com");

        Customer savedCustomer = customerGateway.save(customer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(savedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void shouldFindCustomerByIdSuccessfully() {
        CustomerEntity entity = new CustomerEntity(null, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com");
        CustomerEntity savedEntity = customerRepository.save(entity);

        Optional<Customer> customerOptional = customerGateway.findById(savedEntity.getId());

        assertThat(customerOptional).isPresent();
        Customer customer = customerOptional.get();
        assertThat(customer.getId()).isEqualTo(savedEntity.getId());
        assertThat(customer.getName()).isEqualTo(savedEntity.getName());
    }

    @Test
    void shouldFindAllCustomersSuccessfully() {
        customerRepository.save(new CustomerEntity(null, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com"));
        customerRepository.save(new CustomerEntity(null, "Giovanna Esposito", "Rua Nova, 456", "(11) 91234-5678", "giovanna_esposito@gmail.com"));

        List<Customer> customers = customerGateway.findAll();

        assertThat(customers).hasSize(2);
        assertThat(customers).extracting(Customer::getName).containsExactlyInAnyOrder("Ângelo Lima", "Giovanna Esposito");
    }

    @Test
    void shouldReturnTrueWhenCustomerExistsByEmail() {
        customerRepository.save(new CustomerEntity(null, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com"));

        boolean exists = customerGateway.existsByEmail("emailexemplo@hotmail.com");

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExistByEmail() {
        boolean exists = customerGateway.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void shouldDeleteCustomerByIdSuccessfully() {
        CustomerEntity entity = new CustomerEntity(null, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com");
        CustomerEntity savedEntity = customerRepository.save(entity);

        customerGateway.deleteById(savedEntity.getId());

        Optional<CustomerEntity> deletedEntity = customerRepository.findById(savedEntity.getId());
        assertThat(deletedEntity).isEmpty();
    }
}
