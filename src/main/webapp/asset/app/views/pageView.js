define([
	'backbone',
	'models/timelineModel',
	'views/timelineView',
	'text!template/pageView.html'],
	function(Backbone, TimelineModel, TimelineView, pageViewTemplate) {
		
		return Backbone.View.extend({
			el : '#section',
			initialize : function(){
				this.$el.html(_.template(pageViewTemplate));
				this.$el.find('#timeline').empty();
				this.$el.find('#formSection').empty();
			},
			events : {
				"click #postBt"			: "postMessage"
			},
			postMessage : function() {
				var newPost = new TimelineModel({content: this.$el.find('#content').val()});
				newPost.save();
				new TimelineView();
			}
		});
});