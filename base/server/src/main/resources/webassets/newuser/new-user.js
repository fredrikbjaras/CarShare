var base = base || {};


    var controller = {
        submitUser: function(submitEvent) {
            submitEvent.preventDefault;
            var password = document.getElementById('set-password').value;
            var username = document.getElementById('set-username').value;
            var role = document.getElementById('set-role').value;
            var phoneNr = document.getElementById('set-phoneNr').value;
            var description = document.getElementById('description').value;
            var credentials = {username, password, role,phoneNr, description};
            if (password === '') {
                delete credentials.password;
            }
            else {
                base.rest.addUser(credentials).then(function(user) {
                    if (user.error) {
                        alert(user.message);
                    } else {
                        model.users.push(user);
                        view.renderPart(user);
                        var el = document.querySelector('#user-list .list-group button:last-of-type');
                        el.onclick = () => view.selectUser(user, el);
                        view.selectUser(user, el);
                        location.href='http://localhost:8080/home/home.html
                        
                    }
                });
            }
            return false;
        },
        
        load: function() { 
        document.getElementById('user-form').onsubmit = controller.submitUser;
        }
            return controller;
        };
        
       