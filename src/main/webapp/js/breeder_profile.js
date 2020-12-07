$(document).ready(function () {

    // get breederfc from the url
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var breederfc = urlParams.get('breederfc');

    // load breeder profile informations
    loadBreeder(breederfc);

    // load dogs informations
    loadDogs(breederfc);
});


function loadBreeder(breederfc) {
    var request = new XMLHttpRequest();
    var url = './breeder/' + breederfc;

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
                        "<tr><th scope='row'>Name</th><td>" +
                        resource["name"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Surname</th><td>" +
                        resource["surname"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Email</th><td>" +
                        resource["email"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Telephone</th><td>" +                        
                        resource["telephone"] +
                        "</td></tr>" +
                        "<tr><th scope='row'>Address</th><td>" +                                                
                        resource["address"] +
                        "</td></tr>"
                    );

                    //create the element
                    txt1 = "";
                    txt1 += "<h2 class='p-3 text-center'>" + resource["name"] + " " + resource["surname"] + "</h2>";
                    txt2 = "";
                    txt2 += "<h3>" + resource["name"] + " " + resource["surname"] + "</h3>";

                    //insert it in the page
                    $(txt1).insertBefore($("#id-title"));
                    //$(txt2).insertAfter($("#logo").parent());

                    var photo = resource["photo"];
                    var description = resource["description"];

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

function loadDogs(breederfc) {
    var request = new XMLHttpRequest();
    var url = './breeder/dog/' + breederfc;

    if (!request) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.open('GET', url);
    request.send();
    request.onload = doglist;

    function doglist() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 201) {
                var jsonData = JSON.parse(request.responseText);

                if (jsonData.hasOwnProperty('resource-list')) {
                    var dogs = jsonData["resource-list"];
                    $("#articles").empty();
                    for (var i = 0; i < dogs.length; i++) {
                        //var art = $("<article></article>");
                        var card_div = $("<div class=\"card text-center\"></div>");
                        //var img_div = $("<div class=\"column p-4\"></div>");

                        // LOAD AND APPEND IMAGE
                        dog_img = $("<img/>", {'src': dogs[i].dog["photo"], "class": "dogimage p-1 m-3 img-fluid img-thumbnail rounded card-img-top text-center"});

                        //img_div.append(dog_img);
                        card_div.append(dog_img);
                       
                        // FIRST CHUNK
                        var card_body = $('<div/>', {'class': "c-body"});
                        var title = $('<h5/>', {'class': "card-title", 'html': "<strong>" + dogs[i].dog["name"] + "</strong>"});
                        var text = $('<p/>', {'class': "card-text p-1 my-auto", 'html': dogs[i].dog["sex"]});
                        var footer = $('<div/>', {'class': "card-footer"});
                        var footer_text = $('<small/>', {'class': "text-muted", 'html':  dogs[i].dog["birth"]});
                        
                        
                        card_body.append(title);
                        card_body.append(text);
                        footer.append(footer_text);

                        card_div.append(card_body);
                        card_div.append(footer);
                        
                        var a =
                            $('<a>', {
                                'href': "./dog.html?dogmc=" + dogs[i].dog["microchip"],
                                "class": "dogresult m-1"
                            });

                        a.append(card_div);
                        $("#articles").append(a);
                    }
                }
            }
        }
    }

    
}