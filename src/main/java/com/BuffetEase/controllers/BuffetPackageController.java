package com.BuffetEase.controllers;

import com.BuffetEase.entities.BuffetPackage;
import com.BuffetEase.dtos.BuffetPackageDTO;
import com.BuffetEase.services.BuffetPackageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buffet-package")
public class BuffetPackageController {
    private final BuffetPackageService service;

    public BuffetPackageController(BuffetPackageService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BuffetPackageDTO dto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.create(dto);
        return ResponseEntity.ok("Buffet package created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @Valid @RequestBody BuffetPackageDTO dto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.update(id, dto);
        return ResponseEntity.ok("Buffet package updated");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public BuffetPackage getOne(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return service.getAll();
    }
}
