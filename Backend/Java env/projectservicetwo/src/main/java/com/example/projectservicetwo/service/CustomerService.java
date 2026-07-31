package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.CustomerResponseDTO;
import com.example.projectservicetwo.dto.UserResponseDTO;
import com.example.projectservicetwo.entity.Customer;
import com.example.projectservicetwo.entity.User;
import com.example.projectservicetwo.repository.CustomerRepository;
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

    // Get all customers
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get customer by ID
    public CustomerResponseDTO getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToDTO(customer);
    }

    // Delete customer permanently
    public void deleteCustomer(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // Helper method to convert Customer entity to CustomerResponseDTO
    private CustomerResponseDTO mapToDTO(Customer customer) {
        UserResponseDTO userDTO = null;

        if (customer.getUser() != null) {
            User u = customer.getUser();
            userDTO = new UserResponseDTO(
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getPhone(),
                    u.getRole(),
                    u.getAddress(),
                    u.getStatus(),
                    u.getAdharCard(),
                    u.getCreatedAt()
            );
        }

        return new CustomerResponseDTO(
                customer.getCustomerId(),
                customer.getDrivingLicense(),
                userDTO
        );
    }
}