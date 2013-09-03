define([
	'backbone',
	'collections/timelineCollection', 
	'text!template/timelineView.html'],
	function(Backbone, TimelineCollection, timelineViewTemplate) {
		var template = _.template(timelineViewTemplate);
		
		return Backbone.View.extend({
			el : '#timeline',
			collection : new TimelineCollection,
			initialize : function(args){
				if (args) {
					this.collection.url = '/timelines/'+args.id
				}
				this.render();
			},
			render : function(){
				var that = this;
				this.collection.fetch({
					success : function(models, res, option) {
						that.$el.html(template({timeline : models.toJSON()}));
					}
				});							
				return this;
			}
		});
});