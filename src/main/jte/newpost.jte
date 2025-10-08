@import java.util.ArrayList

@template.layout.main(
    title = "New Post",
    stylesheets = new ArrayList<String>(),
    content = @`
    <div class="new-post-page">
        <h1>New Post</h1>
        <form method="post">
            <label for="category-input">Category</label>
            <div class="category-select-container">
                <select id="group-input" name="group">Test</select>
                <p>/</p>
                <select id="category-input" name="category">Test</select>
            </div>
            <label for="title-input">Title</label>
            <input id="title-input" class="textbox" type="text" name="title" placeholder="My Really Good Title">
            <label for="post-input">Post</label>
            <textarea id="post-input" class="post-textarea" name="content"></textarea>
            <div class="right-side-buttons-container">
                <button class="button" type="submit">Create Post</button>
            </div>
        </form>
    </div>
    `
)
