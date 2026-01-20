package com.endtoend.testdemo.service;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.entities.Customer;
import com.endtoend.testdemo.exception.CustomerNotFoundException;
import com.endtoend.testdemo.exception.EmailAlreadyExistException;
import com.endtoend.testdemo.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistException {
    Optional<Customer> byEmail = customerRepository.findByEmail(customerDTO.getEmail());
    if (byEmail.isPresent()) {
      log.error("Email already exists: {}", customerDTO.getEmail());
      throw new EmailAlreadyExistException("Email already exists: " + customerDTO.getEmail());
    }
    Customer customer = customerMapper.fromCustomerDTO(customerDTO);
    Customer savedCustomer = customerRepository.save(customer);
    return customerMapper.fromCustomer(savedCustomer);
  }

  @Override
  public CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException {
    Optional<Customer> customerOptional = customerRepository.findById(id);
    if (customerOptional.isEmpty()) {
      log.error("Customer not found with id: {}", id);
      throw new CustomerNotFoundException("Customer not found with id: " + id);
    }
    return customerMapper.fromCustomer(customerOptional.get());
  }

  @Override
  public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO)
      throws CustomerNotFoundException {
    Optional<Customer> customerOptional = customerRepository.findById(id);
    if (customerOptional.isEmpty()) {
      log.error("Customer not found with id: {}", id);
      throw new CustomerNotFoundException("Customer not found with id: " + id);
    }
    Customer customerToUpdate = customerOptional.get();
    customerToUpdate.setFirstName(customerDTO.getFirstName());
    customerToUpdate.setLastName(customerDTO.getLastName());
    customerToUpdate.setEmail(customerDTO.getEmail());
    Customer updatedCustomer = customerRepository.save(customerToUpdate);
    return customerMapper.fromCustomer(updatedCustomer);
  }

  @Override
  public List<CustomerDTO> getAllCustomers() {
    List<Customer> customers = customerRepository.findAll();
    return customerMapper.fromCustomerList(customers);
  }

  @Override
  public List<CustomerDTO> searchCustomers(String keyword) {
    List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCase(keyword);
    return customerMapper.fromCustomerList(customers);
  }

  @Override
  public void deleteCustomerById(Long id) throws CustomerNotFoundException {
    Optional<Customer> customerOptional = customerRepository.findById(id);
    if (customerOptional.isEmpty()) {
      log.error("Customer not found with id: {}", id);
      throw new CustomerNotFoundException("Customer not found with id: " + id);
    }
    customerRepository.deleteById(id);
  }
}
