package edu.prz.techbank.customers.domain;

import edu.prz.techbank.foundation.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Customer extends BaseEntity {

  String firstName;
  String lastName;
  @Email
  String email;
  String phone;
  LocalDate birthDate;
  String occupation;
  @Enumerated(EnumType.STRING)
  CustomerRole role;
  boolean vip;
}
