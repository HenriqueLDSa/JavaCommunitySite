// package com.jcs.javacommunitysite.pages.homepage;

// import com.jcs.javacommunitysite.atproto.AtprotoClient;
// import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
// import org.jooq.DSLContext;
// import org.springframework.web.bind.annotation.*;

// import java.time.Duration;
// import java.time.OffsetDateTime;
// import java.util.*;
// import java.util.stream.Collectors;

// import static com.jcs.javacommunitysite.jooq.tables.Category.CATEGORY;
// import static com.jcs.javacommunitysite.jooq.tables.Group.GROUP;
// import static com.jcs.javacommunitysite.jooq.tables.Post.POST;
// import static org.jooq.impl.DSL.currentTimestamp;

// @RestController
// public class HomepageController {

//     private final AtprotoSessionService sessionService;
//     private final DSLContext dsl;

//     public HomepageController(AtprotoSessionService sessionService, DSLContext dsl) {
//         this.sessionService = sessionService;
//         this.dsl = dsl;
//     }

//     @GetMapping("/groups/categories")
//     public List<Map<String, Object>> getGroupsCategories() {

//         try {
//             OffsetDateTime twentyFourHoursAgo = OffsetDateTime.now().minusHours(24);

//             // Fetch all groups with their categories
//             var groupsWithCategories = dsl.select(
//                             GROUP.NAME.as("group_name"),
//                             GROUP.DESCRIPTION.as("group_description"),
//                             GROUP.ATURI.as("group_aturi"),
//                             CATEGORY.NAME.as("category_name"),
//                             CATEGORY.ATURI.as("category_aturi"),
//                             CATEGORY.CATEGORY_TYPE.as("category_type"),
//                             CATEGORY.DESCRIPTION.as("category_description"),
//                             dsl.selectCount()
//                                     .from(POST)
//                                     .where(POST.CATEGORY_ATURI.eq(CATEGORY.ATURI))
//                                     .and(POST.CREATED_AT.greaterThan(twentyFourHoursAgo))
//                                     .asField("posts_24hr")
//                     ).from(GROUP)
//                     .leftJoin(CATEGORY).on(CATEGORY.GROUP.eq(GROUP.ATURI))
//                     .orderBy(GROUP.NAME.asc(), CATEGORY.NAME.asc())
//                     .fetch();

//             // Group the results by group
//             var groupedData = groupsWithCategories.stream()
//                     .collect(Collectors.groupingBy(
//                             record -> Map.of(
//                                     "name", Objects.requireNonNull(record.get("group_name")),
//                                     "description", Objects.requireNonNull(record.get("group_description")),
//                                     "aturi", Objects.requireNonNull(record.get("group_aturi"))
//                             ),
//                             Collectors.mapping(
//                                     record -> {
//                                         var categoryName = record.get("category_name");
//                                         if (categoryName != null) {
//                                             return Map.of(
//                                                     "name", Objects.requireNonNull(record.get("category_name")),
//                                                     "aturi", Objects.requireNonNull(record.get("category_aturi")),
//                                                     "category_type", record.get("category_type") != null ? record.get("category_type") : "",
//                                                     "description", record.get("category_description") != null ? record.get("category_description") : "",
//                                                     "posts_24hr", Objects.requireNonNull(record.get("posts_24hr"))
//                                             );
//                                         }
//                                         return null;
//                                     },
//                                     Collectors.filtering(
//                                             Objects::nonNull,
//                                             Collectors.toList()
//                                     )
//                             )
//                     ));

//             // Convert to a more structured format
//             return groupedData.entrySet().stream()
//                     .map(entry -> {
//                         var group = entry.getKey();
//                         var categories = entry.getValue();
//                         return Map.of(
//                                 "group", group,
//                                 "categories", categories
//                         );
//                     })
//                     .toList();

//         } catch (Exception e){
//             return Collections.emptyList();
//         }
//     }
// }