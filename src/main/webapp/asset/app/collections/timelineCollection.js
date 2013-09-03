define(['backbone',	
	'models/timelineModel'],
	function(Backbone, TimelineModel) {
		return Backbone.Collection.extend({
			model : TimelineModel,
			url : '/timelines',
			parse : function(response){
				return response.timelines;
			}
	});
});