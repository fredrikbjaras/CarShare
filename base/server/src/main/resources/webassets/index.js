var base = base || {};
base.mainController = (function() {

    var routingTable = {
        // first in table is the default
        'home': {
            partial: 'home/home.html',
            controller: base.homeController
        },
        'userpage': {
            partial: 'userpage/userpage.html',
            controller: base.userpageController
        },
        'admin': {
            partial: 'admin/user-admin.html',
            controller: base.userAdminController
        },
        'searchRoute':{
        	partial: 'searchRoute/searchRoute.html',
        	controller: base.searchRouteController
        },
        'addRoute':{
        	partial: 'addRoute/addRoute.html',
        	controller: base.addRouteController
        }
    };

    var model = {
        route: ''
    };

    var view = {
        render: function() {
            var nav = document.getElementById('main-nav');
            var activeTabLink = nav.querySelector('li.active');
            if (activeTabLink) activeTabLink.classList.remove('active');
            var newActiveTabLink = nav.querySelector('a[href="#/'+model.route+'"]');
            if (newActiveTabLink) newActiveTabLink.parentElement.classList.add('active');
        },
        hideAdminLinks() {
            document.querySelectorAll('#main-nav li.admin-only').forEach(li => li.style.display = 'none');
        },
        renderUsername: function() {
            document.getElementById('username').textContent = model.user.userName;
        },
        hideUserLinks(){
            document.querySelectorAll('#main-nav li.user-only').forEach(li => li.style.display = 'none');
        }
    };

    var controller = {
        routingTable: routingTable,
        changeRoute: function() {
            var newRoute = location.hash.slice(2);
            if (!controller.routingTable[newRoute]) {
                location.hash = '/'+Object.keys(controller.routingTable)[0];
                return;
            }
            model.route = newRoute;
            fetch(controller.routingTable[newRoute].partial)
                .then(response => response.text())
                .then(function(tabHtml) {
                    document.getElementById('main-tab').innerHTML = tabHtml;
                    controller.routingTable[newRoute].controller().load();
                });
            view.render();
        },
        load: function() {
            document.getElementById('logout').onclick = controller.logout;
            window.onhashchange = base.mainController.changeRoute;
            base.mainController.changeRoute();
            base.rest.getLoggedInUser().then(function(user) {
                model.user = user;
                view.renderUsername();
                if (user.isNone() || user.userName == 'NONEACCOUNT') {
                    base.changeLocation('/login/login.html');
                } else if (!user.isAdmin()) {
                    view.hideAdminLinks();
                }
                else if (user.isAdmin()) {
                    view.hideUserLinks();
                    document.getElementById('user-admin-link').click()
                }
            });
        },
        logout: function() {
            base.rest.logout().then(function(response) {
                base.changeLocation('/login/login.html');
            });
        },
        initOnLoad: function() {
            document.addEventListener("DOMContentLoaded", base.mainController.load);
        }
    };
    return controller;
})();

base.changeLocation = function(url) {
    window.location.replace(url);
};
