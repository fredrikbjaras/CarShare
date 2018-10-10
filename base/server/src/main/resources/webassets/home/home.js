var base = base || {};
base.changeLocation = function(url) {
    window.location.replace(url);
};
base.homeController = function() {
    var model = [];

    var view = {
        render: function() {
        },

        update: function(trElement, foo) {
        }
    };



    var controller = {
		load: function() {
            view.render();
          //  document.getElementById('search-route').onclick = base.changeLocation('searchRoute/searchRoute.html')
        }
    };
    return controller;
};