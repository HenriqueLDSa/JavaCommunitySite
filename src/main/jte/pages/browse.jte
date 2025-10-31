@import com.jcs.javacommunitysite.atproto.AtUri

@param java.util.List<java.util.Map<String, Object>> groupsWithCategories
@template.layout.main(title = "Browse", content = @`
    <div class="page-content">

        <%-- Header --%>
        @template.components.pageHeader(content = @`
            <h2>Browse Categories.</h2>
            <p>Find and create posts in their appropriate categories.</p>
        `)

        @if(groupsWithCategories.isEmpty())
            <br/>
            <p>No groups!</p>
        @endif

        <%-- Groups --%>
        @for(java.util.Map<String, Object> a : groupsWithCategories)
            !{Object b = a.get("group");}
            !{java.util.Map<?, ?> c = (java.util.Map<?, ?>) b;}
            !{String group = c.get("name").toString();}
            @template.components.labelledSection(
                title = group,
                content = @`
                !{Object d = a.get("categories");}
                !{java.util.List<?> cats = (java.util.List<?>) d;}
                @for(Object cat : cats)
                    !{java.util.Map<?,?> e = (java.util.Map<?, ?>) cat;}
                    @template.components.category(
                        rkey = new AtUri(e.get("aturi").toString()).getRecordKey(),
                        name = e.get("name").toString(),
                        description = e.get("description").toString(),
                        recentPostsCount = (int) e.get("posts_24hr"),
                    )
                @endfor
                `
            )
        @endfor
    </div>
`)