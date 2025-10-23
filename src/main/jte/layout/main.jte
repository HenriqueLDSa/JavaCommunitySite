@param gg.jte.Content content
@param String title

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${title} | JCS</title>
    <link rel="stylesheet" href="/root.css" />
    <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.7/dist/htmx.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/htmx-ext-response-targets@2.0.2"></script>
</head>
<body class="root" hx-ext="response-targets">
<div class="sidebar">

    <h1>jcs.</h1>

    <%-- Nav buttons --%>
    <ul class="nav-buttons">
        <li>
            <a id="browse-button" href="/browse">
                Browse
            </a>
        </li>
        <li>
            <a id="hot-posts-button" href="/hotPosts">
                Hot Posts
            </a>
        </li>
        <li>
            <a id="new-posts-button" href="/newPosts">
                New Posts
            </a>
        </li>
        <li>
            <a id="new-replies-button" href="/newReplies">
                New Replies
            </a>
        </li>
    </ul>

    <%-- Notifications --%>
    <div class="navbar-section notifications">
        <h2>Notifications</h2>
        <ul>
            <li>
                <a href="">
                    <p class="notification-title">New reply to your post</p>
                    <p class="notification-content">Lorem Ipsum filler text</p>
                </a>
            </li>
            <li>
                <a href="">
                    <p class="notification-title">New reaction to your post</p>
                    <p class="notification-content">Lorem Ipsum filler text</p>
                </a>
            </li>
            <li>
                <a href="">
                    <p class="notification-title">New mention on post</p>
                    <p class="notification-content">Lorem Ipsum filler text</p>
                </a>
            </li>
        </ul>
    </div>

    <%-- Recent Browsing --%>
    <div class="navbar-section recent-browsing">
        <h2>Recent Browsing</h2>
        <ul>
            <li>
                <a href="">
                    <p>How to set up Springboot super quickly?</p>
                </a>
            </li>
            <li>
                <a href="">
                    <p>Check out my new tool that I made yesterday</p>
                    <p class="new-content-notif">
                        &#x21AA; 2 new replies
                    </p>
                </a>
            </li>
            <li>
                <a href="">
                    <p>How to make a calculator to calculate 2 + 2</p>
                </a>
            </li>
            <li>
                <a href="">
                    <p>When did everyone start using Java?</p>
                </a>
            </li>
        </ul>
    </div>
</div>

<%-- The Content for the right side of the page --%>
<div class="page-container">
    <div class="page">
        ${content}
    </div>
</div>
</body>
<script>
    document.addEventListener("click", () => {
        document.getElementById("popup-menu")?.remove()
    })

    // Nav button highlighting
    const path = window.location.pathname.split('/')[1];
    const buttons = {
        'browse': 'browse-button',
        'hotPosts': 'hot-posts-button',
        'newPosts': 'new-posts-button',
        'newReplies': 'new-replies-button'
    };

    if (path in buttons) {
        document.getElementById(buttons[path])?.classList.add('active');
    }

</script>
</html>