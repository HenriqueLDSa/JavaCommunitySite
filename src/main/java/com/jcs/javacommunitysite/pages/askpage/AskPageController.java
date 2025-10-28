package com.jcs.javacommunitysite.pages.askpage;

import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;

@Controller
public class AskPageController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public AskPageController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @GetMapping("/ask")
    public String ask(
            Model model
    ) {
        model.addAttribute("postForm", new NewPostForm());
        return "pages/ask";
    }

    @PostMapping("/ask")
    public String createPost(@ModelAttribute NewPostForm postForm, Model model) {
        

        return "";
    }
}
