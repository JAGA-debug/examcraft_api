package com.example.examcraft_api.controller;

import com.example.examcraft_api.model.TestPaper;
import com.example.examcraft_api.service.TestPaperService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test-papers")
public class TestPaperController {

    private final TestPaperService testPaperService;

    public TestPaperController(TestPaperService testPaperService) {
        this.testPaperService = testPaperService;
    }

    @PostMapping("/generate")
    public TestPaper generateTestPaper(
            @Valid @RequestBody TestPaper testPaper) {

        return testPaperService.generateTestPaper(testPaper);
    }

    @GetMapping
    public List<TestPaper> getAllTestPapers() {
        return testPaperService.getAllTestPapers();
    }

    @GetMapping("/{id}")
    public TestPaper getTestPaperById(
            @PathVariable Long id) {

        return testPaperService.getTestPaperById(id);
    }

    @GetMapping("/usage-frequency")
    public Map<Long, Long> getQuestionUsageFrequency() {
        return testPaperService.getQuestionUsageFrequency();
    }
}