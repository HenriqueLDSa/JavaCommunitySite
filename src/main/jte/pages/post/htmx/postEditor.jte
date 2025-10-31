@import com.jcs.javacommunitysite.atproto.AtUri
@import com.jcs.javacommunitysite.forms.UpdatePostForm
@import com.jcs.javacommunitysite.jooq.tables.records.PostRecord
@import com.jcs.javacommunitysite.util.UserInfo

@param PostRecord post
@param UpdatePostForm form
@param UserInfo user

!{AtUri aturi = new AtUri(post.getAturi());}

<div id="post" class="post op-post">
    <div class="content">
        <div class="header">
            <div class="profile-pic-container">
                <img src="${user.avatarUri}" alt="Profile Picture" />
            </div>
            <div style="display: flex; align-items: center;">
                <p><b>Edit your Question</b></p>
            </div>
        </div>
        <form
            class="body"
            hx-put="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/editPost"
            hx-target="#post"
            hx-swap="outerHTML"
        >
            @template.components.postEditor(content=form.getContent())
            <div class="buttons">
                <button
                        class="button transparent"
                        type="button"
                        hx-get="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/cancelEditPost"
                        hx-target="#post"
                        hx-swap="outerHTML"
                >
                    Cancel
                </button>
                <button class="button" type="submit">Save</button>
            </div>
        </form>
    </div>
</div>
