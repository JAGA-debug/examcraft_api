package com.example.examcraft_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student name cannot be null")
    private String studentName;

    @PositiveOrZero(message = "Score cannot be negative")
    private int score;

    @PositiveOrZero(message = "Total marks cannot be negative")
    private int totalMarks;

    @ManyToOne
    @JoinColumn(name = "test_paper_id")
    private TestPaper testPaper;

    public Attempt() {
    }

    public Attempt(String studentName, int score,
                   int totalMarks, TestPaper testPaper) {
        this.studentName = studentName;
        this.score = score;
        this.totalMarks = totalMarks;
        this.testPaper = testPaper;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public TestPaper getTestPaper() {
        return testPaper;
    }

    public void setTestPaper(TestPaper testPaper) {
        this.testPaper = testPaper;
    }
}