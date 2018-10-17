var base = base || {};
base.changeLocation = function(url) {
    window.location.replace(url);
};
base.homeController = function() {
    var model = [];
	var currentUser = 8; 
	var joinedModel = [];
	
	
    var view = {
        render: function() {
        model.forEach(d => view.renderPart(d)); 
        },
        renderReq: function(reqes){
			
        //reqes.forEach(d => view.renderRequest(d));
		var i;
		if(reqes!=null){
		for(i = 0;i<reqes.length;i++){
			//view.renderRequest(reqes[i]);
			var fromUser = reqes[i].fromUserID;
			var routeID = reqes[i].routeID;
			var req = reqes[i]
			base.rest.getUser(fromUser).then(function(user) {
				base.rest.getRoute(routeID).then(function(route) {
				view.renderRequest(req,user,route);
				});
			});
			}
		}
        },
        
        renderJoined: function(){
            joinedModel.forEach(d => view.renderPartJoined(d));	
             },
             
           
        renderPart: function(route){
        		var t = view.template();   
        		view.update(t.content.querySelector('tr'), route);
        		var clone = document.importNode(t.content, true);
                t.parentElement.appendChild(clone);

        },
        
        renderPartJoined: function(joined){
   			var t = view.joinedTemplate();   
    		view.joinedUpdate(t.content.querySelector('tr'), joined);
    		var clone = document.importNode(t.content, true);
            t.parentElement.appendChild(clone);
    },
        
        
        renderRequest: function(req,user,route){
       			var t = view.reqTemplate();   
        		view.reqUpdate(t.content.querySelector('tr'), user,route);
        		var clone = document.importNode(t.content, true);
                t.parentElement.appendChild(clone);
				
				var trElement = document.querySelector('#bookingRequest-template').parentElement.querySelector('tr:last-of-type');
				var buttons = trElement.querySelectorAll('button');
				
				buttons[0].onclick = function(event){
					//IMPLEMENT ACCEPT BOOKREQ HERE
					console.log("Accept");
					
					//Add Passenger
					base.rest.addPassenger(route.routeID,user.userID).then(function(deleted){
						if(deleted.ok==false){
							alert("No empty seats");
						}else{
						location.reload();
						}
					});
					
					//Remove request and, if successfull, row in table
					base.rest.deleteRequest(req.bookingReqID).then(function(deleted){
						if(deleted.ok==true){
							trElement.parentElement.removeChild(trElement);
						}
					});
				};
				buttons[1].onclick = function(event){
					//IMPLEMENT DENY BOOKREQ HERE
					console.log("Deny");
					
					//Remove request and, if successfull, row in table
					base.rest.deleteRequest(req.bookingReqID).then(function(deleted){
						if(deleted.ok==true){
							trElement.parentElement.removeChild(trElement);
						}
					});
				};
        },
        update: function(trElement, route) {
        	 var tds = trElement.children;
        	 //console.log(route);
        	 tds[0].textContent = route.location;
        	 tds[1].textContent = route.destination;
        	 //console.log(route.freeSeats);
        	 tds[2].textContent = route.freeSeats;
        	 tds[3].textContent = route.timeOfDeparture;
        	// tds[4].textContent = route.timeOfArrival;

            
             //tds[3].textContent = e.toLocaleDateString() + ' ' + e.toLocaleTimeString();
        }, 
        reqUpdate: function(trElement,user,route) {
			
            var tds = trElement.children;
			tds[0].textContent = user.userName;
			tds[1].textContent = route.destination;
			tds[2].textContent = route.timeOfDeparture;
			tds[3].textContent = route.bookingEndTimeModifier;
        
        
        },
        
        
        joinedUpdate: function(trElement, joined) {
          	 
        	var tds = trElement.children;
        	tds[0].textContent = joined.location;
        	tds[1].textContent = joined.destination;
        	tds[2].textContent = joined.freeSeats;
        	tds[3].textContent = joined.timeOfDeparture;
        	tds[4].textContent = joined.timeOfArrival;
        },
        
        template: function(){
        	return document.getElementById('createdRoutes-template');
        },
        reqTemplate: function(){
        	return document.getElementById('bookingRequest-template');
        },
        
        joinedTemplate: function(){
        	return document.getElementById('joineddRoutes-template');
        },
        
        
        getUser: function(id){
                	base.rest.getUser(id).then(function(usr){
					return usr;
				});
        }
    };



    var controller = {
		load: function() {
			base.rest.getLoggedInUser().then(function(user) {
				//console.log(user);
                currentUser = user;		// Hämtar user
				base.rest.getRoutes(currentUser.userName).then(function(routes) { // Hämtar sen route när man har usern
	                model = routes;
	                view.render()
	            }); 
	            base.rest.getRequests(currentUser.userID).then(function(bookingRequests) {
	            	view.renderReq(bookingRequests);
	            });        
	            
	            base.rest.getRoutes(null,null,null,null,null,currentUser.userName).then(function(joinedRoutes) {
	            	 joinedModel = joinedRoutes;
	            	 console.log(joinedRoutes);
	            	view.renderJoined(joinedModel);
	            }); 
	            			      
            });

		},
		
    };
    return controller;
};











