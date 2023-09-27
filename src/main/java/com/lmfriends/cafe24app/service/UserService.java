package com.lmfriends.cafe24app.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmfriends.cafe24app.dto.UserDto;
import com.lmfriends.cafe24app.model.Role;
import com.lmfriends.cafe24app.model.User;
import com.lmfriends.cafe24app.repository.RoleRepository;
import com.lmfriends.cafe24app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
  
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public void saveUser(UserDto dto) {
    Optional <Role> optionalRole = roleRepository.findByName("ROLE_USER");
    Role role;
    if (optionalRole.isPresent()) role = optionalRole.get();
    else role = checkRoleExist();

    User user = new User(null, dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), Arrays.asList(role));
    userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  private Role checkRoleExist() {
    Role role = new Role();
    role.setName("ROLE_USER");
    return roleRepository.save(role);
  }
}
