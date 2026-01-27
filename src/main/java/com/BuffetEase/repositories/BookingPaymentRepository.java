package com.BuffetEase.repositories;

import com.BuffetEase.entities.BookingPaymentEntity;
import com.BuffetEase.interfaces.IBookingPaymentRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public class BookingPaymentRepository implements IBookingPaymentRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final BuffetBookingRepository bookingRepository;

    public BookingPaymentRepository(NamedParameterJdbcTemplate jdbc
    , BuffetBookingRepository bookingRepository) {
        this.jdbc = jdbc;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Map<String, Object> getPaymentInitData(int bookingId) {

        String sql = """
        SELECT 
            b.total_price,
            u.name,
            u.phone,
            uc.email
        FROM bookings b
        JOIN users u ON b.user_id = u.id
        JOIN user_credentials uc ON uc.user_id = u.id
        WHERE b.id = :bookingId
        """;

        return jdbc.queryForMap(sql,
                new MapSqlParameterSource("bookingId", bookingId));
    }

    @Override
    public void create(BookingPaymentEntity p) {
        jdbc.update("""
            INSERT INTO booking_payments
            (booking_id, payment_method, payment_status, amount, transaction_id)
            VALUES (:bookingId, :method, :status, :amount, :txn)
        """,
                new MapSqlParameterSource()
                        .addValue("bookingId", p.getBookingId())
                        .addValue("method", p.getPaymentMethod())
                        .addValue("status", p.getPaymentStatus())
                        .addValue("amount", p.getAmount())
                        .addValue("txn", p.getTransactionId()));
    }

    @Override
    public void updateStatus(String transactionId, String status) {
        jdbc.update("""
            UPDATE booking_payments
            SET payment_status = :status, payment_time = NOW()
            WHERE transaction_id = :txn
        """,
                new MapSqlParameterSource()
                        .addValue("status", status)
                        .addValue("txn", transactionId));
        int id = this.findByTransactionId(transactionId);
        this.bookingRepository.updateBookingStatus(id,"CONFIRMED");
    }

    @Override
    public List<Map<String, Object>> findAll() {
        return jdbc.queryForList("SELECT * FROM booking_payments",
                new MapSqlParameterSource());
    }

    @Override
    public Map<String, Object> findByPaymentId(int id) {
        return jdbc.queryForMap("""
            SELECT * FROM booking_payments WHERE id = :id
        """, new MapSqlParameterSource("id", id));
    }

    @Override
    public List<Map<String, Object>> findByBookingId(int bookingId) {
        return jdbc.queryForList("""
            SELECT * FROM booking_payments WHERE booking_id = :bookingId
        """, new MapSqlParameterSource("bookingId", bookingId));
    }

    private int findByTransactionId(String transactionId) {
        String sql = """
        SELECT booking_id from booking_payments WHERE transaction_id = :transactionId
        """;
        return jdbc.queryForObject(sql,new MapSqlParameterSource()
                .addValue("transactionId", transactionId),Integer.class);
    }
}
