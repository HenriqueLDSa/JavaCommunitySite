@import com.jcs.javacommunitysite.atproto.AtUri
@import com.jcs.javacommunitysite.forms.NewReplyForm
@import com.jcs.javacommunitysite.jooq.tables.records.ReplyRecord
@import com.jcs.javacommunitysite.util.UserInfo

@param ReplyRecord reply
@param NewReplyForm form
@param UserInfo user

!{AtUri root = new AtUri(reply.getRootPostAturi());}
!{AtUri self = new AtUri(reply.getAturi());}

<div id="reply-${self.getRecordKey()}">
    <div class="mid-reply-runner"></div>
    <div class="post">
        <div class="content">
            <div class="header">
                <div class="profile-pic-container">
                    <img src="${user.avatarUri}" alt="Profile Picture" />
                </div>
                <div style="display: flex; align-items: center;">
                    <p><b>Edit your Reply</b></p>
                </div>
            </div>
            <form
                class="body"
                hx-put="/post/${root.getRecordKey()}/${root.getRecordKey()}/htmx/editReply?reply=${self.toString()}"
                hx-target="#reply-${self.getRecordKey()}"
                hx-swap="outerHTML"
            >
                @template.components.postEditor(content = form.getContent())
                <div class="buttons">
                    <button
                        class="button transparent"
                        type="button"
                        hx-get="/post/${root.getRecordKey()}/${root.getRecordKey()}/htmx/cancelEditReply?reply=${self.toString()}"
                        hx-target="#reply-${self.getRecordKey()}"
                        hx-swap="outerHTML"
                    >
                        Cancel
                    </button>
                    <button class="button" type="submit">Save</button>
                </div>
            </form>
        </div>

        <div class="runner"></div>
    </div>
</div>