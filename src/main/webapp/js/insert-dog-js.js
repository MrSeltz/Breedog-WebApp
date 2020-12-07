function onFileSelected(event) {

  var selectedFile = event.target.files[0];
  var reader = new FileReader();

  var imgtag = document.getElementById("dog-img-visualize");
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

function addFields(type) {

  var value = ["4052", "4053", "1846", "9218"];
  var text = ["Displasia dell'anca", "Displasia dei gomiti", "Ipotiroidismo", "Epilessia"];

  //Create an input type dynamically.
  var element = document.createElement("select");

  //Assign different attributes to the element.
  element.setAttribute("type", type);
  element.setAttribute("placeholder", "Enter a pathology");
  element.setAttribute("name", type);
  element.setAttribute("class", "form-control");

  //Create and append the options
  for (var i = 0; i < array.length; i++) {
    var option = document.createElement("option");
    option.value = value[i];
    option.text = text[i];
    element.appendChild(option);
  }

  var foo = document.getElementById("bar");

  //Append the element in page (in span).
  foo.appendChild(element);

}

function removeFields() {

  var foo = document.getElementById("bar");

  foo.removeChild(foo.lastElementChild);

}


// insert-dog-Button-click
$("#submit-btn").click(function () {

  var satisfy = false;

  var dogMicrochip = document.getElementById("dog-microchip");
  var dogTattoo = document.getElementById("tattoo-code");
  var dogName = document.getElementById("name");
  var dogBirth = document.getElementById("birth");
  var dogSex = document.querySelector('input[name=sex]:checked');
  var dogHeight = document.getElementById("height");
  var dogWeight = document.getElementById("weight");
  var dogCoat = document.getElementById("coat");
  var dogCharacter = document.getElementById("charachter");
  var dogDna = document.getElementById("dna");
  var dogTeeth = document.getElementById("teeth");
  var dogSigns = document.getElementById("particular-sign");
  var dogPhoto = document.getElementById("photo");
  var dogOwner = document.getElementById("owner");

  var dogStatus = document.getElementById("status");

  var breedChoise = document.getElementById("breed");

  var breederfc = userInfo.breederfc = localStorage.getItem("breederfc");
  var dogKennel = document.getElementById("kennel");

  var dogFather = document.getElementById("father-microchip");
  var dogMother = document.getElementById("mother-microchip");

  var pathologyChoise = document.getElementById("pathology");
  var severityChoise = document.getElementById("severity");

  while (!satisfy) {
    satisfy = true;
    var microchip = dogMicrochip.value;
    var tattoo = dogTattoo.value;
    var name = dogName.value;
    var birth = dogBirth.value;

    // catch error on the sex field
    var validSex = true;
    try {
      var sex = dogSex.value;
      validSex = false;
    } catch (err) {
      alert("Choose the sex of dog");
      validSex = true;
    }

    var height = dogHeight.value;
    var weight = dogWeight.value;
    var coat = dogCoat.value;
    var character = dogCharacter.value;
    var dna = dogDna.value;
    var teeth = dogTeeth.value;
    var signs = dogSigns.value;
    var photo = dogPhoto.value;
    var owner = dogOwner.value;

    var status = dogStatus.options[dogStatus.selectedIndex].value;

    var fci = breedChoise.options[breedChoise.selectedIndex].value;

    var kennel = dogKennel.value;

    var father = dogFather.value;
    var mother = dogMother.value;

    var pathology = pathologyChoise.options[pathologyChoise.selectedIndex].value;
    var severity = severityChoise.options[severityChoise.selectedIndex].value;

    // reset form with no error border
    dogMicrochip.classList.remove('is-invalid');
    dogName.classList.remove('is-invalid');
    dogBirth.classList.remove('is-invalid');
    dogHeight.classList.remove('is-invalid');
    dogWeight.classList.remove('is-invalid');
    dogCoat.classList.remove('is-invalid');
    dogOwner.classList.remove('is-invalid');
    dogStatus.classList.remove('is-invalid');
    breedChoise.classList.remove('is-invalid');

    // check for not null value on required parameters
    if (microchip.length === 0 || name.length === 0 || birth.length === 0 || height.length === 0 || weight.length === 0
      || coat.length === 0 || owner.length === 0 || status.localeCompare("enter") === 0 || fci.localeCompare("enter") === 0 || photo.length === 0) {
      satisfy = false;
      alert("Fill the required field marked in red");
      // mark empty parameters with a red border
      if (microchip.length === 0)
        dogMicrochip.classList.add('is-invalid');
      if (name.length === 0)
        dogName.classList.add('is-invalid');
      if (birth.length === 0)
        dogBirth.classList.add('is-invalid');
      if (height.length === 0)
        dogHeight.classList.add('is-invalid');
      if (weight.length === 0)
        dogWeight.classList.add('is-invalid');
      if (coat.length === 0)
        dogCoat.classList.add('is-invalid');
      if (owner.length === 0)
        dogOwner.classList.add('is-invalid');
      if (status.localeCompare("enter") === 0)
        dogStatus.classList.add('is-invalid');
      if (fci.localeCompare("enter") === 0)
        breedChoise.classList.add('is-invalid');
      if (photo.length === 0)
        alert("Photo of the dog is necessary!");
      break;
    }
  }

  if (satisfy && !validSex) {
    var request = new XMLHttpRequest();

    if (!request) {
      alert('Giving up :( Cannot create an XMLHTTP instance');
      return false;
    }

    // send request to login servlet?
    request.open('POST', '../create-dog');
    // set request header
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.send(JSON.stringify({
      "dog": {

        "microchip": microchip,
        "tattoo": tattoo,
        "name": name,
        "birth": birth,
        "sex": sex,
        "height": height,
        "weight": weight,
        "coat": coat,
        "character": character,
        "dna": dna,
        "teeth": teeth,
        "signs": signs,
        "photo": photo,
        "owner": owner,
        "status": status,
        "fci": fci,
        "breederfc": breederfc,
        "kennel": kennel
      },
    }));

    request.onload = createDog;

    function createDog() {
      if (request.readyState === XMLHttpRequest.DONE) {

        // If there is authentication error
        if (request.status == 401) {
          // append error message to the page
          console.log("Insertion error");
          alert('Insertion Error');
          return
        }

        if (request.status == 405) {
          // append error message to the page
          console.log("Insertion error");
          alert('Insertion Error: METHOD NOT ALLOWED.');
          return
        }
      }
    }

    // insert father 
    var request1 = new XMLHttpRequest();

    if (!request1) {
      alert('Giving up :( Cannot create an XMLHTTP instance');
      return false;
    }

    // send request to login servlet?
    request1.open('POST', '../genealogy?' + 'req=add');
    // set request header
    request1.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request1.send(JSON.stringify({
      "succeed": {
        "predecessor": father,
        "successor": microchip,
        "datebirth": birth
      },
    }));

    request1.onload = createGenealogy;

    // insert mother
    var request2 = new XMLHttpRequest();

    if (!request2) {
      alert('Giving up :( Cannot create an XMLHTTP instance');
      return false;
    }

    // send request to login servlet?
    request2.open('POST', '../genealogy?' + 'req=add');
    // set request header
    request2.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request2.send(JSON.stringify({
      "succeed": {
        "predecessor": mother,
        "successor": microchip,
        "datebirth": birth
      },
    }));

    request2.onload = createGenealogy;

    function createGenealogy() {
      if (request2.readyState === XMLHttpRequest.DONE) {

        // If there is authentication error
        if (request2.status == 401) {
          // append error message to the page
          console.log("Insertion error");
          alert('Insertion Error');
          return
        }

        if (request2.status == 405) {
          // append error message to the page
          console.log("Insertion error");
          alert('Insertion Error: METHOD NOT ALLOWED.');
          return
        }
      }
    }

    // insert dog pathology in relative sql table
    var request3 = new XMLHttpRequest();

    if (!request3) {
      alert('Giving up :( Cannot create an XMLHTTP instance');
      return false;
    }

    // send request to login servlet?
    request3.open('POST', '../pathology?' + 'req=add');
    // set request header
    request3.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request3.send(JSON.stringify({
      "whichas": {
        "dog": microchip,
        "pathology": pathology,
        "severity": severity
      },
    }));

    request3.onload = createPathology;

    function createPathology() {
      if (request3.readyState === XMLHttpRequest.DONE) {

        // If there is authentication error
        if (request3.status == 401) {
          // append error message to the page
          console.log("Insertion error");
          alert('Insertion Error');
          return
        }

        if (request3.status == 405) {
          // append error message to the page
          console.log("Insertion error");
          alert('Insertion Error: METHOD NOT ALLOWED.');
          return
        }

        // similar behavior as an HTTP redirect
        alert("Dog succesfully added!")
        window.location.replace("./home.html");
      }
    }
  }
});

document.getElementById("pop")
  .addEventListener("keyup", function (event) {
    event.preventDefault();
    if (event.keyCode === 13) {
      document.getElementById("submit-btn").click();
    }
  });

