package com.endtoend.testdemo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.entities.Customer;
import com.endtoend.testdemo.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
  @Mock private CustomerRepository customerRepository;

  @Mock private CustomerMapper customerMapper;

  @InjectMocks private CustomerServiceImpl underTest;

  @Test
  void shouldSaveNewCustomer() {
    // given
    Customer customer =
        Customer.builder().firstName("custm1").lastName("cust1").email("cust1@gmail.com").build();
    CustomerDTO customerDto =
        CustomerDTO.builder()
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    Customer savedCustomer =
        Customer.builder().firstName("custm1").lastName("cust1").email("cust1@gmail.com").build();
    CustomerDTO expected =
        CustomerDTO.builder()
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    // when
    Mockito.when(customerRepository.findByEmail(customerDto.getEmail()))
        .thenReturn(Optional.empty());
    Mockito.when(customerMapper.fromCustomerDTO(customerDto)).thenReturn(customer);
    Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
    Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);

    CustomerDTO result = underTest.saveNewCustomer(customerDto);

    // then
    assertThat(result).isNotNull();
    assertThat(expected).usingRecursiveComparison().isEqualTo(result);
  }
}
