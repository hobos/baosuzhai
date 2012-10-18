define([ "dojo/_base/array" ], function(array) {

	return {
		keywords : [ {
			input: "/",
			map: "&#xF7;",
			nodeName: "mo",
			freq: 0
		},{
			input: "/",
			map: "/",
			nodeName: "text",
			freq: 0
		},{
			input: "*",
			map: "&#xD7;",
			nodeName: "mo",
			freq: 0
		},{
			input: "*",
			map: "*",
			nodeName: "mo",
			freq: 0
		} ],

		getProposals : function(prefix) {
			// summary:
			//		根据前缀获取推荐值列表，推荐值按照推荐度倒序排列。
			//		“推荐度”，是整数，值越大推荐度越高。
			
			return array.filter(this.keywords, function(data, index, array) {
				return data.input.indexOf(prefix) == 0;
			});
		}
	};
	
});
