define(["dojo/_base/declare",
        "dojo/_base/array",
        "dojo/_base/lang",
        "dojo/on",
        "dojo/dom-construct",
        "dojo/dom-class",
        "dojo/dom-style",
        "dijit/popup",
        "dijit/DropDownMenu",
        "dijit/MenuItem",
        "drip/mathContentAssist"], function(
		declare,
		array,
		lang,
		on,
		domConstruct,
		domClass,
		domStyle,
		popup,
		DropDownMenu,
		MenuItem,
		mathContentAssist) {

	return declare("drip.ContentAssist",DropDownMenu,{
		// summary:
		//		弹出的建议输入列表提示框
		// 提示框关闭的时机：
		//		1.按下ESC
		//		2.鼠标点击弹出框之外的区域
		//		3.用户用鼠标点击一个建议的值
		//		4.用户选择一个建议值，并按下回车键
		//		5.用户忽略弹出的提示框，新输入的值与前面的缓存值找不出建议项，
		//		  则保留用户的输入，并关闭提示框（这个可提高用户的输入效率）。
		
		proposals : null,
		view: null,
		
		// summary:
		//		缓存的字符串，因为这个字符串中约定不包含unicode字符，所以直接使用字符串存储。
		cacheString: "",
		opened: false,
		
		
		postCreate: function(){
			this.inherited(arguments);
			on(this.view.editorDiv, "mousedown", lang.hitch(this,function(e){
				if(this.opened){
					popup.close(this);
				}
			}));
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
			var self = this;
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
							self.opened = false;
						},
						onCancel : function() {
							popup.close(target);
							self.opened = false;
							// 编辑器获取焦点
							view.focus();
						},
						onClose : function() {
							// 编辑器获取焦点
							view.focus();
							self.opened = false;
						}
					});
					this.opened = true;
					//target.focus();
					// focus是选中加上获取焦点；而select只选中，但不获取焦点。
					target.select();
				}, 0);
			}
		},
		
		_open: function(){
			var cursorPosition = this.view.getCursorPosition();
			var x = cursorPosition.x;
			var y = cursorPosition.y;
			this._scheduleOpen(this, x, y);
		},
		
		_clear: function(){
			var children = this.getChildren();
			array.forEach(children, lang.hitch(this,function(child, index){
				this.removeChild(child);
			}));
		},
		
		_setProposals: function(proposals){
			// summary:
			//		往弹出面板中添加数据。
			// data: Array
			
			this._clear();
			
			this.proposals = proposals;
			array.forEach(proposals,lang.hitch(this,function(jsonObject,index){
				var menuItem = new MenuItem({label:jsonObject.label, iconClass:jsonObject.iconClass});
				// jsonObject
				menuItem.on("click", lang.hitch(this,this._onApplyProposal,jsonObject.map, jsonObject.nodeName));
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
			
			//1. 新输入的data，如何追加。
			//2. 如果用户已经默认某些值，在这里调用应用方法，并关闭打开的提示框。
			
			if(this.opened==false){
				this.cacheString = data;
			}else{
				this.cacheString+=data;
			}
			
			var proposals = mathContentAssist.getProposals(this.cacheString);
			this._setProposals(proposals);
			if(proposals.length > 0){
				this._open();
				var result = proposals[0].map;
				return result;
			}else{
				this.cacheString = "";
				if(this.opened){
					popup.close(this);
					this.opened = false;
				}
				return null;
			}
		},
		
		_onApplyProposal: function(data,nodeName, evt){
			// 因为cacheString的值是实时变化的，所以需要在外面加一层方法调用。
			debugger;
			this.apply(data, nodeName, this.cacheString.length,evt);
		},
		
		apply: function(data, nodeName, cacheCount,evt){
			// summary:
			//		应用某个建议的值，将其最终存入到model中。
			// data：
			//		当前item对应的数据
			// nodeName:
			//		用那个mathml标签封装data
			// cacheCount:
			//		缓存的字符的个数，这些字符已经在view中显示，需要根据这个数字删除
			// evt：
			//		事件对象
			
			console.log(data, cacheCount, evt);
		},
		
		enter: function(evt){
			this.onItemClick(this.selectedChild, evt);
		},
		
		
		/*************************下面是select相关的代码****************************/
		select: function(){
			this.selectFirstChild();
		},
		
		selectFirstChild: function(){
			this.selectChild(this._getFirstSelectableChild());
		},
		
		selectPrev: function(){
			this.selectChild(this._getNextSelectableChild(this.selectedChild, -1), true);
		},
		
		selectNext: function(){
			this.selectChild(this._getNextSelectableChild(this.selectedChild, 1));
		},
		
		_getFirstSelectableChild: function(){
			// summary:
			//		Returns first child that can be focused
			return this._getNextSelectableChild(null, 1);	// dijit/_WidgetBase
		},

		_getLastFocusableChild: function(){
			// summary:
			//		Returns last child that can be focused
			return this._getNextSelectableChild(null, -1);	// dijit/_WidgetBase
		},

		_getNextSelectableChild: function(child, dir){
			// summary:
			//		Returns the next or previous selected child, compared
			//		to "child"
			// child: Widget
			//		The current widget
			// dir: Integer
			//		- 1 = after
			//		- -1 = before
			
			if(child){
				child = this._getSiblingOfChild(child, dir);
			}
			var children = this.getChildren();
			for(var i=0; i < children.length; i++){
				if(!child){
					child = children[(dir>0) ? 0 : (children.length-1)];
				}
				if(child.isFocusable()){
					return child;	// dijit/_WidgetBase
				}
				child = this._getSiblingOfChild(child, dir);
			}
			// no focusable child found
			return null;	// dijit/_WidgetBase
		},
		
		selectChild: function(/*dijit/_WidgetBase*/ widget, /*Boolean*/ last){
			// summary:
			//		选中指定的子部件
			// widget:
			//		子部件
			// last:
			//		If true and if widget has multiple focusable nodes, focus the
			//		last one instead of the first one
			// tags:
			//		protected

			if(!widget){ return; }

			if(this.selectedChild && widget !== this.selectedChild){
				this.selectedChild._setSelected(false);
			}
			
			this.selectedChild = widget;
			widget._setSelected(true);
		}
		
		
	});
	
});