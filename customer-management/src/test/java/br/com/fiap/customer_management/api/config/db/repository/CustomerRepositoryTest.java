package br.com.fiap.customer_management.api.config.db.repository;

import br.com.fiap.customer_management.api.config.db.entity.CustomerEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should return true when Customer exists by email Successfully")
    void existsByEmailSuccessfully() {
        String email = "emailexemplo@hotmail.com";
        CustomerEntity customerEntity = new CustomerEntity(null, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", email);
        this.createCustomer(customerEntity);

        boolean result = this.customerRepository.existsByEmail(email);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when Customer not exists by email")
    void existsByEmailFailed() {
        String email = "emailexemplo@hotmail.com";

        boolean result = this.customerRepository.existsByEmail(email);

        assertThat(result).isFalse();
    }

    private void createCustomer(CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
    }
}