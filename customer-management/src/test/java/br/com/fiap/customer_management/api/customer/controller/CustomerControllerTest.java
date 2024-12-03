package br.com.fiap.customer_management.api.customer.controller;

import br.com.fiap.customer_management.api.customer.domain.Customer;
import br.com.fiap.customer_management.api.customer.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.customer.usecase.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockBean
    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    @MockBean
    private UpdateCustomerUseCase updateCustomerUseCase;

    @MockBean
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @MockBean
    private FindAllCustomersUseCase findAllCustomersUseCase;

    private Customer customer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Ângelo Lima", "Rua Exemplo, 123", "(12) 98765-4321", "emailexemplo@hotmail.com");
    }

    @Test
    void shouldSaveCustomerSuccessfully() throws Exception {
        when(createCustomerUseCase.execute(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));

        verify(createCustomerUseCase).execute(any(Customer.class));
    }

    @Test
    void shouldUpdateCustomerSuccessfully() throws Exception {
        when(updateCustomerUseCase.execute(eq(customer.getId()), any(Customer.class))).thenReturn(customer);

        mockMvc.perform(put("/customer/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()));

        verify(updateCustomerUseCase).execute(eq(customer.getId()), any(Customer.class));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() throws Exception {
        mockMvc.perform(delete("/customer/{id}", customer.getId()))
                .andExpect(status().isNoContent());

        verify(deleteCustomerUseCase).execute(customer.getId());
    }

    @Test
    void shouldGetCustomerByIdSuccessfully() throws Exception {
        when(findCustomerByIdUseCase.execute(customer.getId())).thenReturn(customer);

        mockMvc.perform(get("/customer/{id}", customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));

        verify(findCustomerByIdUseCase).execute(customer.getId());
    }

    @Test
    void shouldListAllCustomersSuccessfully() throws Exception {
        when(findAllCustomersUseCase.execute()).thenReturn(Arrays.asList(customer));

        mockMvc.perform(get("/customer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(customer.getId()))
                .andExpect(jsonPath("$[0].name").value(customer.getName()));

        verify(findAllCustomersUseCase).execute();
    }

    @Test
    void shouldReturnCustomerExists() throws Exception {
        when(findCustomerByIdUseCase.execute(customer.getId())).thenReturn(customer);

        mockMvc.perform(get("/customer/{id}/exists", customer.getId()))
                .andExpect(status().isOk());

        verify(findCustomerByIdUseCase).execute(customer.getId());
    }

    @Test
    void shouldReturnCustomerNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Customer not found"))
                .when(findCustomerByIdUseCase).execute(customer.getId());

        mockMvc.perform(get("/customer/{id}/exists", customer.getId()))
                .andExpect(status().isNotFound());
    }
}
