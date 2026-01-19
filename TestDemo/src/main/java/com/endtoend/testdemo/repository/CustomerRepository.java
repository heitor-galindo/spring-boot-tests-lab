package com.endtoend.testdemo.repository;

import com.endtoend.testdemo.entities.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  List<Customer> findByFirstNameContainingIgnoreCase(String firstName);

  Optional<Customer> findByEmail(String email);
}
