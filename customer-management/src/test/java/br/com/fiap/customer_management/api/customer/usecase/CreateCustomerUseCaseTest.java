package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.DuplicateResourceException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateCustomerUseCaseTest {

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    @Mock
    private CustomerGateway customerGateway;

    private Customer customer;

    AutoCloseable openMock;

    @BeforeEach
    void setUp() {
        openMock = MockitoAnnotations.openMocks(this);
        customer = new Customer(null, "Angelo Lima", "Rua Exemplo, 123", "(11) 91234-5678", "emailexemplo@hotmail.com");
    }

    @AfterEach
    void tearDown() throws Exception {
        openMock.close();
    }

    @Test
    void shouldSaveCustomerWhenEmailIsUnique() {
        when(customerGateway.existsByEmail(customer.getEmail())).thenReturn(false);
        when(customerGateway.save(customer)).thenReturn(new Customer(1L, customer.getName(), customer.getAddress(), customer.getPhone(), customer.getEmail()));

        Customer createdCustomer = createCustomerUseCase.execute(customer);

        assertThat(createdCustomer.getId()).isNotNull();
        assertThat(createdCustomer.getName()).isEqualTo(customer.getName());
        assertThat(createdCustomer.getEmail()).isEqualTo(customer.getEmail());

        verify(customerGateway).existsByEmail(customer.getEmail());
        verify(customerGateway).save(customer);
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldNotSaveCustomerWhenEmailAlreadyExists() {
        when(customerGateway.existsByEmail(customer.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> createCustomerUseCase.execute(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Usuário com o email " + customer.getEmail() + " já existe.");

        verify(customerGateway).existsByEmail(customer.getEmail());
        verify(customerGateway, never()).save(any());
        verifyNoMoreInteractions(customerGateway);
    }
}