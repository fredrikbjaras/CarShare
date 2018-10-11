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
		            var t = document.getElementById('route-template');
		            view.update(t.content.querySelector('button'), route);
		            var clone = document.importNode(t.content, true);
		            t.parentElement.appendChild(clone);
		        },
		        update: function(liElement, route) {
		            liElement.textContent = route.routeID;
		        },
		        render: function() {
		            model.users.forEach(user => view.renderPart(route));
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
		    	// Tror/ den ska anropas när man trycker på en route
		         selectRoute: function(route, clickedEl) {
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
		        		var driverName = document.getElementById('driver-name-input').value;
		        		var origin = document.getElementById('origin-input').value;
		        		var destination = document.getElementById('destination-input').value;
		        		var departureTime = document.getElementById('departure-input').value;
		        		var arrivalTime = document.getElementById('arrival-input').value;

		        	    var routeFilterObj = {driverName: driverName, origin: origin, destination: destination, departureTime: departureTime, arrivalTime: arrivalTime };
		        		return routeFilterObj;

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
		           // fixa? Kan man göra en sån här tilldelning?
		        	var routeObject= document.getElementById('route-search-form').onsubmit =controller.searchRoute;
		        	document.getElementById('listbutton').onclick = (event) => view.showForm('booking-request-form');
		            document.getElementById('booking-request-form').onsubmit = controller.submitBookingRequest;

		            Promise.all([
		                base.rest.getRoutes(routeObject.driverID,routeObject.origin,routeObject.destination,routeObject.departureTime,routeObject.arrivalTime).then(function(routes) {
		                    model.routes = routes;
		                    return routes;
		                }),


		                //getRoles behövs väl ej?
		               base.rest.getRoles().then(function(roles) {
		                    model.roles = roles;
		                    model.roleNames = roles.map(r => r.name);
		                    return roles;
		                })
		            ]) .then(function(values) {
		                view.render(); // har lagt till ID på den knappen för att hantera onclick, kan göra att den nedan blir fel.
		                var routeEls = document.querySelectorAll('#route-list button');
		                routeEls.forEach(function(el, i) {
		                    if (i == routeEls.length-1) { //Skapas en ny route om man är på första platsen? Behöver vi det?
		                        el.onclick = () => view.selectRoute({routeID: '', role: model.roles[0], id: ''}, el);
		                    } else {
		                        var route = model.routes[i]; // Closure on route, not on i
		                        el.onclick = () => view.selectRoute(route, el);
		                    }
		                });
		                routeEls[0].click();
		            });
		        }
		    };

		    return controller;
		};
