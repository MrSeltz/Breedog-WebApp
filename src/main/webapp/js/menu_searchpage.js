$(document).ready(function () {

    /*
        Construct TOPBAR
    */

    var jsFileLocation = $('script[src*=menu_searchpage]').attr('src');  // the js file path
    jsFileLocation = jsFileLocation.replace('menu_searchpage.js', '');


    var breederfc = localStorage.breederfc;

    // Nav element
    let nav = document.createElement("nav");
    nav.setAttribute("class", "row navbar sticky-top navbar-light");
    nav.setAttribute("id", "topbar");

    // Outer collapsible div
    let div2 = document.createElement("div");
    div2.setAttribute("class", "collapse w-100");
    div2.setAttribute("id", "navbarToggleExternalContent");



    // Toggle icon
    let toggle = document.createElement("button");
    toggle.setAttribute("class", "navbar-toggler");
    toggle.setAttribute("type", "button");
    toggle.setAttribute("data-toggle", "collapse");
    toggle.setAttribute("data-target", "#navbarToggleExternalContent");

    let span = document.createElement("span");
    span.setAttribute("class", "navbar-toggler-icon");

    toggle.appendChild(span);
    nav.appendChild(toggle);


    // Inner div with menu links
    let div = document.createElement("div");
    div.setAttribute("class", "nav flex-column");

    if (breederfc !== undefined) {


        // First link with logo
        let a = document.createElement("a");
        a.setAttribute("href", jsFileLocation + "../user/home.html");
        a.setAttribute("class", "col-3");

        let logo = document.createElement("img");
        logo.setAttribute("src", jsFileLocation + '../media/logo.svg');
        logo.setAttribute("id", "logo");
        logo.setAttribute("class", "img-fluid mb-2");
        logo.setAttribute("alt", "Responsive image");


        a.appendChild(logo);
        nav.appendChild(a);

        // Home link
        let icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-house-user fa-lg");
        span = document.createElement("span");
        span.setAttribute("class", "btn-text m-4");
        span.innerHTML = "Home";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn-secondary my-1 text-left");
        a.setAttribute("href", jsFileLocation + "../user/home.html");

        a.appendChild(icon);
        a.appendChild(span);
        div.appendChild(a);


        // Dogs link
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-paw fa-lg");
        span = document.createElement("span");
        span.setAttribute("class", "btn-text m-4");
        span.innerHTML = "Dogs";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn_col my-1 text-left");
        a.setAttribute("href", jsFileLocation + "../search?query=" + breederfc);

        a.appendChild(icon);
        a.appendChild(span);
        div.appendChild(a);


        // Message link
        /*
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-comment-alt fa-lg");
        span = document.createElement("span");
        span.setAttribute("class", "btn-text m-4");
        span.innerHTML = "Message";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn-info my-1 text-left");
        a.setAttribute("href", "#");
    
        a.appendChild(icon);
        a.appendChild(span);
        div.appendChild(a);
        */

        // Settings link
        icon = document.createElement("i");
        icon.setAttribute("class", "far fa-edit fa-lg");
        span = document.createElement("span");
        span.setAttribute("class", "btn-text m-4");
        span.innerHTML = "Edit";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn_col my-1 text-left");
        a.setAttribute("href", jsFileLocation + "../user/edit.html");

        a.appendChild(icon);
        a.appendChild(span);
        div.appendChild(a);


        // Logout link
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-sign-out-alt fa-lg");
        span = document.createElement("span");
        span.setAttribute("class", "btn-text m-4");
        span.innerHTML = "Logout";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn-warning my-1");
        a.setAttribute("id", "nav-logout-btn");

        a.appendChild(icon);
        a.appendChild(span);
        div.appendChild(a);

    } else {
        // First link with logo
        let a = document.createElement("a");
        a.setAttribute("href", jsFileLocation + "../index.html");
        a.setAttribute("class", "col-3");

        let logo = document.createElement("img");
        logo.setAttribute("src", jsFileLocation + '../media/logo.svg');
        logo.setAttribute("id", "logo");
        logo.setAttribute("class", "img-fluid mb-2");
        logo.setAttribute("alt", "Responsive image");


        a.appendChild(logo);
        nav.appendChild(a);


    }

    div2.appendChild(div);
    nav.appendChild(div2);


    $("#main-container").prepend(nav);



    /*
        Construct SIDEBAR
    */

    // Outer div
    div2 = document.createElement("div");
    div2.setAttribute("class", "sidebar col-2 col-sm-3 col-md-2");
    div2.setAttribute("id", "sidebar");




    // Inner div with menu links
    div = document.createElement("div");
    div.setAttribute("class", "nav flex-column")

    if (breederfc !== undefined) {


        // First link with logo
        a = document.createElement("a");
        a.setAttribute("href", jsFileLocation + "../user/home.html");

        logo = document.createElement("img");
        logo.setAttribute("src", jsFileLocation + "../media/logo.svg");
        logo.setAttribute("id", "logo");
        logo.setAttribute("class", "img-fluid mb-2");
        logo.setAttribute("alt", "Responsive image");

        a.appendChild(logo);
        div2.appendChild(a);

        // Home link
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-house-user fa-lg");
        p = document.createElement("p");
        p.setAttribute("class", "btn-text m-0");
        p.innerHTML = "Home";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn-secondary my-3");
        a.setAttribute("href", jsFileLocation + "../user/home.html");

        a.appendChild(icon);
        a.appendChild(p);
        div.appendChild(a);


        // Dogs link
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-paw fa-lg");
        p = document.createElement("p");
        p.setAttribute("class", "btn-text m-0");
        p.innerHTML = "Dogs";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn_col my-3");
        a.setAttribute("href", jsFileLocation + "../search?query=" + breederfc);

        a.appendChild(icon);
        a.appendChild(p);
        div.appendChild(a);


        // Message links
        /*
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-comment-alt fa-lg");
        p = document.createElement("p");
        p.setAttribute("class", "btn-text m-0");
        p.innerHTML = "Message";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn-info my-3");
        a.setAttribute("href", "#");
    
        a.appendChild(icon);
        a.appendChild(p);
        div.appendChild(a);
        */

        // Settings link
        icon = document.createElement("i");
        icon.setAttribute("class", "far fa-edit fa-lg");
        p = document.createElement("p");
        p.setAttribute("class", "btn-text m-0");
        p.innerHTML = "Edit";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn_col my-3");
        a.setAttribute("href", jsFileLocation + "../user/edit.html");

        a.appendChild(icon);
        a.appendChild(p);
        div.appendChild(a);


        //Logout link
        icon = document.createElement("i");
        icon.setAttribute("class", "fas fa-sign-out-alt fa-lg");
        p = document.createElement("p");
        p.setAttribute("class", "btn-text m-0");
        p.innerHTML = "Logout";
        a = document.createElement("a");
        a.setAttribute("type", "button");
        a.setAttribute("class", "btn btn_col my-3");
        a.setAttribute("id", "sidebar-logout-btn");

        a.appendChild(icon);
        a.appendChild(p);
        div.appendChild(a);


        div2.appendChild(div);


        $("#main-row").prepend(div2);



        /*
        Bind click events to elements
        */

        $("#nav-logout-btn").click(function () {
            // Clear cache
            clearUserInfo();
        });
        $("#sidebar-logout-btn").click(function () {
            // Clear cache
            clearUserInfo();
        });


    } else {

        // First link with logo
        a = document.createElement("a");
        a.setAttribute("href", jsFileLocation + "../index.html");

        logo = document.createElement("img");
        logo.setAttribute("src", jsFileLocation + "../media/logo.svg");
        logo.setAttribute("id", "logo");
        logo.setAttribute("class", "img-fluid mb-2");
        logo.setAttribute("alt", "Responsive image");

        a.appendChild(logo);
        div2.appendChild(a);

    }
    $("#main-row").prepend(div2);

    div2.appendChild(div);







    // Import menu CSS into head of page
    let head = document.getElementsByTagName("head")[0];
    let link = document.createElement("link");

    link.setAttribute("rel", "stylesheet");
    link.setAttribute("href", jsFileLocation + '../css/menu_css.css');

    head.appendChild(link);

    /*
        Toggle between sidebar and topbar in mobile version
        575px equals sm in bootstrap
    */

    $('#topbar').hide();
    $('#sidebar').hide();

    if (getWidth() <= 575) {
        $('#topbar').show();
    }
    else {
        $('#sidebar').show();
    }

});



/*
    Toggle between sidebar and topbar when resizing window
    575px equals sm in bootstrap
*/

$(window).resize(function () {

    $('#topbar').hide();
    $('#sidebar').hide();

    if (getWidth() <= 575) {
        $('#topbar').show();
    }
    else {
        $('#sidebar').show();
    }
});



function getWidth() {
    return Math.max(
        document.documentElement.clientWidth,
        document.documentElement.offsetWidth,
        document.documentElement.scrollWidth,
        window.innerWidth,
        $(window).width(),
    );
}