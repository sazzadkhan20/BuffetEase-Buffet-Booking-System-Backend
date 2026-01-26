package com.BuffetEase.repositories;

import com.BuffetEase.dtos.RegisterDTO;
import com.BuffetEase.entities.UserEntity;
import com.BuffetEase.exceptions.EmailAlreadyExistsException;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserCredentialRepository userCredentialRepository;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate,
                          UserCredentialRepository userCredentialRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userCredentialRepository = userCredentialRepository;
    }

    public boolean register(RegisterDTO registerDTO) {

        if (userCredentialRepository.searchCredentail(registerDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        String sql = """
            INSERT INTO users (name, phone,role_id,is_active)
            VALUES (:name, :phone, :role, :active)
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", registerDTO.getName())
                .addValue("phone", registerDTO.getPhone())
                .addValue("role",2)
                .addValue("active",true);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder,new String[]{"id"});
        userCredentialRepository.insertCredentail(registerDTO.getEmail(),registerDTO.getPassword(),keyHolder.getKey().intValue());
        return true;
    }

    public Optional<UserEntity> findByEmail(String email) {
        String sql = """
            SELECT
                u.id            AS user_id,
                u.name          AS name,
                u.phone         AS phone,
                u.role_id       AS role_id,
                u.is_active     AS is_active,
                u.created_at    AS created_at,
                u.updated_at    AS updated_at,
                uc.email        AS email,
                uc.password     AS password,
                r.role_name     AS role_name
            FROM users u
            INNER JOIN user_credentials uc ON u.id = uc.user_id
            INNER JOIN roles r ON u.role_id = r.id
            WHERE uc.email = :email
        """;
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            sql,
                            new MapSqlParameterSource("email", email),
                            userRowMapper
                    )
            );
    }

    public Optional<UserEntity> findById(Long userId) {
         String sql = """
            SELECT
                u.id            AS user_id,
                u.name          AS name,
                u.phone         AS phone,
                u.role_id       AS role_id,
                u.is_active     AS is_active,
                u.created_at    AS created_at,
                u.updated_at    AS updated_at,
                uc.email        AS email,
                uc.password     AS password,
                r.role_name     AS role_name
            FROM users u
            INNER JOIN user_credentials uc ON u.id = uc.user_id
            INNER JOIN roles r ON u.role_id = r.id
            WHERE u.id = :userId
        """;
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            sql,
                            new MapSqlParameterSource("userId", userId),
                            userRowMapper
                    )
            );
    }

    public void updateLastLogin(String email) {

        String sql = """
            UPDATE user_credentials
            SET last_login = :lastLogin
            WHERE email = :email
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("lastLogin", LocalDateTime.now())
                .addValue("email", email);

        jdbcTemplate.update(sql, params);
    }

    private final RowMapper<UserEntity> userRowMapper = (rs, rowNum) -> {
        UserEntity user = new UserEntity();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("name"));
        user.setPhone(rs.getString("phone"));
        user.setRoleId(rs.getLong("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setIsActive(rs.getBoolean("is_active"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return user;
    };
}