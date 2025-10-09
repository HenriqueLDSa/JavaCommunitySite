package com.jcs.javacommunitysite.homepage;

import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.AtUri;
import dev.mccue.json.Json;
import org.jooq.DSLContext;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import static dev.mccue.json.JsonDecoder.field;
import static dev.mccue.json.JsonDecoder.string;

@RestController
@RequestMapping("/homepage")
public class HomepageController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public HomepageController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @PostMapping("/posts")
    public String createPost(Model model) {
        try {
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();

            if (clientOpt.isEmpty()) {
                return "error not authenticated";
            }

            AtprotoClient client = clientOpt.get();

            /*
                logic to fetch data from form into Json variable
            */

            Json postDataJson = Json.objectBuilder()
                    .put("", "")
                    .put("", "")
                    .put("", "")
                    .toJson();

            PostRecord post = new PostRecord(postDataJson);
            client.createRecord(post);

            return "success new post";

        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/posts")
    public String getGroupsCategories(Model model) {

        var posts = dsl.select(
                        POST.ID,
                        POST.TITLE,
                        POST.CONTENT,
                        POST.CREATED_AT,
                        POST.UPDATED_AT,
                        POST.USER_ID,
                        POST.COMMUNITY_ID,
                        POST.CATEGORY_ID,
                        POST.ATURI,
                        POST.TAGS.cast(String.class).as("tags")
                ).from(POST)
                .orderBy(POST.CREATED_AT.desc())
                .fetch();

        return "this will be the form that has the groups and categories";
    }
}