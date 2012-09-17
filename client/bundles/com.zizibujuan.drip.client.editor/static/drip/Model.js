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
	var EMPTY_XML = "<root><line></line></root>";
	return declare(null,{
		path: null,
		xmlString: null,
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
		//node:null, offset : -1, lineIndex:0, nodeIndex:0
		cursorPosition: null,
		
		// 当前节点在xml文件中的具体路径
		
		
		// 调用顺序
		//		如果是新建，则直接new即可
		//		如果已存在内容，则先新建，然后通过loadData，加载内容
		//		因为包含普通文本和math文本，使用span包含普通文本
		//		<root><line><text></text><math></math></line></root>
		//		在构造函数中初始化到位，如果有传入xml文本，则读取文本进行初始化；
		//		如果什么也没有传，则初始化为默认值。
		constructor: function(options){
			// 注意：在类中列出的属性，都必须在这里进行初始化。
			this.doc = xmlParser.parse(EMPTY_XML);
			this.path = [];
			this.cursorPosition = {};
			// FIXME:如何存储呢？
			
			this.cursorPosition.node = this.doc.documentElement.firstChild;
			this.cursorPosition.offset = 0;
			
			this.path.push({nodeName:"root"});
			// offset 偏移量，从1开始
			this.path.push({nodeName:"line", offset:1});
			
			lang.mixin(this, options);
			
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
				// 这里的offset是nodeName为text的节点在父节点中位置。
				this.path.push({nodeName:"text",offset:1});
			}
			
			var offset = this.cursorPosition.offset;
			var oldText = this.cursorPosition.node.textContent;
			var newText = dripString.insertAtOffset(oldText, offset, data);
			
			this.cursorPosition.offset += data.length;
			this.cursorPosition.node.textContent = newText;
			
			this.onChange(data);
		},
		
		// 获取xml文件的字符串值。没有没有输入任何内容则返回空字符串。
		getXML: function(){
			var doc = this.doc;
			if(doc.firstChild.firstChild.childNodes.length == 0){
				return "";
			}
			
			return xmlParser.innerXML(this.doc);
		},
		
		// 获取当前获取焦点的节点相对于根节点的xpath值
		getXPath: function(){
			var xpath = "";
			array.forEach(this.path, function(path, index){
				xpath += "/";
				if(path.nodeName != "text" && path.nodeName != "math"){
					xpath += path.nodeName;
				}else{
					xpath += "*";
				}
				
				if(path.offset){
					xpath += "[" + path.offset + "]";
				}
			});
			return xpath;
		},
		
		getFocusNode: function(){
			return this.cursorPosition.node;
		},
		
		getOffset: function(){
			return this.cursorPosition.offset;
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