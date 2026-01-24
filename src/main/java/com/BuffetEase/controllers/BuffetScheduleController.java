package com.BuffetEase.controllers;

import com.BuffetEase.dtos.BuffetScheduleDTO;
import com.BuffetEase.entities.BuffetSchedule;
import com.BuffetEase.services.BuffetScheduleService;
import org.jspecify.annotations.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buffet-schedule")
public class BuffetScheduleController {
    private BuffetScheduleService service;

    public BuffetScheduleController(BuffetScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BuffetScheduleDTO dto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.create(dto);
        return ResponseEntity.ok("Buffet schedule created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @Valid @RequestBody BuffetScheduleDTO dto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.update(id, dto);
        return ResponseEntity.ok("Buffet schedule updated");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public BuffetSchedule getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping
    public List<Map<String, @Nullable Object>> getAll() {
        return service.getAll();
    }

    @GetMapping("/page/{page}/size/{size}")
    public List<BuffetSchedule> getPaginated(@PathVariable int page,
                                             @PathVariable int size) {
        return service.getPaginated(page, size);
    }
}
