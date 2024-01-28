package com.shruti.quizapp.repository;

import com.shruti.quizapp.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {
    List<QuizQuestion> findByCategory(String category);

    @Query("SELECT DISTINCT q.category FROM QuizQuestion q")
    List<String> findAllCategories();


    @Query(value = "SELECT * FROM quiz_question WHERE category = :category ORDER BY RANDOM() LIMIT :numberOfQuestions", nativeQuery = true)
    List<QuizQuestion> findRandomQuestionsByCategory(
            @Param("category") String category,
            @Param("numberOfQuestions") int numberOfQuestions
    );

    @Query(value = "SELECT answer FROM quiz_question WHERE  id= :id", nativeQuery = true)
    String findAnswerByQuestionId(@Param("id")Long id);
}
