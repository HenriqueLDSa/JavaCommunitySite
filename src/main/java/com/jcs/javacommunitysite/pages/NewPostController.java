package com.jcs.javacommunitysite.pages;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.forms.NewPostForm;
import dev.mccue.json.Json;
import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_DID;
import static com.jcs.javacommunitysite.jooq.tables.Category.CATEGORY;
import static com.jcs.javacommunitysite.jooq.tables.CategoryGroup.CATEGORY_GROUP;

@Controller
public class NewPostController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public NewPostController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }


    @GetMapping("/newPost")
    public String newPost(Model model) {
        // Kick to login screen if not logged in
        if (!sessionService.isAuthenticated()) {
            return "redirect:/login?next=/newPost&msg=To create a post, please log in.";
        }

        var groups = dsl.select(
                    CATEGORY_GROUP.NAME.as("group_name"),
                    CATEGORY_GROUP.ID.as("group_id")
            ).from(CATEGORY_GROUP)
            .orderBy(CATEGORY_GROUP.NAME.asc())
            .fetch();


        var categoryGroups = groups.stream()
                .map(record -> Map.of(
                        "name", record.get("group_name"),
                        "id", record.get("group_id").toString()
                ))
                .toList();

        model.addAttribute("groups", categoryGroups);
        model.addAttribute("postForm", new NewPostForm());
        return "newpost";
    }

    @PostMapping("/newPost")
    public String newPost(@ModelAttribute NewPostForm newPostForm, Model model) {
        try {
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();

            if (clientOpt.isEmpty() || !sessionService.isAuthenticated()) {
                return "redirect:/login?next=/newPost";
            }

            AtprotoClient client = clientOpt.get();

            PostRecord post = new PostRecord(
                newPostForm.getTitle(),
                newPostForm.getContent(),
                new AtUri("at://did:plc:bwh2fxasbh3ieuxjyym7bmeh/dev.fudgeu.experimental.atforumv1.forum.category/3lyyxv7wodj2o"),
                JCS_FORUM_DID
            );
            client.createRecord(post);

            return "redirect:/browse";

        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/newPost/htmx/getCategories")
    public String getCategories(Model model, @RequestParam String group) {
        var categories = dsl.select(
                    CATEGORY.NAME.as("category_name"),
                    CATEGORY.ATURI.as("category_aturi")
            ).from(CATEGORY).where(CATEGORY.CATEGORY_GROUP_ID.eq(UUID.fromString(group)))
            .orderBy(CATEGORY.NAME.asc())
            .fetch();

        var categoryData = categories.stream()
            .map(record -> Map.of(
                    "name", record.get("category_name"),
                    "aturi", record.get("category_aturi")
            ))
            .toList();

        model.addAttribute("categories", categoryData);
        return "newpost_categoryoptions";
    }
}
