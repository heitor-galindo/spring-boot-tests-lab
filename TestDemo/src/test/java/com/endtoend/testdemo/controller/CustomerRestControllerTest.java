package com.endtoend.testdemo.controller;

import com.endtoend.testdemo.dto.CustomerDTO;
import com.endtoend.testdemo.exception.CustomerNotFoundException;
import com.endtoend.testdemo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {

  List<CustomerDTO> customers;
  @Autowired private CustomerService customerService;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.customers =
        List.of(
            CustomerDTO.builder()
                .id(1L)
                .firstName("cust1")
                .lastName("cust1")
                .email("cust1@gmail.com")
                .build(),
            CustomerDTO.builder()
                .id(2L)
                .firstName("cust2")
                .lastName("cust2")
                .email("cust2@gmail.com")
                .build(),
            CustomerDTO.builder()
                .id(3L)
                .firstName("cust3")
                .lastName("cust3")
                .email("cust3@gmail.com")
                .build());
  }

  @Test
  void shouldGetAllCustomers() throws Exception {
    Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/customers"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
        .andExpect(
            MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
  }

  @Test
  void shouldGetCustomerById() throws Exception {
    Long id = 1L;

    Mockito.when(customerService.getCustomerById(id)).thenReturn(customers.get(0));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/customers/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content()
                .json(objectMapper.writeValueAsString(customers.get(0))));
  }

  @Test
  void shouldNotGetCustomerByInvalidId() throws Exception {
    Long id = 9L;

    Mockito.when(customerService.getCustomerById(id)).thenThrow(CustomerNotFoundException.class);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/customers/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string(""));
  }

  @Test
  void searchCustomers() throws Exception {
    String keyword = "m";

    Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/customers?keyword=" + keyword))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
        .andExpect(
            MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
  }

  @Test
  void shouldSaveCustomer() throws Exception {
    CustomerDTO customerDTO = customers.getFirst();

    String expected =
        """
            {
              "id": 1,
              "firstName": "cust1",
              "lastName": "cust1",
              "email": "cust1@gmail.com"
            }
            """;

    Mockito.when(customerService.saveNewCustomer(Mockito.any())).thenReturn(customers.get(0));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(expected));
  }

  @Test
  void testUpdateCustomer() throws Exception {
    Long customerId = 1L;
    CustomerDTO customerDTO = customers.get(0);

    Mockito.when(customerService.updateCustomer(Mockito.eq(customerId), Mockito.any()))
        .thenReturn(customers.get(0));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
  }

  @Test
  void shouldDeleteCustomer() throws Exception {
    Long customerId = 1L;

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/customers/{id}", customerId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    public CustomerService customerService() {
      return Mockito.mock(CustomerService.class);
    }
  }
}
