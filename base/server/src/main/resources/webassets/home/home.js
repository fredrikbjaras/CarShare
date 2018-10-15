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
        },
        renderPart: function(route){
        		var t = view.template();   
        		view.update(t.content.querySelector('tr'), route);
        		var clone = document.importNode(t.content, true);
                t.parentElement.appendChild(clone);

        },

        update: function(trElement, route) {
        	 var tds = trElement.children;
        	 console.log(route);
        	 tds[0].textContent = route.location;
        	 tds[1].textContent = route.destination;
        	 console.log(route.freeSeats);
        	 tds[2].textContent = route.freeSeats;
        	 tds[3].textContent = route.timeOfDeparture;
        	 tds[4].textContent = route.timeOfArrival;

            
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











