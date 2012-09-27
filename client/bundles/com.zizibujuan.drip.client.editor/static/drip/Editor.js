define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/event",
        "dijit/_WidgetBase",
        "dojo/on",
        "dojo/keys",
        "dojo/dom-construct",
        "drip/Model",
        "drip/View"],function(
        		 declare,
        		 lang,
        		 event,
        		 _WidgetBase,
        		 on,
        		 keys,
        		 domConstruct,
        		 Model,
        		 View){
	
	return declare("drip.Editor",[_WidgetBase],{
		model : null,
		view : null,
		textarea : null,
		
		// value: String
		//		编辑器的值
		value : "",
		
		_getValueAttr: function(value){
			return this.model.getXML();
	    },
		
		postCreate : function(){
			var textarea = this.textarea = domConstruct.create("textarea",{style:{position:"absolute",top:"300px"}}, this.domNode);
			
			this.model = new Model();
			this.view = new View({
				model:this.model, 
				parentNode : this.domNode,
				textarea : this.textarea
			});
			
			// chrome
			on(textarea, "textInput", lang.hitch(this,this._onTextInput));
			// firefox
			on(textarea, "input", function(e){
				//console.log(e);
			});
			
			on(textarea, "keydown", lang.hitch(this,function(e){
				if(e.keyCode === keys.LEFT_ARROW){
					this._moveLeft();
				}else if(e.keyCode === keys.RIGHT_ARROW){
					this._moveRight();
				}else if(e.keyCode === keys.UP_ARROW){
					this._moveUp();
				}else if(e.keyCode === keys.DOWN_ARROW){
					this._moveDown();
				}
				// event.stop(e);
			}));
		},
		
		_moveLeft : function(){
			
		},
		
		_moveRight : function(){
			
		},
		
		_moveUp : function(){
			
		},
		
		_moveDown : function(){
			
		},
		
		_onTextInput: function(e){
			var textarea = this.textarea;
			var model = this.model;
			
			// 对输入的内容进行拦截，判断是否有推荐的可选项。
			
			// 当model的内容发生变化时，View自动更新,所以这里不写View相关的代码
			model.setData(e.data);
			
			setTimeout(function() {
				textarea.value = "";
		    });
		}
	});
	
});