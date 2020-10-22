package org.example.userservice.service.role;

import org.example.userservice.model.Role;
import org.example.userservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Role add(Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);

    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
