/**
 * 光标
 */
define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/dom-construct",
        "dojo/dom-class",
        "dojo/dom-style"],function(
        		declare,
        		lang,
        		domConstruct,
        		domClass,
        		domStyle){
	
	return declare("drip.layer.Cursor",null,{
		
		// parentEl: domNode
		//		放置光标的容器节点
		parentEl: null,
		
		caret: null,
		
		isVisible: false,
		
		cursorConfig: {top:0, left:0},
		
		constructor: function(kwArgs){
			lang.mixin(this, kwArgs);
			var caret = this.caret = domConstruct.create("div",{class:"drip_cursor"},this.parentEl);
			caret.style.visibility = "hidden";
			
			this.defaultHeight = caret.clientHeight;
			this.cursorConfig.height = this.defaultHeight;
		},
		
		show: function(){
			// summary:
			//		显示光标
			
			this.isVisible = true;
			this.caret.style.visibility = "";
			
			this._restartTimer();
		},
		
		move: function(cursorConfig){
			if(this.isVisible == false)return;
			this.cursorConfig = cursorConfig;
			var top = cursorConfig.top;
			var left = cursorConfig.left;
			var height = cursorConfig.height;
			var style = this.caret.style;
			style.top = top+"px";
			style.left = left+"px";
			if(height && height > 0){
				style.height = height+"px";
			}
			
			this.caret.style.visibility = "";
			this._restartTimer();
		},
		
		getCursorConfig: function(){
			return this.cursorConfig;
		},
		
		hide: function(){
			// summary:
			//		隐藏光标
			
			this.isVisible = false;
			clearInterval(this.blinkId);
			this.blinkId = null;
		},
		
		_restartTimer: function(){
			if(this.blinkId != null){
				clearInterval(this.blinkId);
				this.blinkId = null;
			}
			
			var caret = this.caret;
			this.blinkId = setInterval(function(){
				caret.style.visibility = "hidden";
	            setTimeout(function() {
	            	caret.style.visibility = "";
	            }, 400);
			},1000);
		},
		
		destroy: function(){
			if(this.blinkId != null){
				clearInterval(this.blinkId);
			}
			// 删除光标
			domConstruct.destroy(this.caret);
		}
	
	});
});