define([ "dojo/_base/declare",
         "dojo/_base/lang",
         "dojo/_base/array",
         "dojo/dom-construct",
         "dojox/xml/parser",
         "drip/string",
         "drip/dataUtil",
         "drip/lang"], function(
        		 declare,
        		 lang,
        		 array,
        		 domConstruct,
        		 xmlParser,
        		 dripString,
        		 dataUtil,
        		 dripLang) {
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
		
		_splitData: function(data){
			// summary:
			//		将传入的数据拆分为最小单元的html符号。
			//		dataArray的每个元素都只能看作一个字符。
			var len = data.length;
			var result = [];
			var index = 0;
			var append = false;
			var cache = "";
			for(var i = 0; i < len; i++){
				var char = data.charAt(i);
				if(char == "&"){
					append = true;
					cache = char;
				}else if(append && char == ";"){
					append = false;
					cache += char;
					result[index] = cache;
					index++;
					cache = "";
				}else{
					if(append){
						cache += char;
					}else{
						result[index] = data.charAt(i);
						index++;
					}
				}
			}
			return result;
		},
		
		// TODO：需要加入位置参数，指明在什么地方插入
		setData: function(data, nodeName){
			// summary:
			//		往model中插入数据。
			// data: String
			//		要插入的内容
			// nodeName：String
			//		将data作为什么节点插入，这个通常由人工选择，如果没有值，则系统自动判断。
			
			
			// 这里需要对data做一个加工，&#xD7;只能看作一个字符。
			var dataArray = this._splitData(data);
			// 走到这一步，dataArray的每个元素都只能看作一个字符
			
			var xmlDoc = this.doc;
			// 注意：把对数据类型的判断放在判断节点类型的外面。除非有充分的理由不要修改这个逻辑
			// 先循环字符，再判断当前要插入字符的环境。
			// data中可能包含多个字符，通过循环，单独处理每个字符
			array.forEach(dataArray,lang.hitch(this,function(eachData, index){

				var char = eachData;
				var node = this.cursorPosition.node;
				if(dripLang.isNumber(char)){
					if(this._isLineNode(node)){
						// FIXME：重构出一个方法
						var mathNode = xmlDoc.createElement("math");
						node.appendChild(mathNode);
						var mnNode = xmlDoc.createElement("mn");
						mathNode.appendChild(mnNode);
						this.cursorPosition.node = mnNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						this.path.push({nodeName:"math", offset:1});
						this.path.push({nodeName:"mn", offset:1});
					}else if(this._isTextNode(node)){
						// FIXME：重构出一个方法
						var mathNode = xmlDoc.createElement("math");
						// math应该放在textNode之后
						dripLang.insertNodeAfter(mathNode, node);
						var mnNode = xmlDoc.createElement("mn");
						mathNode.appendChild(mnNode);
						this.cursorPosition.node = mnNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						var pos = this.path.pop();
						this.path.push({nodeName:"math", offset:pos.offset+1});
						this.path.push({nodeName:"mn", offset:1});
					}else if(this._isMathTokenNode(node)){
						// FIXME：重构，可抽象出一个逻辑，期望新建的节点与当前节点的类型不同。
						//如果当前节点不是操作符节点，则新建一个操作符节点
						var node = this.cursorPosition.node;
						if(node.nodeName != "mn"){
							var mnNode = xmlDoc.createElement("mn");
							dripLang.insertNodeAfter(mnNode,node);
							
							this.cursorPosition.node = mnNode;
							this.cursorPosition.offset = 0;
							
							this._insertChar(char);
							
							var pos = this.path.pop();
							this.path.push({nodeName:"mn", offset:pos.offset+1});
						}else{
							this._insertChar(char);
						}
					}
				}else if(dripLang.isOperator(char)){
					if(this._isLineNode(node)){
						var mathNode = xmlDoc.createElement("math");
						node.appendChild(mathNode);
						var mnNode = xmlDoc.createElement("mo");
						mathNode.appendChild(mnNode);
						this.cursorPosition.node = mnNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						this.path.push({nodeName:"math", offset:1});
						this.path.push({nodeName:"mo", offset:1});
					}else if(this._isMathTokenNode(node)){
						//如果当前节点不是操作符节点，则新建一个操作符节点
						var node = this.cursorPosition.node;
						// 不论是不是mo节点，都单独新建，因此处理逻辑一样，就不再分开。
						var moNode = xmlDoc.createElement("mo");
						dripLang.insertNodeAfter(moNode,node);
						
						this.cursorPosition.node = moNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						var pos = this.path.pop();
						this.path.push({nodeName:"mo", offset:pos.offset+1});
					}else if(this._isTextNode(node)){
						// FIXME：重构出一个方法
						var mathNode = xmlDoc.createElement("math");
						// math应该放在textNode之后
						dripLang.insertNodeAfter(mathNode, node);
						var mnNode = xmlDoc.createElement("mo");
						mathNode.appendChild(mnNode);
						this.cursorPosition.node = mnNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						var pos = this.path.pop();
						this.path.push({nodeName:"math", offset:pos.offset+1});
						this.path.push({nodeName:"mo", offset:1});
					}
				}else if(dripLang.isNewLine(char)){
					// TODO:在指定位置新增一行
					// 暂时只实现了在最后一行新增
					var focusedLine = this._getFocusLine();
					// 新建一个空的line节点
					var newLineNode = this.doc.createElement("line");
					dripLang.insertNodeAfter(newLineNode, focusedLine);
					
					this.cursorPosition.node = newLineNode;
					this.cursorPosition.offset = 0;
					
					// 将之前缓存的上一行的信息都清除
					var pos = this.path.pop();
					while(pos.nodeName != "line"){
						pos = this.path.pop();
					}
					// 然后加入新行
					// FIXME：这里需要重构
					this.path.push({nodeName:"line", offset:pos.offset+1});
					
				}else{
					if(this._isLineNode(node)){
						var textSpanNode = xmlDoc.createElement("text");
						node.appendChild(textSpanNode);
						
						this.cursorPosition.node = textSpanNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						// 这里的offset是nodeName为text的节点在父节点中位置。
						this.path.push({nodeName:"text",offset:1});
					}else if(this._isTextNode(node)){
						this._insertChar(char);
					}else if(this._isMathTokenNode(node)){
						// 要往上移到math节点之外
						var pos = null;
						
						do{
							pos = this.path.pop();
						}while(pos && pos.nodeName != "math");
						
						var textSpanNode = xmlDoc.createElement("text");
						// 获取math节点，然后将新节点插入到math节点之后
						var mathNode = node;
						while(mathNode.nodeName != "math"){
							mathNode = mathNode.parentNode;
						}
						dripLang.insertNodeAfter(textSpanNode, mathNode);
						
						this.cursorPosition.node = textSpanNode;
						this.cursorPosition.offset = 0;
						
						this._insertChar(char);
						
						
						this.path.push({nodeName:"text", offset:pos.offset+1});
					}
				}
				// TODO:重构 moveNext
			}));
			
			this.onChange(data);
		},
		
		getLineCount: function(){
			return this.doc.documentElement.childNodes.length;
		},
		
		_getFocusLine: function(){
			// summary:
			//		当前节点往上追溯，获取nodeName为line的行
			
			var focusNode = this.getFocusNode();
			var lineNode = focusNode;
			while(lineNode && lineNode.nodeName != "line"){
				lineNode = lineNode.parentNode;
			}
			
			return lineNode;
		}, 
		
		_isLineNode: function(node){
			return node.nodeName == "line";
		},
		
		// TODO：起更好的名字，因为textNode容易与document中定义的Text类型的Node混淆
		_isTextNode: function(node){
			return node.nodeName == "text";
		},
		
		_isMathTokenNode: function(node){
			var isTokenNode = false;
			var nodeName = node.nodeName;
			var tokenNames = ["mi","mn","mo","mtext","mspace","ms"];
			array.forEach(tokenNames, function(name,index){
				if(nodeName == name){
					isTokenNode = true;
					return;
				}
			});
			return isTokenNode;
		},
		
		_insertChar: function(char){
			// summary:
			//		在聚焦的节点中，当前光标新的字符，并移动当前光标的位置。
			
			var offset = this.cursorPosition.offset;
			var oldText = this.cursorPosition.node.textContent;
			var newText = dripString.insertAtOffset(oldText, offset, char);
			this.cursorPosition.node.textContent = newText;
			// 这里输入的char，不管几个字符都当作一个长度处理。
			this.cursorPosition.offset += 1; // char.length
		},
		
		
		// 获取xml文件的字符串值。没有没有输入任何内容则返回空字符串。
		getXML: function(){
			var doc = this.doc;
			if(doc.firstChild.firstChild.childNodes.length == 0){
				return "";
			}
			
			return xmlParser.innerXML(this.doc);
		},
		
		// summary:
		//		获取当前获取焦点的节点相对于根节点的path值.
		//		注意，获取的值与xpath并不一致，这里只是将nodeName和offset用字符串形式表示出来。
		getPath: function(){
			var xpath = "";
			array.forEach(this.path, function(path, index){
				xpath += "/";
				xpath += path.nodeName;
				
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
		
		getLineAt: function(lineIndex){
			// summary: 
			//		获取行节点。
			// lineIndex: Number
			//		行节点的索引，从0开始
			
			return this.doc.documentElement.childNodes[lineIndex];
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