define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/event",
        "dijit/_WidgetBase",
        "dojo/on",
        "dojo/keys",
        "dojo/aspect",
        "dojo/dom-construct",
        "drip/Model",
        "drip/View",
        "drip/ContentAssist"],function(
        		 declare,
        		 lang,
        		 event,
        		 _WidgetBase,
        		 on,
        		 keys,
        		 aspect,
        		 domConstruct,
        		 Model,
        		 View,
        		 ContentAssist){
	
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
			
			var model = this.model = new Model();
			this.view = new View({
				model:this.model, 
				parentNode : this.domNode,
				textarea : this.textarea
			});
			
			var contentAssist = this.contentAssist = new ContentAssist({view:this.view});
			aspect.after(contentAssist,"apply", function(input, event){
				// FIXME:这里直接获取map值的逻辑不正确，如果处于cache状态，则不应该往model中
				// 输入值。
				model.setData({data:input});
				setTimeout(function() {
					textarea.value = "";
			    });
			},true);
			
			// chrome
			on(textarea, "textInput", lang.hitch(this,this._onTextInput));
			// firefox
			on(textarea, "input", function(e){
				//console.log(e);
			});
			
			on(textarea, "keydown", lang.hitch(this,function(e){
				console.log(e, e.keyCode);
				if(e.keyCode === keys.LEFT_ARROW){
					this._moveLeft();
				}else if(e.keyCode === keys.RIGHT_ARROW){
					this._moveRight();
				}else if(e.keyCode === keys.UP_ARROW){
					this._moveUp();
				}else if(e.keyCode === keys.DOWN_ARROW){
					this._moveDown();
				}else if(e.keyCode === keys.BACKSPACE){
					//this.model.removeLeft();
					this.model.doDelete(); // TODO:使用removeLeft代替doDelete
				}else if(e.altKey && e.keyCode === 191){
					// ALT+/ 弹出提示信息
					this.contentAssist.open();
					event.stop(e);
				}else if(e.keyCode === keys.ENTER){
					debugger;
					this.model.setData({data:"\n"});
					// 回车换行。
					event.stop(e);
				}
				
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
			var inputData = e.data;
			
			// TODO:重构，提出方法,放到contentAssist中，输入参数为e.data,返回值boolean
			debugger;
			var adviceData = this.contentAssist.show(inputData);
			if(adviceData != null){
				// 优先显示提示框中级别最高的数据。而不是直接输入的内容。
				inputData = adviceData;
			}
			// 对输入的内容进行拦截，判断是否有推荐的可选项。
			
			// 当model的内容发生变化时，View自动更新,所以这里不写View相关的代码
			var model = this.model;
			model.setData({data:inputData});
			
			var textarea = this.textarea;
			setTimeout(function() {
				textarea.value = "";
		    });
		}
	});
	
});