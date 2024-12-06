package edu.prz.techbank.authority.domain;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  final UserRepository repository;

  public Optional<User> get(Long id) {
    return repository.findById(id);
  }

  public User update(User entity) {
    return repository.save(entity);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public Page<User> list(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<User> list(Pageable pageable, Specification<User> filter) {
    return repository.findAll(filter, pageable);
  }

  public int count() {
    return (int) repository.count();
  }

}
