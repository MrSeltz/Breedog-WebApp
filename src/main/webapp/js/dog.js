// Initialize as global variables al dog info parameters
var dog_info;
var breed;
var pathology;
var awards;
var events;
var parents;
var sons;


function onFileSelected(event) {

    var selectedFile = event.target.files[0];
    var reader = new FileReader();

    var imgtag = document.getElementById("modal_dog_photo");
    imgtag.title = selectedFile.name;

    imgtag.style.width = '300px'
    imgtag.style.height = '300px'
    imgtag.style.objectFit = 'cover'


    reader.onload = function (event) {
        imgtag.src = event.target.result;
        document.getElementById("photo").value = reader.result;
    };

    reader.readAsDataURL(selectedFile);
}

function parse_comp(text_cid) {
    for (var j = 0; j < awards.length; j++) {
        if (awards[j].competition["cid"] == text_cid) {
            $("#modal_comp_id").attr("value", awards[j].competition["cid"]);
            $("#modal_comp_type").attr("value", awards[j].competition["ctype"]);
            $("#modal_comp_group").attr("value", awards[j].competition["cgroup"]);
            $("#modal_comp_class").attr("value", awards[j].competition["cclass"]);

            if (awards[j].competition["cwin"] == "t")
                $("#modal_comp_win").attr("value", "Yes");
            else
                $("#modal_comp_win").attr("value", "No");
        }
    }
}

function parse_event(text_eid) {
    for (var j = 0; j < events.length; j++) {
        if (events[j].event["eid"] == text_eid) {
            $("#modal_event_id").attr("value", events[j].event["eid"]);
            $("#modal_event_location").attr("value", events[j].event["elocation"]);
            $("#modal_event_begin").attr("value", events[j].event["ebegin"]);
            $("#modal_event_finish").attr("value", events[j].event["efinish"]);
            $("#modal_event_zip").attr("value", events[j].event["ezip"]);
        }
    }
}

// Check if the logged breeder is the owner of the dog and show the edit buttons
function check_dog_owner(fc) {
    if (userInfo.breederfc == fc) {
        var x = document.getElementsByClassName("edit");
        for (i = 0; i < x.length; i++) {
            x[i].style.display = "inline";
        }
    }
}


// Load DOG data
$(document).ready(function () {
    // get all dog info request/parsing stuffss
    load_dog();
})


