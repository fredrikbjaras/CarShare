var base = base || {};
base.newRouteController = function() {

    var model = "";

    var view = {
        renderPart: function(user) {

        },
        update: function(liElement, user) {

        },

        render: function() {
        	console.log('render');
        	document.getElementById('set-origin').defaultValue = "origin";
        	document.getElementById('set-destination').defaultValue = 'destination';
        },

        resetEdit: function() {
           view.render();
        }

    };

    var controller = {
        submitChange: function() { //pop up notis att det är ändrad
        	console.log('changing');
        	model.origin = document.getElementById('set-origin').value;
        	model.destination = document.getElementById('set-destination').value;
        	//model.departure = document.getElementById('set-departure').value;
            //model.arrival = document.getElementById('set-arrival').value;

        	//base.rest.updateUser(model.userID, model.userName, model.password, model.phoneNr, null, model.description);

        },

        resetEdit: function(resetEvent){
        	view.render();
        },
        
        load: function() {
        	document.getElementById('submit-user').onclick = controller.submitChange;
            model = {
			    var origin;
			    var destination;
			    var departure;
			    var arrival;
		    	};
            });
        }

    };
    return controller;
};

