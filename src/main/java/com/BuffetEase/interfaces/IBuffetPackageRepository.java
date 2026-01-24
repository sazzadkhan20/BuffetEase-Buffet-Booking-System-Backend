package com.BuffetEase.interfaces;

import com.BuffetEase.entities.BuffetPackage;
import java.util.List;
import java.util.Map;

public interface IBuffetPackageRepository {
    void create(BuffetPackage buffetPackage);

    void update(int id, BuffetPackage buffetPackage);

    void delete(int id);

    BuffetPackage getById(int id);

    List<Map<String, Object>> getAll();
}