function load_dog() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);

    var microchip = urlParams.get('dogmc');

    var request = new XMLHttpRequest();
    var url = '../user/dog/' + microchip;

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    request.overrideMimeType("application/json");
    request.open('GET', url);
    request.send();
    request.onload = parseDog

    // parse dog informations from the response
    function parseDog() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 201) {

                var jsonData = JSON.parse(request.responseText);
                // Parse response in the respective global variable
                dog_info = jsonData["dog"];
                breed = jsonData["breed"];
                pathology = jsonData["pathologies"];
                awards = jsonData["competitions"];
                events = jsonData["events"];
                parents = jsonData["parents"];
                sons = jsonData["sons"];

                // Get basic data
                var photo = dog_info["photo"];
                var name = dog_info["name"];
                var kennel_name = dog_info["kennel"];
                var microchip_code = dog_info["microchip"];
                var tatoo_code = dog_info["tattoo"];
                var birth = dog_info["birth"];
                var gender = dog_info["sex"];
                var owner = dog_info["owner"];
                var status = dog_info["status"];
                var height = dog_info["height"];
                var weight = dog_info["weight"];
                var coat_color = dog_info["coat"];
                var character = dog_info["character"];
                var teeth_structure = dog_info["teeth"];
                var particular_sign = dog_info["signs"];
                var dna = dog_info["dna"];

                // Get breed data
                var fci_code = breed["fci"];
                var group = breed["bgroup"];
                var breed_name = breed["bname"];

                $("#dog_name").empty();
                $("#kennel_name").empty();
                $("#pathology").empty();
                $("#awards").empty();
                $("#parents").empty();
                $("#sons").empty();

                // LOAD DOG PHOTO
                document.getElementById("dog_photo").src = photo;

                // LOAD BASIC DATA
                var a_kennel = document.createElement('a');
                a_kennel.append(kennel_name);
                a_kennel.href = './breeder.html?breederfc=' + dog_info["breederfc"];
                $("#kennel_name").append(a_kennel);

                $("#dog_name").prepend(name);
                $("#dog_name").append('<a id="update-info-btn" class="edit" data-toggle="modal" data-target="#update-info-modal"><i class="fas fa-pencil-alt"></i></a>');

                $("#microchip_code").text(microchip_code);
                $("#tatoo_code").text(tatoo_code);
                $("#birth").text(birth);
                $("#gender").text(gender);
                $("#owner").text(owner);
                $("#status").text(status);
                $("#fci_code").text(dna);
                $("#height").text(height + " " + "cm");
                $("#weight").text(weight + " " + "Kg");
                $("#coat_color").text(coat_color);
                $("#character").text(character);
                $("#teeth_structure").text(teeth_structure);
                $("#particular_sign").text(particular_sign);

                // LOAD BREED DATA
                $("#fci_code").text(fci_code);
                $("#group").text(group);
                $("#breed_name").text(breed_name);

                //LOAD PATHOLOGIES -> generate table from scratch
                for (j = 0; j < pathology.length; j++) {
                    $("#pathology").append("<tr><td>" + pathology[j].pathology["pcode"] + "</td>" + "<td>" + pathology[j].pathology["pname"] + "</td>" + "<td>" + pathology[j].pathology["pseverity"] + "</td></tr>");
                }


                var showVictory = document.getElementById("victory-check");
                //LOAD AWARDS -> generate table from scratch

                if (showVictory.checked === true) {
                    for (var j = 0; j < awards.length; j++) {
                        // Show only victories
                        if (awards[j].competition["cwin"] == "t") {

                            // create new row
                            var row = document.createElement('tr');

                            var text_cid = awards[j].competition["cid"];
                            var text_eid = awards[j].competition["ceventid"];

                            // create text nodes
                            var text_comp = document.createTextNode(text_cid);
                            var text_type = document.createTextNode(awards[j].competition["ctype"]);
                            var text_event = document.createTextNode(text_eid);

                            // create cells
                            var td_comp = document.createElement('td');
                            var td_type = document.createElement('td');
                            var td_event = document.createElement('td');

                            // create link
                            var a_comp = document.createElement('a');
                            a_comp.append(text_comp);
                            //a_comp.href = '#';
                            a_comp.type = "button";
                            a_comp.class = "btn btn-info my-3";
                            a_comp.id = "comp-btn-" + text_cid;
                            a_comp.setAttribute("onclick", "parse_comp(" + "\'" + text_cid + "\'" + ")");
                            a_comp.setAttribute("data-toggle", "modal");
                            a_comp.setAttribute("data-target", "#competition-modal");

                            var a_event = document.createElement('a');
                            a_event.appendChild(text_event);
                            a_event.type = "button";
                            a_event.class = "btn btn-info my-3";
                            a_event.id = "event-btn-" + text_eid;
                            a_event.setAttribute("onclick", "parse_event(" + "\'" + text_eid + "\'" + ")");
                            a_event.setAttribute("data-toggle", "modal");
                            a_event.setAttribute("data-target", "#event-modal");

                            td_comp.appendChild(a_comp);
                            td_type.appendChild(text_type);
                            td_event.appendChild(a_event);

                            row.appendChild(td_comp);
                            row.appendChild(td_type);
                            row.appendChild(td_event);

                            $("#awards").append(row);
                        }
                    }
                }
                else {
                    for (var j = 0; j < awards.length; j++) {
                        // create new row
                        var row = document.createElement('tr');

                        var text_cid = awards[j].competition["cid"];
                        var text_eid = awards[j].competition["ceventid"];

                        // create text nodes
                        var text_comp = document.createTextNode(text_cid);
                        var text_type = document.createTextNode(awards[j].competition["ctype"]);
                        var text_event = document.createTextNode(text_eid);

                        // create cells
                        var td_comp = document.createElement('td');
                        var td_type = document.createElement('td');
                        var td_event = document.createElement('td');

                        // create link
                        var a_comp = document.createElement('a');
                        a_comp.append(text_comp);
                        //a_comp.href = '#';
                        a_comp.type = "button";
                        a_comp.class = "btn btn-info my-3";
                        a_comp.id = "comp-btn-" + text_cid;
                        a_comp.setAttribute("onclick", "parse_comp(" + "\'" + text_cid + "\'" + ")");
                        a_comp.setAttribute("data-toggle", "modal");
                        a_comp.setAttribute("data-target", "#competition-modal");

                        var a_event = document.createElement('a');
                        a_event.appendChild(text_event);
                        a_event.type = "button";
                        a_event.class = "btn btn-info my-3";
                        a_event.id = "event-btn-" + text_eid;
                        a_event.setAttribute("onclick", "parse_event(" + "\'" + text_eid + "\'" + ")");
                        a_event.setAttribute("data-toggle", "modal");
                        a_event.setAttribute("data-target", "#event-modal");

                        td_comp.appendChild(a_comp);
                        td_type.appendChild(text_type);
                        td_event.appendChild(a_event);
                        -
                            row.appendChild(td_comp);
                        row.appendChild(td_type);
                        row.appendChild(td_event);

                        $("#awards").append(row);
                    }
                }


                //LOAD GENEALOGY
                // Load parents
                for (j = 0; j < parents.length; j++) {
                    // create new row
                    var row = document.createElement('tr')
                    //create text node (for the name)
                    var text_microchip = document.createTextNode(parents[j].parent["microchip"]);
                    var text_name = document.createTextNode("")
                    var text_role = document.createTextNode("")
                    // create cell elements
                    var td_name = document.createElement('td');
                    var td_microchip = document.createElement('td');
                    var td_role = document.createElement('td');

                    // fill text and links
                    if (parents[j].parent.hasOwnProperty('name')) {
                        text_name = document.createTextNode(parents[j].parent["name"]);
                        // Check role
                        if (parents[j].parent["sex"] == "Male") {
                            text_role = document.createTextNode("Father");
                        }
                        else {
                            text_role = document.createTextNode("Mother");
                        }

                        var a = document.createElement('a');
                        a.append(text_name);
                        a.href = './dog.html?dogmc=' + parents[j].parent["microchip"];
                        td_name.appendChild(a);
                    }
                    else {
                        text_name = document.createTextNode("NOT AVAILABLE");
                        text_role = document.createTextNode("NOT AVAILABLE");
                        td_name.appendChild(text_name);
                    }

                    td_microchip.appendChild(text_microchip);
                    td_role.appendChild(text_role);

                    row.append(td_name)
                    row.appendChild(td_microchip);
                    row.appendChild(td_role);
                    $("#parents").append(row);
                }

                // Load sons
                for (k = 0; k < sons.length; k++) {
                    var row = document.createElement('tr')

                    // create text nodes
                    var text_name = document.createTextNode(sons[k].son["name"]);
                    var text_microchip = document.createTextNode(sons[k].son["microchip"]);
                    var text_birth = document.createTextNode(sons[k].son["birth"]);

                    // create cells
                    var td_name = document.createElement('td');
                    var td_microchip = document.createElement('td');
                    var td_birth = document.createElement('td');

                    // create link
                    var a = document.createElement('a');
                    a.append(text_name);
                    a.href = './dog.html?dogmc=' + sons[k].son["microchip"];

                    td_name.appendChild(a);
                    td_microchip.appendChild(text_microchip);
                    td_birth.appendChild(text_birth);

                    row.appendChild(td_name);
                    row.appendChild(td_microchip);
                    row.appendChild(td_birth);

                    $("#sons").append(row);
                }
            }

            // Check the dog owner
            check_dog_owner(dog_info["breederfc"]);

            // Add event listner to edit buttons
            function load_info_modal() {
                img_box = document.getElementById("modal_dog_photo");
                img_box.src = dog_info["photo"];

                $("#modal_actual_name").attr("value", dog_info["name"]);
                $("#modal_actual_birth").attr("value", dog_info["birth"]);
                $("#modal_actual_owner").attr("value", dog_info["owner"]);

                $("#modal_actual_status").val(dog_info["status"]);
                //$('#leaveCode').val(dog_info["status"]);
                $("#modal_actual_height").attr("value", dog_info["height"]);
                $("#modal_actual_weight").attr("value", dog_info["weight"]);
                $("#modal_actual_color").attr("value", dog_info["coat"]);
                $("#modal_actual_character").attr("value", dog_info["character"]);
                $("#modal_actual_teeth").attr("value", dog_info["teeth"]);
                $("#modal_actual_signs").attr("value", dog_info["signs"]);
            }

            document.getElementById('update-info-btn').addEventListener('click', load_info_modal());
            document.getElementById('submit-update-info-btn').addEventListener('click', (function update_info() {
                const queryString = window.location.search;
                const urlParams = new URLSearchParams(queryString);

                var microchip = urlParams.get('dogmc');

                var request = new XMLHttpRequest();
                var url = '../user/dog/' + microchip;

                if (!request) {
                    alert('Giving up :( Cannot create an XMLHTTP instance');
                    return false;
                }

                var data = JSON.stringify({
                    "dog": {
                        "photo": $.trim($("#photo").val()),
                        "microchip": dog_info["microchip"],
                        "tattoo": dog_info["tattoo"],
                        "name": $.trim($("#modal_actual_name").val()),
                        "birth": $.trim($("#modal_actual_birth").val()),
                        "sex": dog_info["microchip"],
                        "height": $.trim($("#modal_actual_height").val()),
                        "weight": $.trim($("#modal_actual_weight").val()),
                        "coat": $.trim($("#modal_actual_color").val()),
                        "character": $.trim($("#modal_actual_character").val()),
                        "teeth": $.trim($("#modal_actual_teeth").val()),
                        "signs": $.trim($("#modal_actual_signs").val()),
                        "owner": $.trim($("#modal_actual_owner").val()),
                        "status": $.trim($("#modal_actual_status").val())
                    }
                });

                request.overrideMimeType("application/json");
                request.open('POST', url);
                request.send(data);
                request.onload = update_response;

                function update_response() {
                    if (request.readyState === XMLHttpRequest.DONE) {
                        if (request.status == 200) {
                            var m = JSON.parse(request.responseText);
                            var res = m["message"];
                            if (res.message == "Update Successfull.") {
                                alert(res.message + " :)");
                                location.reload();
                            }
                            else
                                alert("something went wrong... :(");
                        }
                    }
                }
            }));

        } else {
            alert('There was a problem with the request.');
        }
    }
}

