var base = base || {};
base.changeLocation = function(url) {
    window.location.replace(url);
};
base.homeController = function() {
    var model = [];
	var currentUser = 8; 


    var view = {
        render: function() {
        	model.forEach(d => view.renderPart(d)); // KOlla upp vad d är!
        },
        renderpart: function(){
        	var t = view.template();
        	 view.update(t.content.querySelector('tr'), route);

        },

        update: function(trElement, route) {
        	 var tds = trElement.children;
        	 tds[0].textContent = route.location;
        	 tds[1].textContent = route.destination;
             var d = route.timeOfDeparture;
             tds[2].textContent = d.toLocaleDateString() + ' ' + d.toLocaleTimeString();
             var e = route.timeOfArrival;
        	 tds[3].textContent = e.toLocaleDateString() + ' ' + e.toLocaleTimeString();
        	
        },
        
        template: function(){
        	return document.getElementById('createdRoutes-template');
        }
    };



    var controller = {
		load: function() {
			base.rest.getLoggedInUser().then(function(user) {
				console.log(user);
                currentUser = user;		// Hämtar user
				console.log(user.userID);	 
				base.rest.getRoutes(currentUser.userID).then(function(routes) { // Hämtar sen route när man har usern
	                model = routes;
	                view.render();
	            });        
            });
			
		},
		
    };
    return controller;
};











