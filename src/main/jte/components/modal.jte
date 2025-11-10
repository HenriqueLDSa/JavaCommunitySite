@import gg.jte.Content

@param String title
@param Content content
@param Content buttons

<div id="modal-bkg" class="modal-bkg">
    <div class="modal">
        <div class="header">
            <h3>${title}</h3>
            <button class="button icon" onclick="closeModal()">
                <img src="/close.svg" alt="Close" />
            </button>
        </div>

        ${content}

        <div class="buttons">
            ${buttons}
        </div>
    </div>

    <script>
        function closeModal() {
            document.getElementById("modal-bkg").remove()
        }
    </script>
</div>