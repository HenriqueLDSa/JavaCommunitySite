@import com.jcs.javacommunitysite.atproto.AtUri

@param String id
@param AtUri post
@param AtUri reply
@param boolean ownsThisReply
@param boolean isCurUserAdmin
@param boolean isHidden

<div id="${id}" class="popup-menu hidden preload">
    @if(ownsThisReply)
        <button
            class="popup-option"
            hx-get="/post/${post.getDid()}/${post.getRecordKey()}/htmx/openEditReply?reply=${reply.toString()}"
            hx-target="#reply-${reply.getRecordKey()}"
            hx-swap="outerHTML"
            hx-on::after-request="togglePopup('${id}')"
        >
            <img src="/edit.svg" alt="" />
            Edit
        </button>
        <button
            class="popup-option"
            hx-get="/post/${post.getDid()}/${post.getRecordKey()}/htmx/confirmDeleteReply?reply=${reply.toString()}"
            hx-target="body"
            hx-swap="beforeend"
            hx-on::after-request="togglePopup('${id}')"
        >
            <img src="/delete.svg" alt="" />
            Delete
        </button>
    @endif
    @if(isCurUserAdmin)
        @template.pages.post.htmx.hideButton(post = post, reply = reply, isHidden = isHidden)
    @endif

    @if(!ownsThisReply && !isCurUserAdmin)
        <p style="color: var(--subtext-color); font-size: var(--xsmall-text); text-align: center">
            Sorry, nothing you can do here!
        </p>
    @endif
</div>