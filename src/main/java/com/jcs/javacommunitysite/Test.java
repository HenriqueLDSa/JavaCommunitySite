package com.jcs.javacommunitysite;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_ATURI;
import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoUnauthorized;
import com.jcs.javacommunitysite.atproto.records.ForumCategoryRecord;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.records.ReplyRecord;
import com.jcs.javacommunitysite.atproto.session.AtprotoJwtSession;
import dev.mccue.json.*;
import org.jooq.DSLContext;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_DID;
import static com.jcs.javacommunitysite.atproto.AtprotoUtil.getPdsHostFromHandle;
import static com.jcs.javacommunitysite.jooq.tables.Category.CATEGORY;
import static com.jcs.javacommunitysite.jooq.tables.Group.GROUP;
import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import static com.jcs.javacommunitysite.jooq.tables.Reply.REPLY;
import static dev.mccue.json.JsonDecoder.object;
import static dev.mccue.json.JsonDecoder.string;
import static dev.mccue.json.JsonDecoder.field;
import static dev.mccue.json.JsonDecoder.optionalField;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backend")
public class Test {

    private final AtprotoSessionService sessionService;
    private final DSLContext dsl;

    public Test(AtprotoSessionService sessionService, DSLContext dsl) {
        this.sessionService = sessionService;
        this.dsl = dsl;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String handle = credentials.get("handle");
            String password = credentials.get("password");
            String pdsHost = getPdsHostFromHandle(handle);;

            sessionService.createSession(pdsHost, handle, password);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "handle", handle,
                    "message", "Login successful"
            ));

        } catch (AtprotoUnauthorized e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid credentials"
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Connection error"
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        sessionService.clearSession();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {
        if (sessionService.isAuthenticated()) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "handle", Objects.requireNonNull(sessionService.getCurrentSession().map(AtprotoJwtSession::getHandle).orElse(null))
            ));
        } else {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }

    @GetMapping("/groups/categories")
    public ResponseEntity<?> getGroupsCategories() {

        try {
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            // Fetch all groups with their categories
            var groupsWithCategories = dsl.select(
                            GROUP.NAME.as("group_name"),
                            GROUP.DESCRIPTION.as("group_description"),
                            GROUP.ATURI.as("group_aturi"),
                            CATEGORY.NAME.as("category_name"),
                            CATEGORY.ATURI.as("category_aturi"),
                            CATEGORY.CATEGORY_TYPE.as("category_type"),
                            CATEGORY.DESCRIPTION.as("category_description")
                    ).from(GROUP)
                    .leftJoin(CATEGORY).on(CATEGORY.GROUP.eq(GROUP.ATURI))
                    .orderBy(GROUP.NAME.asc(), CATEGORY.NAME.asc())
                    .fetch();

            // Group the results by group
            var groupedData = groupsWithCategories.stream()
                    .collect(Collectors.groupingBy(
                            record -> Map.of(
                                    "name", Objects.requireNonNull(record.get("group_name")),
                                    "description", Objects.requireNonNull(record.get("group_description")),
                                    "aturi", Objects.requireNonNull(record.get("group_aturi"))
                            ),
                            Collectors.mapping(
                                    record -> {
                                        var categoryName = record.get("category_name");
                                        if (categoryName != null) {
                                            return Map.of(
                                                    "name", Objects.requireNonNull(record.get("category_name")),
                                                    "aturi", Objects.requireNonNull(record.get("category_aturi")),
                                                    "category_type", record.get("category_type") != null ? record.get("category_type") : "",
                                                    "description", record.get("category_description") != null ? record.get("category_description") : ""
                                            );
                                        }
                                        return null;
                                    },
                                    Collectors.filtering(
                                            Objects::nonNull,
                                            Collectors.toList()
                                    )
                            )
                    ));

            // Convert to a more structured format
            var result = groupedData.entrySet().stream()
                    .map(entry -> {
                        var group = entry.getKey();
                        var categories = entry.getValue();
                        return Map.of(
                                "group", group,
                                "categories", categories
                        );
                    })
                    .toList();

            return ResponseEntity.status(200).body(Map.of(
                    "success", true,
                    "data", result));

        } catch (Exception e){
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error fetching data: " + e.getMessage()
            ));
        }
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
                    "atUri", Objects.requireNonNull(post.getAtUri().toString())
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error creating post: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        try {
            Json posts = dsl.select()
                    .from(POST)
                    .orderBy(POST.CREATED_AT.desc())
                    .fetch()
                    .stream()
                    .map(record -> Json.objectBuilder()
                            .put("title", record.get("title", String.class))
                            .put("content", record.get("content", String.class))
                            .put("createdAt", record.get("created_at", Instant.class).toString())
                            .put("updatedAt", record.get("updated_at", Instant.class).toString())
                            .put("category", record.get("category_aturi", String.class))
                            .put("forum", record.get("forum", String.class))
                            .put("tags", Json.of(record.get("tags", String.class)))
                            .put("solution", record.get("solution", String.class))
                            .put("atUri", record.get("aturi", String.class))
                            .build()
                    )
                    .collect(
                            Json::arrayBuilder,
                            (builder, item) -> builder.add(item),
                            (builder1, builder2) -> builder1.addAll(builder2.build())
                    )
                    .build();

            return ResponseEntity.status(200).body(Map.of(
                    "success", true,
                    "data", posts
            ));

        } catch (Exception e){
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error fetching data: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/post")
    public ResponseEntity<?> getSinglePost(@RequestBody Json postData) {
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

    @PutMapping("/posts")
    public ResponseEntity<?> updatePost(@RequestBody Json postData) {
        try {
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            AtprotoClient client = clientOpt.get();

            AtUri postAtUri = field(postData, "atUri", AtUri::fromJson);

            PostRecord currentPost = new PostRecord(postAtUri, dsl);

            System.out.println("Current Post AtUri: " + currentPost.getAtUri());
            System.out.println("Current Post CreatedAt: " + currentPost.getCreatedAt());
            System.out.println("Current Post UpdatedAt: " + currentPost.getUpdatedAt());
            System.out.println("Current Post Title: " + currentPost.getTitle());
            System.out.println("Current Post Content: " + currentPost.getContent());
            System.out.println("Current Post Category: " + currentPost.getCategory());
            System.out.println("Current Post Forum: " + currentPost.getForum());
            System.out.println("Current Post Tags: " + currentPost.getTags());
            System.out.println("Current Post Solution: " + currentPost.getSolution());

            Json postDataUpdated = Json.objectBuilder()
                    .put("title", optionalField(postData, "title", string()).orElse(currentPost.getTitle()))
                    .put("content", optionalField(postData, "content", string()).orElse(currentPost.getContent()))
                    .put("category", optionalField(postData, "category", AtUri::fromJson).orElse(currentPost.getCategory()))
                    .put("createdAt", currentPost.getCreatedAt().toString())
                    .put("updatedAt", Instant.now().toString())
                    .put("tags", optionalField(postData, "tags", Json::of).orElse(Json.of(currentPost.getTags(), Json::of)))
                    .put("solution", optionalField(postData, "solution", AtUri::fromJson).orElse(currentPost.getSolution()))
                    .put("forum", optionalField(postData, "forum", string()).orElse(currentPost.getForum()))
                    .build();

            PostRecord updatedPost = new PostRecord(postDataUpdated);
            updatedPost.setAtUri(postAtUri);

            client.updateRecord(updatedPost);

            return ResponseEntity.ok(Map.of(
                    "success", true
                    // "message", "Post updated successfully",
                    // "atUri", Objects.requireNonNull(updatedPost.getAtUri().toString())
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error updating post: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/posts")
    public ResponseEntity<?> deletePost(@RequestBody Json postData) {
        try{
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            AtprotoClient client = clientOpt.get();

            AtUri postAtUri = field(postData, "atUri", AtUri::fromJson);

            System.out.println("Deleting post with AtUri: " + postAtUri);

            PostRecord post = new PostRecord(postAtUri);

            System.out.println("Deleting post record from AtProto: " + post.getAtUri());

            client.deleteRecord(post);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Post deleted successfully"
            ));

        } catch (Exception e) {
            System.out.println("Error deleting post: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error deleting post: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<?> createReply(@RequestBody Json replyData) {
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

            Json replyDataUpdated = Json.objectBuilder(object(replyData))
                    .put("content", field(replyData, "content", string()))
                    .put("createdAt", createdAt.toString())
                    .put("updatedAt", updatedAt.toString())
                    .put("root", field(replyData, "root", string()))
                    .build();

            ReplyRecord reply = new ReplyRecord(replyDataUpdated);

            client.createRecord(reply);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Reply created successfully",
                    "atUri", Objects.requireNonNull(reply.getAtUri().toString())
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error creating post: " + e.getMessage()
            ));
        }
    }

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

    @PutMapping("/reply")
    public ResponseEntity<?> updateReply(@RequestBody Json replyData) {
        try {
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            AtprotoClient client = clientOpt.get();

            AtUri replyAtUri = field(replyData, "atUri", AtUri::fromJson);

            // Fetch current reply from database using the DSLContext constructor
            ReplyRecord currentReply = new ReplyRecord(replyAtUri, dsl);

            if (currentReply.getContent() == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Reply not found"
                ));
            }

            // Build updated reply data
            Json replyDataUpdated = Json.objectBuilder()
                    .put("content", optionalField(replyData, "content", string()).orElse(currentReply.getContent()))
                    .put("createdAt", currentReply.getCreatedAt().toString())
                    .put("updatedAt", Instant.now().toString())
                    .put("root", currentReply.getRoot())
                    .build();

            ReplyRecord updatedReply = new ReplyRecord(replyDataUpdated);
            updatedReply.setAtUri(replyAtUri);

            client.updateRecord(updatedReply);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Reply updated successfully",
                    "atUri", Objects.requireNonNull(updatedReply.getAtUri().toString())
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error updating reply: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/reply")
    public ResponseEntity<?> deleteReply(@RequestBody Json replyData) {
        try{
            Optional<AtprotoClient> clientOpt = sessionService.getCurrentClient();
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            AtprotoClient client = clientOpt.get();

            AtUri replyAtUri = field(replyData, "atUri", AtUri::fromJson);

            System.out.println("Deleting reply with AtUri: " + replyAtUri);

            ReplyRecord reply = new ReplyRecord(replyAtUri);

            System.out.println("Deleting reply record from AtProto: " + reply.getAtUri());

            client.deleteRecord(reply);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Reply deleted successfully"
            ));

        } catch (Exception e) {
            System.out.println("Error deleting reply: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error deleting reply: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{categoryRKey}")
    public ResponseEntity<?> getCategoryAllPosts(@PathVariable String categoryRKey) {
        try {
            var clientOpt = sessionService.getCurrentClient();
            if (!sessionService.isAuthenticated() || clientOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Not authenticated"
                ));
            }

            AtUri categoryAtUri = new AtUri(JCS_FORUM_DID, ForumCategoryRecord.recordCollection, categoryRKey);

            var allPostsInCategory = dsl
                    .selectFrom(POST)
                    .where(POST.CATEGORY_ATURI.eq(categoryAtUri.toString()))
                    .fetch()
                    .stream()
                    .map(record -> Json.objectBuilder()
                            .put("title", record.get("title", String.class))
                            .put("content", record.get("content", String.class))
                            .put("createdAt", record.get("created_at", Instant.class).toString())
                            .put("updatedAt", record.get("updated_at", Instant.class).toString())
                            .put("category", record.get("category_aturi", String.class))
                            .put("forum", record.get("forum", String.class))
                            .put("tags", Json.of(record.get("tags", String.class)))
                            .put("solution", record.get("solution", String.class))
                            .put("atUri", record.get("aturi", String.class))
                            .put("is_deleted", record.get("is_deleted", Boolean.class))
                            .build()
                    )
                    .collect(
                            Json::arrayBuilder,
                            JsonArray.Builder::add,
                            (builder1, builder2) -> builder1.addAll(builder2.build())
                    )
                    .build();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", allPostsInCategory
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error fetching posts: " + e.getMessage()
            ));
        }
    }
}