define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/dom-construct",
        "dojo/on",
        "dojo/aspect",
        "drip/Model"],function(
		declare,
		lang,
		domConstruct,
		on,
		aspect,
		Model){
	
	return declare("drip.View",null,{
		model : null,
		editorDiv : null,
		parentNode : null,
		textarea : null,
		
		constructor: function(options){
			lang.mixin(this, options);
			// 创建一个文本框
			var editorDiv = this.editorDiv = domConstruct.create("div",{style:{height:"100%"}}, this.parentNode, "first");
			
			var textarea = this.textarea;
			on(editorDiv, "mousedown",function(e){
				setTimeout(function() {
					 textarea.focus();
			    });
			});
			
			aspect.after(this.model, "onChange", lang.hitch(this,this._onChange));
		},
		
		_onChange : function(){
			this.editorDiv.innerHTML = this.model.getHTML();
		}
		
	});
	
});