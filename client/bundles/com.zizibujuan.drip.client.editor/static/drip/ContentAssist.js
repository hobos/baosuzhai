define(["dojo/_base/declare",
        "dojo/_base/array",
        "dojo/_base/lang",
        "dojo/dom-construct",
        "dojo/dom-class",
        "dijit/popup",
        "dijit/DropDownMenu",
        "dijit/MenuItem",
        "drip/mathContentAssist"], function(
		declare,
		array,
		lang,
		domConstruct,
		domClass,
		popup,
		DropDownMenu,
		MenuItem,
		mathContentAssist) {

	return declare("drip.ContentAssist",DropDownMenu,{
		proposals : null,
		view: null,
		cacheString:"",
		
		postCreate: function(){
			this.inherited(arguments);
		},
		
		startup: function(){
			this.inherited(arguments);
		},
		
		// 来自dijit/Menu
		_scheduleOpen: function(/*DomNode?*/ target, x, y){
			// summary:
			//		Set timer to display myself.  Using a timer rather than displaying immediately solves
			//		two problems:
			//
			//		1. IE: without the delay, focus work in "open" causes the system
			//		context menu to appear in spite of stopEvent.
			//
			//		2. Avoid double-shows on linux, where shift-F10 generates an oncontextmenu event
			//		even after a event.stop(e).  (Shift-F10 on windows doesn't generate the
			//		oncontextmenu event.)

			view = this.view;
			if(!this._openTimer){
				this._openTimer = this.defer(function(){
					delete this._openTimer;
					popup.open({
						popup : target,
						x : x,
						y : y,
						onExecute : function() {
							popup.close(target);
							// 编辑器获取焦点
							view.focus();
						},
						onCancel : function() {
							popup.close(target);
							// 编辑器获取焦点
							view.focus();
						},
						onClose : function() {
							// 编辑器获取焦点
							view.focus();
						}
					});
					target.focus();
				}, 0);
			}
		},
		
		_open: function(){
			var x = 100; // 从view中获取
			var y = 100; // 从view中获取
			this._scheduleOpen(this, x, y);
		},
		
		_setProposals: function(proposals){
			// summary:
			//		往弹出面板中添加数据。
			// data: Array
			
			this.proposals = proposals;
			
			this.destroyDescendants();
			array.forEach(proposals,lang.hitch(this,function(jsonObject,index){
				var menuItem = new MenuItem({label:jsonObject.map});
				// jsonObject
				menuItem.on("click", lang.hitch(this,this.apply,this.cacheString));
				this.addChild(menuItem);
			}));
		},

		// TODO:需要弹出框与编辑器之间切换焦点
		// 当弹出框时，弹出框获取焦点；当关闭弹出狂框，编辑器获取焦点。
		show: function(data){
			// summary:
			//		判断输入的内容是否可以获取到建议的映射值
			// data: String
			//		输入的字符
			// return:String
			//		推荐的值，如果没有则返回null。
			// FIXME：返回一个数组，每个值是一个“单个”字符
			
			this.cacheString += data;
			var proposals = mathContentAssist.getProposals(this.cacheString);
			this._setProposals(proposals);
			if(proposals.length > 0){
				this._open();
				return proposals[0].map;
			}else{
				this.cacheString = "";
				return null;
			}
		},
		
		apply: function(data, evt){
			// summary:
			//		应用某个建议的值，将其最终存入到model中。
			// data：
			//		当前item对应的数据
			// evt：
			//		事件对象
			
			// 根据data的值，从数组中获取相应的位置。好像已经没有这个必要了。
			debugger;
			console.log(data, evt);
		}
		
		
	});
	
});