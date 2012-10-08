define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/dom-construct",
        "dojo/on",
        "dojo/aspect",
        "drip/Model",
        "drip/layer/Cursor"],function(
		declare,
		lang,
		array,
		domConstruct,
		on,
		aspect,
		Model,
		Cursor){
	
	var ELEMENT = 1, TEXT = 3;
	
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
			
			// 初始化视图
			textLayer.innerHTML = this.model.getHTML();
			aspect.after(this.model, "onChange", lang.hitch(this,this._onChange));
		},
		
		_onChange : function(){
			this.textLayer.innerHTML = this.model.getHTML();
			MathJax.Hub.Queue(["Typeset",MathJax.Hub, this.textLayer]);
			// 因为是异步操作，需要把显示光标的方法放在MathJax的异步函数中。
			MathJax.Hub.Queue(lang.hitch(this,this._showCursor));
			var top = 0;
			// TODO:这里需要一个根据model中的数据映射到浏览器中的dom节点
			
			//this.cursor.move(top,left);//如果传入参数，可能会弄错顺序；直接传入对象，可避免这个错误。
		},
		
		_showCursor: function(){
			console.log("Typeset完成后执行此方法");
			var cursorConfig = this._getCursorConfig();
			this.cursor.move(cursorConfig);
		},
		
		_getFocusInfo: function(){
			// summary:
			//		使用节点和节点中的值的偏移量来表示光标位置
			
			var pathes = this.model.path;// TODO:重构，想个更好的方法名，getPath已经被使用。
			
			var focusDomNode = this.textLayer;
			array.forEach(pathes, function(path, index){
				// 移除root
				if(path.nodeName != "root"){
					console.log(path);
					focusDomNode = focusDomNode.childNodes[path.offset - 1];
				}
			});
			return {node:focusDomNode, offset:this.model.getOffset()};
		},
		
		_getCursorConfig: function(){
			// summary:
			//		使用坐标值表示光标的位置。
			//		使用_getCursorPosition获取的信息进行计算。
			
			var top = 0, left = 0, height = 0, width = 0;
			var focusInfo = this._getFocusInfo();
			var node = focusInfo.node;
			var offset = focusInfo.offset;
			
			debugger;
			
			top = node.offsetTop;
			left = node.offsetLeft;
			//left += 字节点的宽度
			if(node.nodeType == ELEMENT){
				if(node.nodeName.toLowerCase() == "span"){
					left += node.offsetWidth;
				}
			}else if(node.nodeType == TEXT){
				
			}
			
			//left = node.offsetWidth;
			//var left = this.textLayer.firstChild.firstChild.offsetWidth;
			
			
			return {top:top,left:left,height:height, width:width};
		}
		
	});
	
});