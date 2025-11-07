@import com.jcs.javacommunitysite.atproto.AtUri

@param AtUri post
@param AtUri reply = null
@param boolean isHidden

!{
    String baseUrl = "/post/" + post.getDid() + "/" + post.getRecordKey() + "/htmx/";
    String target = "";
    String id = "";
    if (reply != null) {
        target = "Reply?reply=" + reply.toString();
        id = "reply-popup-" + reply.getRecordKey();
    } else {
        target = "Post";
        id = "post-popup";
    }
}

@if(isHidden)
    <button
        class="popup-option"
        hx-post="${baseUrl}unhide${target}"
        hx-target="this"
        hx-swap="outerHTML"
        hx-on::before-request="togglePopup('${id}')"
    >
        <img src="/visible.svg" alt="" />
        Unhide
    </button>
@else
    <button
        class="popup-option"
        hx-post="${baseUrl}hide${target}"
        hx-target="this"
        hx-swap="outerHTML"
        hx-on::before-request="togglePopup('${id}')"
    >
        <img src="/hidden.svg" alt="" />
        Hide
    </button>
@endif
