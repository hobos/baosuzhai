define([ "dojo/_base/declare",
         "dojo/_base/lang",
         "dojo/_base/array",
         "dojo/dom-construct",
         "dojox/xml/parser",
         "drip/string",
         "drip/dataUtil"], function(
        		 declare,
        		 lang,
        		 array,
        		 domConstruct,
        		 xmlParser,
        		 dripString,
        		 dataUtil) {

	return declare("drip.Model",null,{
	
		doc: null,
		
		// summary:
		//		当前光标所在的位置。
		//		lineIndex和nodeIndex只定位到text和math节点。FIXME：math中的内容不再跟踪？
		//		还是使用xPath Axes(轴)来定位好一些。
		//		在一个数组中pop或push每个节点的名称，然后形成一个xpath，然后根据xpath定位到该节点。
		//		又能如何呢？还是无法将model与view中的位置相对应。
		// node:
		//		光标所在的节点
		// offset：
		//		光标在node节点中的偏移量，主要是node的子节点或者文本节点内容的偏移量
		// lineIndex：
		//		附加信息，主要用于定位node。
		//		node节点在doc中行数
		// nodeIndex：
		//		附加信息，主要用于定位node。
		//		node在lineIndex指定的行中的索引
		cursorPosition: {node:null, offset : -1, lineIndex:0, nodeIndex:0},
		
		// 当前节点在xml文件中的具体路径
		path: [],
		
		// 调用顺序
		//		如果是新建，则直接new即可
		//		如果已存在内容，则先新建，然后通过loadData，加载内容
		//		因为包含普通文本和math文本，使用span包含普通文本
		//		<root><line><text></text><math></math></line></root>
		constructor: function(options){
			lang.mixin(this, options);
			this.doc = xmlParser.parse("<root><line></line></root>");
			
			this.cursorPosition.node = this.doc.documentElement.firstChild;
			this.cursorPosition.offset = 0;
			
		},
		
		// 如果没有内容，则创建一个新行
		// 如果存在内容，则加载内容，并将光标移到最后
		loadData: function(xmlString){
			
		},
		
		setData: function(data){
			var node = this.cursorPosition.node;
			
			var xmlDoc = this.doc;
			
			if(node.nodeName == "line"){
				var textSpanNode = xmlDoc.createElement("text");
				node.appendChild(textSpanNode);
				
				this.cursorPosition.node = textSpanNode;
				this.cursorPosition.offset = 0;
			}
			
			var offset = this.cursorPosition.offset;
			var oldText = this.cursorPosition.node.textContent;
			var newText = dripString.insertAtOffset(oldText, offset, data);
			
			this.cursorPosition.offset += data.length;
			this.cursorPosition.node.textContent = newText;
			
			this.onChange(data);
		},
		
		// 获取xml文件的字符串值
		getData: function(){
			return xmlParser.innerXML(this.doc);
		},
		
		// 习题 line 获取html格式的数据
		//		展示页面时使用
		getHTML: function(){
			return dataUtil.xmlDocToHtml(this.doc);
		},
		
		
		
		onChange: function(data){
			// 什么也不做，View在该方法执行完毕后，执行刷新操作
		}
	
	});
	
});