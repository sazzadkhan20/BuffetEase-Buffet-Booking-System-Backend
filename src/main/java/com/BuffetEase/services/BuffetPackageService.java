package com.BuffetEase.services;

import com.BuffetEase.entities.BuffetPackage;
import com.BuffetEase.dtos.BuffetPackageDTO;
import com.BuffetEase.interfaces.IBuffetPackageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;



@Service
public class BuffetPackageService {
    private final IBuffetPackageRepository repository;

    public BuffetPackageService(IBuffetPackageRepository repository) {
        this.repository = repository;
    }

    public void create(BuffetPackageDTO dto) {
        repository.create(toEntity(dto));
    }

    public void update(int id, BuffetPackageDTO dto) {
        repository.update(id, toEntity(dto));
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public BuffetPackage getById(int id) {
        return repository.getById(id);
    }

    public List<Map<String, Object>> getAll() {
        return repository.getAll();
    }

    // Manual mapping
    private BuffetPackage toEntity(BuffetPackageDTO dto) {
        BuffetPackage p = new BuffetPackage();
        p.setId(dto.getId());
        p.setPackageName(dto.getPackageName());
        p.setDescription(dto.getDescription());
        p.setPricePerPerson(dto.getPricePerPerson());
        p.setStartTime(dto.getStartTime());
        p.setEndTime(dto.getEndTime());
        p.setMaxCapacity(dto.getMaxCapacity());
        p.setIsActive(dto.getIsActive());
        return p;
    }
}
