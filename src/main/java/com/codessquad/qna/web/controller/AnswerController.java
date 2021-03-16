package com.codessquad.qna.web.controller;

import com.codessquad.qna.web.domain.answer.Answer;
import com.codessquad.qna.web.domain.answer.AnswerRepository;
import com.codessquad.qna.web.domain.question.Question;
import com.codessquad.qna.web.domain.question.QuestionRepository;
import com.codessquad.qna.web.domain.user.User;
import com.codessquad.qna.web.utils.SessionUtils;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;


    public AnswerController(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping("/")
    public String create(@PathVariable Long questionId, @PathVariable Long id,
                         String contents, HttpSession session) throws NotFoundException {
        if (!SessionUtils.isLoginUser(session)) {
            return "redirect:/users/login-form";
        }
        User user = SessionUtils.getLoginUser(session)
                .orElseThrow(() -> new NotFoundException("No login user"));

        Question question = questionRepository.findById(questionId).orElseThrow(IllegalArgumentException::new);

        Answer answer = new Answer(user, question, contents);
        answerRepository.save(answer);
        return "redirect:/questions/" + questionId;
    }

}
