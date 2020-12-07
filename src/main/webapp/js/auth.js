// UserInfo OBJ 
var userInfo = {
    username: null,
    breederfc: null,
}

// Get userInfo from cache
function getUserInfo() {
    userInfo.username = localStorage.getItem("username");
    userInfo.breederfc = localStorage.getItem("breederfc");
    return userInfo;
}


// Clear Cache
function clearUserInfo() {
    // REMOVE SESSION COOKIE CALLING LOGOUT REST API
    var request = new XMLHttpRequest();

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var jsFileLocation = $('script[src*=auth]').attr('src');  // the js file path
    jsFileLocation = jsFileLocation.replace('auth.js', '');

    // send request to login servlet?
    request.open('POST', jsFileLocation + '../user/logout');
    request.send();
    request.onload = logout;

    function logout () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200)
            {
                alert("Successfully Logout.");
                // Clear local cache
                localStorage.clear();
                window.location = jsFileLocation + '../index.html';
            }
            else
                alert("We incurred in some problems during logout" );
        }
        else
            alert("Problems for the response - ERROR: " + request.status);
    }
}