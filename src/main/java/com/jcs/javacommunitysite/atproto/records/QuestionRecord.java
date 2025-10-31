package com.jcs.javacommunitysite.atproto.records;

import com.jcs.javacommunitysite.atproto.AtUri;
import dev.mccue.json.Json;
import org.jooq.DSLContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_DID;
import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.addLexiconPrefix;
import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import static dev.mccue.json.JsonDecoder.*;

public class QuestionRecord extends AtprotoRecord {
    public static final String recordCollection = addLexiconPrefix("feed.question");

    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt = null;
    private String forum;
    private List<String> tags;

    private boolean isOpen;

    @Override
    public Json toJson() {
        return Json.objectBuilder()
                .put("title", title)
                .put("content", content)
                .put("createdAt", createdAt.toString())
                .put("updatedAt", updatedAt == null ? null : updatedAt.toString())
                .put("forum", forum)
                .put("tags", Json.of(tags, Json::of))
                .put("isOpen", isOpen)
                .build();
    }

    public QuestionRecord(AtUri atUri) {
        super(atUri);
    }

    public QuestionRecord(AtUri atUri, DSLContext dsl) {
        super(atUri);
        fetchFromDB(atUri, dsl);
    }

    private void fetchFromDB(AtUri atUri, DSLContext dsl){

        var record = dsl.select()
                .from(POST)
                .where(POST.ATURI.eq(atUri.toString()))
                .fetchOne();

        if(record != null){
            this.title = record.get(POST.TITLE);
            this.content = record.get(POST.CONTENT);
            this.createdAt = record.get(POST.CREATED_AT).toInstant();
            this.updatedAt = record.get(POST.UPDATED_AT) == null ? null : record.get(POST.UPDATED_AT).toInstant();
            this.isOpen = record.get(POST.IS_OPEN);
            this.forum = JCS_FORUM_DID;

            var tagsJsonb = record.get(POST.TAGS);
            if (tagsJsonb != null) {
                var tagsJson = Json.read(tagsJsonb.data());
                this.tags = array(string()).decode(tagsJson);
            } else {
                this.tags = new ArrayList<>();
            }
        }
    }

    public QuestionRecord(AtUri atUri, Json json) {
        super(atUri, json);
        this.title = field(json, "title", string());
        this.content = field(json, "content", string());
        this.createdAt = Instant.parse(field(json, "createdAt", string()));
        this.updatedAt = optionalNullableField(json, "updatedAt", string())
                .map(Instant::parse)
                .orElse(null);
        this.forum = field(json, "forum", string());
        this.tags = field(json, "tags", array(string()));
        this.isOpen = field(json, "isOpen", boolean_());
    }

    public QuestionRecord(Json json) {
        super();
        this.title = field(json, "title", string());
        this.content = field(json, "content", string());
        this.createdAt = Instant.parse(field(json, "createdAt", string()));
        this.updatedAt = optionalNullableField(json, "updatedAt", string())
                .map(Instant::parse)
                .orElse(null);
        this.forum = field(json, "forum", string());
        this.tags = field(json, "tags", array(string()));
        this.isOpen = field(json, "isOpen", boolean_());
    }

    public QuestionRecord(String title, String content, String forum) {
        this.title = title;
        this.content = content;
        this.createdAt = Instant.now();
        this.forum = forum;
        this.tags = new ArrayList<>();
        this.isOpen = true;
    }

    public QuestionRecord(String title, String content, String forum, List<String> tags) {
        this.title = title;
        this.content = content;
        this.createdAt = Instant.now();
        this.forum = forum;
        this.tags = tags;
        this.isOpen = true;
    }

    @Override
    public boolean isValid() {
        if (title == null || title.isEmpty() || title.length() > 100) return false;
        if (content == null || content.isEmpty() || content.length() > 10000) return false;
        if (createdAt == null) return false;
        if (forum == null) return false;
        if (tags == null || tags.stream().anyMatch(t -> t.length() > 25)) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return recordCollection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
