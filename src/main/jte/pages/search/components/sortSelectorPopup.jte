@param String id

<div id="${id}" class="tag-selector-popup hidden preload">
    <p><b>Sort By</b></p>

    <div class="simple-radio-selector">
        <label>
            <input type="radio" name="sortBy" value="relevance" checked>
            <span>Relevance</span>
        </label>
        <label>
            <input type="radio" name="sortBy" value="time-posted">
            <span>Time Posted</span>
        </label>
        <label>
            <input type="radio" name="sortBy" value="num-replies">
            <span>Number of Replies</span>
        </label>
    </div>


    <p><b>Direction</b></p>
    <div class="simple-radio-selector">
        <label>
            <input type="radio" name="sortDir" value="asc" checked>
            <span>Ascending</span>
        </label>
        <label>
            <input type="radio" name="sortDir" value="desc">
            <span>Descending</span>
        </label>
    </div>

</div>
