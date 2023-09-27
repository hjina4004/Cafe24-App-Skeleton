package com.lmfriends.cafe24app.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lmfriends.cafe24app.model.Role;
import com.lmfriends.cafe24app.model.User;
import com.lmfriends.cafe24app.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("CustomUserDetailsService::loadUserByUsername {}", username);
    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      return new org.springframework.security.core.userdetails.User(
          user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    } else {
      throw new UsernameNotFoundException("Invalid username or password.");
    }
  }

  private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
    Collection<? extends GrantedAuthority> mapRoles = roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
    return mapRoles;
  }
}
