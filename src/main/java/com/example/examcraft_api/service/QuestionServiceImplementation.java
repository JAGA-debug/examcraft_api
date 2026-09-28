package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Question;
import com.example.examcraft_api.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImplementation implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImplementation(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    @Override
    public List<Question> getQuestionsByUnit(Long unitId) {
        return questionRepository.findByUnitId(unitId);
    }
}