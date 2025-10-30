@import com.jcs.javacommunitysite.atproto.AtUri
@import java.util.List
@import java.util.Map

@param com.jcs.javacommunitysite.pages.askpage.NewPostForm postForm
@param String currentUserAvatarUrl = null
@param List<com.jcs.javacommunitysite.jooq.tables.records.PostRecord> userPosts = null
@param Map<String, Integer> replyCountsMap = null
@param Map<String, String> timeTextsMap = null
@param Map<String, List<String>> tagsMap = null
@param boolean loggedIn

@template.layout.main(
    title = "Ask",
    content = @`
    <div class="page-content">

        @template.components.pageHeader(
            content = @`<h2>Ask a Question</h2>`,
            avatarUrl = currentUserAvatarUrl
        )

        @if(loggedIn)

            <div class="new-question-container">
                <form
                    hx-post="/ask"
                    hx-target="#my-questions-header"
                    hx-swap="afterend"
                    hx-on::after-request="if (event.detail.successful && event.target == this) this.reset()"
                >
                    <div class="form-group">
                        <label for="title-input">Title</label>
                        <input id="title-input" class="textbox" type="text" name="title" value="${postForm.getTitle()}" placeholder="My Really Good Title" required>
                    </div>

                    <div class="form-group">
                        <label for="post-input">Post</label>
                        @template.components.postEditor()
                    </div>

                    <div class="form-group">
                        <label for="tags-input">Tags</label>
                        <div class="tags-input-container">
                            <div class="selected-tags">
                                <div class="tag" data-tag="java-21">Java 21</div>
                                <div class="tag" data-tag="spring-boot">Spring Boot</div>
                                <div class="tag" data-tag="minecraft">Minecraft</div>
                                <div class="tag" data-tag="jte">JTE</div>
                                <div class="tag" data-tag="forge">Forge</div>
                                <div class="tag" data-tag="thymeleaf">Thymeleaf</div>
                                <div class="tag" data-tag="linux">Linux</div>

                                <!--
                                <div class="tag dropdown-toggle" onclick="toggleTagDropdown()">
                                    More... ▼
                                    <div class="tag-dropdown" id="tagDropdown" style="display: none;">
                                        <div class="tag-dropdown-item" data-tag="security">Security</div>
                                        <div class="tag-dropdown-item" data-tag="testing">Testing</div>
                                        <div class="tag-dropdown-item" data-tag="deployment">Deployment</div>
                                    </div>
                                </div>
                                -->

                            </div>
                            <div id="hiddenTagsContainer">
                                <!-- Hidden inputs for selected tags will be added here dynamically -->
                            </div>
                        </div>
                    </div>

                    <div class="right-side-buttons-container">
                        <button class="button" type="submit">Create Question</button>
                    </div>
                </form>
            </div>

            <div class="divider"></div>

            <h2 id="my-questions-header">My Questions</h2>

            @if(userPosts != null && !userPosts.isEmpty())
                @for(var post : userPosts)
                    @template.components.post(
                        aturi = new AtUri(post.getAturi()),
                        title = post.getTitle(),
                        content = post.getContent(),
                        amtReplies = (replyCountsMap != null && replyCountsMap.containsKey(post.getAturi())) ? replyCountsMap.get(post.getAturi()) : 0,
                        timeText = (timeTextsMap != null && timeTextsMap.containsKey(post.getAturi())) ? timeTextsMap.get(post.getAturi()) : null,
                        tags = (tagsMap != null && tagsMap.containsKey(post.getAturi())) ? tagsMap.get(post.getAturi()) : null
                    )
                @endfor
            @else
                <p>You haven't asked any questions yet. Ask your first question above!</p>
            @endif

        @else
            <p>
                To ask a question, please
                <a href="/login?next=/ask&msg=To ask a question, please log in">
                    log in.
                </a>
            </p>
        @endif

    </div>

    <script>
        var selectedTags = new Set();

        function toggleTagDropdown() {
            var dropdown = document.getElementById('tagDropdown');
            var isVisible = dropdown.style.display !== 'none';
            dropdown.style.display = isVisible ? 'none' : 'block';
        }

        function updateSelectedTagsInput() {
            var hiddenContainer = document.getElementById('hiddenTagsContainer');
            // Clear existing hidden inputs
            hiddenContainer.innerHTML = '';
            
            // Create a hidden input for each selected tag
            selectedTags.forEach(function(tagText) {
                var hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = 'tags';
                hiddenInput.value = tagText;
                hiddenContainer.appendChild(hiddenInput);
            });
        }

        function toggleTagSelection(tagElement, tagName) {
            var tagText = tagElement.textContent.trim();
            if (selectedTags.has(tagText)) {
                selectedTags.delete(tagText);
                tagElement.classList.remove('selected');
            } else {
                selectedTags.add(tagText);
                tagElement.classList.add('selected');
            }
            updateSelectedTagsInput();
        }

        // Close dropdown when clicking outside
        document.addEventListener('click', function(event) {
            var dropdown = document.getElementById('tagDropdown');
            var dropdownToggle = event.target.closest('.dropdown-toggle');
            
            if (!dropdownToggle && dropdown.style.display === 'block') {
                dropdown.style.display = 'none';
            }
        });

        // Add tag selection functionality
        document.addEventListener('DOMContentLoaded', function() {
            var tagDropdownItems = document.querySelectorAll('.tag-dropdown-item');
            
            // Initialize selected tags from form data (for error cases)
            @if(postForm.getTags() != null && !postForm.getTags().isEmpty())
                @for(var tag : postForm.getTags())
                    selectedTags.add('${tag}');
                    // Find tag element by matching text content instead of data-tag
                    var allTagElements = document.querySelectorAll('.tag, .tag-dropdown-item');
                    allTagElements.forEach(function(tagEl) {
                        if (tagEl.textContent.trim() === '${tag}') {
                            tagEl.classList.add('selected');
                        }
                    });
                @endfor
                updateSelectedTagsInput();
            @endif
            
            tagDropdownItems.forEach(function(item) {
                item.addEventListener('click', function(e) {
                    e.stopPropagation();
                    var tagName = this.dataset.tag;
                    
                    toggleTagSelection(this, tagName);
                    
                    // Hide dropdown
                    document.getElementById('tagDropdown').style.display = 'none';
                });
            });
            
            // Add click to toggle functionality for existing tags
            var existingTags = document.querySelectorAll('.selected-tags .tag:not(.dropdown-toggle)');
            existingTags.forEach(function(tag) {
                tag.addEventListener('click', function() {
                    var tagName = this.dataset.tag;
                    toggleTagSelection(this, tagName);
                });
            });

            // Initialize form submission handler
            var form = document.querySelector('form');
            form.addEventListener('submit', function() {
                updateSelectedTagsInput();
            });
        });
    </script>
    `
)