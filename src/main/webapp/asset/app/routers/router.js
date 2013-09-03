define([
	'backbone',
	'views/pageView',
	'views/dashboardView',
	'views/formView',
	'views/timelineView'
], function(Backbone, PageView, DashboardView, FormView, TimelineView) {
	return Backbone.Router.extend({
		routes: {
			'dash' : 'dashboard',
			'form' : 'form',
			'timeline' : 'timeline',
			'timeline/:id' : 'timelineDash'
		},
		initialize : function(options){
			this.mainView = options.mainView;
			new DashboardView();
		},
		dashboard : function(){
			new DashboardView();
		},
		form : function(){
			new FormView();
		},
		timeline : function(){
			new PageView();
			this.form();
			new TimelineView();
		},
		timelineDash : function(id){
			new PageView();
			this.form();
			new TimelineView({id : id});
		}
	});
});