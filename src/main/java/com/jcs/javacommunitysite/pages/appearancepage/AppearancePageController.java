package com.jcs.javacommunitysite.pages.appearancepage;

import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.AtprotoUtil;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.util.UserInfo;

import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class AppearancePageController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public AppearancePageController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @GetMapping("/appearance")
    public String appearance(Model model) {

        if (sessionService.isAuthenticated()) {
            var clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isPresent()) {
                try {
                    AtprotoClient client = clientOpt.get();
                    String handle = client.getSession().getHandle();
                    var profile = AtprotoUtil.getBskyProfile(handle);
                    String userDid = profile.get("did").toString().replace("\"", "");

                    model.addAttribute("loggedIn", true);
                    model.addAttribute("user", UserInfo.getSelfFromDb(dsl, sessionService));
                } catch (IOException e) {
                    System.err.println("Error fetching user posts: " + e.getMessage());
                }
            }
        } else {
            model.addAttribute("loggedIn", false);
        }

            return "pages/appearance";
    }
}