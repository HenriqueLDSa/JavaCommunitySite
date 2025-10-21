@param String groupName
@param String categoryName
@param java.util.List<com.jcs.javacommunitysite.jooq.tables.records.PostRecord> allPostsInCategory

@template.layout.main(title =categoryName, content = @`
    <div class="ml-4 mr-4 flex-1 p-6 min-h-svh">
        <div class="flex flex-row justify-between">
            <div class="flex flex-col w-200">
                <p class="text-sm text-gray-500 mt-1">${groupName} /</p>
                <h1 class="flex text-3xl mt-2">${categoryName}</h1>
            </div>
            <div class="flex flex-row justify-between w-35 items-start">
                <a href="" class="rounded-md pl-2 pr-2 pt-1 pb-1 text-sm bg-sky-500 hover:bg-sky-600 text-white">
                    + New Post
                </a>
                <a href="" class="rounded-4xl bg-gray-500 p-1">
                    pfp
                </a>
            </div>
        </div>
        <div class="flex-col border-1 border-black rounded-sm mt-8">
            <div class="border-b-1 border-b-black m-2 pb-1">
                <a href="" class="text-gray-500 hover:underline">Posts</a>
            </div>
            <ul class="m-3">
                @for(int i = 0; i < allPostsInCategory.size(); i++)
                    <li class="text-sm m-2">
                        <a class="hover:underline" href="">
                            <p>${allPostsInCategory.get(i).getTitle()}</p>
<%--                            <p class="text-xs text-gray-400">Lorem Ipsum</p>--%>
                        </a>
                    </li>
                @endfor
            </ul>
        </div>
    </div>
`)
