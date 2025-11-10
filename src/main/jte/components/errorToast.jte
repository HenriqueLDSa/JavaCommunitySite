@param String toastMsg

!{String id = String.valueOf(Math.round(Math.random() * 10000));}

<div id="toast-${id}" class="toast">

    <img src="/error.svg" alt="Error" />
    <p>${toastMsg}</p>

    <script>
        setTimeout(() => {
            const toast = document.getElementById("toast-${id}")
            toast.style.animation = "toast-outro 200ms forwards"
            setTimeout(() => {
                toast.remove()
            }, 200)
        }, 5000)
    </script>
</div>