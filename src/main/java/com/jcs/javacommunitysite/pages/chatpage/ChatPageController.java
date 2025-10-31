package com.jcs.javacommunitysite.pages.chatpage;

import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;

@Controller
public class ChatPageController {
    
    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public ChatPageController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @GetMapping("/chat")
    public String chat() {
        return "pages/chat";
    }
}
