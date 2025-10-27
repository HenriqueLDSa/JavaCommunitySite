package com.jcs.javacommunitysite.pages.searchpage;

import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;

@Controller
public class SearchPageController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public SearchPageController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @GetMapping("/search")
    public String search() {
        return "pages/search";
    }
    
}
