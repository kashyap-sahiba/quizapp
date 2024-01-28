package com.shruti.quizapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruti.quizapp.model.QuizQuestion;
import com.shruti.quizapp.repository.QuizQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class QuizQuestionService {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    public void loadQuestionsFromJson(String jsonFilePath) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        List<QuizQuestion>  questions = objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<QuizQuestion>>() {});
        quizQuestionRepository.saveAll(questions);
    }

    public List<QuizQuestion> getAllQuestions() {
        return quizQuestionRepository.findAll();
    }

    public List<QuizQuestion> getQuestionsByCategory(String category){
        return quizQuestionRepository.findByCategory(category);
    }

    public String addQuestion(QuizQuestion question) {
        try {
            quizQuestionRepository.save(question);
            return "Question added successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add question.";
        }
    }

    public void deleteQuestion(Long questionId){
        quizQuestionRepository.deleteById(questionId);
    }

    public void updateQuestion(Long questionId , QuizQuestion updatedQuestion){
        if(quizQuestionRepository.existsById(questionId)){
            updatedQuestion.setId(questionId);
            quizQuestionRepository.save(updatedQuestion);
        }else{
            throw new EntityNotFoundException("Question with ID "+questionId+" not found");
        }
    }

    public QuizQuestion getQuestionById(Long questionId){
        return quizQuestionRepository.findById(questionId)
                .orElseThrow(()-> new EntityNotFoundException("Question with ID "+questionId+" not found"));

    }

    public Page<QuizQuestion> getAllQuestionsPage(Pageable pageable){
        return quizQuestionRepository.findAll(pageable);
    }

}
