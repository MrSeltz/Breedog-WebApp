//Edit Button post changes when submitting
document.getElementById('edit-photo-btn').addEventListener('click',
    postPhoto);

//Edit Button post changes when submitting
document.getElementById('edit-description-btn').addEventListener('click',
    postDescription);

//Edit Button post changes when submitting
document.getElementById('edit-address-btn').addEventListener('click',
    postAddress);

//Edit Button post changes when submitting
document.getElementById('edit-vat-btn').addEventListener('click',
    postVat);

//Edit Button post changes when submitting
document.getElementById('edit-email-btn').addEventListener('click',
    postEmail);

//Edit Button post changes when submitting
document.getElementById('edit-phone-btn').addEventListener('click',
    postPhone);

//Edit Button post changes when submitting
document.getElementById('change-password-btn').addEventListener('click',
    changePassword);


$(document).ready(function () {

    var userInfo = getUserInfo();

    // Load profile info
    loadBreeder(userInfo);

});

function loadBreeder(userInfo) {
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('GET', url);
    request.send();
    request.onload = load;

    function load() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 201) {
                var jsonData = JSON.parse(request.responseText);

                if (jsonData.hasOwnProperty('breeds')) {
                    var breeds_res = jsonData["breeds"];
                    for (var i = 0; i < breeds_res.length; i++) {
                        //var breed = breeds_res[i].breed;
                        //$("#breed-info").append("<tr><td>" + breeds_res[i].breed["fci"] + "</td>" + "<td>" + breeds_res[i].breed["bgroup"] +"</td>" + "<td>"+ breeds_res[i].breed["bname"] +"</td>" + "<td>"+ breeds_res[i].breed["bcount"] +"</td>" + "</tr>" );
                    }
                }

                if (jsonData.hasOwnProperty('breeder')) {
                    var resource = jsonData['breeder'];
                    // load retrieved data into the page
                    var name = resource["name"];
                    var surname = resource["surname"];
                    var birth = resource["birth"];
                    var vat = resource["vat"];
                    var tel = resource["telephone"];
                    var full_add = resource["address"];
                    var email = resource["email"];
                    var photo = resource["photo"];
                    var description = resource["description"];

                    // split the full_add chunk into: address-number-city-zip 
                    var add = full_add.split(',');

                    //$("#user-name").attr("value", name);
                    //$("#user-surname").attr("value", surname);
                    //$("#user-birth").attr("value", birth);
                    //$("#user-username").attr("value", userInfo.username);
                    //$("#user-fc").attr("value", userInfo.breederfc);
                    $("#user-vat").attr("value", vat);
                    $("#user-email").attr("value", email);
                    $("#user-telephone").attr("value", tel);
                    $("#user-address").attr("value", add[0]);
                    $("#user-number").attr("value", add[1]);
                    $("#user-city").attr("value", add[2]);
                    //$("#user-state").attr("value", add[1]);
                    $("#user-zip").attr("value", add[3]);

                    if (photo == null || photo == ''){
                        document.getElementById('user-photo').src = '../media/user.jpg';
                    }
                    
                    else{
                        $("#user-photo").attr("src", photo);
                    }

                    if(description == null || description == ''){
                        description = 'Hey! Add a description of your activity!';
                        $("#user-description").text(description);
                    }

                    else{
                        $("#user-description").text(description);
                    }

                    /* User state dropdown bar selection feature
                    $("#user-state option[selected=\"selected\"]").attr("selected", false);
                    $("#user-state option[value=\"" + state + "\"]").attr("selected", true);
                    */

                    //create the element
                    //txt = "";
                    //txt += "<h3>" + name + " " + surname + "</h3>";

                    //insert it in the page
                    //$(txt).insertAfter($("#logo").parent());
                    //$(txt).insertBefore($("#user-descr"));

                    if (vat.length > 0) {
                        // Hide vat block if user has already added vat
                        document.getElementById("vat-row").style.display = "none";
                    }

                    return
                }
                else {
                    var resource = jsonData['message'];
                    alert(resource['message']);
                }
            }
        }
    }
}


