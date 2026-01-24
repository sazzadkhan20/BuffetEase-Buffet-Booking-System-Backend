package com.BuffetEase.repositories;

import com.BuffetEase.entities.RefreshTokenEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class RefreshTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public RefreshTokenEntity save(RefreshTokenEntity RefreshTokenEntity) {
        String sql = "INSERT INTO refresh_tokens (token, user_id, expiry_date) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, RefreshTokenEntity.getToken());
            ps.setLong(2, RefreshTokenEntity.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(RefreshTokenEntity.getExpiryDate()));
            return ps;
        }, keyHolder);

        RefreshTokenEntity.setId(keyHolder.getKey().longValue());
        return RefreshTokenEntity;
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        String sql = "SELECT * FROM refresh_tokens WHERE token = ?";

        try {
            RefreshTokenEntity RefreshTokenEntity = jdbcTemplate.queryForObject(
                    sql,
                    new RefreshTokenEntityRowMapper(),
                    token
            );
            return Optional.ofNullable(RefreshTokenEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<RefreshTokenEntity> findByUserId(Long userId) {
        String sql = "SELECT * FROM refresh_tokens WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";

        try {
            RefreshTokenEntity RefreshTokenEntity = jdbcTemplate.queryForObject(
                    sql,
                    new RefreshTokenEntityRowMapper(),
                    userId
            );
            return Optional.ofNullable(RefreshTokenEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM refresh_tokens WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public void deleteByToken(String token) {
        String sql = "DELETE FROM refresh_tokens WHERE token = ?";
        jdbcTemplate.update(sql, token);
    }

    public void deleteExpiredTokens() {
        String sql = "DELETE FROM refresh_tokens WHERE expiry_date < ?";
        jdbcTemplate.update(sql, LocalDateTime.now());
    }

    private static class RefreshTokenEntityRowMapper implements RowMapper<RefreshTokenEntity> {
        @Override
        public RefreshTokenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            RefreshTokenEntity RefreshTokenEntity = new RefreshTokenEntity();
            RefreshTokenEntity.setId(rs.getLong("id"));
            RefreshTokenEntity.setToken(rs.getString("token"));
            RefreshTokenEntity.setUserId(rs.getLong("user_id"));
            RefreshTokenEntity.setExpiryDate(rs.getTimestamp("expiry_date").toLocalDateTime());

            var createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                RefreshTokenEntity.setCreatedAt(createdAt.toLocalDateTime());
            }

            return RefreshTokenEntity;
        }
    }
}