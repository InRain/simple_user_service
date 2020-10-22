package org.example.userservice.service.role;

import org.example.userservice.model.Role;

import java.util.List;

public interface RoleService {
    Role getById(Long id);
    Role add(Role role);
    void delete(Long id);
    void delete(Role role);
    List<Role> getAll();
    Role findByName(String roleName);
}
