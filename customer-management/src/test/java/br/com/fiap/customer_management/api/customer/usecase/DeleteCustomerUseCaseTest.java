package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteCustomerUseCaseTest {

    @InjectMocks
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Mock
    private CustomerGateway customerGateway;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        Long customerId = 1L;
        when(customerGateway.existsById(customerId)).thenReturn(true);

        deleteCustomerUseCase.execute(customerId);

        verify(customerGateway).existsById(customerId);
        verify(customerGateway).deleteById(customerId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerDoesNotExist() {
        Long customerId = 999L;
        when(customerGateway.existsById(customerId)).thenReturn(false);

        assertThatThrownBy(() -> deleteCustomerUseCase.execute(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente com o id " + customerId + " não encontrado.");
        verify(customerGateway).existsById(customerId);
        verify(customerGateway, never()).deleteById(anyLong());
    }
}