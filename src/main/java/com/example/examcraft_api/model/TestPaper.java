package com.example.examcraft_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TestPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Total questions cannot be null")
    @Positive(message = "Total questions must be positive")
    private Integer totalQuestions;

    @NotNull(message = "Easy count cannot be null")
    @Positive(message = "Easy count must be positive")
    private Integer easyCount;

    @NotNull(message = "Medium count cannot be null")
    @Positive(message = "Medium count must be positive")
    private Integer mediumCount;

    @NotNull(message = "Hard count cannot be null")
    @Positive(message = "Hard count must be positive")
    private Integer hardCount;

    @ManyToMany
    @JoinTable(
            name = "test_paper_questions",
            joinColumns = @JoinColumn(name = "test_paper_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

    public TestPaper() {
    }

    public TestPaper(String title, int totalQuestions,
                     int easyCount, int mediumCount, int hardCount) {
        this.title = title;
        this.totalQuestions = totalQuestions;
        this.easyCount = easyCount;
        this.mediumCount = mediumCount;
        this.hardCount = hardCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getEasyCount() {
        return easyCount;
    }

    public void setEasyCount(Integer easyCount) {
        this.easyCount = easyCount;
    }

    public Integer getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(Integer mediumCount) {
        this.mediumCount = mediumCount;
    }

    public Integer getHardCount() {
        return hardCount;
    }

    public void setHardCount(Integer hardCount) {
        this.hardCount = hardCount;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}