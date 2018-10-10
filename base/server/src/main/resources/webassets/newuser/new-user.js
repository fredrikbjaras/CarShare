var base = base || {};
base.newUserController = (function() {
    var controller = {

        submitUser: function(submitEvent) {
            submitEvent.preventDefault;
            var username = document.getElementById('set-username').value;
            var password = document.getElementById('set-password').value;
            var phoneNr = document.getElementById('set-phoneNr').value; 
            base.rest.addUser(username, password,phoneNr).then(function(user) {
            	console.log("inside add user then");
                if (user.error) {
                	console.log("ERROR ADDING USER");
                    alert(user.message);
                } else {
                	console.log("all good?")
                }
            });
        },

        load: function() {
            console.log("new user page loaded");
            document.getElementById('user-form').onsubmit = controller.submitUser;
        },

        initOnLoad: function() {
        	console.log("haaloj");
            document.addEventListener('DOMContentLoaded', base.newUserController.load);
        }
    };
    return controller;
})();
