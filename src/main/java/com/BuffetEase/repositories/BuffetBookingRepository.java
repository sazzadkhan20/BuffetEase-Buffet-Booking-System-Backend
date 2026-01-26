package com.BuffetEase.repositories;

import com.BuffetEase.entities.BuffetBookingEntity;
import com.BuffetEase.interfaces.IBuffetBookingRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class BuffetBookingRepository implements IBuffetBookingRepository {
    private NamedParameterJdbcTemplate jdbc;

    public BuffetBookingRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int createBooking(BuffetBookingEntity booking) {
        String sql = """
            INSERT INTO bookings(user_id, buffet_schedule_id, number_of_guests, 
                                 total_price, booking_status, booking_time)
            VALUES(:userId, :scheduleId, :guests, :price, :status, :time)
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", booking.getUserId())
                .addValue("scheduleId", booking.getBuffetScheduleId())
                .addValue("guests", booking.getNumberOfGuests())
                .addValue("price", booking.getTotalPrice())
                .addValue("status", booking.getBookingStatus())
                .addValue("time", Timestamp.valueOf(booking.getBookingTime()));

        jdbc.update(sql, params);
        return 1;
    }

    @Override
    public BuffetBookingEntity findById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = :id";
        return jdbc.queryForObject(sql,
                new MapSqlParameterSource("id", bookingId),
                (rs, rowNum) -> {
                    BuffetBookingEntity b = new BuffetBookingEntity();
                    b.setId(rs.getInt("id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setBuffetScheduleId(rs.getInt("buffet_schedule_id"));
                    b.setNumberOfGuests(rs.getInt("number_of_guests"));
                    b.setTotalPrice(rs.getBigDecimal("total_price"));
                    b.setBookingStatus(rs.getString("booking_status"));
                    b.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
                    return b;
                });
    }

    @Override
    public boolean bookingExists(int bookingId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE id=:id";
        Integer count = jdbc.queryForObject(sql, new MapSqlParameterSource("id", bookingId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void updateBookingStatus(int bookingId, String status) {
        jdbc.update(
                "UPDATE bookings SET booking_status=:status WHERE id=:id",
                new MapSqlParameterSource()
                        .addValue("status", status)
                        .addValue("id", bookingId)
        );
    }

    @Override
    public void updateGuestCount(int bookingId, int guestCount, double newTotalPrice) {
        jdbc.update("""
                UPDATE bookings 
                SET number_of_guests=:guests, total_price=:price 
                WHERE id=:id
                """,
                new MapSqlParameterSource()
                        .addValue("guests", guestCount)
                        .addValue("price", newTotalPrice)
                        .addValue("id", bookingId)
        );
    }

    @Override
    public void increaseAvailableCapacity(int scheduleId, int seats) {
        jdbc.update("""
            UPDATE buffet_schedules 
            SET available_capacity = available_capacity + :seats 
            WHERE id=:id
        """, new MapSqlParameterSource().addValue("seats", seats).addValue("id", scheduleId));
    }

    @Override
    public void decreaseAvailableCapacity(int scheduleId, int seats) {
        jdbc.update("""
            UPDATE buffet_schedules 
            SET available_capacity = available_capacity - :seats 
            WHERE id=:id
        """, new MapSqlParameterSource().addValue("seats", seats).addValue("id", scheduleId));
    }

    @Override
    public int getAvailableCapacity(int scheduleId) {
        return jdbc.queryForObject(
                "SELECT available_capacity FROM buffet_schedules WHERE id=:id",
                new MapSqlParameterSource("id", scheduleId),
                Integer.class);
    }

    @Override
    public LocalDateTime getBookingCutoffTime(int scheduleId) {

        String sql = """
        SELECT buffet_date, booking_cutoff_time
        FROM buffet_schedules
        WHERE id=:id
    """;

        return jdbc.queryForObject(
                sql,
                new MapSqlParameterSource("id", scheduleId),
                (rs, rowNum) ->
                        rs.getDate("buffet_date").toLocalDate()
                                .atTime(rs.getTime("booking_cutoff_time").toLocalTime())
        );
    }


    @Override
    public boolean buffetScheduleExistsAndOpen(int scheduleId) {
        Integer c = jdbc.queryForObject("""
            SELECT COUNT(*) FROM buffet_schedules 
            WHERE id=:id AND is_open=true
        """, new MapSqlParameterSource("id", scheduleId), Integer.class);
        return c != null && c > 0;
    }

    @Override
    public boolean buffetScheduleDateValid(int scheduleId) {
        Integer c = jdbc.queryForObject("""
            SELECT COUNT(*) FROM buffet_schedules 
            WHERE id=:id AND buffet_date >= CURRENT_DATE
        """, new MapSqlParameterSource("id", scheduleId), Integer.class);
        return c != null && c > 0;
    }

    @Override
    public boolean buffetPackageActiveForSchedule(int scheduleId) {
        Integer c = jdbc.queryForObject("""
            SELECT COUNT(*)
            FROM buffet_packages bp
            JOIN buffet_schedules bs ON bp.id = bs.buffet_package_id
            WHERE bs.id=:id AND bp.is_active=true
        """, new MapSqlParameterSource("id", scheduleId), Integer.class);
        return c != null && c > 0;
    }

    @Override
    public boolean userExistsAndActive(int userId) {
        Integer c = jdbc.queryForObject("""
            SELECT COUNT(*) FROM users 
            WHERE id=:id AND is_active=true
        """, new MapSqlParameterSource("id", userId), Integer.class);
        return c != null && c > 0;
    }

    @Override
    public boolean paymentVerified(int paymentId) {
        Integer c = jdbc.queryForObject("""
            SELECT COUNT(*) FROM booking_payments 
            WHERE id=:id AND payment_status='PAID'
        """, new MapSqlParameterSource("id", paymentId), Integer.class);
        return c != null && c > 0;
    }

    @Override
    public double calculateTotalPrice(int scheduleId, int guestCount) {
        Double price = jdbc.queryForObject("""
            SELECT price_per_person 
            FROM buffet_packages bp
            JOIN buffet_schedules bs ON bp.id = bs.buffet_package_id
            WHERE bs.id=:id
        """, new MapSqlParameterSource("id", scheduleId), Double.class);
        return price * guestCount;
    }

    @Override
    public List<Map<String, Object>> findBookingsByUserId(int userId) {
        return jdbc.queryForList(
                "SELECT * FROM bookings WHERE user_id=:id",
                new MapSqlParameterSource("id", userId));
    }

    @Override
    public List<Map<String, Object>> adminFilterBookings(String status, Integer userId) {
        String sql = "SELECT * FROM bookings WHERE 1=1";
        MapSqlParameterSource p = new MapSqlParameterSource();

        if (status != null) {
            sql += " AND booking_status=:status";
            p.addValue("status", status);
        }
        if (userId != null) {
            sql += " AND user_id=:uid";
            p.addValue("uid", userId);
        }

        return jdbc.queryForList(sql, p);
    }
}
