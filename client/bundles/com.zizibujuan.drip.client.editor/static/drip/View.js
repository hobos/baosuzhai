define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/dom-construct",
        "dojo/on",
        "dojo/aspect",
        "drip/Model",
        "drip/layer/Cursor"],function(
		declare,
		lang,
		domConstruct,
		on,
		aspect,
		Model,
		Cursor){
	
	return declare("drip.View",null,{
		model : null,
		editorDiv : null,
		parentNode : null,
		textarea : null,
		
		constructor: function(options){
			lang.mixin(this, options);
			// 创建一个div容器，然后其中按照垂直层次，罗列各div
			var editorDiv = this.editorDiv = domConstruct.create("div",{style:{height:"100%",width:"100%", position:"absolute"}}, this.parentNode);
			// 内容层
			var textLayer = this.textLayer = domConstruct.create("div",{"class":"drip_layer"}, editorDiv);
			// 光标层， 看是否需要把光标放到光标层中
			var cursor = this.cursor = new Cursor({parentEl:editorDiv});
			
			var textarea = this.textarea;
			on(editorDiv, "mousedown",function(e){
				setTimeout(function() {
					 textarea.focus();
					 cursor.show();
			    });
			});
			
			aspect.after(this.model, "onChange", lang.hitch(this,this._onChange));
		},
		
		_onChange : function(){
			this.textLayer.innerHTML = this.model.getHTML();
			var top = 0;
			// TODO:这里需要一个根据model中的数据映射到浏览器中的dom节点
			var left = this.textLayer.firstChild.firstChild.offsetWidth;
			this.cursor.move(top,left);//如果传入参数，可能会弄错顺序；直接传入对象，可避免这个错误。
		}
		
	});
	
});