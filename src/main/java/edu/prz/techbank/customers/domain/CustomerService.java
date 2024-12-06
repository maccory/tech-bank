package edu.prz.techbank.customers.domain;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

  final CustomerRepository repository;

  public Optional<Customer> get(Long id) {
    return repository.findById(id);
  }

  public Customer update(Customer entity) {
    return repository.save(entity);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public Page<Customer> list(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<Customer> list(Pageable pageable, Specification<Customer> filter) {
    return repository.findAll(filter, pageable);
  }

  public int count() {
    return (int) repository.count();
  }

}