// reset update name form if nothing changed
$('#update-name-modal').on('hidden.bs.modal', function () {
    $(this).find('form').trigger('reset');
})

// Pathology modal selection function
$("#pathology_action").on("change", function () {
    if ($(this).val() == "add_p") {
        // Show
        $("#modal_p_code").show();
        $("#modal_p_severity").show();
        $("#modal_padd").show();
        $("#modal_premove").hide();
    }
    if ($(this).val() == "remove_p") {
        $("#modal_p_code").show();
        $("#modal_p_severity").hide();
        $("#modal_padd").hide();
        $("#modal_premove").show();
    }
});

//competition modal  selection option
$("#competition_action").on("change", function () {
    if ($(this).val() == "add_comp") {
        $("#u_cadd").show();
        $("#edit_modal_ctype").show();
        $("#edit_modal_cgroup").show();
        $("#edit_modal_cclass").show();
        $("#edit_modal_cwon").show();
        $("#edit_modal_eid").show();
        $("#edit_modal_elocation").show();
        $("#edit_modal_ebegin").show();
        $("#edit_modal_efinish").show();
        $("#edit_modal_ezip").show();
        $("#u_cremove").hide();
    }
    if ($(this).val() == "remove_comp") {
        $("#u_cremove").show();
        $("#u_cadd").hide();
        $("#edit_modal_ctype").hide();
        $("#edit_modal_cgroup").hide();
        $("#edit_modal_cclass").hide();
        $("#edit_modal_cwon").hide();
        $("#edit_modal_eid").hide();
        $("#edit_modal_elocation").hide();
        $("#edit_modal_ebegin").hide();
        $("#edit_modal_efinish").hide();
        $("#edit_modal_ezip").hide();
    }
});

