package com.BuffetEase.interfaces;
import java.util.List;
import java.util.Map;
import com.BuffetEase.entities.BuffetSchedule;

public interface IBuffetScheduleRepository {
    void create(BuffetSchedule schedule);

    void update(int id, BuffetSchedule schedule);

    void delete(int id);

    BuffetSchedule getById(int id);

    List<Map<String, Object>> getAll();

    List<BuffetSchedule> getPaginated(int page, int size);
}
