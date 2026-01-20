package com.endtoend.testdemo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.entities.Customer;
import com.endtoend.testdemo.exception.CustomerNotFoundException;
import com.endtoend.testdemo.exception.EmailAlreadyExistException;
import com.endtoend.testdemo.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
  @Mock private CustomerRepository customerRepository;
  @Mock private CustomerMapper customerMapper;
  @InjectMocks private CustomerServiceImpl underTest;

  @Test
  void shouldSaveNewCustomer() {
    CustomerDTO customerDTO =
        CustomerDTO.builder()
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();
    Customer customer =
        Customer.builder().firstName("custm1").lastName("cust1").email("cust1@gmail.com").build();
    Customer savedCustomer =
        Customer.builder()
            .firstName("custm1")
            .id(1L)
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();
    CustomerDTO expected =
        CustomerDTO.builder()
            .firstName("custm1")
            .id(1L)
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    Mockito.when(customerRepository.findByEmail(customerDTO.getEmail()))
        .thenReturn(Optional.empty());
    Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
    Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
    Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);

    CustomerDTO result = underTest.saveNewCustomer(customerDTO);

    AssertionsForClassTypes.assertThat(result).isNotNull();
    AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
  }

  @Test
  void shouldNotSaveNewCustomerWhenEmailExists() {
    CustomerDTO customerDTO =
        CustomerDTO.builder()
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();
    Customer customer =
        Customer.builder().firstName("custm1").lastName("cust1").email("cust1@gmail.com").build();

    Mockito.when(customerRepository.findByEmail(customerDTO.getEmail()))
        .thenReturn(Optional.of(customer));

    AssertionsForClassTypes.assertThatThrownBy(() -> underTest.saveNewCustomer(customerDTO))
        .isInstanceOf(EmailAlreadyExistException.class);

    verify(customerRepository, atLeastOnce()).save(any());
  }

  @Test
  void shouldGetAllCustomerTest() {
    List<Customer> customers =
        List.of(
            Customer.builder()
                .firstName("custm1")
                .lastName("cust1")
                .email("cust1@gmail.com")
                .build(),
            Customer.builder()
                .firstName("custm2")
                .lastName("cust2")
                .email("cust2@gmail.com")
                .build());

    List<CustomerDTO> expected =
        List.of(
            CustomerDTO.builder()
                .firstName("custm1")
                .lastName("cust1")
                .email("cust1@gmail.com")
                .build(),
            CustomerDTO.builder()
                .firstName("custm2")
                .lastName("cust2")
                .email("cust2@gmail.com")
                .build());

    Mockito.when(customerRepository.findAll()).thenReturn(customers);
    Mockito.when(customerMapper.fromCustomerList(customers)).thenReturn(expected);

    List<CustomerDTO> result = underTest.getAllCustomers();

    AssertionsForClassTypes.assertThat(result).isNotNull();
    AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    verify(customerRepository, times(1)).findAll();
    verify(customerMapper, times(1)).fromCustomerList(any());
    verify(customerRepository, never()).delete(any());
  }

  @Test
  void shouldDeleteCustomer() {
    Customer existing = Customer.builder().id(4L).build();

    Mockito.when(customerRepository.findById(4L)).thenReturn(Optional.of(existing));

    underTest.deleteCustomerById(4L);

    Mockito.verify(customerRepository, times(1)).deleteById(4L);
    Mockito.verify(customerRepository, times(1)).findById(4L);
    Mockito.verifyNoMoreInteractions(customerRepository);
  }

  @Test
  void shouldThrowWhenDeleteCustomerNotFound() {
    Mockito.when(customerRepository.findById(404L)).thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> underTest.deleteCustomerById(404L))
        .isInstanceOf(CustomerNotFoundException.class);
  }
}
