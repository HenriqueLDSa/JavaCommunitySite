package com.jcs.javacommunitysite.pages;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/groups/{groupName}")
    public String groupPage(@PathVariable String groupName, Model model) {
        model.addAttribute("groupName", groupName);
        return "pages/groupCategories";
    }

}
