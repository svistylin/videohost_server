package com.videohost.videohost_server.service.impl;

import com.videohost.videohost_server.command.UserCreateUpdateCommand;
import com.videohost.videohost_server.dto.UserDto;
import com.videohost.videohost_server.model.Role;
import com.videohost.videohost_server.model.User;
import com.videohost.videohost_server.repository.RoleRepository;
import com.videohost.videohost_server.repository.UserRepository;
import com.videohost.videohost_server.security.jwt.JwtUser;
import com.videohost.videohost_server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto save(UserCreateUpdateCommand userCreateUpdateCommand) {
        return UserDto.fromUser(userRepository.save(userCreateUpdateCommand.toUser()));
    }

    @Override
    public UserDto update(UserCreateUpdateCommand userCreateUpdateCommand, JwtUser jwtUser) {
        User userFromDb = userRepository.findByEmail(jwtUser.getEmail()).orElseThrow(NoSuchElementException::new);
        if (!userFromDb.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
            if (userCreateUpdateCommand.getId().equals(jwtUser.getId())) {
                return UserDto.fromUser(userRepository.save(userCreateUpdateCommand.toUser()));
            }
        } else {
            List<Role> roles = userCreateUpdateCommand.getRoles().stream()
                    .map(role -> roleRepository.findByName(role.getName()).orElseThrow(NoSuchElementException::new))
                    .collect(Collectors.toList());
            User user = userCreateUpdateCommand.toUser();
            user.setRoles(roles);
            return UserDto.fromUser(userRepository.save(user));
        }
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email"
                + email + "not foud"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id" + id +
                "not found"));
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
