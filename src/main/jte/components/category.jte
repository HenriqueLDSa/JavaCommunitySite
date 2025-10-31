@param String rkey
@param String name
@param String description
@param int recentPostsCount

<li class="category">
    <a href="/category/${rkey}">
        <h4 class="cat-name">${name}</h4>
        <p class="cat-desc">${description}</p>
    </a>
    <div style="min-width: 200px; text-align: right;">
        <p>${recentPostsCount} recent posts</p>
    </div>
</li>