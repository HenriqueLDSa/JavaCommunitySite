package com.jcs.javacommunitysite;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoUnauthorized;
import com.jcs.javacommunitysite.atproto.records.ForumCategoryRecord;
import com.jcs.javacommunitysite.atproto.records.ForumGroupRecord;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.session.AtprotoAuthSession;
import com.jcs.javacommunitysite.atproto.session.AtprotoJwtSession;
import com.jcs.javacommunitysite.forms.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_DID;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String test(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, Model model) {
        // check login stuffs
        var loginValid = false;

        if (!loginValid) {
            model.addAttribute("loginForm", loginForm);
            model.addAttribute("errMsg", "Could not log you in. Your handle or password may be incorrect.");
            return "login";
        }

        return "redirect:/";
    }

}
