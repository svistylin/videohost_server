package com.videohost.videohost_server.service.impl;

import com.videohost.videohost_server.model.Role;
import com.videohost.videohost_server.repository.RoleRepository;
import com.videohost.videohost_server.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        roleRepository.findByName(role.getName()).orElseThrow(NoSuchElementException::new);
        return roleRepository.save(role);
    }
}
