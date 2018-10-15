var base = base || {};
base.userAdminController = function() {

    var model = {
        users: [],
        routes: [],
       
    };

    var view = {
    		
    	 render: function() {
            model.users.forEach(user => view.renderPart(user));
           },
           
        renderRoute: function() {
            model.routes.forEach(route => view.renderPartRoute(route));
        },
    		
        renderPart: function(user) {
            var t = document.getElementById('allUsers-template');
            view.updateUser(t.content.querySelector('tr'), user);
            var clone = document.importNode(t.content, true);
            t.parentElement.appendChild(clone);
        },
        
        renderPartRoute: function(route) {
        	 var t = document.getElementById('allRoutes-template');
             view.updateRoute(t.content.querySelector('tr'), route);
             var clone = document.importNode(t.content, true);
             t.parentElement.appendChild(clone);
        },
        
        updateUser: function(liElement, user) {
        	 var tds = liElement.children;
        	 tds[0].textContent = user.userName;
        	 tds[1].textContent = user.role.label;
        	 tds[2].textContent = user.phoneNr;
        	 },
    
        updateRoute: function(liElement,route){
        	 var tds = liElement.children;
        	 console.log(route);
        	 tds[0].textContent = route.passengers;
        	 tds[1].textContent = route.location;
        	 tds[2].textContent = route.destination;
        	 tds[3].textContent = route.timeOfDeparture;
        	 tds[4].textContent = route.timeOfArrival;
        	 tds[5].textContent = route.freeSeats;

        },
/*
        showForm: function(formId){
        	console.log('första hej');
            var form = document.getElementById(formId);
            if (form.style.display == 'block') {
            	form.style.display = 'none';
            	console.log('if hej');
            } else {
            	form.style.display = 'block';
            	console.log('else hej');
            }
    	},*/

     

           
            // Set defaults of form values. This will allow the HTML reset button to work by default HTML behaviour.
         //  }
    };

    var controller = {

   	   		userSearchSubmit: function(){		
   			var userName = document.getElementById('user-name-input').value;
    		var phoneNr = document.getElementById('phone-input').value;
    		var routeId = document.getElementById('routeId-input').value;
    	    
			base.rest.getUsers(userName,phoneNr,routeId).then(function(users){
				model.users = [];
				model.users = users;
				view.render(); 
			});
				
			},
			 
		 routeSearchSubmit: function(){
			 	var driverName = document.getElementById('driver-name-input').value;
	    		var origin = document.getElementById('origin-input').value;
	    		var destination = document.getElementById('destination-input').value;
	    		var departureTime = document.getElementById('departure-input').value;
	    		var arrivalTime = document.getElementById('arrival-input').value;
	    		//document.getElementById('route-tbody').innerHTML = '';
	    		
	    		
	    		base.rest.getRoutes(driverName, origin, destination, departureTime, arrivalTime).then(function(routes){
					model.routes = routes;
					view.renderRoute(); 
				});
			 },
   		
   		load: function() {
            
   			document.getElementById('user-search-form').onsubmit = controller.userSearchSubmit;
   			document.getElementById('route-search-form').onsubmit = controller.routeSearchSubmit;
  		
   			base.rest.getUsers().then(function(users) { 
                model.users = users;
                view.render();
            }); 
   				
   			base.rest.getRoutes().then(function(routes) { 
                model.routes = routes;
                view.renderRoute();
            }); 
   		
         
          }
    };

    return controller;
};