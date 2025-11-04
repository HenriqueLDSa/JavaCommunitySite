package com.jcs.javacommunitysite.atproto.jetstream.handlers;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.jetstream.JetstreamHandler;
import com.jcs.javacommunitysite.atproto.records.AdminGrantRecord;
import dev.mccue.json.Json;
import org.jooq.DSLContext;
import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.JCS_FORUM_DID;
import static com.jcs.javacommunitysite.jooq.tables.UserRole.USER_ROLE;

public class JetstreamAdminGrantHandler implements JetstreamHandler {

    private final DSLContext dsl;

    public JetstreamAdminGrantHandler(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void handleCreated(AtUri atUri, Json recordJson) {
        AdminGrantRecord record = new AdminGrantRecord(atUri, recordJson);

        String recordOwnerDid = record.getOwnerDid().orElseThrow();

        if(!(recordOwnerDid.equals(JCS_FORUM_DID) || dsl.fetchExists(USER_ROLE, USER_ROLE.USER_DID.eq(recordOwnerDid).and(USER_ROLE.ROLE_ID.eq(1))))) {
            System.out.println("Ignoring AdminGrant record creation from non-super-admin: " + recordOwnerDid);
            return;
        }

        try {
            if(!dsl.fetchExists(USER_ROLE, USER_ROLE.USER_DID.eq(record.getTarget()).and(USER_ROLE.ROLE_ID.eq(2)))) {
                dsl.insertInto(USER_ROLE)
                    .set(USER_ROLE.USER_DID, record.getTarget())
                    .set(USER_ROLE.ROLE_ID, 2)
                    .execute();

                System.out.println("AdminGrant record created:");
                System.out.println(" - AtUri: " + record.getAtUri());
                System.out.println(" - Target DID: " + record.getTarget());
                System.out.println(" - Created At: " + record.getCreatedAt());
                System.out.println(" - Admin role granted to user");
            } else {
                System.out.println("User already has admin role, skipping grant.");
            }
        } catch (Exception e) {
            System.out.println("Error inserting admin role record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handleUpdated(AtUri atUri, Json recordJson) {
        AdminGrantRecord record = new AdminGrantRecord(atUri, recordJson);

        String recordOwnerDid = record.getOwnerDid().orElseThrow();

        if(!(recordOwnerDid.equals(JCS_FORUM_DID) || dsl.fetchExists(USER_ROLE, USER_ROLE.USER_DID.eq(recordOwnerDid).and(USER_ROLE.ROLE_ID.eq(1))))) {
            System.out.println("Ignoring AdminGrant record update from non-super-admin: " + recordOwnerDid);
            return;
        }

        try {
            // For admin grants, updates might change the target user
            // Ensure the new target has admin role
            if(!dsl.fetchExists(USER_ROLE, USER_ROLE.USER_DID.eq(record.getTarget()).and(USER_ROLE.ROLE_ID.eq(2)))) {
                dsl.insertInto(USER_ROLE)
                    .set(USER_ROLE.USER_DID, record.getTarget())
                    .set(USER_ROLE.ROLE_ID, 2)
                    .execute();

                System.out.println("AdminGrant record updated:");
                System.out.println(" - AtUri: " + record.getAtUri());
                System.out.println(" - Target DID: " + record.getTarget());
                System.out.println(" - Created At: " + record.getCreatedAt());
                System.out.println(" - Admin role granted to new target user");
            } else {
                System.out.println("AdminGrant record updated - target user already has admin role");
            }
        } catch (Exception e) {
            System.out.println("Error updating admin role record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handleDeleted(AtUri atUri) {
        AdminGrantRecord record = new AdminGrantRecord(atUri);

        String recordOwnerDid = record.getOwnerDid().orElseThrow();

        if(!(recordOwnerDid.equals(JCS_FORUM_DID) || dsl.fetchExists(USER_ROLE, USER_ROLE.USER_DID.eq(recordOwnerDid).and(USER_ROLE.ROLE_ID.eq(1))))) {
            System.out.println("Ignoring AdminGrant record deletion from non-super-admin: " + recordOwnerDid);
            return;
        }

        try {
            // We need to get the target from the record to know which user to revoke admin from
            String targetDid = record.getTarget();
            
            if (targetDid != null) {
                // Delete the admin role record for the target user
                int deletedRecords = dsl.deleteFrom(USER_ROLE)
                    .where(USER_ROLE.USER_DID.eq(targetDid).and(USER_ROLE.ROLE_ID.eq(2)))
                    .execute();

                if (deletedRecords > 0) {
                    System.out.println("AdminGrant record deleted: " + atUri);
                    System.out.println("Admin role revoked for user: " + targetDid);
                    System.out.println("Deleted admin role record from USER_ROLE table");
                } else {
                    System.out.println("AdminGrant record deleted: " + atUri);
                    System.out.println("No admin role record found for user: " + targetDid);
                }
            } else {
                System.out.println("AdminGrant record deleted: " + atUri);
                System.out.println("Could not determine target user for role revocation");
            }
        } catch (Exception e) {
            System.out.println("Error processing admin grant deletion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
