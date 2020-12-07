/*
//Edit Button post changes when submitting
document.getElementById('edit-profile-btn').addEventListener('click',
    postUser);

//Edit Button post changes when submitting
document.getElementById('edit-breed-btn').addEventListener('click',
    postBreed);

//When selecting another state change attribute in select element
/* State dropdown menù features
document.getElementById('user-state').onchange = function () {

    $("#user-state option[selected=\"selected\"]").attr("selected", false);
    $("#user-state option[value=\"" + this.value + "\"]").attr("selected", true);

};
*/

$(document).ready(function () {

    var userInfo = getUserInfo();

    loadBreeder(userInfo);

    // Set link to show profile from others view
    document.getElementById("show-profile").href = "./breeder.html?breederfc=" + userInfo.breederfc;
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
                        $("#breed-info").append("<tr><td>" + breeds_res[i].breed["fci"] + "</td>" + "<td>" + breeds_res[i].breed["bgroup"] + "</td>" + "<td>" + breeds_res[i].breed["bname"] + "</td>" + "<td>" + breeds_res[i].breed["bcount"] + "</td>" + "</tr>");
                    }
                }

                if (jsonData.hasOwnProperty('breeder')) {
                    var resource = jsonData['breeder'];
                    $("#user-info").append(
                        "<tr><th scope='row'>Username</th><td>" +
                        userInfo.username +
                        "</td></tr>" +
                        "<tr><th scope='row'>Email</th><td>" +
                        resource["email"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Telephone</th><td>" +                        
                        resource["telephone"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Birthday</th><td>" +                                                
                        resource["birth"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Address</th><td>" +                                                
                        resource["address"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Breeder FC</th><td>" +                                                
                        userInfo.breederfc +
                        "</td></tr>" +
                        "<tr><th scope='row'>VAT</th><td>" +                                                
                        resource["vat"] +
                        "</td></tr>"
                    );
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

                    $("#user-name").attr("value", name);
                    $("#user-surname").attr("value", surname);
                    $("#user-birth").attr("value", birth);
                    $("#user-username").attr("value", userInfo.username);
                    $("#user-fc").attr("value", userInfo.breederfc);
                    $("#user-vat").attr("value", vat);
                    $("#user-email").attr("value", email);
                    $("#user-telephone").attr("value", tel);
                    $("#user-address").attr("value", add[0] + ", " + add[1]);
                    $("#user-city").attr("value", add[2]);
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
                    txt = "";
                    txt += "<h3>" + name + " " + surname + "</h3>";

                    $(txt).insertAfter($("#logo").parent());
                    $(txt).insertBefore($("#user-descr"));

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

/*
    Populate Breed Row and Modal

function populateBreed() {

    var request = new XMLHttpRequest();

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }
    request.onload = parseBreed;
    request.overrideMimeType("application/json");
    request.open('GET', '../mockup-data/mockup-breed.json');
    request.send();


    function parseBreed() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {

                //jsonResponse contains data about breeds related to user
                var jsonResponse = JSON.parse(request.responseText);

                for (i = 0; i < jsonResponse.length; i++) {
                    var fci = jsonResponse[i]["fci"];
                    var group = jsonResponse[i]["group"];
                    var name = jsonResponse[i]["name"];

                    $("#breed-fci").attr("value", fci);
                    $("#breed-group").attr("value", group);
                    $("#breed-name").attr("value", name);

                    //create the table cells
                    txt = "";
                    txt += "<td>" + fci + "</td>";
                    txt += "<td>" + group + "</td>";
                    txt += "<td>" + name + "</td>";

                    //append the element to the table
                    $("#breed-row").append(txt);
                }
            } else {
                console.log('There was a problem with the request.');
            }
        }
    }
}*/

/*
    Populate user information in page and modal

function populateUser() {

    var request = new XMLHttpRequest();

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }
    request.onload = parseUser;
    request.overrideMimeType("application/json");
    request.open('GET', '../mockup-data/mockup-user.json');
    request.send();


    function parseUser() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {

                //jsonResponse contains data about the user
                var jsonResponse = JSON.parse(request.responseText);

                for (i = 0; i < jsonResponse.length; i++) {
                    var name = jsonResponse[i]["name"];
                    var surname = jsonResponse[i]["surname"];
                    var birth = jsonResponse[i]["birth"];
                    var usr = jsonResponse[i]["usr"];
                    var pwd = jsonResponse[i]["pwd"];
                    var fc = jsonResponse[i]["FC"];
                    var vat = jsonResponse[i]["VAT"];
                    var license = jsonResponse[i]["license"];
                    var tel = jsonResponse[i]["tel"];
                    var address = jsonResponse[i]["address"];
                    var city = jsonResponse[i]["city"];
                    var state = jsonResponse[i]["state"];
                    var zip = jsonResponse[i]["zip"];
                    var email = jsonResponse[i]["email"];


                    $("#user-name").attr("value", name);
                    $("#user-surname").attr("value", surname);
                    $("#user-birth").attr("value", birth);
                    $("#user-username").attr("value", usr);
                    $("#user-fc").attr("value", fc);
                    $("#user-vat").attr("value", vat);
                    $("#user-license").attr("value", license);
                    $("#user-password").attr("value", pwd);
                    $("#user-email").attr("value", email);
                    $("#user-telephone").attr("value", tel);
                    $("#user-address").attr("value", address);
                    $("#user-city").attr("value", city);
                    $("#user-state").attr("value", state);
                    $("#user-zip").attr("value", zip);

                    
                    $("#user-state option[selected=\"selected\"]").attr("selected", false);
                    $("#user-state option[value=\"" + state + "\"]").attr("selected", true);
                    

                    //create the element
                    txt = "";
                    txt += "<h3>" + name + " " + surname + "</h3>";

                    //insert it in the page
                    $(txt).insertAfter($("#logo").parent());
                    $(txt).insertBefore($("#user-descr"));
                }
            } else {
                console.log('There was a problem with the request.');
            }
        }
    }

}*/

/*
    Populate country picker
*/
function populateCountries() {

    var request = new XMLHttpRequest();

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }
    request.onload = parseCountries;
    request.overrideMimeType("application/json");
    request.open('GET', '../mockup-data/countries.json');
    request.send();


    function parseCountries() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {

                var jsonResponse = JSON.parse(request.responseText);

                for (i = 0; i < jsonResponse.length; i++) {
                    var country = jsonResponse[i]["name"];
                    var code = jsonResponse[i]["code"];

                    txt = "";
                    txt += "<option value=\"" + code +
                        "\">" + country + "</option>";

                    $("#user-state").append(txt);
                }
            } else {
                console.log('There was a problem with the request.');
            }
        }
    }
}


/*
    Log modified data
*/
function postUser() {
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
            "address": $.trim(add + "," + n + "," + $("#user-city").val() + "," + $("#user-zip").val()),
            "breederfc": $.trim($("#user-fc").val()),
            "vat": $.trim($("#user-vat").val())
        }
    });

    console.log(data);
    var request = new XMLHttpRequest();
    var url = './breeder/' + $.trim($("#user-fc").val());

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

/*
    Log modified data
*/
function postBreed() {

    var data = JSON.stringify({
        "group": $.trim($("#breed-group").val()),
        "name": $.trim($("#breed-name").val())
    });
    console.log(data);

}

//DEFAULT IMAGE SCRIPT

/*
function standby() {
    document.getElementById('user-photo').src = 'user.jpg'
}
*/
