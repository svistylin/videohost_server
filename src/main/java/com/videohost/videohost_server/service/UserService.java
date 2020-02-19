package com.videohost.videohost_server.service;

import com.videohost.videohost_server.command.UserCreateUpdateCommand;
import com.videohost.videohost_server.dto.UserDto;
import com.videohost.videohost_server.model.User;
import com.videohost.videohost_server.security.jwt.JwtUser;

import java.util.List;

public interface UserService {

    UserDto save(UserCreateUpdateCommand userCreateUpdateCommand);

    UserDto update(UserCreateUpdateCommand userCreateUpdateCommand, JwtUser jwtUser);

    List<UserDto> getAll();

    User findByEmail(String username);

    User findById(Long id);

    void delete(Long id);
}
