package com.shruti.quizapp.service;

import com.shruti.quizapp.model.Quiz;
import com.shruti.quizapp.model.QuizQuestion;
import com.shruti.quizapp.model.QuizResult;

import java.util.List;
import java.util.Map;

public interface QuizService {

    List<QuizQuestion> getAllQuestions();
    List<String> getAllCategories();
    List<QuizQuestion> getRandomQuestionsByCategory(String category, int numberOfQuestions);

    QuizResult evaluateQuiz(Map<Long,String> selectedAnswersMap);
}
