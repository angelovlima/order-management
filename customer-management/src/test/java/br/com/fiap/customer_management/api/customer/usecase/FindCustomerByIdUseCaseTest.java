package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FindCustomerByIdUseCaseTest {

    @InjectMocks
    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    @Mock
    private CustomerGateway customerGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldReturnCustomerWhenIdExists() {
        Long customerId = 1L;
        Customer expectedCustomer = new Customer(1L, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com");
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        Customer result = findCustomerByIdUseCase.execute(customerId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expectedCustomer.getId());
        assertThat(result.getName()).isEqualTo(expectedCustomer.getName());
        assertThat(result.getEmail()).isEqualTo(expectedCustomer.getEmail());
        verify(customerGateway).findById(customerId);
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Long customerId = 1L;
        when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findCustomerByIdUseCase.execute(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente com o id " + customerId + " não encontrado.");
        verify(customerGateway).findById(customerId);
        verifyNoMoreInteractions(customerGateway);
    }
}
