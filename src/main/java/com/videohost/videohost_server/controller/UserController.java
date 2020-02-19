package com.videohost.videohost_server.controller;

import com.videohost.videohost_server.command.UserCreateUpdateCommand;
import com.videohost.videohost_server.dto.UserDto;
import com.videohost.videohost_server.security.jwt.JwtUser;
import com.videohost.videohost_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDto> save(UserCreateUpdateCommand userCreateUpdateCommand, JwtUser jwtUser) {
        return new ResponseEntity<>(userService.update(userCreateUpdateCommand, jwtUser), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDto> saveOrUpdate(UserCreateUpdateCommand createUpdateCommand, JwtUser user) {
        if (createUpdateCommand.getId()==null){
            return new ResponseEntity<>(userService.save(createUpdateCommand), HttpStatus.OK);
        }
        else return new ResponseEntity<>(userService.update(createUpdateCommand, user), HttpStatus.OK);
    }
}
