package com.jcs.javacommunitysite.pages;
import com.jcs.javacommunitysite.pages.homepage.HomepageController; // fix the package
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    private final HomepageController hpc; // or a Service, ideally

    public PageController(HomepageController homepageController) {
        this.hpc = homepageController;
    }

    @GetMapping("/browse")
    public String home(Model model) {

        model.addAttribute("groupsWithCategories", hpc.getGroupsCategories());
        return "pages/browse";
    }

    @GetMapping("/hotPosts")
    public String hotPosts() {
        return "pages/hotPosts";
    }

    @GetMapping("/newPosts")
    public String newPosts() {
        return "pages/newPosts";
    }

    @GetMapping("/newReplies")
    public String newReplies() {
        return "pages/newReplies";
    }

    @GetMapping("/groups/{groupName}")
    public String groupPage(@PathVariable String groupName, Model model) {
        groupName = groupName.replaceAll("_", " ");
        groupName = toTitleCase(groupName);
        model.addAttribute("groupName", groupName);
        return "pages/groupCategories";
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder titleCase = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                titleCase.append(c);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                titleCase.append(Character.toTitleCase(c));
                capitalizeNext = false;
            } else {
                titleCase.append(Character.toLowerCase(c));
            }
        }
        return titleCase.toString();
    }
}
