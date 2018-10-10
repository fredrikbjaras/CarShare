var base = base || {};
base.searchRouteController = function() {

	  var model = {
		        users: [],
		        roles: [],
		        roleNames: []
		    };

		    var view = {
		        renderPart: function(user) {
		            var t = document.getElementById('user-template');
		            view.update(t.content.querySelector('button'), user);
		            var clone = document.importNode(t.content, true);
		            t.parentElement.appendChild(clone);
		        },
		        update: function(liElement, user) {
		            liElement.textContent = user.username;
		        },
		        render: function() {
		            model.users.forEach(user => view.renderPart(user));
		            var roleTemplate = document.getElementById('role-template');
		            model.roles.forEach(function(role) {
		                var o = roleTemplate.content.querySelector('option');
		                o.textContent = role.label;
		                o.value = role.name;
		                var clone = document.importNode(roleTemplate.content, true);
		                roleTemplate.parentElement.appendChild(clone);
		            });
		        },
		        removeUser: function(user, ix) {
		            var el = document.getElementById('user-list').querySelectorAll('button')[ix];
		            el.parentElement.removeChild(el);
		        },

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

		        resetEdit: function() {
		            var userView = document.getElementById('user-view');
		            var isEdit = userView.classList.contains('edit');
		            view.editPassword(isEdit);
		        },
		        editPassword: function(disabled) {
		            var pi = document.getElementById('set-password');
		            pi.disabled = disabled;
		        },
		        selectUser: function(user, clickedEl) {
		            // Set appropriate user-view class to either add or edit.
		            var userView = document.getElementById('user-view');
		            view.showForm('user-form');
		            if (user.username === '') {
		                userView.classList.remove('edit');
		                userView.classList.add('add');
		                view.editPassword(false);
		            } else {
		                userView.classList.add('edit');
		                userView.classList.remove('add');
		                view.editPassword(true);
		            }

		            // Set active link the left-hand side menu.
		            document.getElementById('user-list')
		                .querySelectorAll('.active')
		                .forEach(activeEl => activeEl.classList.remove('active'));
		            clickedEl.classList.add('active');

		            document.getElementById('user-data').querySelector('a').href = '/rest/foo/user/'+user.id;

		            // Set defaults of form values. This will allow the HTML reset button to work by default HTML behaviour.
		            document.getElementById('user-id').defaultValue = user.id;
		            document.getElementById('set-username').defaultValue = user.username;
		            document.getElementById('set-password').defaultValue = '';
		            document.getElementById('set-phoneNr').defaultValue = user.phoneNr; //Kanske phoneNr? Se hur JSON ser ut
		            document.getElementById('description').defaultValue = user.description;

		            var roleIx = model.roleNames.indexOf(user.role.name);
		            var options = document.getElementById('set-role').querySelectorAll('option');
		            options.forEach(o => o.defaultSelected = false);
		            options[roleIx].defaultSelected = true;
		            document.getElementById('user-form').reset();
		        }
		    };

		    var controller = {

		   		searchRoute: function(){
		        		var driverID = document.getElementById('driver-id-input').value;
		        		var origin = document.getElementById('origin-input').value;
		        		var destination = document.getElementById('destination-input').value;
		        		var departureTime = document.getElementById('departure-input').value;
		        		var arrivalTime = document.getElementById('arrival-input').value;

		        	    var routeFilterObj = {driverID: driverID, origin: origin, destination: destination, departureTime: departureTime, arrivalTime: arrivalTime };
		        		return routeFilterObj; 
		      
		   		},

		        submitUser: function(submitEvent) {
		            submitEvent.preventDefault;
		            var password = document.getElementById('set-password').value;
		            //var username = document.getElementById('set-username').value;
		            var role = document.getElementById('set-role').value;
		            var id = document.getElementById('user-id').value;
		            var phoneNr = document.getElementById('set-phoneNr').value;
		            var description = document.getElementById('description').value;
		            password = (password === "") ? null: password;
		            phoneNr = (phoneNr === "") ? null: phoneNr;
		            if (id !== '') {
		                base.rest.updateUser(id, password, phoneNr, role, description).then(function(user) {
		                    if (user.error) {
		                        alert(user.message);
		                    } else {
		                        var el = document.querySelector('#user-list .active');
		                        el.onclick = () => view.selectUser(user, el);
		                        view.update(el, user);
		                        view.selectUser(user, el);
		                    }
		                });
		            } else {
		                base.rest.addUser(username, password, phoneNr).then(function(user) {
		                    if (user.error) {
		                        alert(user.message);
		                    } else {
		                        model.users.push(user);
		                        view.renderPart(user);
		                        var el = document.querySelector('#user-list .list-group button:last-of-type');
		                        el.onclick = () => view.selectUser(user, el);
		                        view.selectUser(user, el);
		                    }
		                });
		            }
		            return false;
		        },

		       
		        load: function() {
		           
		            document.getElementById('route-search-form').onsubmit =controller.searchRoute();
		            var routeObject = controller.searchRoute();
		            //base.mainController.view.hideUserLinks(); // Visa bara admin-länkar
		            Promise.all([
		                base.rest.getRoutes(routeObject.driverID,routeObject.origin,routeObject.destination,routeObject.departureTime,routeObject.arrivalTime).then(function(routes) {
		                    model.routes = routes;
		                    return routes;
		                }), // Kommentera bort härifrån?
		                
		                
		                
		                
		               base.rest.getRoles().then(function(roles) {
		                    model.roles = roles;
		                    model.roleNames = roles.map(r => r.name);
		                    return roles;
		                })
		            ]) .then(function(values) {
		                view.render();
		                var userEls = document.querySelectorAll('#user-list button');
		                userEls.forEach(function(el, i) {
		                    if (i == userEls.length-1) {
		                        el.onclick = () => view.selectUser({username: '', role: model.roles[0], id: ''}, el);
		                    } else {
		                        var user = model.users[i]; // Closure on user, not on i
		                        el.onclick = () => view.selectUser(user, el);
		                    }
		                });
		                userEls[0].click();
		            });
		        }
		    };

		    return controller;
		};
