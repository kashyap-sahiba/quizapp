package com.shruti.quizapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruti.quizapp.model.Quiz;
import com.shruti.quizapp.model.QuizQuestion;
import com.shruti.quizapp.model.QuizResult;
import com.shruti.quizapp.repository.QuizQuestionRepository;
import com.shruti.quizapp.service.QuizQuestionService;
import com.shruti.quizapp.service.QuizService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/question")
public class QuizController {

    @Autowired
    private QuizQuestionService quizQuestionService;
    @Autowired
    private QuizService quizService;

    @RequestMapping(value="/load-questions", method = RequestMethod.GET)
    public String loadQuestions(){
        try{
            quizQuestionService.loadQuestionsFromJson("src/main/resources/questions.json");
            return "Questions loaded successfully";
        }catch (IOException e){
            e.printStackTrace();
            return "Failed to load questions";
        }
    }

    @GetMapping("allQuestions")
    public ResponseEntity<List<QuizQuestion>> getAllQuestions(){
        List<QuizQuestion> questions = quizQuestionService.getAllQuestions();

        if(!questions.isEmpty()){
            return ResponseEntity.ok(questions);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("category/{category}")
    public List<QuizQuestion> getQuestionsByCategory(@PathVariable String category){
        return quizQuestionService.getQuestionsByCategory(category);
    }


    @GetMapping("/add-question-form")
    public String showAddQuestionForm(){
        return "addQuestionForm";
    }
    @PostMapping("/add-question")
    public String addQuestion(@RequestBody QuizQuestion question) {
        return quizQuestionService.addQuestion(question);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId){
        try{
            quizQuestionService.deleteQuestion(questionId);
            return ResponseEntity.ok("Question with ID "+ questionId + " deleted successfully.");
        }catch (Exception e){
return (ResponseEntity<String>) ResponseEntity.notFound();
        }
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<String> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuizQuestion updatedQuestion) {
        try {
              quizQuestionService.updateQuestion(questionId, updatedQuestion);
            return ResponseEntity.ok("Question with ID " + questionId + " updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuizQuestion> getQuestionById(@PathVariable Long questionId){
        try{
            QuizQuestion question = quizQuestionService.getQuestionById(questionId);
            return ResponseEntity.ok(question);
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all-questions-page")
    public ResponseEntity<Page<QuizQuestion>> getAllQuestions(Pageable pageable){
        Page<QuizQuestion> questions = quizQuestionService.getAllQuestionsPage(pageable);
        if(!questions.isEmpty()){
            return ResponseEntity.ok(questions);
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/quiz-selection")
    public String showQuizSelectingPage(Model model){
        List<String> categories = quizService.getAllCategories();
        model.addAttribute("categories",categories);
        return "quiz-selection";
    }

    // Inside QuizController class
    @GetMapping("/start-quiz")
    public String startQuiz(
            @RequestParam(required = false) String category,
            Model model) {

        List<QuizQuestion> quizQuestions;
        if (category != null && !category.isEmpty()) {
            // If a specific category is selected, fetch random questions from that category
            quizQuestions = quizService.getRandomQuestionsByCategory(category, 3);
        } else {
            // Handle the case when neither mixed nor a specific category is selected
            return "redirect:/question/quiz-selection"; // Redirect back to the selection page
        }

        // Create a new quiz with the fetched questions
        Quiz quiz = new Quiz();
        quiz.setTitle("Dynamic Quiz"); // You can customize the title
        quiz.setQuestions(quizQuestions);

        // Pass the created quiz to the Thymeleaf template
        model.addAttribute("quiz", quiz);

        return "quiz-start"; // Return the name of the Thymeleaf template for quiz start
    }

    @PostMapping("/submit-quiz")
    public String submitQuiz(@ModelAttribute Quiz quiz, Model model, HttpServletRequest request){
    //Process the submitted quiz

    //Create a map to store selected answers with questionId as the key
       Map<Long,String> selectedAnswersMap = new HashMap<>();

    //Iterate over questions and get the selected option for each
    for(QuizQuestion question: quiz.getQuestions()){
        String paramName = "answer_"+question.getId();
        String selectedOption = request.getParameter(paramName);

        //Put the selected ID and selected answer in the map
        selectedAnswersMap.put(question.getId(),selectedOption);
    }

    //Pass the selected answer map to the service for evaluation
    QuizResult  quizResult = quizService.evaluateQuiz(selectedAnswersMap);

    //Add the quiz and results to the model
    model.addAttribute("quiz",quiz);
    model.addAttribute("quizResult",quizResult);

    //Redirect to result page
    return "quiz-submitted";
    }
}
