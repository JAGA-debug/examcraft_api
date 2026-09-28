package com.example.examcraft_api.repository;

import com.example.examcraft_api.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByDifficulty(String difficulty);

    List<Question> findByUnitId(Long unitId);
}