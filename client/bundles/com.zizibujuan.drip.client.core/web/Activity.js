// 显示活动列表
define(["dojo/_base/declare",
        "dojo/_base/array",
        "dojo/_base/lang",
        "dijit/_WidgetBase",
        "dijit/_TemplatedMixin",
        "dojo/store/JsonRest",
        "dojo/text!/templates/ActivityNode.html",
        "dojo/text!/templates/ActivityList.html"], function(
        		declare,
        		array,
        		lang,
        		_WidgetBase,
        		_TemplatedMixin,
        		JsonRest,
        		nodeTemplate,
        		listTemplate){
	
	var ActivityNode = declare("ActivityNode",[_WidgetBase, _TemplatedMixin],{
		templateString: nodeTemplate,
		data:{},
		postCreate : function(){
			this.inherited(arguments);
		}
		
	});
	
	var Activity = declare("Activity",[_WidgetBase, _TemplatedMixin],{
		templateString: listTemplate,
		 
		 store:new JsonRest({
			 target:"/activities/"
		 }),
		 
		 // 如果没有习题，则显示没有习题，
		 // 可扩展提示用户录入习题。
		 postCreate : function(){
			 this.inherited(arguments);
			 this.store.query(/*TODO:加入分页信息*/).then(lang.hitch(this, this._load));
		 },
		 
		 _load : function(items){
			 debugger;
			 if(items.length == 0){
				 this.domNode.innerHTML = "没有活动";
			 }else{
				 array.forEach(items, lang.hitch(this,function(item, index){
					 var node = new ActivityNode({
						 data : item
					 });
					 this.domNode.appendChild(node.domNode);
				 }));
				 // 使用mathjax进行呈现
				 MathJax.Hub.Queue(["Typeset",MathJax.Hub, this.domNode]);
			 }
		 }
		 
	});
	
	return Activity;
	
});