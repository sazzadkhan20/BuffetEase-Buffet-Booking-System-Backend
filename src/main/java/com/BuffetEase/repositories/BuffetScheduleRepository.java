package com.BuffetEase.repositories;

import com.BuffetEase.entities.BuffetSchedule;
import com.BuffetEase.interfaces.IBuffetScheduleRepository;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class BuffetScheduleRepository implements IBuffetScheduleRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public BuffetScheduleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(BuffetSchedule s) {
        String sql = """
            INSERT INTO buffet_schedules
            (buffet_package_id, buffet_date, available_capacity, booking_cutoff_time, is_open)
            VALUES (:packageId, :date, :capacity, :cutoff, :open)
            """;

        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("packageId", s.getBuffetPackageId())
                .addValue("date", s.getBuffetDate())
                .addValue("capacity", s.getAvailableCapacity())
                .addValue("cutoff", s.getBookingCutoffTime())
                .addValue("open", s.isOpen()));
    }

    @Override
    public void update(int id, BuffetSchedule s) {
        String sql = """
            UPDATE buffet_schedules
            SET buffet_date = :date,
                available_capacity = :capacity,
                booking_cutoff_time = :cutoff,
                is_open = :open
            WHERE id = :id
            """;

        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("date", s.getBuffetDate())
                .addValue("capacity", s.getAvailableCapacity())
                .addValue("cutoff", s.getBookingCutoffTime())
                .addValue("open", s.isOpen())
                .addValue("id", id));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM buffet_schedules WHERE id = :id",
                new MapSqlParameterSource("id", id)
        );
    }

    @Override
    public BuffetSchedule getById(int id) {
        String sql = "SELECT * FROM buffet_schedules WHERE id = :id";

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("id", id),
                (rs, rowNum) -> map(rs));
    }

    @Override
    public List<Map<String, Object>> getAll() {
        return jdbcTemplate.queryForList(
                "SELECT * FROM buffet_schedules",
                new MapSqlParameterSource()
        );
    }

    @Override
    public List<BuffetSchedule> getPaginated(int page, int size) {
        int offset = page * size;

        String sql = """
            SELECT * FROM buffet_schedules
            ORDER BY buffet_date
            LIMIT :limit OFFSET :offset
            """;

        return jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("limit", size)
                        .addValue("offset", offset),
                (rs, rowNum) -> map(rs));
    }

    private BuffetSchedule map(ResultSet rs) throws SQLException {
        BuffetSchedule s = new BuffetSchedule();
        s.setId(rs.getInt("id"));
        s.setBuffetPackageId(rs.getInt("buffet_package_id"));
        s.setBuffetDate(rs.getDate("buffet_date").toLocalDate());
        s.setAvailableCapacity(rs.getInt("available_capacity"));
        s.setBookingCutoffTime(rs.getTime("booking_cutoff_time").toLocalTime());
        s.setOpen(rs.getBoolean("is_open"));
        return s;
    }
}
