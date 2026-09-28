package com.example.examcraft_api.repository;

import com.example.examcraft_api.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
}