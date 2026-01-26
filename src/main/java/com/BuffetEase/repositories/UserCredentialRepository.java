package com.BuffetEase.repositories;

import com.BuffetEase.dtos.RegisterDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserCredentialRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserCredentialRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertCredentail(String email, String password,int id) {
        String sql = """
            INSERT INTO user_credentials (email, password, user_id)
            VALUES (:email, :password, :userId)
        """;
        jdbcTemplate.update(sql,new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password)
                .addValue("userId",id));
        return true;
    }

    public boolean searchCredentail(String email) {
        String sql = """
            SELECT COUNT(*) 
            FROM user_credentials 
            WHERE email = :email
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);
        int count = jdbcTemplate.queryForObject(sql,params,Integer.class);
        return count > 0;
    }


    public void updatePassword(String email, String encodedPassword) {

        String sql = """
        UPDATE user_credentials
        SET password = :password
        WHERE email = :email
    """;

        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("password", encodedPassword)
                .addValue("email", email));
    }

}
