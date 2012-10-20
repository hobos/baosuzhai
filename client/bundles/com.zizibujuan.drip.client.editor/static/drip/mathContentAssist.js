define([ "dojo/_base/array" ], function(array) {

	return {
		keywords : [ {
			input: "/", // 用户输入的值
			map: "&#xF7;",  // 在编辑器中实际输入的值
			nodeName: "mo", // 使用那个标签封装
			freq: 0, // 用户选择的频率
			label: "除号", // 在提示菜单中显示的值
			iconClass: "drip_equation_icon drip_division" // 在提示菜单中显示的图标
		},{
			input: "/",
			map: "/",
			nodeName: "text",
			freq: 0,
			label: "/",
			iconClass: ""
		},{
			// <mfrac> numerator(分子) denominator(分母) </mfrac>
			input: "/",
			map: "",
			nodeName: "mfrac",
			freq: 0,
			label: "分数",
			iconClass: "drip_equation_icon drip_frac"
		},{
			input: "*",
			map: "&#xD7;",
			nodeName: "mo",
			freq: 0,
			label: "乘号",
			iconClass: "drip_equation_icon drip_multiplication"
		},{
			input: "*",
			map: "*",
			nodeName: "text",
			freq: 0,
			label: "*",
			iconClass: ""
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
