package com.BuffetEase.interfaces;

import com.BuffetEase.entities.Role;

import java.util.List;

public interface IRoleRepository {
    void create(Role role);

    void update(int id, Role role);

    void delete(int id);

    Role getRole(int id);

    List<Role> getAll();

    List<Role> getAllPaginated(int page, int size);
}
