@import com.jcs.javacommunitysite.atproto.AtUri
@import com.jcs.javacommunitysite.atproto.AtprotoUtil
@import com.jcs.javacommunitysite.jooq.tables.records.PostRecord
@import dev.mccue.json.JsonObject
@import java.io.IOException

@param PostRecord post

!{AtUri aturi = new AtUri(post.getAturi());}

!{
    // TODO This is just a proof of concept that we can fetch profiles from bsky.
    //      We will remove this in favor of fetching user data directly from the DB
    //      once implemented
    JsonObject profile = null;
    try {
        profile = AtprotoUtil.getBskyProfile(aturi.getDid());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    var handle = profile.get("handle").toString();
    var displayName = profile.get("displayName").toString();
    var pfp = profile.get("avatar").toString();
}


<div id="post" class="post op-post">
    <div class="content">
        <div class="header">
            <div class="profile-pic-container">
                <img src="${pfp}">
            </div>
            <div class="poster-info">
                <p class="username">${displayName}</p>
                <p class="handle">@${handle}</p>
            </div>
            <div class="time-info">
                ${post.getCreatedAt().toString()}
            </div>
            <div id="post-popup-button-container" class="popup-menu-container">
                <button
                    class="button icon"
                    hx-get="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/postMenu"
                    hx-target="#post-popup-button-container"
                    hx-swap="beforeend"
                >
                    <img src="/img/more.svg" alt="More Options" width="20" />
                </button>
            </div>
        </div>
        <div class="body">
            $unsafe{post.getContent()}
        </div>
    </div>
</div>
