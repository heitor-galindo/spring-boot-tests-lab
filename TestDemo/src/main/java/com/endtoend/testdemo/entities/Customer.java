package com.endtoend.testdemo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Size(min = 3)
  private String firstName;

  @NotEmpty
  @Size(min = 3)
  private String lastName;

  @NotEmpty
  @Column(unique = true)
  private String email;
}
