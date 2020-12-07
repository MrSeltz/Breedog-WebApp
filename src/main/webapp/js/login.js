// Login-Button-click
$("#login_input").click(function () {

    var username = document.getElementById("input_username").value;
    var password = document.getElementById("input_pwd").value;

    var request = new XMLHttpRequest();

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    // send request to login servlet?
    request.open('POST', './auth?' + 'req=login');
    // set request header
    request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    request.send();
    request.onload = login;

    function login() {
        if (request.readyState === XMLHttpRequest.DONE) {

            // If there is authentication error
            if (request.status == 401) {
                // append error message to the page
                console.log("Auth error");
                alert('Authentication Error: Username or Password is wrong.');
                return
            }

            if (request.status == 405) {
                // append error message to the page
                console.log("Auth error");
                alert('Authentication Error: METHOD NOT ALLOWED.');
                return
            }

            // -----NEED TO ADD MANAGEMENT FOR AUTHENTICATION ERROR -----
            var jsonData = JSON.parse(request.responseText);

            if (jsonData.hasOwnProperty('account')) {
                var resource = jsonData['account'];
                if (request.status == 200) {
                    // redirect to the user homepage
                    saveUserInfo(resource["username"], resource["breederfc"]);
                    window.location = 'user/home.html';
                    return
                }
            }
            else {
                var resource = jsonData['message'];
                if (request.status == 200) {
                    alert(resource['message']);
                    return;
                }
            }
        }
    }

    // Save UserInfo in cache
    function saveUserInfo(username, breederfc) {
        localStorage.setItem("username", username);
        localStorage.setItem("breederfc", breederfc);
    }
});


