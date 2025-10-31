@import com.jcs.javacommunitysite.atproto.AtUri
@import com.jcs.javacommunitysite.jooq.tables.records.ReplyRecord
@import com.jcs.javacommunitysite.util.MarkdownUtil
@import com.jcs.javacommunitysite.util.TimeUtil
@import com.jcs.javacommunitysite.util.UserInfo

@param ReplyRecord reply
@param UserInfo user

!{AtUri root = new AtUri(reply.getRootPostAturi());}
!{
    AtUri self = null;
    if (reply.getAturi() != null) {
        self = new AtUri(reply.getAturi());
    }
}

<div id="reply-${self == null ? "new" : self.getRecordKey()}">
    <div class="mid-reply-runner"></div>
    <div class="post">
        <div class="content">
            <div class="header">
                <div class="profile-pic-container">
                    <img src="${user.avatarUri}" alt="Profile Picture" />
                </div>
                <div class="poster-info">
                    <p class="username">${user.displayName}</p>
                    <p class="handle">@${user.handle}</p>
                </div>
                <div class="time-info">
                    ${TimeUtil.calculateTimeText(reply.getCreatedAt())}
                </div>
                @if(self != null)
                    <div id="reply-popup-button-container-${self.getRecordKey()}" class="popup-menu-container">
                        <button
                                class="button icon"
                                hx-get="/post/${root.getDid()}/${root.getRecordKey()}/htmx/replyMenu?reply=${reply.getAturi()}"
                                hx-target="#reply-popup-button-container-${self.getRecordKey()}"
                                hx-swap="beforeend"
                        >
                            <img src="/more.svg" alt="More Options" width="20" />
                        </button>
                    </div>
                @endif
            </div>
            <div class="body">
                <p>
                    $unsafe{MarkdownUtil.render(reply.getContent().toString())}
                </p>
            </div>
        </div>

        <div class="runner"></div>
    </div>
</div>
