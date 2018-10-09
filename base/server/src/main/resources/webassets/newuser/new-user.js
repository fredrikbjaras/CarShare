var base = base || {};


    var controller = {
    		
        submitUser: function(submitEvent) {
            submitEvent.preventDefault;
            //console.log('första hej');
            var password = document.getElementById('set-password').value;
            var username = document.getElementById('set-username').value;
            
            if (password === '') {
                delete credentials.password;
            }
            else {
                base.rest.addUser(username, password).then(function(user)
                
                {
                    if (user.error) {
                        alert(user.message);
                    } else {
                    	location.href='http://localhost:8080/home/home.html'
                     //   model.users.push(user);
                       // view.renderPart(user);
                        //var el = document.querySelector('#user-list .list-group button:last-of-type');
                        //el.onclick = () => view.selectUser(user, el);
                        //view.selectUser(user, el);
                       
                        
                    }
                });
            }
            return false;
        },
        
        load: function() { 
        document.getElementById('user-form').onsubmit = controller.submitUser;
        }
            //return controller;
        };
        
       