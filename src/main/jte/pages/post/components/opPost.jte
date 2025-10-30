@import com.jcs.javacommunitysite.atproto.AtUri
@import com.jcs.javacommunitysite.jooq.tables.records.PostRecord
@import com.jcs.javacommunitysite.util.TimeUtil
@import com.jcs.javacommunitysite.util.UserInfo

@param PostRecord post
@param UserInfo user

!{AtUri aturi = new AtUri(post.getAturi());}

<div id="post" class="post op-post">
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
                ${TimeUtil.calculateTimeText(post.getCreatedAt())}
            </div>
            <div id="post-popup-button-container" class="popup-menu-container">
                <button
                    class="button icon"
                    hx-get="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/postMenu"
                    hx-target="#post-popup-button-container"
                    hx-swap="beforeend"
                >
                    <img src="/more.svg" alt="More Options" width="20" />
                </button>
            </div>
        </div>
        <div class="body">
            $unsafe{post.getContent()}
        </div>
    </div>
</div>
