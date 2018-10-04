var base = base || {};
base.homeController = function() {
	// List of all foo data, will be useful to have when update functionality is added.
    var model = [];

    var view = {
        // Creates HTML for each foo in model
        render: function() {
            model.forEach(d => view.renderPart(d));
        },
        // Creates HTML for foo parameter and adds it to the parent of the template
        renderPart: function(foo) {
            // Gets a reference to the template.
            // A template element is a special element used only to add dynamic content multiple times.
            // See: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/template
            var t = view.template();
            view.update(t.content.querySelector('tr'), foo);
            var clone = document.importNode(t.content, true);
            t.parentElement.appendChild(clone);
        },
        // Update a single table row to display a foo
        update: function(trElement, foo) {
        
            var tds = trElement.children;
            tds[0].textContent = foo.payload;
            var d = foo.createdDate;
            tds[1].textContent = d.toLocaleDateString() + ' ' + d.toLocaleTimeString();        },
        template: function() {
            return document.getElementById('createdRoutes-template');
        }
    };

	
	
    var controller = {
    		load: function() {
                base.rest.getFoos().then(function(foos) {
                    model = foos;
                    view.render();
                });

    		
    }
    };
    window.initOnLoad();
    return controller;
};