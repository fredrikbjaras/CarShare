var base = base || {};
base.userpageController = function() {

    var model = "";

    var view = {
        renderPart: function(user) {

        },
        update: function(liElement, user) {

        },

        render: function() {
        	document.getElementById('set-username').defaultValue = model.username;
        	document.getElementById('set-password').defaultValue = '';//model.password;
        	document.getElementById('set-phoneNbr').defaultValue = model.phoneNr;
        	document.getElementById('set-description').defaultValue = model.description;
        },

        resetEdit: function() {
           view.render();
        }

    };

    var controller = {
        submitChange: function(submitEvent) { //pop up notis att det är ändrad
        	//model.username = document.getElementById('set-username').value;
        	model.password = document.getElementById('set-password').value;
        	model.phoneNr = document.getElementById('set-phoneNbr').value;
        	model.description = document.getElementById('set-description').value;
            model.password = (model.password === "") ? null : model.password;

        	base.rest.updateUser(model.userID, model.password, model.phoneNr, null, model.description);

        },

        resetEdit: function(resetEvent){
        	view.render();
        },


        deleteUser: function(delteEvent){
        	base.rest.deleteUser(model.userId).then(function(response) {
            	base.rest.logout().then(function(response){
                base.changeLocation('/login/login.html');
            	});
            });

        },
        load: function() {
            base.rest.getLoggedInUser().then(function(user) {
                model = user;

                view.render();
            });
        }

    };
    return controller;
};

