package com.BuffetEase.services;

import com.BuffetEase.interfaces.IRoleRepository;
import org.springframework.stereotype.Service;
import com.BuffetEase.entities.Role;
import java.util.List;

@Service
public class RoleService {
    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void create(Role role) {
        roleRepository.create(role);
    }

    public void update(int id, Role role) {
        roleRepository.update(id, role);
    }

    public void delete(int id) {
        roleRepository.delete(id);
    }

    public Role getRole(int id) {
        return roleRepository.getRole(id);
    }

    public List<Role> getAll() {
        return roleRepository.getAll();
    }

    public List<Role> getAllPaginated(int page, int size) {
        return roleRepository.getAllPaginated(page, size);
    }
}
