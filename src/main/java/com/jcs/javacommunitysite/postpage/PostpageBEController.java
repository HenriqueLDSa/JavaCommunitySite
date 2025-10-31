package com.jcs.javacommunitysite.postpage;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_ATURI;
import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;

import static com.jcs.javacommunitysite.jooq.Tables.REPLY;
import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import static dev.mccue.json.JsonDecoder.*;

import dev.mccue.json.Json;

@RestController
@RequestMapping("/postpagebe/api/postpage")
public class PostpageBEController {
    private final DSLContext dsl;
    private final AtprotoSessionService sessionService;

    //Constructors

   public PostpageBEController(DSLContext dsl, AtprotoSessionService sessionService) {
       this.dsl = dsl;
       this.sessionService = sessionService;
   }

    //Create post API
    //Create post API

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

            Instant createdAt = Instant.now();
            Instant updatedAt = createdAt;

            Json postDataUpdated = Json.objectBuilder(object(postData))
                    .put("createdAt", createdAt.toString())
                    .put("updatedAt", updatedAt.toString())
                    .put("forum", JCS_FORUM_ATURI)
                    .build();


            PostRecord post = new PostRecord(postDataUpdated);

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

    //Get post API
    @GetMapping("/post")
    public ResponseEntity<?> getPost(@RequestBody Json postData) {
        try {
            if (sessionService.getCurrentClient().isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }
            AtUri atUri = field(postData, "atUri", AtUri::fromJson);
            var post = dsl.selectFrom(POST)
                    .where(POST.ATURI.eq(atUri.toString()))  // <-- was at_uri
                    .fetchOneMap();

            if (post == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Post not found"
                ));
            }

            // NOTE: only fields that actually exist in your schema
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("atUri", post.get("aturi"));
            postMap.put("title", post.get("title"));
            postMap.put("content", post.get("content"));
            postMap.put("createdAt", String.valueOf(post.get("created_at")));
            postMap.put("updatedAt", post.get("updated_at") != null ? String.valueOf(post.get("updated_at")) : null);
            postMap.put("tags", post.get("tags"));
            postMap.put("category_aturi", post.get("category_aturi"));

            // Convert JSONB tags to a proper serializable format
            Object tagsObj = post.get("tags");
            if (tagsObj != null) {
                postMap.put("tags", tagsObj.toString());
            } else {
                postMap.put("tags", null);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Post fetched successfully",
                    "post", postMap
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error fetching post",
                    "error", e.getMessage()
            ));
        }
    }
    //Update post API


    //Delete post API


    //Get votes from post API

    //Get replies for post API
    @GetMapping("/reply")
    public ResponseEntity<?> getPostReplies(@RequestBody Json postData) {
        try {
            Json postReplies = dsl.select()
                    .from(REPLY)
                    .where(REPLY.ROOT.eq(field(postData, "atUri", string())))
                    .orderBy(REPLY.CREATED_AT.desc())
                    .fetch()
                    .stream()
                    .map(record -> Json.objectBuilder()
                            .put("content", record.get("content", String.class))
                            .put("createdAt", record.get("created_at", Instant.class).toString())
                            .put("updatedAt", record.get("updated_at", Instant.class).toString())
                            .put("root", record.get("root", String.class))
                            .build()
                    )
                    .collect(
                        Json::arrayBuilder,
                        (builder, item) -> builder.add(item),
                        (builder1, builder2) -> builder1.addAll(builder2.build())
                    )
                    .build();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", postReplies
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error getting replies: " + e.getMessage()
            ));
        }
    }


}
