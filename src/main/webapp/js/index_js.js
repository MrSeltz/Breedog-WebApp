
// Redirect the to user.html if already login
(function () {
    $(document).ready(function () {
        const info = getUserInfo();

        if (info.username != null)
            window.location = 'user/home.html';
    });
})();