package com.shruti.quizapp.model;

import lombok.Data;

import java.util.List;

@Data
public class QuizResult {

    private List<QuestionResult> questionResults;

    public QuizResult(List<QuestionResult> questionResults) {
        this.questionResults = questionResults;
    }

    public List<QuestionResult> getQuestionResults() {
        return questionResults;
    }

}

