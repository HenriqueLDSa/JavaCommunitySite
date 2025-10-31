@import java.util.List
@import java.util.Map

@param List<Map<String, String>> categories

<option value="" disabled selected>Select a Category</option>
@for(Map<String, String> category : categories)
    <option value="${category.get("aturi")}">${category.get("name")}</option>
@endfor
