package com.BuffetEase.entities;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Role {
    @Min(1)
    private int id;

    @NotBlank
    private String roleName;

    public Role() {}

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
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
