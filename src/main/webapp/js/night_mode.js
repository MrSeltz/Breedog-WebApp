// DayNight 
var daynight = true;

// Get Setting from cache
function getSett() {
    daynight = localStorage.getItem("daynight");
    return daynight;
}

getSett();

const checkbox = document.getElementById('checkbox');

checkbox.addEventListener('change', () => 
{
    if(daynight==true){
        daynight = false;
    }

    if(daynight==false){
        daynight = true;
    }
    
    //change theme of the website
    
    //document.body.classList.toggle('dark');

    menu_css.body.toggle('dark')
});

