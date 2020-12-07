
$(document).ready(function(){
  //$("#querybox").val(getQuery);
  make_request();
});

$("#pageorder").on("change", make_request);

$("#queryform").submit(function(){
   $(this).find(':input[value=""]').prop('disabled', true)
  make_request()  
});

$("#Previous").on("click",function(){
  $("#page").val(Math.abs(parseInt($("#page").val())-1));
  make_request();
});

$("#Next").on("click",function(){
  $("#page").val(Math.abs(parseInt($("#page").val())+1));
  make_request();
});

$("#searchButton").on("click",function(){
  make_request();
});


$("#filterButton").on("click",function(){
  make_request();
});

$("#querybox").on('keypress',function(e) {
  if(e.which == 13) {
     make_request();
  }
});

//request without backend
function make_request() {
  var formParams = $('#queryform').serializeArray();
  var data = $(this).serializeArray();

  $.get({
      cache: false,
      url: "./searchDogs",
      data: $.param(formParams),
    }).done(getDogsData)
    .fail(function(jqXHR, textStatus, errorThrown) {
      console.log(errorThrown)
    });
};
//get data to insert in the page form json
function getDogsData(data) {
  var items = data;

  dogs_data = items["resource-list"];
  //remove the prevous dogs and insert the new one in article elements
  $("#articles").empty();
  if(dogs_data.length == 0){ //if no result found
    var nores_div =
      $('<div>', {
        'html': "No result found.",
        'class': "row m-3 p-3 bg-white rounded box-shadow"
      }).appendTo("#articles");
  }
  else {
  $.each(dogs_data, function(index, dog) {
    dog = dog["dog"];
    var art = $("<article></article>");
    var row_div = $("<div class=\"row m-3 p-1  bg-white rounded box-shadow \"></div>");
    var img_div = $("<div class=\"column p-2\"></div>");

    dog_img = $("<img/>", {
      'src': dog["photo"],
      "class": "dogimage p-1 m-3 img-fluid img-thumbnail rounded card-img-top text-center"
    });

    img_div.append(dog_img);
    row_div.append(img_div);

    var col_div =
      $('<div/>', {
        'class': "column m-3 p-3  align-self-center",
        'html': "<div> Name: <strong> " + dog["name"] + " </strong> </div><br><div> Kannel: " + dog["kennel"] + "  </div><br><div> Owner: " + dog["owner"] + "</div>"
      });
    row_div.append(col_div);

    var col_div =
      $('<div/>', {
        'class': "column m-3 p-3  align-self-center",
        'html': "<div> Gender: " + dog["sex"] + " </div><br><div> Microchip code: " + dog["microchip"] + "  </div><br><div> FCI code:" + dog["fci"] +"</div>"
      }); 
    row_div.append(col_div);

    var col_div =
      $('<div/>', {
        'class': "column m-3 p-3 align-self-center",
        'html': "<div> Birth: " + dog["birth"] + " </div><br><div> Height: " + dog["height"] + "cm </div><br><div> Weight: " + dog["weight"] + " Kg </div>"
      });
    row_div.append(col_div);

    var a =
      $('<a>', {
        'href': "./user/dog.html?dogmc=" + dog["microchip"],
        "class": "dogresult"
      });

    art.append(row_div);
    a.append(art);

    $("#articles").append(a);

  });
  
}
};
