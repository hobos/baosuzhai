define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/dom",
        "dojo/dom-class",
        "dojo/dom-construct",
        "dojo/dom-geometry",
        "dojo/on",
        "dojo/aspect",
        "drip/Model",
        "drip/layer/Cursor",
        "drip/lang"],function(
		declare,
		lang,
		array,
		dom,
		domClass,
		domConstruct,
		domGeom,
		on,
		aspect,
		Model,
		Cursor,
		dripLang){
	
	var ELEMENT = 1, TEXT = 3;
	
	return declare("drip.View",null,{
		model : null,
		editorDiv : null,
		parentNode : null,
		textarea : null,
		
		readOnly: false, // 有时，编辑状态和只读状态显示的样式，是不一样的。暂时还没有区分对待。
		
		constructor: function(options){
			lang.mixin(this, options);
			// 创建一个div容器，然后其中按照垂直层次，罗列各div
			var editorDiv = this.editorDiv = domConstruct.create("div",{style:{height:"100%",width:"100%", position:"absolute"}}, this.parentNode);
			// 内容层
			var textLayer = this.textLayer = domConstruct.create("div",{"class":"drip_layer"}, editorDiv);
			this.textLayerPosition = domGeom.position(textLayer);
			// 光标层， 看是否需要把光标放到光标层中
			var cursor = this.cursor = new Cursor({parentEl:editorDiv});
			
			on(editorDiv, "mousedown",lang.hitch(this, this.focus));
			
			// 初始化视图
			textLayer.innerHTML = this.model.getHTML();
			aspect.after(this.model, "onChange", lang.hitch(this,this._onChange));
		},
		
		focus: function(){
			var textarea = this.textarea;
			var cursor = this.cursor;
			
			setTimeout(function() {
				 textarea.focus();
				 cursor.show();
		    });
		},
		
		_onChange : function(){
			this.textLayer.innerHTML = this.model.getHTML();
			MathJax.Hub.Queue(["Typeset",MathJax.Hub, this.textLayer]);
			// 因为是异步操作，需要把显示光标的方法放在MathJax的异步函数中。
			MathJax.Hub.Queue(lang.hitch(this,this.showCursor));
		},
		
		showCursor: function(){
			console.log("Typeset完成后执行此方法");
			console.log(this.editorDiv);
			var cursorConfig = this._getCursorConfig();
			this.cursor.move(cursorConfig);
		},
		
		moveLeft: function(){
			this.model.moveLeft();
			this.showCursor();
		},
		
		_getFocusInfo: function(){
			// summary:
			//		使用节点和节点中的值的偏移量来表示光标位置
			
			var pathes = this.model.path;// TODO:重构，想个更好的方法名，getPath已经被使用。
			
			var focusDomNode = this.textLayer;
			var elementJax = null;
			var mrowNode = null;
			// 如果是math节点，则需要先
			array.forEach(pathes, function(path, index){
				// 移除root
				if(path.nodeName == 'root')return;
				if(path.nodeName == "line"){
					focusDomNode = focusDomNode.childNodes[path.offset - 1];
				}else if(path.nodeName == "text" || path.nodeName == "math"){
					var childNodes = focusDomNode.childNodes;
					var filtered = array.filter(childNodes, function(node, i){
						return node.nodeName.toLowerCase() == 'span';
					});
					focusDomNode = filtered[path.offset - 1];
					// 如果是math，还需要继续往下找节点
					// 或者根据这个div找到script中的数据，来进行循环
					// 如果已经定位到设置的层级，但是发现是mrow，则需要继续往下走一步。
					if(path.nodeName == "math"){
						var scriptNode = focusDomNode.nextSibling;
						elementJax = scriptNode.MathJax.elementJax.root;
					}
				}else{
					if(elementJax){
						if(dripLang.isMathTokenName(path.nodeName)){
							elementJax = elementJax.data[0];
						}else{
							elementJax = elementJax.data[path.offset - 1];
						}
						focusDomNode = dom.byId("MathJax-Span-"+elementJax.spanID);
						
						if(domClass.contains(focusDomNode, "mstyle")){
							if(path != "mstyle"){
								elementJax = elementJax.data[path.offset - 1];
								focusDomNode = dom.byId("MathJax-Span-"+elementJax.spanID);
							}
						}else if(domClass.contains(focusDomNode, "mrow")){
							mrowNode = focusDomNode;
							if(path != "mrow"){
								elementJax = elementJax.data[path.offset - 1];
								focusDomNode = dom.byId("MathJax-Span-"+elementJax.spanID);
							}
						}
					}
				}
			});
			
			return {node:focusDomNode, offset:this.model.getOffset(), mrowNode:mrowNode};
		},
		
		_getCursorConfig: function(){
			// summary:
			//		使用坐标值表示光标的位置。
			//		使用_getCursorPosition获取的信息进行计算。
			
			var top = 0, left = 0, height = 0, width = 0;
			var focusInfo = this._getFocusInfo();
			var node = focusInfo.node;
			var offset = focusInfo.offset;
			
			var textLayerPosition = this.textLayerPosition;
			var mrowNode = focusInfo.mrowNode;
			if(mrowNode){
				var mrowPosition = domGeom.position(mrowNode);
				top = mrowPosition.y - textLayerPosition.y;
				height = mrowPosition.h;
				
				var position = domGeom.position(node);
				left = position.x - textLayerPosition.x;
			}else{
				var position = domGeom.position(node);
				top = position.y - textLayerPosition.y;
				height = position.h;
				left = position.x - textLayerPosition.x;
			}
			
			//left += 字节点的宽度
			if(node.nodeType == ELEMENT){
				var childNodes = node.childNodes;
				if(childNodes.length == 1 && childNodes[0].nodeType == TEXT){
					// 如果childNodes的长度不是1，则offset对应的必是这些字节点的偏移量，而不是文本的
					if(node.textContent.length == offset){
						left += node.offsetWidth;
					}else{
						// 测宽度
						var text = node.textContent.substring(0, offset);
						var width = dripLang.measureTextSize(node, text).width;
						left += width;
					}
				}else{
					
				}
			}
			return {top:top,left:left,height:height, width:width};
		},
		
		getCursorPosition: function(){
			// summary:
			//		相对浏览器视窗的左上角位置
			//		注意这里定位的是提示框弹出前的位置。用在更普遍的场合，是在执行其他操作前的光标位置。
			
			var textLayerPosition = this.textLayerPosition;
			var x = textLayerPosition.x;
			var y = textLayerPosition.y;
			var cursorConfig = this.cursor.getCursorConfig();
			x += cursorConfig.left;
			y += cursorConfig.top + cursorConfig.height;
			return {x:x, y:y};
		}
		
	});
	
});