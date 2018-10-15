var base = base || {};
base.changeLocation = function(url) {
    window.location.replace(url);
};
base.homeController = function() {
    var model = [];
	var currentUser = 8; 


    var view = {
        render: function() {
        	model.forEach(d => view.renderPart(d)); 
        	console.log(model[1]);
        	console.log(model);
        },
        renderPart: function(route){
        		var t = view.template();   
        		console.log(route);
        		view.update(t.content.querySelector('tr'), route);

        },

        update: function(trElement, route) {
        	 var tds = trElement.children;
        	 tds[0].textContent = route.location;
        	 console.log(route);
        	 tds[1].textContent = route.destination;
             tds[2] = route.freeSeats;
             tds[3] = route.timeOfDeparture;
        	 //tds[3].textContent = e.toLocaleDateString() + ' ' + e.toLocaleTimeString();
        	
        },
        
        template: function(){
        	return document.getElementById('createdRoutes-template');
        }
    };



    var controller = {
		load: function() {
			base.rest.getLoggedInUser().then(function(user) {
				//console.log(user);
                currentUser = user;		// Hämtar user
				base.rest.getRoutes(currentUser.userName).then(function(routes) { // Hämtar sen route när man har usern
	                model = routes;
	                view.render();
	            });        
            });
			
		},
		
    };
    return controller;
};











