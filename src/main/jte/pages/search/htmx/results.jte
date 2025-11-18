@import com.jcs.javacommunitysite.pages.searchpage.SearchResult
@import java.util.List
@import java.util.List
@import java.util.ArrayList

@param List<SearchResult> searchResults = null
@param List<String> hiddenPosts = new ArrayList<>()

@if(searchResults.isEmpty())
    <div class="no-results">
        <p>No posts found matching your query. Try different keywords or check your spelling.</p>
    </div>
@else
    <div class="results-info">
        Found ${searchResults.size()} result${searchResults.size() == 1 ? "" : "s"}
    </div>

    <ul class="results-list">
        @for(var result : searchResults)
            @template.components.post(
                aturi = result.getAtUri(),
                title = result.getTitle(),
                content = result.getContent(),
                amtReplies = result.getReplyCount(),
                timeText = result.getTimeText(),
                tags = result.getTags(),
                closed = !result.isOpen(),
                hidden = hiddenPosts.contains(result.getAturi())
            )
        @endfor
    </ul>
@endif