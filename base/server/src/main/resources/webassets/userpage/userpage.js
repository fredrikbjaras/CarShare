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
        	document.getElementById('set-username').defaultValue = model.userName;
        	document.getElementById('set-password').defaultValue = '';//model.password;
        	document.getElementById('set-phoneNbr').defaultValue = model.phoneNr;
        	document.getElementById('set-description').defaultValue = model.description;
        },

        resetEdit: function() {
           view.render();
        }

    };

    var controller = {
        submitChange: function() { //pop up notis att det är ändrad
        	console.log('changing');
        	//model.username = document.getElementById('set-username').value;
        	if(document.getElementById('set-password').value != ''){
        		model.password = document.getElementById('set-password').value;
    		}
        	model.phoneNr = document.getElementById('set-phoneNbr').value;
        	model.description = document.getElementById('set-description').value;
            //model.password = (model.password === "") ? null : model.password;

        	base.rest.updateUser(model.userID, model.userName, model.password, model.phoneNr, null, model.description);

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
        	document.getElementById('submit-user').onclick = controller.submitChange;
            base.rest.getLoggedInUser().then(function(user) {
                model = user;

                view.render();
            });
        }

    };
    return controller;
};

