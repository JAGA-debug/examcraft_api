package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Attempt;

import java.util.List;
import java.util.Map;

public interface AttemptService {

    Attempt submitAttempt(
            String studentName,
            Long testPaperId,
            Map<Long, String> answers
    );

    List<Attempt> getAllAttempts();

    Attempt getAttemptById(Long id);
}