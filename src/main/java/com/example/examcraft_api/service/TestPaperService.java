package com.example.examcraft_api.service;

import com.example.examcraft_api.model.TestPaper;

import java.util.List;
import java.util.Map;

public interface TestPaperService {

    TestPaper generateTestPaper(TestPaper testPaper);

    List<TestPaper> getAllTestPapers();

    TestPaper getTestPaperById(Long id);

    Map<Long, Long> getQuestionUsageFrequency();
}