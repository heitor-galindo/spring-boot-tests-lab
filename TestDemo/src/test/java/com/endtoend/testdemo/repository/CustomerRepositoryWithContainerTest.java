package com.endtoend.testdemo.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.endtoend.testdemo.entities.Customer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryWithContainerTest {
  @Container @ServiceConnection
  private static PostgreSQLContainer container = new PostgreSQLContainer("postgres:16");
  @Autowired CustomerRepository customerRepository;

  @Test
  public void connectionTest() {
    assertThat(container.isCreated()).isTrue();
    assertThat(container.isRunning()).isTrue();
  }

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
}
