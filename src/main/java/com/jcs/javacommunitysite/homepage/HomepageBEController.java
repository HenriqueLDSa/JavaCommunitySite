package com.jcs.javacommunitysite.homepage;

import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.AtUri;
import dev.mccue.json.Json;
import org.jooq.DSLContext;
import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/homepagebe")
public class HomepageBEController {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public HomepageBEController(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody Json postData) {
        try {
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            AtprotoClient client = clientOpt.get();

            PostRecord post = new PostRecord(postData);

            client.createRecord(post);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Post created successfully",
                    "atUri", Objects.requireNonNull(post.getAtUri().map(AtUri::toString).orElse(null))
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error creating post: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getGroupsCategories() {
        try {
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

            return ResponseEntity.ok(Map.of(
                    "posts", posts.intoMaps(),
                    "count", posts.size()
            ));
        } catch (Exception e){
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error creating post: " + e.getMessage()
            ));
        }
    }
}