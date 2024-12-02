package br.com.fiap.customer_management.api.service;

import br.com.fiap.customer_management.api.exception.DuplicateResourceException;
import br.com.fiap.customer_management.api.exception.ResourceNotFoundException;
import br.com.fiap.customer_management.api.model.dto.CustomerDTO;
import br.com.fiap.customer_management.api.model.entity.Customer;
import br.com.fiap.customer_management.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.email())) {
            throw new DuplicateResourceException("Usuário com o email " + customerDTO.email() + " já existe.");
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.name());
        customer.setAddress(customerDTO.address());
        customer.setPhone(customerDTO.phone());
        customer.setEmail(customerDTO.email());
        Customer newCustomer = customerRepository.save(customer);
        return toDto(newCustomer);
    }

    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com o id " + id + " não encontrado."));
        return toDto(customer);
    }

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com o id " + id + " não encontrado."));

        if (!existingCustomer.getEmail().equals(customerDTO.email()) &&
                customerRepository.existsByEmail(customerDTO.email())) {
            throw new DuplicateResourceException("Usuário com o email " + customerDTO.email() + " já existe.");
        }

        existingCustomer.setName(customerDTO.name());
        existingCustomer.setAddress(customerDTO.address());
        existingCustomer.setPhone(customerDTO.phone());
        existingCustomer.setEmail(customerDTO.email());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return toDto(updatedCustomer);
    }

    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente com o id " + id + " não encontrado.");
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO toDto(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getEmail()
        );
    }
}
