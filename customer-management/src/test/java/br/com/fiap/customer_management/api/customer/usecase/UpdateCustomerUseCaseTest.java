package br.com.fiap.customer_management.api.customer.usecase;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.DuplicateResourceException;
import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.gateway.CustomerGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateCustomerUseCaseTest {

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Mock
    private CustomerGateway customerGateway;

    private AutoCloseable openMocks;
    private Customer existingCustomer;
    private Customer updatedCustomer;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        existingCustomer = new Customer(1L, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com");
        updatedCustomer = new Customer(null, "Giovanna Esposito", "Rua Nova, 456", "(11) 91234-5678", "giovanna_esposito@gmail.com");
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        when(customerGateway.findById(existingCustomer.getId())).thenReturn(java.util.Optional.of(existingCustomer));
        when(customerGateway.existsByEmail(updatedCustomer.getEmail())).thenReturn(false);

        updateCustomerUseCase.execute(existingCustomer.getId(), updatedCustomer);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerGateway).save(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(existingCustomer.getId());
        assertThat(capturedCustomer.getName()).isEqualTo(updatedCustomer.getName());
        assertThat(capturedCustomer.getAddress()).isEqualTo(updatedCustomer.getAddress());
        assertThat(capturedCustomer.getPhone()).isEqualTo(updatedCustomer.getPhone());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updatedCustomer.getEmail());

        verify(customerGateway).findById(existingCustomer.getId());
        verify(customerGateway).existsByEmail(updatedCustomer.getEmail());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerDoesNotExist() {
        when(customerGateway.findById(existingCustomer.getId())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> updateCustomerUseCase.execute(existingCustomer.getId(), updatedCustomer))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente com o id " + existingCustomer.getId() + " não encontrado.");

        verify(customerGateway).findById(existingCustomer.getId());
        verify(customerGateway, never()).existsByEmail(anyString());
        verify(customerGateway, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenEmailAlreadyExists() {
        when(customerGateway.findById(existingCustomer.getId())).thenReturn(java.util.Optional.of(existingCustomer));
        when(customerGateway.existsByEmail(updatedCustomer.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> updateCustomerUseCase.execute(existingCustomer.getId(), updatedCustomer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Usuário com o email " + updatedCustomer.getEmail() + " já existe.");

        verify(customerGateway).findById(existingCustomer.getId());
        verify(customerGateway).existsByEmail(updatedCustomer.getEmail());
        verify(customerGateway, never()).save(any());
    }

    @Test
    void shouldAllowUpdatingWithSameEmail() {
        when(customerGateway.findById(existingCustomer.getId())).thenReturn(java.util.Optional.of(existingCustomer));
        when(customerGateway.existsByEmail(existingCustomer.getEmail())).thenReturn(false);

        updatedCustomer = new Customer(null, updatedCustomer.getName(), updatedCustomer.getAddress(), updatedCustomer.getPhone(), existingCustomer.getEmail());

        updateCustomerUseCase.execute(existingCustomer.getId(), updatedCustomer);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerGateway).save(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(existingCustomer.getEmail());
        verify(customerGateway).findById(existingCustomer.getId());
        verify(customerGateway, never()).existsByEmail(existingCustomer.getEmail());
    }
}
