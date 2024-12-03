package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FindAllCustomersUseCaseTest {

    @InjectMocks
    private FindAllCustomersUseCase findAllCustomersUseCase;

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
    void shouldReturnAllCustomers() {
        List<Customer> expectedCustomers = Arrays.asList(
                new Customer(1L, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com"),
                new Customer(2L, "Giovanna Esposito", "Rua Nova, 456", "(11) 91234-5678", "giovanna_esposito@gmail.com")
        );
        when(customerGateway.findAll()).thenReturn(expectedCustomers);

        List<Customer> result = findAllCustomersUseCase.execute();

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(expectedCustomers.size())
                .containsAll(expectedCustomers);

        verify(customerGateway).findAll();
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomersExist() {
        when(customerGateway.findAll()).thenReturn(List.of());

        List<Customer> result = findAllCustomersUseCase.execute();

        assertThat(result).isNotNull().isEmpty();

        verify(customerGateway).findAll();
        verifyNoMoreInteractions(customerGateway);
    }
}
