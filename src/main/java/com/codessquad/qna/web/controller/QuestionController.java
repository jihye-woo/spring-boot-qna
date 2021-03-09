package com.codessquad.qna.web.controller;

import com.codessquad.qna.web.exception.QuestionListIndexOutOfBoundException;
import com.codessquad.qna.web.domain.question.Question;
import com.codessquad.qna.web.validation.CastValidation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionController {

    private List<Question> questions = new ArrayList<>();

    @GetMapping("/questions/{index}")
    public String getQuestionDetail(@PathVariable("index") String stringIdx, Model model){
        int index = getIndex(stringIdx);
        Question question = questions.get(index);
        model.addAttribute("question", question);
        return "/qna/show";
    }

    @PostMapping("/questions")
    public String create(Question question){
        question.setId(nextId());
        questions.add(question);
        return "redirect:/";
    }

    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("questions", questions);
        return "index";
    }

    private int getIndex(String stringIdx){
        int index = CastValidation.stringToInt(stringIdx) - 1;
        indexValidation(index);
        return index;
    }

    private int nextId(){
        return questions.size() + 1;
    }

    private void indexValidation(int index){
        if(index < 0 || index > questions.size()){
            throw new QuestionListIndexOutOfBoundException(index, questions.size());
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(){
        return "/errors/invalidInput";
    }

}
