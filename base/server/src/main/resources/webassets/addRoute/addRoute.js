var base = base || {};
base.addRouteController = function() {

    var model = "";
    var thisUser = "";

    var view = {
        renderPart: function(user) {

        },
        update: function(liElement, user) {

        },

        render: function() {
        	console.log('render');
        	document.getElementById('set-origin').value = "Origin";
        	document.getElementById('set-dest').value = "Destination";
            document.getElementById('set-description').value = "Description"
            document.getElementById('nr').textContent = "0";
        },

        resetEdit: function() {
           view.render();
        }

    };

    var controller = {
        submitChange: function() { //pop up notis att det är ändrad
        	console.log('changing');
        	model.origin = document.getElementById('set-origin').value;
        	model.destination = document.getElementById('set-dest').value;
            model.description = document.getElementById('set-description').value;
            arrivalTime = "2018-"
            month = document.getElementById('month')
            arrivalTime += month.options[month.selectedIndex].text + "-";
            day = document.getElementById('day')
            arrivalTime += day.options[day.selectedIndex].text + " ";
            hour = document.getElementById('hour')
            arrivalTime += hour.options[hour.selectedIndex].text + ":";
            minute = document.getElementById('min')
            arrivalTime += minute.options[minute.selectedIndex].text + ":00";

            departureTime = "2018-"
            month = document.getElementById('month2')
            departureTime += month.options[month.selectedIndex].text + "-";
            day = document.getElementById('day2')
            departureTime += day.options[day.selectedIndex].text + " ";
            hour = document.getElementById('hour2')
            departureTime += hour.options[hour.selectedIndex].text + ":";
            minute = document.getElementById('min2')
            departureTime += minute.options[minute.selectedIndex].text + ":00";

            limit = document.getElementById('limit')
            limitTime = limit.options[limit.selectedIndex].text + ";";
            console.log(thisUser.userID);
            console.log(model.origin);
            console.log(model.destination);
            console.log(arrivalTime);
            console.log(departureTime);
            console.log(model.passengers);
            console.log(model.description);
            console.log(limitTime);
        	base.rest.addRoute(thisUser.userID, model.origin, model.destination, arrivalTime, departureTime, model.passengers, model.description, limitTime).then(function(obj) {
                if (obj.error) {
                    alert(obj.message);
                } else {
                	alert("Route added");
                }
            });
            view.render();
        },
        plusPass: function() {
            model.passengers += 1;
            document.getElementById('nr').textContent = model.passengers;
        },

        negPass: function() {
            if(model.passengers != 0){
                model.passengers -= 1;
                document.getElementById('nr').textContent = model.passengers;
            }
        },

        resetEdit: function(resetEvent){
        	view.render();
        },

        load: function() {
        	document.getElementById('submit-user').onclick = controller.submitChange;
            document.getElementById('up').onclick = controller.plusPass;
            document.getElementById('dwn').onclick = controller.negPass;
            model = {
                origin:"",
                destination:"",
                departure:"",
                arrival:"",
                passengers:0,
                description:""
            }
            base.rest.getLoggedInUser().then(function(user) {
                thisUser = user
            });
            view.render();
        }
    };
    return controller;
};
