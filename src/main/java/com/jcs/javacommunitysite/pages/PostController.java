package com.jcs.javacommunitysite.pages;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoUnauthorized;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import com.jcs.javacommunitysite.atproto.records.ReplyRecord;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.forms.NewReplyForm;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Node;
import org.commonmark.node.Heading;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import static com.jcs.javacommunitysite.jooq.tables.Reply.REPLY;

@Controller
public class PostController {

    private final DSLContext dsl;
    private final AtprotoSessionService sessionService;

    //Constructors

    public PostController(DSLContext dsl, AtprotoSessionService sessionService) {
        this.dsl = dsl;
        this.sessionService = sessionService;
    }

    @GetMapping("/post/{user}/{rkey}")
    public String post(Model model, @PathVariable("user") String user, @PathVariable("rkey") String rkey) {
        AtUri aturi = new AtUri(user, PostRecord.recordCollection, rkey);

        // Get post
        Map<String, Object> post;
        try {
            post = dsl.selectFrom(POST)
                    .where(POST.ATURI.eq(aturi.toString()))
                    .fetchOneMap();

            if (post == null) {
                // TODO return 404 page
            }
        } catch (Exception e) {
            return ""; // TODO
        }

        // Get replies
        List<Map<String, Object>> postReplies = null;
        try {
            postReplies = dsl.select()
                    .from(REPLY)
                    .where(REPLY.ROOT.eq(aturi.toString()))
                    .orderBy(REPLY.CREATED_AT.desc())
                    .fetchMaps();
        } catch (Exception e) {
            // TODO
        }

//        Map<String, Object> postMap = new HashMap<>();
//        postMap.put("atUri", post.get("aturi"));
//        postMap.put("title", post.get("title"));
//        postMap.put("content", post.get("content"));
//        postMap.put("createdAt", String.valueOf(post.get("created_at")));
//        postMap.put("updatedAt", post.get("updated_at") != null ? String.valueOf(post.get("updated_at")) : null);
//        postMap.put("tags", post.get("tags"));
//        postMap.put("category_aturi", post.get("category_aturi"));

//        String exampleText = "# Test \n This is a test post \n ```java\nSystem.out.println(\"Hello World!\");\n``` \n ## Test 2 \n ### Test 3 \n - lol \n - [x] lol x2 \n #### Test 4 \n testinggggg";
        Parser parser = Parser.builder().build();
        Node document = parser.parse((String) post.get("content"));

        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Heading heading) {
                int newLevel = Math.min(heading.getLevel() + 2, 6);
                heading.setLevel(newLevel);
                visitChildren(heading);
            }
        });


        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String contentHtml = renderer.render(document);

        model.addAttribute("did", user);
        model.addAttribute("rkey", rkey);
        model.addAttribute("title", post.get("title"));
        model.addAttribute("postContent", contentHtml);
        model.addAttribute("postTimestamp", ((OffsetDateTime) post.get("created_at")).toString());
        model.addAttribute("newReplyForm", new NewReplyForm());

        model.addAttribute("postReplies", postReplies);

        return "post";
    }

    @PostMapping("/post/{user}/{rkey}/htmx/reply")
    public String makeReply(@ModelAttribute NewReplyForm newReplyForm, Model model, @PathVariable("user") String user, @PathVariable("rkey") String rkey, @RequestParam String content) throws AtprotoUnauthorized, IOException {
        AtUri aturi = new AtUri(user, PostRecord.recordCollection, rkey);

        var atprotoSessionOpt = sessionService.getCurrentClient();
        if (atprotoSessionOpt.isEmpty() || !sessionService.isAuthenticated()) {
            // TODO
            return "redirect:/login?next=/post/" + user + "/" + rkey + "/&msg=To reply to a post, please log in.";
        }
        var atprotoSession = atprotoSessionOpt.get();


        ReplyRecord reply = new ReplyRecord(
            newReplyForm.getContent(),
            aturi
        );

        atprotoSession.createRecord(reply);
        return "redirect:/browse";
    }
}
