package com.jcs.javacommunitysite.pages.answerpage;

import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;

@Controller
public class AnswerPageController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public AnswerPageController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @GetMapping("/answer")
    public String answer() {
        return "pages/answer";
    }
    
}
