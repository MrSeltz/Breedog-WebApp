// Save UserInfo in cache
function saveUserInfo() {
  localStorage.setItem("username", document.getElementById("input_username").value);
  localStorage.setItem("token", "token");
}

// Login-Button-click trigger the reading of accounts saved in JSON file
$(".logreg").click(function() {

  var username = document.getElementById("input_username").value;
  var password = document.getElementById("input_pwd").value;

  $.getJSON("./mockup-data/user.json", function(data) {
    $.each(data, function(key, val) {
      if (username == val.username && password == val.password) {
        // Save user info in cache
        saveUserInfo();
        window.location = 'user/home.html';
        return;
      }
    });

    var info = getUserInfo();
    if (info.username == null)
      alert('Invalid Usernamer or Password.');

  });
});
