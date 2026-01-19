package com.endtoend.testdemo;

import com.endtoend.testdemo.entities.Customer;
import com.endtoend.testdemo.repository.CustomerRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Slf4j
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  @Profile("!test")
  CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
    log.info("================= Initialization ================");
    return args -> {
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
                  .build(),
              Customer.builder()
                  .firstName("cust3")
                  .lastName("cust3")
                  .email("cust3@gmail.com")
                  .build());
      customerRepository.saveAll(customers);
    };
  }
}
