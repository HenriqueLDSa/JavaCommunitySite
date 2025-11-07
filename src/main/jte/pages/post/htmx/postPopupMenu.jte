@import com.jcs.javacommunitysite.atproto.AtUri

@param String id
@param AtUri aturi
@param boolean ownsThisPost
@param boolean isCurUserAdmin
@param boolean isHidden

<div id="${id}" class="popup-menu hidden preload">
    @if(ownsThisPost)
        <button
            class="popup-option"
            hx-get="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/openEditPost"
            hx-target="#post"
            hx-swap="outerHTML"
            hx-on::after-request="togglePopup('${id}')"
        >
            <img src="/edit.svg" alt="" />
            Edit
        </button>
        <button
            class="popup-option"
            hx-get="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/confirmDeletePost"
            hx-target="body"
            hx-swap="beforeend"
            hx-on::after-request="togglePopup('${id}')"
        >
            <img src="/delete.svg" alt="" />
            Delete
        </button>
    @endif
    @if(isCurUserAdmin)
        @template.pages.post.htmx.hideButton(post = aturi, isHidden = isHidden)
    @endif

    @if(!ownsThisPost && !isCurUserAdmin)
        <p style="color: var(--subtext-color); font-size: var(--xsmall-text); text-align: center">
            Sorry, nothing you can do here!
        </p>
    @endif
</div>