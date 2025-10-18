package com.jcs.javacommunitysite.userauth;

import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoUnauthorized;
import com.jcs.javacommunitysite.atproto.session.AtprotoJwtSession;
import com.jcs.javacommunitysite.forms.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class UserAuthController {
    
    private final AtprotoSessionService sessionService;

    public UserAuthController(AtprotoSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String msg) {
        // Check if user is already logged in - redirect if so
        if (sessionService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("loginForm", new LoginForm());
        if (msg != null && !msg.isBlank()) {
            model.addAttribute("customMsg", msg);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(
            Model model,
            @ModelAttribute LoginForm loginForm,
            @RequestParam(required = false) String next,
            @RequestParam(required = false) String msg
    ) {
        // Check if user is already logged in - log out if so
        if (sessionService.isAuthenticated()) {
            sessionService.clearSession();
        }

        try {
            String pdsHost = "https://bsky.social"; // TODO auto get pds host from handle
            String handle = loginForm.getHandle();
            String password = loginForm.getPassword();

            sessionService.createSession(pdsHost, handle, password);

            // Success
            if (next != null && !next.isBlank()) {
                return "redirect:" + next;
            } else {
                return "redirect:/";
            }

        } catch (AtprotoUnauthorized e) {
            model.addAttribute("errMsg", "Could not log you in. Your handle or password may be incorrect.");
        } catch (IOException e) {
            model.addAttribute("errMsg", "Could not log you in. Please try again later.");
        }

        model.addAttribute("loginForm", loginForm);
        if (msg != null && !msg.isBlank()) {
            model.addAttribute("customMsg", msg);
        }
        return "login";
    }
    
    @PostMapping("/logout")
    public String logout(Model model) {
        if (sessionService.isAuthenticated()) {
            sessionService.clearSession();
        }

        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
}