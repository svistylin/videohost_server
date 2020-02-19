package com.videohost.videohost_server.command;

import lombok.Data;

@Data
public class AuthenticationRequestCommand {
    private String email;
    private String password;
}
