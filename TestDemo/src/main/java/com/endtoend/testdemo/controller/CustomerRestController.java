package com.endtoend.testdemo.controller;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.exception.CustomerNotFoundException;
import com.endtoend.testdemo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CustomerRestController {
  private CustomerService customerService;

  @GetMapping("/customers/{id}")
  public CustomerDTO getCustomerById(@PathVariable Long id) {
    CustomerDTO customer = customerService.getCustomerById(id);
    if (customer == null) {
      throw new CustomerNotFoundException();
    }
    return customer;
  }

  @GetMapping("/customers")
  @Operation(
      summary = "Get all customers",
      description = "Retrieve all customers from the database")
  @ApiResponse(responseCode = "200", description = "List of customers returned successfully")
  public List<CustomerDTO> getAllCustomers() {
    return customerService.getAllCustomers();
  }

  @GetMapping("/customers/search")
  public List<CustomerDTO> searchCustomers(@RequestParam String keyword) {
    return customerService.searchCustomers(keyword);
  }

  @Operation(summary = "Create a new contracts", description = "Add a new contracts to the system")
  @ApiResponse(responseCode = "201", description = "Customer created successfully")
  @PostMapping("/customers")
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerDTO saveCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
    return customerService.saveNewCustomer(customerDTO);
  }

  @PutMapping("/customers/{id}")
  public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
    return customerService.updateCustomer(id, customerDTO);
  }

  @DeleteMapping("/customers/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomerById(id);
  }
}
