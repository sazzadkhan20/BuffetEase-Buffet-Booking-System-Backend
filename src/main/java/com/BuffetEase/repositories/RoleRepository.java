package com.BuffetEase.repositories;

import com.BuffetEase.entities.Role;
import java.util.List;
import com.BuffetEase.interfaces.IRoleRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository implements IRoleRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RoleRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void create(Role role) {
        String sql = "INSERT INTO Roles(id, role_name) VALUES(:id, :roleName)";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("id", role.getId())
                .addValue("roleName", role.getRoleName()));
    }

    @Override
    public void update(int id, Role role) {
        String sql = "UPDATE Roles SET role_name = :roleName WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("roleName", role.getRoleName())
                .addValue("id", id));
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Roles WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource().addValue("id", id));
    }

    @Override
    public Role getRole(int id) {
        String sql = "SELECT id, role_name FROM Roles WHERE id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource().addValue("id", id), (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setRoleName(rs.getString("role_name"));
            return role;
        });
    }

    @Override
    public List<Role> getAll() {
        String sql = "SELECT id, role_name FROM Roles";
        return namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setRoleName(rs.getString("role_name"));
            return role;
        });
    }

    @Override
    public List<Role> getAllPaginated(int page, int size) {
        int offset = page * size;
        String sql = "SELECT id, role_name FROM Roles ORDER BY id LIMIT :limit OFFSET :offset";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("limit", size)
                .addValue("offset", offset);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setRoleName(rs.getString("role_name"));
            return role;
        });
    }
}
