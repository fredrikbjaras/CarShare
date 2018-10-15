var base = base || {};
base.searchRouteController = function() {

	  var model = {
		        routes: [],
		        //roles: [],
		        //roleNames: []
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
		    	},
		    	// Tror den ska anropas när man trycker på en route
		        selectRoute: function(route, clickedEl) {
		        	console.log("selectRoute()");
		        	/*  // Set appropriate user-view class to either add or edit.BEHÖVS EJ FÖR ROUTE
		            var userView = document.getElementById('user-view');
		            view.showForm('booking-request-form');
		            if (user.username === '') {
		                userView.classList.remove('edit');
		                userView.classList.add('add');
		                view.editPassword(false);
		            } else {
		                userView.classList.add('edit');
		                userView.classList.remove('add');
		                view.editPassword(true);
		            }*/

		            // Set active link the left-hand side menu.
		            document.getElementById('route-list')
		                .querySelectorAll('.active')
		                .forEach(activeEl => activeEl.classList.remove('active'));
		            clickedEl.classList.add('active');
		          //Behöver ej hämta user-data
		          //  document.getElementById('user-data').querySelector('a').href = '/rest/foo/user/'+user.id;

		            // Set defaults of form values. This will allow the HTML reset button to work by default HTML behaviour.
		            document.getElementById('route-input').defaultValue = route.routeID;
		            document.getElementById('description-input').defaultValue = '';
		            document.getElementById('route-form').reset();
		        }
		    };

		    var controller = {
		    	// Anropas när man trycker på searchRoute
		   		searchRoute: function(){
		   			console.log("searchRoute()");
	        		var driverName = document.getElementById('driver-name-input').value;
	        		var origin = document.getElementById('origin-input').value;
	        		var destination = document.getElementById('destination-input').value;
	        		var departureTime = document.getElementById('departure-input').value;
	        		var arrivalTime = document.getElementById('arrival-input').value;
	        		base.rest.getRoutes(driverName, origin, destination, departureTime, arrivalTime).then(function(routes) {
	        			console.log("Inside getRoutes REST call")
	        			routes.forEach(route => console.log("route: " + route));
	        			model.routes = routes;
	        			view.showRoutesTable();
	        			view.render();
	        		});

		   		},
		   		// Anropas när man skickar in sin booking-request
		        submitBookingRequest: function(submitEvent) {
		            submitEvent.preventDefault;
		            var routeID = route.routeID;
		            base.rest.getLoggedInUser().then(function(user){
		            	var currentUser = user;
		            });
		            var fronUserID = user.userID;
		            var toUserID = route.driverID;
		            var description = document.getElementById('description').value;
		                base.rest.addBookingRequest(routeID, currentUserID, toUserID,description);
		            },

		        load: function() {
		        	console.log("load()");
		        	var routeObject= document.getElementById('route-search-form').onsubmit = controller.searchRoute;
		            document.getElementById('booking-request-form').onsubmit = controller.submitBookingRequest;

		        }
		    };

		    return controller;
		};
