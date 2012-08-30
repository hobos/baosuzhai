define([ "dojo/_base/declare",
         "dojo/_base/lang",
         "dojo/_base/array",
         "dojo/dom-construct",
         "dojox/xml/parser"], function(
        		 declare,
        		 lang,
        		 array,
        		 domConstruct,
        		 xmlParser) {

	return declare("drip.Model",null,{
	
		doc : null,
		
		// 当前节点所在的位置
		position : {node:null, offset : -1},
		
		// 调用顺序
		//		如果是新建，则直接new即可
		//		如果已存在内容，则先新建，然后通过loadData，加载内容
		constructor: function(options){
			lang.mixin(this, options);
			this.doc = xmlParser.parse("<root><line></line></root>");
			this.position.node = this.doc.documentElement.firstChild;
			this.position.offset = 0;
		},
		
		// 如果没有内容，则创建一个新行
		// 如果存在内容，则加载内容，并将光标移到最后
		loadData : function(xmlString){
			
		},
		
		
		
		setData : function(data){
			var node = this.position.node;
			var offset = this.position.offset;
			
			var oldText = this.position.node.textContent;
			var newText = this._insertAtOffset(oldText, offset, data);
			
			this.position.offset += data.length;
			node.textContent = newText;
			
			this.onChange(data);
		},
		
		// 习题 line
		getData : function(){
			var xmlString = "";
			var root = this.doc.documentElement;
			var lines = root.childNodes;
			array.forEach(lines, function(line, index){
				var lineString = "<div>"+line.textContent+"</div>";
				xmlString += lineString;
			});
			return xmlString;
		},
		
		onChange : function(data){
			// 什么也不做，View在该方法执行完毕后，执行刷新操作
		},
		
		// FIXME：放到相应的js文件中
		_insertAtOffset : function(target, offset, source){
			var len = target.length;
			if(offset < 0 || len < offset) return target;		
			return target.substring(0,offset)+source+target.substring(offset);
		}
	
	});
	
});