package com.BuffetEase.controllers;
import org.springframework.http.ResponseEntity;
import com.BuffetEase.dtos.RoleDTO;
import com.BuffetEase.entities.Role;
import com.BuffetEase.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable int id) {
        return roleService.getRole(id);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        roleService.create(role);
        return ResponseEntity.ok("Role created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable int id, @Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        roleService.update(id, role);
        return ResponseEntity.ok("Role updated successfully");
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable int id) {
        roleService.delete(id);
    }

    @GetMapping("/page/{page}/size/{size}")
    public List<Role> getRolesPaginated(@PathVariable("page") int page, @PathVariable("size") int size) {
        return roleService.getAllPaginated(page, size);
    }
}
