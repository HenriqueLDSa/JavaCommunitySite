@param java.util.List<java.util.Map<String, Object>> groupsWithCategories
@template.layout.main(title = "Browse", content = @`
    <div class="browse-page">

        <%-- Header --%>
        <div class="header">
            <div>
                <h2>Browse Categories.</h2>
                <p>Find and create posts in their appropriate categories.</p>
            </div>
            <div class="top-right-buttons">
                <a href="/newPost" class="button">
                    + New Post
                </a>
                <a href="" class="">
                    pfp
                </a>
            </div>
        </div>

        @if(groupsWithCategories.isEmpty())
            <br/>
            <p>No categories!</p>
        @endif

        @for(java.util.Map<String, Object> a : groupsWithCategories)
            !{Object b = a.get("group");}
            !{java.util.Map<?, ?> c = (java.util.Map<?, ?>) b;}
            !{String group = c.get("name").toString();}
            <div class="group-container">
                <h3 href="/topics/${group.toLowerCase()}">
                    ${group}
                </h3>

                <div class="divider"></div>

                <ul class="m-3">
                    !{Object d = a.get("categories");}
                    !{java.util.List<?> cats = (java.util.List<?>) d;}
                    @for(Object cat : cats)
                        !{java.util.Map<?,?> e = (java.util.Map<?, ?>) cat;}
                        !{String cat_name = e.get("name").toString();}
                        !{String cat_desc = e.get("description").toString();}
                        !{String cat_atUri = e.get("aturi").toString();}
                        !{String recordKey = cat_atUri.substring(cat_atUri.lastIndexOf("/") + 1);}
                        <li>
                            <a href="/${group}/${recordKey}">
                                <h4 class="cat-name">${cat_name}</h4>
                                <p class="cat-desc">${cat_desc}</p>
                            </a>
                            <div style="min-width: 200px; text-align: right;">
                                <p>2 recent posts</p>
                            </div>
                        </li>
                    @endfor
                </ul>
            </div>
        @endfor
<%--        <div class="flex-col border-1 border-black rounded-sm mt-8">--%>
<%--            <div class="border-b-1 border-b-black m-2 pb-1">--%>
<%--                <a href="/topics/questions" class="text-gray-500 hover:underline">Questions</a>--%>
<%--            </div>--%>
<%--            <ul class="m-3">--%>
<%--                <li class="text-sm">--%>
<%--                    <a class="hover:underline" href="/topics/questions/getting_started">--%>
<%--                        <p>Getting Started</p>--%>
<%--                        <p class="text-xs text-gray-400">If you're brand new, start here!</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="/topics/questions/springboot">--%>
<%--                        <p>Springboot Questions</p>--%>
<%--                        <p class="text-xs text-gray-400">Questions about Springboot</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="/topics/questions/minecraft_modding_questions">--%>
<%--                        <p>Minecraft Modding Questions</p>--%>
<%--                        <p class="text-xs text-gray-400">Questions about writing mods for Minecraft</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="/topics/questions/kotlin_questions">--%>
<%--                        <p>Kotlin Questions</p>--%>
<%--                        <p class="text-xs text-gray-400">Questions about Kotlin</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </div>--%>
<%--        <div class="flex-col border-1 border-black rounded-sm mt-8">--%>
<%--            <div class="border-b-1 border-b-black m-2 pb-1">--%>
<%--                <a href="/topics/showcase" class="text-gray-500 hover:underline">Showcase</a>--%>
<%--            </div>--%>
<%--            <ul class="m-3">--%>
<%--                <li class="text-sm">--%>
<%--                    <a class="hover:underline" href="/topics/showcase/applications">--%>
<%--                        <p>Applications</p>--%>
<%--                        <p class="text-xs text-gray-400">Show off your cool applications</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="/topics/showcase/libraries">--%>
<%--                        <p>Libraries</p>--%>
<%--                        <p class="text-xs text-gray-400">Show off your cool libraries</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="/topics/showcase/minecraft_mods">--%>
<%--                        <p>Minecraft Mods</p>--%>
<%--                        <p class="text-xs text-gray-400">Show off your cool Minecraft mods</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </div>--%>
<%--        <div class="flex-col border-1 border-black rounded-sm mt-8">--%>
<%--            <div class="border-b-1 border-b-black m-2 pb-1">--%>
<%--                <a href="" class="text-gray-500 hover:underline">Lorem Ipsum</a>--%>
<%--            </div>--%>
<%--            <ul class="m-3">--%>
<%--                <li class="text-sm">--%>
<%--                    <a class="hover:underline" href="">--%>
<%--                        <p>Lorem Ipsum</p>--%>
<%--                        <p class="text-xs text-gray-400">Lorem Ipsum dolor filler text</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="">--%>
<%--                        <p>Lorem Ipsum</p>--%>
<%--                        <p class="text-xs text-gray-400">Lorem Ipsum dolor filler text</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="">--%>
<%--                        <p>Lorem Ipsum</p>--%>
<%--                        <p class="text-xs text-gray-400">Lorem Ipsum dolor filler text</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--                <li class="text-sm mt-3">--%>
<%--                    <a class="hover:underline" href="">--%>
<%--                        <p>Lorem Ipsum</p>--%>
<%--                        <p class="text-xs text-gray-400">Lorem Ipsum dolor filler text</p>--%>
<%--                    </a>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </div>--%>
    </div>
`)