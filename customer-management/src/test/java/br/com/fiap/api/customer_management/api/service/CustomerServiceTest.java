package br.com.fiap.api.customer_management.api.service;

import br.com.fiap.customer_management.api.exception.DuplicateResourceException;
import br.com.fiap.customer_management.api.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.model.dto.CustomerDTO;
import br.com.fiap.customer_management.api.model.entity.Customer;
import br.com.fiap.customer_management.api.repository.CustomerRepository;
import br.com.fiap.customer_management.api.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final CustomerService customerService = new CustomerService(customerRepository);

    @Test
    void shouldUpdateCustomerSuccessfully() {
//        Customer existingCustomer = new Customer(1L, "John Doe", "123 Street", "123456789", "john@example.com");
//        Customer updatedCustomer = new Customer(1L, "Jane Doe", "456 Avenue", "987654321", "jane@example.com");
//
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
//        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(updatedCustomer);
//
//        CustomerDTO customerDTO = new CustomerDTO(1L, "Jane Doe", "456 Avenue", "987654321", "jane@example.com");
//        CustomerDTO result = customerService.update(1L, customerDTO);
//
//        assertEquals("Jane Doe", result.name());
//        assertEquals("456 Avenue", result.address());
//        assertEquals("987654321", result.phone());
//        assertEquals("jane@example.com", result.email());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
//        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        CustomerDTO customerDTO = new CustomerDTO(1L, "Jane Doe", "456 Avenue", "987654321", "jane@example.com");
//
//        assertThrows(ResourceNotFoundException.class, () -> customerService.update(1L, customerDTO));
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenEmailExists() {
//        Customer existingCustomer = new Customer(1L, "John Doe", "123 Street", "123456789", "john@example.com");
//
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
//        when(customerRepository.existsByEmail("jane@example.com")).thenReturn(true);
//
//        CustomerDTO customerDTO = new CustomerDTO(1L, "Jane Doe", "456 Avenue", "987654321", "jane@example.com");
//
//        assertThrows(DuplicateResourceException.class, () -> customerService.update(1L, customerDTO));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
//        Long id = 1L;
//        when(customerRepository.existsById(id)).thenReturn(true);
//
//        customerService.delete(id);
//
//        verify(customerRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerDoesNotExist() {
//        Long id = 1L;
//        when(customerRepository.existsById(id)).thenReturn(false);
//
//        assertThrows(ResourceNotFoundException.class, () -> customerService.delete(id));
//        verify(customerRepository, never()).deleteById(id);
    }

    @Test
    void shouldFindCustomerByIdSuccessfully() {
//        Customer customer = new Customer(1L, "John Doe", "123 Street", "123456789", "john@example.com");
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
//
//        CustomerDTO result = customerService.findById(1L);
//
//        assertEquals(1L, result.id());
//        assertEquals("John Doe", result.name());
//        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerDoesNotExist() {
//        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> customerService.findById(1L));
//        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindAllCustomersSuccessfully() {
//        List<Customer> customers = Arrays.asList(
//                new Customer(1L, "John Doe", "123 Street", "123456789", "john@example.com"),
//                new Customer(2L, "Jane Doe", "456 Avenue", "987654321", "jane@example.com")
//        );
//        when(customerRepository.findAll()).thenReturn(customers);
//
//        List<CustomerDTO> result = customerService.findAll();
//
//        assertEquals(2, result.size());
//        assertEquals("John Doe", result.get(0).name());
//        assertEquals("Jane Doe", result.get(1).name());
//        verify(customerRepository, times(1)).findAll();
    }
}
