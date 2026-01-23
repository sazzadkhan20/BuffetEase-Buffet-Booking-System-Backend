package com.BuffetEase.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RoleDTO {
    private int id;

    @NotBlank
    private String roleName;

    public RoleDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
