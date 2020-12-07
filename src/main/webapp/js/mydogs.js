// Load function on ready
$(document).ready(function () {
    var userInfo = getUserInfo();
    //window.location.replace("../search?query=" + userInfo.breederfc);
    loadDogs(userInfo);

});


function loadDogs(userInfo) {
    var request = new XMLHttpRequest();
    var url = './breeder/dog/' + userInfo.breederfc;

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
                        var art = $("<article></article>");
                        var row_div = $("<div class=\"row m-3 p-3 bg-white rounded box-shadow dog-box\"></div>");
                        var img_div = $("<div class=\"column p-4\"></div>");

                        // LOAD AND APPEND IMAGE
                        dog_img = $("<img/>", {
                            'src': dogs[i].dog["photo"],
                            "class": "dogimage my-auto"
                        });

                        img_div.append(dog_img);
                        row_div.append(img_div);

                        // FIRST CHUNK
                        var col_div =
                            $('<div/>', {
                                'class': "column p-3 my-auto",
                                //'style':"font-size: calc(12px + 1vw);",
                                'html': "Name: <strong> " + dogs[i].dog["name"] + " </strong><br> Gender: " + dogs[i].dog["sex"] + "  <br> Breed: " + dogs[i].dog["fci"]
                            });
                        row_div.append(col_div);

                        // SECOND CHUNK
                        var col_div =
                            $('<div/>', {
                                'class': "column p-3 my-auto",
                                'html': "Birth: " + dogs[i].dog["birth"] + "<br> Kannel: " + dogs[i].dog["kennel"] + " <br> Microchip: " + dogs[i].dog["microchip"]
                            });
                        row_div.append(col_div);

                        var a =
                            $('<a>', {
                                'href': "./dog.html?dogmc=" + dogs[i].dog["microchip"],
                                "class": "dogresult"
                            });
                        row_div.append(col_div);

                        art.append(row_div);
                        a.append(art);
                        $("#articles").append(a);
                    }
                }
            }
        }
    }
}
