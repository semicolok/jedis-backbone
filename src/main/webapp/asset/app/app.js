require([
	'backbone',
	'routers/router',
	'views/mainView'
	], 
	function(Backbone, Router, MainView) {

	new Router({mainView: new MainView()});

	Backbone.history.start();

});