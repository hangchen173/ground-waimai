package com.restaurant.service;

import com.restaurant.dto.CustomerDTO;
import com.restaurant.entity.Customer;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_ShouldReturnList() {
        Customer c1 = new Customer(); c1.setId(1L); c1.setName("Alice");
        Customer c2 = new Customer(); c2.setId(2L); c2.setName("Bob");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void getCustomerById_WhenExists_ShouldReturnDTO() {
        Customer c = new Customer(); c.setId(1L); c.setName("Alice");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));

        CustomerDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCustomerById_WhenNotExists_ShouldThrowException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(99L));
    }

    @Test
    void createCustomer_ShouldSaveAndReturnDTO() {
        CustomerDTO dto = new CustomerDTO(); dto.setName("New User");
        Customer savedEntity = new Customer(); savedEntity.setId(10L); savedEntity.setName("New User");

        when(customerRepository.save(any(Customer.class))).thenReturn(savedEntity);

        CustomerDTO result = customerService.createCustomer(dto);

        assertEquals(10L, result.getId());
        assertEquals("New User", result.getName());
    }

    @Test
    void updateCustomer_WhenExists_ShouldUpdateFields() {
        Customer existing = new Customer(); existing.setId(1L); existing.setName("Old Name");
        CustomerDTO updateInfo = new CustomerDTO(); updateInfo.setName("New Name");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = customerService.updateCustomer(1L, updateInfo);

        assertEquals("New Name", result.getName());
    }

    @Test
    void deleteCustomer_WhenExists_ShouldDelete() {
        Customer c = new Customer(); c.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).delete(c);
    }
}