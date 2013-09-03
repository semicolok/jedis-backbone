define([
	'backbone',
	'text!template/mainView.html',
	'text!template/timelineView.html'],
	function(Backbone, mainViewTemplate, timelineViewTemplate) {	
		return Backbone.View.extend({
			el : 'body',
			initialize : function(){
				this.render();
			},
			render : function(){
				this.$el.html(_.template(mainViewTemplate));				
				return this;
			}
		});
});