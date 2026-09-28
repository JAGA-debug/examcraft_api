package com.example.examcraft_api.controller;

import com.example.examcraft_api.model.Attempt;
import com.example.examcraft_api.service.AttemptService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping("/submit")
    public Attempt submitAttempt(
            @RequestParam String studentName,
            @RequestParam Long testPaperId,
            @Valid @RequestBody Map<Long, String> answers) {

        return attemptService.submitAttempt(
                studentName,
                testPaperId,
                answers
        );
    }

    @GetMapping
    public List<Attempt> getAllAttempts() {
        return attemptService.getAllAttempts();
    }

    @GetMapping("/{id}")
    public Attempt getAttemptById(
            @PathVariable Long id) {

        return attemptService.getAttemptById(id);
    }
}