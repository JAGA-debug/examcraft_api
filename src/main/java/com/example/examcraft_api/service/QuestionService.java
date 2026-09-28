package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Question;

import java.util.List;

public interface QuestionService {

    Question addQuestion(Question question);

    List<Question> getAllQuestions();

    List<Question> getQuestionsByDifficulty(String difficulty);

    List<Question> getQuestionsByUnit(Long unitId);
}