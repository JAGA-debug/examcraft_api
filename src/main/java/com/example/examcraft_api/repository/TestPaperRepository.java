package com.example.examcraft_api.repository;

import com.example.examcraft_api.model.TestPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestPaperRepository extends JpaRepository<TestPaper, Long> {
}