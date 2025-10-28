@import java.util.List

@param com.jcs.javacommunitysite.atproto.AtUri aturi
@param String title
@param String content
@param int amtReplies
@param String timeText = null
@param List<String> tags = null

<li class="post-item">
    <a href="/post/${aturi.getDid()}/${aturi.getRecordKey()}">
        <div class="post-name-container">
            <h4 class="post-name">${title}</h4>
        </div>
        <p class="post-preview">${content}</p>
        <div class="tags">
            @if(tags != null && !tags.isEmpty())
                @for(var tag : tags)
                    <div class="tag">${tag}</div>
                @endfor
            @endif
        </div>
    </a>

    <div style="min-width: 150px; text-align: right">
        @if(timeText != null)
            <p>${timeText}</p>
        @else
            <p>2 days ago</p>
        @endif
        <p style="color: var(--subtext-color)">${amtReplies} ${amtReplies == 1 ? "reply" : "replies"}</p>
    </div>
</li>