function postPhoto() {
    
    var address = $("#user-address").val();
    var add = address.split(", ")[0];
    var n = address.split(", ")[1];

    var data = JSON.stringify({
        "breeder": {
            "name": $.trim($("#user-name").val()),
            "surname": $.trim($("#user-surname").val()),
            "birth": $.trim($("#user-birth").val()),
            "email": $.trim($("#user-email").val()),
            "telephone": $.trim($("#user-telephone").val()),
            "address": $.trim($("#user-address").val() + "," + $("#user-number").val() + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc": userInfo.breederfc,
            "vat": $.trim($("#user-vat").val()),
            "photo": $.trim($("#photo").val()),
            "description": $.trim($("#user-description").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('POST', url);
    request.send(data);
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }
}

function postDescription() {
    
    var address = $("#user-address").val();
    var add = address.split(", ")[0];
    var n = address.split(", ")[1];

    var data = JSON.stringify({
        "breeder": {
            "name": $.trim($("#user-name").val()),
            "surname": $.trim($("#user-surname").val()),
            "birth": $.trim($("#user-birth").val()),
            "email": $.trim($("#user-email").val()),
            "telephone": $.trim($("#user-telephone").val()),
            "address": $.trim($("#user-address").val() + "," + $("#user-number").val() + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc": userInfo.breederfc,
            "vat": $.trim($("#user-vat").val()),
            "photo": $.trim($("#user-photo").val()),
            "description": $.trim($("#user-description").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('POST', url);
    request.send(data);
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }
}

function postAddress() {

    var address = $("#user-address").val();
    var add = address.split(", ")[0];
    var n = address.split(", ")[1];

    var data = JSON.stringify({
        "breeder": {
            "name": $.trim($("#user-name").val()),
            "surname": $.trim($("#user-surname").val()),
            "birth": $.trim($("#user-birth").val()),
            "email": $.trim($("#user-email").val()),
            "telephone": $.trim($("#user-telephone").val()),
            "address": $.trim($("#user-address").val() + "," + $("#user-number").val() + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc": userInfo.breederfc,
            "vat": $.trim($("#user-vat").val()),
            "photo": $.trim($("#user-photo").val()),
            "description": $.trim($("#user-description").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('POST', url);
    request.send(data);
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }
}

function postVat() {

    var address = $("#user-address").val();
    var add = address.split(", ")[0];
    var n = address.split(", ")[1];

    var data = JSON.stringify({
        "breeder": {
            "name": $.trim($("#user-name").val()),
            "surname": $.trim($("#user-surname").val()),
            "birth": $.trim($("#user-birth").val()),
            "email": $.trim($("#user-email").val()),
            "telephone": $.trim($("#user-telephone").val()),
            "address": $.trim($("#user-address").val() + "," + $("#user-number").val() + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc": userInfo.breederfc,
            "vat": $.trim($("#user-vat").val()),
            "photo": $.trim($("#user-photo").val()),
            "description": $.trim($("#user-description").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('POST', url);
    request.send(data);
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }
}

function postEmail() {

    var address = $("#user-address").val();
    var add = address.split(", ")[0];
    var n = address.split(", ")[1];

    var data = JSON.stringify({
        "breeder": {
            "name": $.trim($("#user-name").val()),
            "surname": $.trim($("#user-surname").val()),
            "birth": $.trim($("#user-birth").val()),
            "email": $.trim($("#user-email").val()),
            "telephone": $.trim($("#user-telephone").val()),
            "address": $.trim($("#user-address").val() + "," + $("#user-number").val() + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc": userInfo.breederfc,
            "vat": $.trim($("#user-vat").val()),
            "photo": $.trim($("#user-photo").val()),
            "description": $.trim($("#user-description").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('POST', url);
    request.send(data);
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }
}

function postPhone() {

    var address = $("#user-address").val();
    var add = address.split(", ")[0];
    var n = address.split(", ")[1];

    var data = JSON.stringify({
        "breeder": {
            "name": $.trim($("#user-name").val()),
            "surname": $.trim($("#user-surname").val()),
            "birth": $.trim($("#user-birth").val()),
            "email": $.trim($("#user-email").val()),
            "telephone": $.trim($("#user-telephone").val()),
            "address": $.trim($("#user-address").val() + "," + $("#user-number").val() + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc" : userInfo.breederfc,
            "vat": $.trim($("#user-vat").val()),
            "photo": $.trim($("#user-photo").val()),
            "description": $.trim($("#user-description").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + userInfo.breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('POST', url);
    request.send(data);
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }
}


function changePassword(){

    function check_pwd(pwd1, pwd2)
    {
        if(pwd1 == pwd2)
            return true;
        else
            return false;
    }

    var userInfo = getUserInfo();
    var username = userInfo.username

    var old_password = document.getElementById("old-password").value;
    var new_password = document.getElementById("new-password").value;
    var check_new_password = document.getElementById("check-new-password").value;

    if(!check_pwd(new_password, check_new_password))
    {
        alert("New passwords do not match, fix and retry");
        return;
    }  
    
    var request = new XMLHttpRequest();

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    // send request to login servlet?
    request.open('POST', '../auth?' + 'req=changepw');

    // set request header
    request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + old_password + ":" + new_password));
    request.send();
    request.onload = update_response;

    function update_response() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var jsonData = JSON.parse(request.responseText);
                var message = jsonData["message"];
                alert(message["message"]);
            }
        }
    }

}

/*
function postBreed() {

    var data = JSON.stringify({
        "group": $.trim($("#breed-group").val()),
        "name": $.trim($("#breed-name").val())
    });
    console.log(data);

}
*/

// MODAL RESET WHEN CLOSED

$('#cvat-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#photo-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#cdes-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#cadd-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#cphone-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#cmail-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#cpsw-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

$('#cmail-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

function onFileSelected(event) {

    var selectedFile = event.target.files[0];
    var reader = new FileReader();
  
    var imgtag = document.getElementById("user-photo");
    imgtag.title = selectedFile.name;

    imgtag.style.width = '300px'
    imgtag.style.height = '300px'
    imgtag.style.objectFit = 'cover'

  
    reader.onload = function(event) {
      imgtag.src = event.target.result;
      document.getElementById("photo").value = reader.result;
    }; 
  
    reader.readAsDataURL(selectedFile);
}
