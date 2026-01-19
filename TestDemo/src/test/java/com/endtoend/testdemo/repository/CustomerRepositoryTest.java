package com.endtoend.testdemo.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.endtoend.testdemo.entities.Customer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

  @Autowired CustomerRepository customerRepository;

  @BeforeEach
  public void setUp() {
    customerRepository.save(
        Customer.builder().firstName("custm1").lastName("cust1").email("cust1@gmail.com").build());
    customerRepository.save(
        Customer.builder().firstName("custm2").lastName("cust2").email("cust2@gmail.com").build());
    customerRepository.save(
        Customer.builder().firstName("cust3").lastName("cust3").email("cust3@gmail.com").build());
  }

  @Test
  @DisplayName("should find customer by First name Ignoring Case")
  void findByFirstnameContainingIgnoreCase() {

    // given
    List<Customer> expected =
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

    // when
    List<Customer> result = customerRepository.findByFirstNameContainingIgnoreCase("m");

    // then
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(expected.size());
    assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
  }

  @Test
  @DisplayName("should find customer by Email")
  void findByEmail() {
    // give
    String givenEmail = "cust1@gmail.com";
    Customer expected =
        Customer.builder().firstName("custm1").lastName("cust1").email("cust1@gmail.com").build();
    // when
    Optional<Customer> result = customerRepository.findByEmail(givenEmail);

    // then
    assertThat(result).isNotNull();
    assertThat(result).isPresent();
    assertThat(expected).usingRecursiveComparison().ignoringFields("id").isEqualTo(result.get());
  }

  @Test
  @DisplayName("should return empty when customer not found by Email")
  void findByEmail_NotFound() {
    // give
    String givenEmail = "cust@gmail.com";
    // when
    Optional<Customer> result = customerRepository.findByEmail(givenEmail);
    // then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();
  }
}
