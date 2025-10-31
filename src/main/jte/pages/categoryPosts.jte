@import com.jcs.javacommunitysite.atproto.AtUri
@import org.jooq.Record

@param String groupName
@param String categoryName
@param java.util.List<Record> allPostsInCategory

@template.layout.main(title = categoryName, content = @`
    <div class="page-content">

        <%-- Header --%>
        @template.components.pageHeader(
            content = @`
                <p>${groupName} /</p>
                <h2>${categoryName}</h2>
            `,
            backHref = "/browse"
        )

        <%-- Posts --%>
        @template.components.labelledSection(
            title = "Posts",
            content = @`

            @for(var post : allPostsInCategory)
                !{AtUri aturi = new AtUri(post.get("aturi").toString());}
                <li class="post-item">
                    <a href="/post/${aturi.getDid()}/${aturi.getRecordKey()}">
                        <h4 class="post-name">${post.get("title").toString()}</h4>
                        <p class="post-preview">${post.get("content").toString()}</p>
                    </a>

                    <div style="min-width: 150px; text-align: right">${post.get("countReplies").toString()} replies</div>
                </li>
            @endfor

            @if(allPostsInCategory.isEmpty())
                <p>No posts found.</p>
            @endif
            `
        )


    </div>
`)
