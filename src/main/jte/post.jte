@import com.jcs.javacommunitysite.forms.NewReplyForm
@import java.util.ArrayList
@import java.util.List
@import java.util.Map

@param String did
@param String rkey

@param String title
@param String postContent
@param String postTimestamp

@param List<Map<String, Object>> postReplies

@param NewReplyForm newReplyForm

@template.layout.main(
    title = "Post",
    stylesheets = new ArrayList<String>(),
    content = @`
    <div class="post-page">
        <h2>${title}</h2>
        @template.components.opPost(content=postContent, username="Test", handle="test.bsky.social", timestamp=postTimestamp)

        @for(var reply : postReplies)
            <div class="mid-reply-runner"></div>
            @template.components.reply()
        @endfor

<%--        @template.components.reply()--%>
<%--        <div class="mid-reply-runner"></div>--%>
<%--        @template.components.reply()--%>
<%--        <div class="mid-reply-runner"></div>--%>
<%--        @template.components.reply()--%>
<%--        <div class="mid-reply-runner"></div>--%>

        <form class="reply-container" hx-post="/post/${did}/${rkey}/htmx/reply" hx-swap="none">
            <p>Make a Reply</p>
            <div class="reply-bottom">
                <img src="/img/user.svg">
                <textarea class="post-textarea" name="content" value="${newReplyForm.getContent()}"></textarea>
            </div>
            <div class="reply-buttons" type="submit">
                <button class="button">Reply</button>
            </div>
            <div class="runner"></div>
        </form>
    </div>
    `
)