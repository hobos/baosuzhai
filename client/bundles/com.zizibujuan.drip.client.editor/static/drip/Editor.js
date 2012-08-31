define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/event",
        "dijit/_WidgetBase",
        "dojo/on",
        "dojo/dom-construct",
        "drip/Model",
        "drip/View"],function(
        		 declare,
        		 lang,
        		 event,
        		 _WidgetBase,
        		 on,
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
			return this.model.getData();
	    },
		
		postCreate : function(){
			var textarea = this.textarea = domConstruct.create("textarea",null, this.domNode);
			
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
		},
		
		
		
		_onTextInput :function(e){
			var textarea = this.textarea;
			var model = this.model;
			
			// 当model的内容发生变化时，View自动更新,所以这里不写View相关的代码
			model.setData(e.data);
			
			setTimeout(function() {
				textarea.value = "";
		    });
		}
	});
	
});