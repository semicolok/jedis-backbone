define(['backbone'], function(Backbone){
	return Backbone.Model.extend({
		defaults : {
			content : '',
			name : 'jknyou',
			pid : '',
			replyPid : '',
			replyTo : '',
			time : '',
			timeArg : ''
		},
		url : '/timelines'
	});
});