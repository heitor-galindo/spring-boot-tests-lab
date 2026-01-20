package com.endtoend.testdemo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.entities.Customer;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerMapperTest {

  private CustomerMapper customerMapper = new CustomerMapper();

  @Test
  @DisplayName("should map Customer to CustomerDTO")
  void shouldMapCustomerToCustomerDTO() {
    // given
    Customer givenCustomer =
        Customer.builder()
            .id(1L)
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    CustomerDTO expectedCustomerDTO =
        CustomerDTO.builder()
            .id(1L)
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    // when
    CustomerDTO result = customerMapper.fromCustomer(givenCustomer);

    // then
    assertThat(expectedCustomerDTO).usingRecursiveComparison().isEqualTo(result);
  }

  @Test
  @DisplayName("should map CustomerDTO to Customer")
  void shouldMapCustomerDTOToCustomer() {
    // given
    Customer givenCustomer =
        Customer.builder()
            .id(1L)
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    CustomerDTO expectedCustomerDTO =
        CustomerDTO.builder()
            .id(1L)
            .firstName("custm1")
            .lastName("cust1")
            .email("cust1@gmail.com")
            .build();

    // when
    Customer result = customerMapper.fromCustomerDTO(expectedCustomerDTO);

    // then
    assertThat(givenCustomer).usingRecursiveComparison().isEqualTo(result);
  }

  @Test
  @DisplayName("shoud map list of Customers to list of CustomerDTOs")
  void shouldMapListOfCustomersToListOfCustomerDTOs() {
    // given
    List<Customer> givenCustomers =
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

    // when
    List<CustomerDTO> result = customerMapper.fromCustomerList(givenCustomers);

    // then
    assertThat(expected).usingRecursiveComparison().isEqualTo(result);
  }
}