//sons modal  selection option
$("#sons_action").on("change", function () {
    if ($(this).val() == "add_son") {
        $("#modal_sadd").show();
        $("#modal_sremove").hide();
    }
    if ($(this).val() == "remove_son") {
        $("#modal_sadd").hide();
        $("#modal_sremove").show();
    }
});


// ADD PATHOLOGY FUNCTION
function add_pathology() {

    var request = new XMLHttpRequest();
    var url = '../pathology?' + "req=add";

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var data = JSON.stringify({
        "whichas": {
            "dog": dog_info["microchip"],
            "pathology": $.trim($("#modal_pathology").val()),
            "severity": $.trim($("#pathology_severity").val())
        }
    });

    request.overrideMimeType("application/json");
    request.open('POST', url);
    request.send(data);
    request.onload = (function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var m = JSON.parse(request.responseText);
                var res = m["message"];
                alert(res.message);
                location.reload();
            }
        }
    })
}


// REMOVE PATHOLOGY FUNCTION
function remove_pathology() {

    var request = new XMLHttpRequest();
    var url = '../pathology?' + 'req=remove';

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var data = JSON.stringify({
        "whichas": {
            "dog": dog_info["microchip"],
            "pathology": $.trim($("#modal_pathology").val()),
            "severity": ""
        }
    });

    request.overrideMimeType("application/json");
    request.open('POST', url);
    request.send(data);
    request.onload = (function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var m = JSON.parse(request.responseText);
                var res = m["message"];
                alert(res.message);
                location.reload();
            }
        }
    })
}


