package com.endtoend.testdemo.service;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.exception.CustomerNotFoundException;
import com.endtoend.testdemo.exception.EmailAlreadyExistException;
import java.util.List;

public interface CustomerService {
  CustomerDTO saveNewCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistException;

  CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException;

  CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException;

  List<CustomerDTO> getAllCustomers();

  List<CustomerDTO> searchCustomers(String keyword);

  void deleteCustomerById(Long id) throws CustomerNotFoundException;
}