//Register-Button-click
$("#register_input").click(function () {

    var satisfy = false;

    var userUsername = document.getElementById("runame");
    var userPassword = document.getElementById("rpsw");
    var userCheck_password = document.getElementById("rco_psw");
    var userBirth = document.getElementById("birth");
    var userNam = document.getElementById("rname");
    var userSurname = document.getElementById("rsurname");
    var userAddress = document.getElementById("raddress");
    var userNumber = document.getElementById("rn");
    var userCity = document.getElementById("rcity");
    var userCap = document.getElementById("rcap");
    var userBreederfc = document.getElementById("rbreederfc");
    var userPhnum = document.getElementById("rphnum");
    var userEmail = document.getElementById("remail");

    while (!satisfy) {
        satisfy = true;
        var username = userUsername.value;
        var password = userPassword.value;
        //Check if password is valid
        if (!check_valid_pwd(password)) {
            alert("Invalid password!");
            satisfy = false;
            return;
        }
        var check_password = userCheck_password.value;
        // Check if passwords are the same
        if(!check_pwd(password, check_password))
        {
            alert("Passwords do not match, fix and retry :(");
            satisfy = false;
            return;
        }
        var birth = userBirth.value;
        var name = userNam.value;
        var surname = userSurname.value;
        var address = userAddress.value;
        var number = userNumber.value;
        var city = userCity.value;
        var cap = userCap.value;
        var breederfc = userBreederfc.value;
        var phnum = userPhnum.value;
        var email = userEmail.value;

        //Remove error border
        userUsername.classList.remove('is-invalid');
        userPassword.classList.remove('is-invalid');
        userCheck_password.classList.remove('is-invalid');
        userBirth.classList.remove('is-invalid');
        userNam.classList.remove('is-invalid');
        userSurname.classList.remove('is-invalid');
        userAddress.classList.remove('is-invalid');
        userNumber.classList.remove('is-invalid');
        userCity.classList.remove('is-invalid');
        userCap.classList.remove('is-invalid');
        userBreederfc.classList.remove('is-invalid');
        userPhnum.classList.remove('is-invalid');
        userEmail.classList.remove('is-invalid');

        //Check if there are null parameters
        if (username.length === 0 || password.length === 0 || check_password.length === 0 || birth.length === 0 || name.length === 0 
            || surname.length === 0 || address.length === 0 || number.length === 0 || city.length === 0 || cap.length === 0 || breederfc.length === 0 
            || phnum.length === 0 || email.length === 0) {
            satisfy = false;
            alert("Fill the required field marked in red");
            // mark empty parameters with a red border
            if (username.length === 0)
                userUsername.classList.add('is-invalid');
            if (password.length === 0)
                userPassword.classList.add('is-invalid');
            if (check_password.length === 0)
                userCheck_password.classList.add('is-invalid');
            if (birth.length === 0)
                userBirth.classList.add('is-invalid');
            if (name.length === 0)
                userNam.classList.add('is-invalid');
            if (surname.length === 0)
                userSurname.classList.add('is-invalid');
            if (address.length === 0)
                userAddress.classList.add('is-invalid');
            if (number.length === 0)
                userNumber.classList.add('is-invalid');
            if (city.length === 0)
                userCity.classList.add('is-invalid');
            if (cap.length === 0)
                userCap.classList.add('is-invalid');
            if (breederfc.length === 0)
                userBreederfc.classList.add('is-invalid');
            if (phnum.length === 0)
                userPhnum.classList.add('is-invalid');
            if (email.length === 0)
                userEmail.classList.add('is-invalid');
            break;
        }
    }
    
    if (satisfy) {

        userBirth.classList.add("is-invalid");

        var request = new XMLHttpRequest();

        if (!request) {
            alert('Giving up :( Cannot create an XMLHTTP instance');
            return false;
        }

        // send request to login servlet?
        request.open('POST', './signup');
        // set request header
        request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        request.send(JSON.stringify({"account" : {
                                                                                    "username" : username,
                                                                                    "password" : password,
                                                                                    "breederfc" : breederfc
                                                                                    },
                                                            "breeder" : {
                                                                                    "name" : name, 
                                                                                    "surname" : surname,
                                                                                    "birth" : birth,
                                                                                    "address" : address + ',' + number + ',' + city + ',' +cap,
                                                                                    "breederfc" : breederfc,
                                                                                    "telephone" : phnum,
                                                                                    "email" : email
                                                            }}));
        request.onload = signup;

    }

    function check_valid_pwd(pwd) {
        var validPassword = [false, false, false];
        for (i = 0; i < pwd.length; i++) {
            c = pwd.charAt(i);
            if (!isNaN(c * 1)) {
                validPassword[0] = true;
            } else {
                if (c == c.toUpperCase()) {
                    validPassword[1] = true;
                }
                if (c == c.toLowerCase()) {
                    validPassword[2] = true;
                }
            }
        }    
        if (pwd.length < 8 || pwd.length > 30 || !validPassword[0] || !validPassword[1] || !validPassword[2]) {
            return false;
        } else {
            return true;
        }
    }
    
    function check_pwd(pwd1, pwd2)
    {
        if(pwd1 == pwd2)
            return true;
        else
            return false;
    }


    function signup() {
        if (request.readyState === XMLHttpRequest.DONE) {

            // If there is authentication error
            if (request.status == 401) {
                alert('Authentication Error: Username or Password is wrong.');
                return
            }

            if (request.status == 405) {
                alert('Authentication Error: METHOD NOT ALLOWED.');
                return
            }

            // If all goes well, aler with server response and redirect to the homepage
            if(request.status == 200){
                var jsonData = JSON.parse(request.responseText);
                alert(jsonData["message"].message);
                window.location = './';
            }
        }
    }
});

//enter key trigger login button inside login modal
document.getElementById("pop1")
    .addEventListener("keyup", function (event) {
        event.preventDefault();
        if (event.keyCode === 13) {
            document.getElementById("login_input").click();
        }
    });

//enter key trigger login button inside register modal
document.getElementById("pop2")
    .addEventListener("keyup", function (event) {
        event.preventDefault();
        if (event.keyCode === 13) {
            document.getElementById("register_input").click();
        }
    });
