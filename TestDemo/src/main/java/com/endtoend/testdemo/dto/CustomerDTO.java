package com.endtoend.testdemo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
}
