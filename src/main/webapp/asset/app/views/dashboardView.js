define([
	'backbone',
	'text!template/dashboardView.html'],
	function(Backbone, dashboardViewTemplate) {	
		return Backbone.View.extend({
			el : '#section',
			initialize : function(){
				this.render();
			},
			render : function(){
				this.$el.html(_.template(dashboardViewTemplate));				
				return this;
			}
		});
});