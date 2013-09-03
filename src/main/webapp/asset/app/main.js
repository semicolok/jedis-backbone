require.config({
	deps: ['app'],
	paths : {
		'jquery' : 'libs/jquery/jquery-1.10.0.min',
		'underscore' : 'libs/underscore/underscore-min',
		'backbone' : 'libs/backbone/backbone-min'
	},
	shim : {
		'underscore' : {exports: '_'},
		'backbone' : {
			deps: ['underscore', 'jquery'],
			exports: 'Backbone'
		}
	}
});