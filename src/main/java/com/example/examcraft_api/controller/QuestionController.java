package com.example.examcraft_api.controller;

import com.example.examcraft_api.model.Question;
import com.example.examcraft_api.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public Question addQuestion(@Valid @RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/difficulty/{difficulty}")
    public List<Question> getQuestionsByDifficulty(
            @PathVariable String difficulty) {

        return questionService.getQuestionsByDifficulty(difficulty);
    }

    @GetMapping("/unit/{unitId}")
    public List<Question> getQuestionsByUnit(
            @PathVariable Long unitId) {

        return questionService.getQuestionsByUnit(unitId);
    }
}