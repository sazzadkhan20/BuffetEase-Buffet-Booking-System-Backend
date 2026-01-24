package com.BuffetEase.services;

import com.BuffetEase.dtos.BuffetScheduleDTO;
import com.BuffetEase.entities.BuffetSchedule;
import com.BuffetEase.interfaces.IBuffetScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BuffetScheduleService {

    private IBuffetScheduleRepository repository;

    public BuffetScheduleService(IBuffetScheduleRepository repository) {
        this.repository = repository;
    }

    public void create(BuffetScheduleDTO dto) {
        repository.create(toEntity(dto));
    }

    public void update(int id, BuffetScheduleDTO dto) {
        repository.update(id, toEntity(dto));
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public BuffetSchedule getById(int id) {
        return repository.getById(id);
    }

    public List<Map<String, Object>> getAll() {
        return repository.getAll();
    }

    public List<BuffetSchedule> getPaginated(int page, int size) {
        return repository.getPaginated(page, size);
    }

    private BuffetSchedule toEntity(BuffetScheduleDTO dto) {
        BuffetSchedule s = new BuffetSchedule();
        s.setBuffetPackageId(dto.getBuffetPackageId());
        s.setBuffetDate(dto.getBuffetDate());
        s.setAvailableCapacity(dto.getAvailableCapacity());
        s.setBookingCutoffTime(dto.getBookingCutoffTime());
        s.setOpen(dto.isOpen());
        return s;
    }
}
