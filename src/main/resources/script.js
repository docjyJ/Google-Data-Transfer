(function (){
    document.querySelectorAll('.hider').forEach(item => {
        item.addEventListener('click', event => {
            event.preventDefault();
            var element = event.target.parentNode.parentNode.classList;
            if(element.contains("hide")){
                element.remove("hide")
            } else {
                element.add("hide")
            }
        })
    })
})("docReady", window);
