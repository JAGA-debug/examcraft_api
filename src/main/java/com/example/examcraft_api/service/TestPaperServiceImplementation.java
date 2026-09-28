package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Question;
import com.example.examcraft_api.model.TestPaper;
import com.example.examcraft_api.repository.QuestionRepository;
import com.example.examcraft_api.repository.TestPaperRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestPaperServiceImplementation implements TestPaperService {

    private final TestPaperRepository testPaperRepository;
    private final QuestionRepository questionRepository;

    public TestPaperServiceImplementation(
            TestPaperRepository testPaperRepository,
            QuestionRepository questionRepository) {

        this.testPaperRepository = testPaperRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public TestPaper generateTestPaper(TestPaper testPaper) {

        int totalQuestions = testPaper.getTotalQuestions();
        int easyCount = testPaper.getEasyCount();
        int mediumCount = testPaper.getMediumCount();
        int hardCount = testPaper.getHardCount();

        // Check difficulty counts
        if (easyCount + mediumCount + hardCount != totalQuestions) {
            throw new RuntimeException(
                    "Easy, Medium and Hard counts must equal total questions"
            );
        }

        // Get questions by difficulty
        List<Question> easyQuestions =
                new ArrayList<>(
                        questionRepository.findByDifficulty("Easy")
                );

        List<Question> mediumQuestions =
                new ArrayList<>(
                        questionRepository.findByDifficulty("Medium")
                );

        List<Question> hardQuestions =
                new ArrayList<>(
                        questionRepository.findByDifficulty("Hard")
                );

        // Check whether enough questions are available
        if (easyQuestions.size() < easyCount) {
            throw new RuntimeException(
                    "Not enough Easy questions available"
            );
        }

        if (mediumQuestions.size() < mediumCount) {
            throw new RuntimeException(
                    "Not enough Medium questions available"
            );
        }

        if (hardQuestions.size() < hardCount) {
            throw new RuntimeException(
                    "Not enough Hard questions available"
            );
        }

        // Randomize questions
        Collections.shuffle(easyQuestions);
        Collections.shuffle(mediumQuestions);
        Collections.shuffle(hardQuestions);

        List<Question> selectedQuestions = new ArrayList<>();

        // Select required number from each difficulty
        selectedQuestions.addAll(
                easyQuestions.subList(0, easyCount)
        );

        selectedQuestions.addAll(
                mediumQuestions.subList(0, mediumCount)
        );

        selectedQuestions.addAll(
                hardQuestions.subList(0, hardCount)
        );

        // Randomize the final question order
        Collections.shuffle(selectedQuestions);

        testPaper.setQuestions(selectedQuestions);

        return testPaperRepository.save(testPaper);
    }

    @Override
    public List<TestPaper> getAllTestPapers() {
        return testPaperRepository.findAll();
    }

    @Override
    public TestPaper getTestPaperById(Long id) {

        return testPaperRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Test Paper not found"
                        )
                );
    }

    @Override
    public Map<Long, Long> getQuestionUsageFrequency() {

        List<TestPaper> testPapers =
                testPaperRepository.findAll();

        Map<Long, Long> usageFrequency =
                new HashMap<>();

        for (TestPaper testPaper : testPapers) {

            for (Question question : testPaper.getQuestions()) {

                Long questionId = question.getId();

                usageFrequency.put(
                        questionId,
                        usageFrequency.getOrDefault(questionId, 0L) + 1
                );
            }
        }

        return usageFrequency;
    }
}