var base = base || {};
base.userpageController = function() {

    var model = "";

    var view = {
        renderPart: function(user) {

        },
        update: function(liElement, user) {

        },

        render: function() {
        	console.log('render');
        	document.getElementsByTagName('h1')[0].innerHTML = "User Profile: " + model.userName;
        	document.getElementById('set-password').defaultValue = '';//model.password;
        	document.getElementById('set-phoneNbr').defaultValue = model.phoneNr;
        	document.getElementById('set-description').defaultValue = model.description;
        	var deleteButton = document.getElementById('delete-user');
        	
        	deleteButton.onclick = function(event){
        	base.rest.getLoggedInUser().then(function(u) {
					base.rest.deleteUser(u.userID).then(function(response) {
            			base.rest.logout().then(function(response){
                			base.changeLocation('/login/login.html');
            		});
				});	
			});
    	};
	}	
	};

    var controller = {
        submitChange: function() { //pop up notis att det är ändrad
        	console.log('changing');
        	var password = document.getElementById('set-password').value;
        	var phoneNr = document.getElementById('set-phoneNbr').value;
        	var description = document.getElementById('set-description').value;
        	base.rest.updateUser(model.userID, model.userName, password, phoneNr, null, description);
        },

        resetEdit: function(resetEvent){
        	view.render();
        },


        deleteUser: function(deleteEvent){
        	base.rest.deleteUser(model.userId).then(function(response) {
            	base.rest.logout().then(function(response){
                base.changeLocation('/login/login.html');
            	});
            });

        },
        load: function() {
        	document.getElementById('submit-user').onclick = controller.submitChange;
            base.rest.getLoggedInUser().then(function(user) {
                model = user;
                view.render();
            });
        }

    };
    return controller;
};
