package com.shruti.quizapp.model;

import lombok.Data;

@Data
public class QuestionResult {
    private Long questionId;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public QuestionResult(Long questionId, String selectedAnswer, String correctAnswer, boolean isCorrect) {
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
