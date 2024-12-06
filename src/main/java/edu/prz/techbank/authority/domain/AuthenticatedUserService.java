package edu.prz.techbank.authority.domain;

import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

  final UserRepository userRepository;
  final AuthenticationContext authenticationContext;

  public Optional<User> getUsingContext() {
    return authenticationContext.getAuthenticatedUser(UserDetails.class)
        .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
  }

  public void logout() {
    authenticationContext.logout();
  }

}
