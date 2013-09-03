define([
	'backbone',
	'text!template/formView.html'],
	function(Backbone, formViewTemplate) {
		return Backbone.View.extend({
			el : '#formSection',
			initialize : function(){
				this.$el.html(_.template(formViewTemplate));
			}
		});
});