@param com.jcs.javacommunitysite.atproto.AtUri aturi

@template.components.modal(
    title="Close Question?",
    content=@`
        <p>Close a question once it has been solved. Once closed, no more replies can be made.</p>
    `,
    buttons=@`
        <img id="close-post-indicator" class="htmx-indicator" src="/throbber.svg" width="24" />
        <button class="button transparent" onclick="closeModal()">Cancel</button>
        <button
            class="button"
            hx-post="/post/${aturi.getDid()}/${aturi.getRecordKey()}/htmx/closePost"
            hx-target="#reply-container"
            hx-swap="outerHTML"
            hx-disabled-elt="this"
            hx-indicator="#close-post-indicator"
            hx-on::after-request="closeModal()"
        >
            Close Question
        </button>
    `
)