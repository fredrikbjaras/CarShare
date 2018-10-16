var base = base || {};
base.searchRouteController = function() {

	  var model = {
		        routes: [],
		        currentUser: []
		    };

		    var view = {

		    	// Följande tre funktioner laddar in tabellen
		        renderPart: function(route) {
		        	console.log("renderPart()");
		            var t = document.getElementById('route-template');
		            view.update(t.content.querySelector('tr'), route);
		            var clone = document.importNode(t.content, true);
		            t.parentElement.appendChild(clone);
		        },
		        update: function(trElement, route) {
		        	console.log("update()");
		        	var tds = trElement.children;
		        	tds[0].innerHTML = route.driverID;
		        	tds[1].innerHTML = route.location;
		        	tds[2].innerHTML = route.destination;
		        	tds[3].innerHTML = route.timeOfDeparture;
		        	tds[4].innerHTML = route.timeOfArrival;
		        	tds[5].innerHTML = route.freeSeats;
		        	var bookBtn = tds[6].getElementsByTagName('button')[0];
		        	bookBtn.onclick = controller.submitBookingRequest(route.routeID, route.driverID);
		        },
		        render: function() {
		        	console.log("render()");
		            model.routes.forEach(route => view.renderPart(route));
		        },
		        // Gör booking-request synlig när route valts
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
		    	},
		    	showRoutesTable: function() {
		    		var routeTable = document.getElementById('route-table');
		    		routeTable.style.display = 'block'
		    	}
		    };

		    var controller = {
		    	// Anropas när man trycker på searchRoute
		   		searchRoute: function(submitEvent) {
		   			submitEvent.preventDefault();
		   			console.log("searchRoute()");
	        		var driverName = document.getElementById('driver-name-input').value;
	        		var origin = document.getElementById('origin-input').value;
	        		var destination = document.getElementById('destination-input').value;
	        		var departureTime = document.getElementById('departure-input').value;
	        		var arrivalTime = document.getElementById('arrival-input').value;
	        		base.rest.getRoutes(driverName, origin, destination, departureTime, arrivalTime).then(function(routes) {
	        			console.log("Inside getRoutes REST call")
	        			routes.forEach(route => console.log(route));
	        			model.routes = routes;
	        			view.showRoutesTable();
	        			view.render();
	        		});

		   		},
		   		// Anropas när man skickar in sin booking-request
		        submitBookingRequest: function(routeID, driverID) {
		        	console.log("submitBookingRequest(" + routeID + ", " + driverID + ")");
		            base.rest.getLoggedInUser().then(function(user){
		            	var fromUserID = user.userID;
	                	base.rest.addBookingRequest(routeID, fromUserID, driverID);
		            });
		            return false;
	            },

		        load: function() {
		        	console.log("load()");
		        	document.getElementById('route-search-form').onsubmit = controller.searchRoute;
		            document.getElementById('booking-request-form').onsubmit = controller.submitBookingRequest;

		        }
		    };

		    return controller;
		};
