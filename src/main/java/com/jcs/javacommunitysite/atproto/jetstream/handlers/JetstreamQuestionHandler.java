package com.jcs.javacommunitysite.atproto.jetstream.handlers;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.jetstream.JetstreamHandler;
import com.jcs.javacommunitysite.atproto.records.QuestionRecord;
import dev.mccue.json.Json;
import org.jooq.DSLContext;
import org.jooq.JSONB;

import java.time.ZoneOffset;

import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
import static dev.mccue.json.JsonDecoder.field;
import static dev.mccue.json.JsonDecoder.string;

public class JetstreamQuestionHandler implements JetstreamHandler {

    private final DSLContext dsl;

    public JetstreamQuestionHandler(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void handleCreated(AtUri atUri, Json recordJson) {
         QuestionRecord record = new QuestionRecord(atUri, recordJson);

         System.out.println("Question record received from AtProto!");
         System.out.println(" - AtUri: " + record.getAtUri());
         System.out.println(" - Title: " + record.getTitle());
         System.out.println(" - Content: " + record.getContent());
         System.out.println(" - Forum: " + record.getForum());
         System.out.println(" - Created At: " + record.getCreatedAt());
         System.out.println(" - Updated At: " + record.getUpdatedAt());

         if(dsl.fetchExists(POST, POST.ATURI.eq(record.getAtUri().toString()))){
             System.out.println("Post record already exists in database, skipping insert.");
             return;
         }

         try{
             Json questionJson = record.toJson();

             dsl.insertInto(POST)
             .set(POST.TITLE, field(questionJson, "title", string()))
             .set(POST.CONTENT, field(questionJson, "content", string()))
             .set(POST.CREATED_AT, record.getCreatedAt().atOffset(ZoneOffset.UTC))
             .set(POST.UPDATED_AT, record.getUpdatedAt() != null ? record.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
//             .set(POST.TAGS, JSONB.valueOf(field(questionJson, "tags", Json::of).toString())) TODO
             .set(POST.ATURI, atUri.toString())
             .set(POST.IS_OPEN, record.isOpen())
             .set(POST.IS_DELETED, false)
             .set(POST.OWNER_DID, record.getAtUri().getDid())
             .execute();
         } catch(Exception e){
             System.out.println("Error inserting post record: " + e.getMessage());
             e.printStackTrace();
         }
    }

    @Override
    public void handleUpdated(AtUri atUri, Json recordJson) {
         QuestionRecord record = new QuestionRecord(atUri, recordJson);

         if(!dsl.fetchExists(POST, POST.ATURI.eq(record.getAtUri().toString()))){
             System.out.println("Post record does not exist in database, skipping update.");
             return;
         }

         System.out.println("Post record received from AtProto!");
         System.out.println(" - AtUri: " + record.getAtUri());
         System.out.println(" - Title: " + record.getTitle());
         System.out.println(" - Content: " + record.getContent());
         System.out.println(" - Forum: " + record.getForum());
         System.out.println(" - Created At: " + record.getCreatedAt());
         System.out.println(" - Updated At: " + record.getUpdatedAt());

         try{
             Json postJson = record.toJson();

             dsl.update(POST)
                 .set(POST.TITLE, field(postJson, "title", string()))
                 .set(POST.CONTENT, field(postJson, "content", string()))
                 .set(POST.UPDATED_AT, record.getUpdatedAt().atOffset(ZoneOffset.UTC))
                 .set(POST.IS_OPEN, record.isOpen())
//                 .set(POST.TAGS, JSONB.valueOf(field(postJson, "tags", Json::of).toString())) TODO
                 .where(POST.ATURI.eq(atUri.toString()))
                 .execute();
         } catch(Exception e){
             System.out.println("Error updating post record: " + e.getMessage());
             e.printStackTrace();
         }
    }

    @Override
    public void handleDeleted(AtUri atUri) {
        QuestionRecord record = new QuestionRecord(atUri);

        if(!dsl.fetchExists(POST, POST.ATURI.eq(record.getAtUri().toString()))){
            System.out.println("Post record does not exist in database, skipping delete.");
            return;
        }

        try{
            dsl.update(POST)
                .set(POST.IS_DELETED, true)
                .where(POST.ATURI.eq(atUri.toString()))
                .execute();
        } catch(Exception e){
            System.out.println("Error deleting post record: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
