package com.jcs.javacommunitysite.userauth;

import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoUnauthorized;
import com.jcs.javacommunitysite.atproto.session.AtprotoJwtSession;
import com.jcs.javacommunitysite.forms.LoginForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    
    private final AtprotoSessionService sessionService;

    public UserAuthController(AtprotoSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, Model model) {
        // Check if user is already logged in - log out if so
        if (sessionService.isAuthenticated()) {
            sessionService.clearSession();
        }

        try {
            String pdsHost = sessionService.getCurrentSession()
                    .map(AtprotoJwtSession::getPdsHost)
                    .orElse(null);
            String handle = loginForm.getHandle();
            String password = loginForm.getPassword();
            
            sessionService.createSession(pdsHost, handle, password);

            // Success
            return "redirect:/";

        } catch (AtprotoUnauthorized e) {
            model.addAttribute("loginForm", loginForm);
            model.addAttribute("errMsg", "Could not log you in. Your handle or password may be incorrect.");
            return "login";
        } catch (IOException e) {
            model.addAttribute("loginForm", loginForm);
            model.addAttribute("errMsg", "Could not log you in. Please try again later.");
            return "login";
        }
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