// ADD SON FUNCTION
function add_son() {

    var request = new XMLHttpRequest();
    var url = '../genealogy?' + "req=add";

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var data = JSON.stringify({
        "succeed": {
            "predecessor": dog_info["microchip"],
            "successor": $.trim($("#modal_microchip").val()),
        }
    });

    request.overrideMimeType("application/json");
    request.open('POST', url);
    request.send(data);
    request.onload = (function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var m = JSON.parse(request.responseText);
                var res = m["message"];
                alert(res.message);
                location.reload();
            }
        }
    })
}


// REMOVE SON FUNCTION
function remove_son() {

    var request = new XMLHttpRequest();
    var url = '../genealogy?' + 'req=remove';

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var data = JSON.stringify({
        "succeed": {
            "predecessor": dog_info["microchip"],
            "successor": $.trim($("#modal_microchip").val()),
        }
    });

    request.overrideMimeType("application/json");
    request.open('POST', url);
    request.send(data);
    request.onload = (function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var m = JSON.parse(request.responseText);
                var res = m["message"];
                alert(res.message);
                location.reload();
            }
        }
    })
}


// ADD COMPETITION FUNCTION
function add_competition (){
    var request = new XMLHttpRequest();
    var url = '../competition?' + "req=add";

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var data = JSON.stringify({
                                                    "competition": {
                                                                                    "win": $.trim($("#ucwon").val()),
                                                                                    "comp_id": $.trim($("#ucid").val()),
                                                                                    "comp_type": $.trim($("#uctype").val()),
                                                                                    "comp_group": $.trim($("#ucgroup").val()),
                                                                                    "comp_class": $.trim($("#ucclass").val()),
                                                                                    "event_id": $.trim($("#ueid").val()),
                                                                                },
                                                    "event": {
                                                                        "event_id": $.trim($("#ueid").val()),
                                                                        "event_location": $.trim($("#uelocation").val()), 
                                                                        "event_begin": $.trim($("#uebegin").val()),
                                                                        "event_finish":$.trim($("#uefinish").val()), 
                                                                        "event_zip": $.trim($("#uezip").val())
                                                                     },
                                                    "dog": {
                                                                    "microchip": dog_info["microchip"]
                                                                }
                                                 });

    request.overrideMimeType("application/json");
    request.open('POST', url);
    request.send(data);
    request.onload = (function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var m = JSON.parse(request.responseText);
                var res = m["message"];
                alert(res.message);
                location.reload();
            }
        }
    })
}

// REMOVE COMPETITION FUNCTION
function remove_competition (){
    var request = new XMLHttpRequest();
    var url = '../competition?' + "req=remove";

    if (!request) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    var data = JSON.stringify({
                                                    "competition": {
                                                                                    "comp_id": $.trim($("#ucid").val())
                                                                                },
                                                    "dog": {
                                                                    "microchip": dog_info["microchip"]
                                                                }
                                                 });

    request.overrideMimeType("application/json");
    request.open('POST', url);
    request.send(data);
    request.onload = (function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status == 200) {
                var m = JSON.parse(request.responseText);
                var res = m["message"];
                alert(res.message);
                location.reload();
            }
        }
    })
}