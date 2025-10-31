@import java.util.Random

@param String content = ""
@param String id = null

!{
    if (id == null) {
        id = Integer.toString(new Random().nextInt(100000));
    }
}

<div class="post-textarea-container">
    <div class="control-buttons">
        <button
                id="edit-button-${id}"
                type="button"
                class="control-button active"
                onclick="setEditMode('${id}')"
        >
            Edit
        </button>
        <button
                id="preview-button-${id}"
                type="button"
                class="control-button"
                onclick="setPreviewMode('${id}')"
                hx-post="/previewMarkdown"
                hx-target="#preview-container-${id}"
                hx-swap="innerHTML"
                hx-trigger="click consume"
        >
            Preview
        </button>
    </div>
    <textarea id="post-input-${id}" class="post-textarea" name="content" required>${content}</textarea>
    <div id="preview-container-${id}" class="preview-container hidden">

    </div>
</div>