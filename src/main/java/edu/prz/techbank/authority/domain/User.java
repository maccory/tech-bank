package edu.prz.techbank.authority.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.prz.techbank.foundation.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = "passwordSignature")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class User extends BaseEntity {

  String username;
  String name;

  @JsonIgnore
  String passwordSignature;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  Set<Role> roles;

  @Lob
  @Column(length = 1_000_000)
  byte[] profilePicture;
}
