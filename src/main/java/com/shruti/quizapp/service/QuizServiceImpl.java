package com.shruti.quizapp.service;

import com.shruti.quizapp.model.QuestionResult;
import com.shruti.quizapp.model.QuizQuestion;
import com.shruti.quizapp.model.QuizResult;
import com.shruti.quizapp.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Override
    public List<QuizQuestion> getAllQuestions() {
        return quizQuestionRepository.findAll();
    }

   @Override
    public List<String> getAllCategories() {
        return quizQuestionRepository.findAllCategories();
    }

    @Override
    public List<QuizQuestion> getRandomQuestionsByCategory(String category, int numberOfQuestions) {
        // Implement logic to fetch random questions from a specific category
        // Use a query or custom logic to randomize questions within the specified category
        // Ensure that the number of questions fetched is equal to numberOfQuestions
        // Return the list of random questions
        return quizQuestionRepository.findRandomQuestionsByCategory(category, numberOfQuestions);
    }

    @Override
    public QuizResult evaluateQuiz(Map<Long,String> selectedAnswersMap){
        Map<Long,String> correctAnswersMap = fetchCorrectAnswersFromDatabase(selectedAnswersMap.keySet());
       List<QuestionResult> questionResults = calculateQuestionResults(selectedAnswersMap,correctAnswersMap);
       return new QuizResult(questionResults);
        
    }

    private List<QuestionResult> calculateQuestionResults(Map<Long,String> selectedAnswersMap,Map<Long,String> correctAnswerMap){
        List<QuestionResult> questionResults = new ArrayList<>();

        for(Long questionId : selectedAnswersMap.keySet()){
            String selectedAnswer = selectedAnswersMap.get(questionId);
            String correctAnswer = correctAnswerMap.get(questionId);

            boolean isCorrect = selectedAnswer.equals(correctAnswer);

            QuestionResult questionResult = new QuestionResult(questionId,selectedAnswer,correctAnswer,isCorrect);
            questionResults.add(questionResult);
        }
        return questionResults;
    }

    private Map<Long,String> fetchCorrectAnswersFromDatabase(Set<Long> question){
        Map<Long,String> correctAnswersMap = new HashMap<>();
        for(Long id : question){
            String correctAnswer = quizQuestionRepository.findAnswerByQuestionId(id);
            correctAnswersMap.put(id,correctAnswer);
        }

        return correctAnswersMap;
    }

}
