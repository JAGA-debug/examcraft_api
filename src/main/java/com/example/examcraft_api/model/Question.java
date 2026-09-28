package com.example.examcraft_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Question text cannot be null")
    private String questionText;

    @NotNull(message = "Topic cannot be null")
    private String topic;

    @NotNull(message = "Difficulty cannot be null")
    private String difficulty;

    @NotNull(message = "Option A cannot be null")
    private String optionA;

    @NotNull(message = "Option B cannot be null")
    private String optionB;

    @NotNull(message = "Option C cannot be null")
    private String optionC;

    @NotNull(message = "Option D cannot be null")
    private String optionD;

    @NotNull(message = "Correct answer cannot be null")
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public Question() {
    }

    public Question(String questionText, String topic, String difficulty,
                    String optionA, String optionB, String optionC,
                    String optionD, String correctAnswer, Unit unit) {

        this.questionText = questionText;
        this.topic = topic;
        this.difficulty = difficulty;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}