package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Attempt;
import com.example.examcraft_api.model.Question;
import com.example.examcraft_api.model.TestPaper;
import com.example.examcraft_api.repository.AttemptRepository;
import com.example.examcraft_api.repository.TestPaperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AttemptServiceImplementation implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final TestPaperRepository testPaperRepository;

    public AttemptServiceImplementation(
            AttemptRepository attemptRepository,
            TestPaperRepository testPaperRepository) {

        this.attemptRepository = attemptRepository;
        this.testPaperRepository = testPaperRepository;
    }

    @Override
    public Attempt submitAttempt(
            String studentName,
            Long testPaperId,
            Map<Long, String> answers) {

        TestPaper testPaper = testPaperRepository.findById(testPaperId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Test Paper not found"
                        )
                );

        int score = 0;

        List<Question> questions = testPaper.getQuestions();

        for (Question question : questions) {

            String submittedAnswer =
                    answers.get(question.getId());

            if (submittedAnswer != null &&
                    submittedAnswer.equalsIgnoreCase(
                            question.getCorrectAnswer())) {

                score++;
            }
        }

        Attempt attempt = new Attempt();

        attempt.setStudentName(studentName);
        attempt.setScore(score);
        attempt.setTotalMarks(questions.size());
        attempt.setTestPaper(testPaper);

        return attemptRepository.save(attempt);
    }

    @Override
    public List<Attempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    @Override
    public Attempt getAttemptById(Long id) {

        return attemptRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Attempt not found"
                        )
                );
    }
}