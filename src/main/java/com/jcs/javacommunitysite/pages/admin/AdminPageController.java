package com.jcs.javacommunitysite.pages.admin;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.AtprotoUtil;
import com.jcs.javacommunitysite.atproto.records.TagRecord;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.util.UserInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.jooq.DSLContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

import static com.jcs.javacommunitysite.jooq.tables.Tags.TAGS;
import static com.jcs.javacommunitysite.jooq.tables.User.USER;
import static dev.mccue.json.JsonDecoder.field;
import static dev.mccue.json.JsonDecoder.string;


@Controller
public class AdminPageController {
    private final DSLContext dsl;
    private final AtprotoSessionService sessionService;

    public AdminPageController(DSLContext dsl, AtprotoSessionService sessionService) {
        this.dsl = dsl;
        this.sessionService = sessionService;
    }

    @GetMapping("/admin")
    public String admin(
            Model model
    ) {
        var clientOpt = sessionService.getCurrentClient();
        if (clientOpt.isEmpty() || !sessionService.isAuthenticated()) {
            return "redirect:/login?next=/admin&msg=To access the admin panel, please log in.";
        }
        AtprotoClient client = clientOpt.get();
        // TODO check admin

        Map<String, String> myTags = dsl
                .select(TAGS.ATURI, TAGS.TAG_NAME)
                .from(TAGS)
                .where(TAGS.CREATED_BY.eq(client.getSession().getDid()))
                .fetchMap(TAGS.ATURI, TAGS.TAG_NAME);

        Map<String, String> othersTags = dsl
                .select(TAGS.ATURI, TAGS.TAG_NAME)
                .from(TAGS)
                .where(TAGS.CREATED_BY.ne(client.getSession().getDid()))
                .fetchMap(TAGS.ATURI, TAGS.TAG_NAME);

        model.addAttribute("myTags", myTags);
        model.addAttribute("othersTags", othersTags);
        model.addAttribute("user", UserInfo.getSelfFromDb(dsl, sessionService));
        return "pages/admin/admin";
    }

    @PostMapping("/admin/htmx/createTag")
    public String createTag(
            NewTagForm newTagForm,
            HttpServletResponse response,
            Model model
    ) {
        var clientOpt = sessionService.getCurrentClient();
        if (clientOpt.isEmpty() || !sessionService.isAuthenticated()) {
            response.setHeader("HX-Redirect", "/login?next=/admin&msg=To access the admin panel, please log in.");
            return "empty";
        }
        AtprotoClient client = clientOpt.get();
        // TODO check admin

        var newTag = new TagRecord(newTagForm.getName());
        try {
            var result = client.createRecord(newTag);
            model.addAttribute("atUri", new AtUri(field(result, "uri", string())));
            model.addAttribute("name", newTagForm.getName());
            model.addAttribute("isOwnTag", true);
            return "pages/admin/components/tag";
        } catch (Exception e) {
            // TODO error handling
            return "empty";
        }
    }

    @PostMapping("/admin/htmx/deleteTag")
    public String deleteTag(
            HttpServletResponse response,
            @RequestParam("tag") String tagAtUri
    ) {
        var clientOpt = sessionService.getCurrentClient();
        if (clientOpt.isEmpty() || !sessionService.isAuthenticated()) {
            response.setHeader("HX-Redirect", "/login?next=/admin&msg=To access the admin panel, please log in.");
            return "empty";
        }
        AtprotoClient client = clientOpt.get();
        // TODO check admin

        try {
            client.deleteRecord(new AtUri(tagAtUri));
            return "empty";
        } catch (Exception e) {
            // TODO error handling
            return "empty";
        }
    }
}
