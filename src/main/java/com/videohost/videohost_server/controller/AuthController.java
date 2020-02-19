package com.videohost.videohost_server.controller;

import com.videohost.videohost_server.command.AuthenticationRequestCommand;
import com.videohost.videohost_server.command.UserCreateUpdateCommand;
import com.videohost.videohost_server.model.User;
import com.videohost.videohost_server.security.jwt.JwtTokenProvider;
import com.videohost.videohost_server.service.RoleService;
import com.videohost.videohost_server.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;


    public AuthController(JwtTokenProvider jwtTokenProvider, UserService userService,
                          BCryptPasswordEncoder passwordEncoder, RoleService roleService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        UserCreateUpdateCommand admin = new UserCreateUpdateCommand();
        admin.setEmail("admin");
        admin.setPassword("admin");
        userService.save(admin);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody AuthenticationRequestCommand requestDto) {
        try {
            String email = requestDto.getEmail();
            User user = userService.findByEmail(email);
            user.setRoles(new ArrayList<>());
            if (user == null || !user.getPassword().equals(requestDto.getPassword())) {
                throw new UsernameNotFoundException("wrong data please try again");
            }
            String token = jwtTokenProvider.createToken(email, user.getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", email);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
