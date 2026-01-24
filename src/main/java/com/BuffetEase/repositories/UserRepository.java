package com.BuffetEase.repositories;

import com.BuffetEase.entities.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UserEntity> findByEmail(String email) {
        String sql = """
            SELECT 
                u.id, u.name, u.phone, u.role_id, u.is_active, 
                u.created_at, u.updated_at,
                uc.email, uc.password,
                r.role_name
            FROM users u
            INNER JOIN user_credentials uc ON u.id = uc.user_id
            INNER JOIN roles r ON u.role_id = r.id
            WHERE uc.email = ?
            """;

        try {
            UserEntity user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> findById(Long userId) {
        String sql = """
            SELECT 
                u.id, u.name, u.phone, u.role_id, u.is_active, 
                u.created_at, u.updated_at,
                uc.email, uc.password,
                r.role_name
            FROM users u
            INNER JOIN user_credentials uc ON u.id = uc.user_id
            INNER JOIN roles r ON u.role_id = r.id
            WHERE u.id = ?
            """;

        try {
            UserEntity user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void updateLastLogin(String email) {
        String sql = "UPDATE user_credentials SET last_login = ? WHERE email = ?";
        jdbcTemplate.update(sql, LocalDateTime.now(), email);
    }

    private static class UserRowMapper implements RowMapper<UserEntity> {
        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserEntity user = new UserEntity();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setRoleId(rs.getLong("role_id"));
            user.setRoleName(rs.getString("role_name"));
            user.setIsActive(rs.getBoolean("is_active"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));

            // Handle timestamps (can be null)
            var createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                user.setCreatedAt(createdAt.toLocalDateTime());
            }

            var updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                user.setUpdatedAt(updatedAt.toLocalDateTime());
            }

            return user;
        }
    }
}