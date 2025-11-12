@import com.jcs.javacommunitysite.util.UserInfo

@param boolean loggedIn
@param UserInfo user = null

@template.layout.settings(content = @`
    <div class="page-content">

        @template.components.pageHeader(
        content = @`<h2>Notifications</h2>`,
        user = user
        )

        <p>hi</p>
    </div>
`)