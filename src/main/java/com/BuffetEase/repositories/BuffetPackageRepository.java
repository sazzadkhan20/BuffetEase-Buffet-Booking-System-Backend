package com.BuffetEase.repositories;

import com.BuffetEase.entities.BuffetPackage;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import com.BuffetEase.interfaces.IBuffetPackageRepository;

@Repository
public class BuffetPackageRepository implements IBuffetPackageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BuffetPackageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(BuffetPackage p) {
        String sql = """
            INSERT INTO buffet_packages
            (id, package_name, description, price_per_person, start_time, end_time,
             max_capacity, is_active, created_at)
            VALUES
            (:id, :packageName, :description, :price, :startTime, :endTime,
             :capacity, :active, NOW())
        """;

        jdbcTemplate.update(sql, params(p));
    }

    @Override
    public void update(int id, BuffetPackage p) {
        String sql = """
            UPDATE buffet_packages SET
            package_name = :packageName,
            description = :description,
            price_per_person = :price,
            start_time = :startTime,
            end_time = :endTime,
            max_capacity = :capacity,
            is_active = :active
            WHERE id = :id
        """;

        MapSqlParameterSource src = params(p).addValue("id", id);
        jdbcTemplate.update(sql, src);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM buffet_packages WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public BuffetPackage getById(int id) {
        String sql = "SELECT * FROM buffet_packages WHERE id = :id";

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("id", id),
                (rs, rowNum) -> mapRow(rs));
    }

    public BigDecimal checkAvailability(int id) {
        BuffetPackage p = getById(id);
        if(p == null)
            return BigDecimal.ZERO;
        else if(!p.getIsActive())
            return BigDecimal.ZERO;
        else if(p.getStartTime() != null && p.getStartTime().equals(LocalTime.now()) && p.getStartTime().isBefore(LocalTime.now()))
            return BigDecimal.ZERO;
        System.out.println(p.getPricePerPerson());
        return p.getPricePerPerson();
    }

    @Override
    public List<Map<String, Object>> getAll() {
        String sql = "SELECT * FROM buffet_packages ORDER BY id";
        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource());
    }

    private BuffetPackage mapRow(ResultSet rs) throws SQLException {
        BuffetPackage p = new BuffetPackage();
        p.setId(rs.getInt("id"));
        p.setPackageName(rs.getString("package_name"));
        p.setDescription(rs.getString("description"));
        p.setPricePerPerson(rs.getBigDecimal("price_per_person"));
        p.setStartTime(rs.getTime("start_time").toLocalTime());
        p.setEndTime(rs.getTime("end_time").toLocalTime());
        p.setMaxCapacity(rs.getInt("max_capacity"));
        p.setIsActive(rs.getBoolean("is_active"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return p;
    }

    private MapSqlParameterSource params(BuffetPackage p) {
        return new MapSqlParameterSource()
                .addValue("id", p.getId())
                .addValue("packageName", p.getPackageName())
                .addValue("description", p.getDescription())
                .addValue("price", p.getPricePerPerson())
                .addValue("startTime", p.getStartTime())
                .addValue("endTime", p.getEndTime())
                .addValue("capacity", p.getMaxCapacity())
                .addValue("active", p.getIsActive());
    }
}
