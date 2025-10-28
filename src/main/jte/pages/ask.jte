@import com.jcs.javacommunitysite.atproto.AtUri
@import com.jcs.javacommunitysite.atproto.records.PostRecord
@import java.util.List
@import java.util.Map

@param com.jcs.javacommunitysite.pages.askpage.NewPostForm postForm
@param String currentUserAvatarUrl = null
@param List<com.jcs.javacommunitysite.jooq.tables.records.PostRecord> userPosts = null
@param Map<String, Integer> replyCountsMap = null
@param Map<String, String> timeTextsMap = null
@param Map<String, List<String>> tagsMap = null

@template.layout.main(
    title = "Ask",
    content = @`
    <div class="page-content">

        @template.components.pageHeader(
            content = @`<h2>Ask a Question</h2>`,
            avatarUrl = currentUserAvatarUrl
        )

        <div class="new-question-container">
            <form method="post">
                <div class="form-group">
                    <label for="title-input">Title</label>
                    <input id="title-input" class="textbox" type="text" name="title" value="${postForm.getTitle()}" placeholder="My Really Good Title" required>
                </div>

                <div class="form-group">
                    <label for="post-input">Post</label>
                    <textarea id="post-input" class="post-textarea" name="content" name="content" value="${postForm.getContent()}" required></textarea>
                </div>

                Put tags here

                <div class="right-side-buttons-container">
                    <button class="button" type="submit">Create Question</button>
                </div>
            </form>
        </div>

        <div class="divider"></div>

        <h2>My Questions</h2>

        @if(userPosts != null && !userPosts.isEmpty())
            @for(var post : userPosts)
                @template.components.post(
                    aturi = new AtUri(post.getAturi()),
                    title = post.getTitle(),
                    content = post.getContent(),
                    amtReplies = (replyCountsMap != null && replyCountsMap.containsKey(post.getAturi())) ? replyCountsMap.get(post.getAturi()) : 0,
                    timeText = (timeTextsMap != null && timeTextsMap.containsKey(post.getAturi())) ? timeTextsMap.get(post.getAturi()) : null,
                    tags = (tagsMap != null && tagsMap.containsKey(post.getAturi())) ? tagsMap.get(post.getAturi()) : null
                )
            @endfor
        @else
            <p>You haven't asked any questions yet. Ask your first question above!</p>
        @endif



    </div>
    `
)