define(["dojo/_base/declare",
        "dojo/on",
        "dojo/_base/event",
        "dijit/_WidgetBase",
        "dojo/dom-construct"],function(
        		 declare,
        		 on,
        		 event,
        		 _WidgetBase,
        		 domConstruct){
	
	return declare("drip.Editor",[_WidgetBase],{
		
		postCreate : function(){
			// 创建一个文本框
			var editorDiv = domConstruct.create("div",{style:{height:"100%"}}, this.domNode);
			var textarea = domConstruct.create("textarea",null, this.domNode);
			
			on(editorDiv, "mousedown",function(e){
				setTimeout(function() {
					 textarea.focus();
			    });
			});
			
			// chrome
			on(textarea, "textInput", function(e){
				var original = editorDiv.innerHTML;
				editorDiv.innerHTML = original + e.data;
				setTimeout(function() {
					textarea.value = "";
			    });
			});
			
			// firefox
			on(textarea, "input", function(e){
				console.log(e);
			});
		}
		
	});
	
});