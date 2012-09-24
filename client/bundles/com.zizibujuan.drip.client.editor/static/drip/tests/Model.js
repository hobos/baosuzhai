define([ "doh","drip/Model" ], function(doh,Model) {
	
	function getNodeByXPath(xpath, node){
		var xpathResult = document.evaluate(xpath, node,null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		var node = xpathResult.iterateNext();
		return node;
	}
	
	doh.register("Model",[
		{
			name: "测试空的model",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				// 主要测试内容如下
				//		mode中的内容
				//		当前插入点的位置
				
				var model = this.model;
				// 如果没有内容，则返回空字符串，提交时可据此做必输校验。
				t.is("", model.getXML());
				t.is("/root/line[1]", model.getPath());
				// 获取与该路径对应的节点
				var node = getNodeByXPath(model.getPath(), model.doc);
				t.is(model.getFocusNode(), node);
				// 如果子节点是文本，则获取文本中的偏移量；
				// 如果子节点不是文本节点，则偏移量为0
				t.is(0, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "敲击回车插入空行, 使用\n表示换行，因此遇到keyCode为13时，转换为\n",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				// 遇到如回车符号或者换行符号这类特殊的字符时，使用转义字符表示。
				model.setData("\n");
				t.t(model.getLineCount() == 2);
				// 如果刚开始什么都不输入，则只插入一个空的line节点，不预插入text节点
				t.is("/root/line[2]", model.getPath());
				// 测试用例需要确定，这个节点不是第一个节点
				t.is(model.getFocusNode(), model.getLineAt(0));
				t.is(model.getFocusNode().nodeName, "line");
				t.is(0, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在空的model中，敲击tab键盘之后插入制表符号，使用\t表示制表符，keyCode为9",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData("\t");
				// 制表符在model中用什么节点表示呢， 制表符用4个空格表示，还是8个空格？
				// 在model中直接用\t表示，但是在界面上进行绘制的时候，需要使用span节点封装。
				// 并在其中放置指定个数的空格。
				
				// 暂不支持在math模式下输入制表符号
				t.is("/root/line[1]/text[1]", model.getPath());
				t.is(model.getFocusNode().nodeName,"text");
				t.is(1,model.getOffset()); // 在model中任何用转移符号表示的字符的长度都为1
				// 判断插入的值
				t.is("\t",model.getFocusNode().textContent);
			},
			tearDown: function(){
				
			}
		},
		// 纯输入中文，不换行
		{
			name: "在空的model中添加中文",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				// 如果是中文，则放在text节点中
				model.setData("中文"); // 注意中文可以一次插入多个字
				
				// 因为line节点中包含
				t.is("/root/line[1]/text[1]", model.getPath());
				var node = getNodeByXPath(model.getPath(), model.doc);
				t.is(model.getFocusNode(), node);
				t.is(2, model.getOffset());
				t.is("text", node.nodeName);
				
				// 追加中文
				model.setData("一");
				t.is("/root/line[1]/text[1]", model.getPath());
				t.is(model.getFocusNode().nodeName, "text");
				t.is(3, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在空的model中添加数值",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				// 如果是中文，则放在text节点中
				model.setData("1"); // 数字一次可以插入1个，但都当多个处理
				
				// 因为line节点中包含
				t.is("/root/line[1]/math[1]/mn[1]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mn");
				t.is(1, model.getOffset());
				
				// 追加数值
				model.setData("2");
				t.is("/root/line[1]/math[1]/mn[1]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mn");
				t.is(2, model.getOffset());
				t.is("12", model.getFocusNode().textContent);
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在空的model中添加英文字母",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				// 如果是中文，则放在text节点中
				// 注意，当调用setData的时候，所有数据都是已经处理好的。
				// 两种判断数据类型的方法：1是系统自动判断；2是人工判断
				// 所以setData应该再加一个参数，表示人工判断的结果，表名数据是什么类型。
				// 如果没有这个参数，则系统自动判断
				model.setData("a");
				// 需要阻断
				// 因为line节点中包含
				t.is("/root/line[1]/text[1]", model.getPath());
				var node = getNodeByXPath(model.getPath(), model.doc);
				t.is(model.getFocusNode(), node);
				t.is(1, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在已有一个中文字符的model中添加数字",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				// 结果是在line中先加一个text节点，然后再加一个math节点
				var model = this.model;
				// 如果是中文，则放在text节点中
				model.setData("中");
				model.setData("1");
				t.is("/root/line[1]/math[2]/mn[1]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mn");
				t.is(1, model.getOffset());
				t.is(2, model.getLineAt(0).childNodes.length);
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在已有一个数字的model中添加中文",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				// 结果是在line中先加一个text节点，然后再加一个math节点
				var model = this.model;
				// 如果是中文，则放在text节点中
				model.setData("1");
				model.setData("中");
				t.is("/root/line[1]/text[2]", model.getPath());
				t.is(model.getFocusNode().nodeName, "text");
				t.is(1, model.getOffset());
				// 确认text没有被放在math节点中
				t.is(2, model.getLineAt(0).childNodes.length);
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在一个空的model中输入1+1=2",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				// 结果是在line中先加一个text节点，然后再加一个math节点
				var model = this.model;
				// 如果是中文，则放在text节点中
				model.setData("1");
				t.is("/root/line[1]/math[1]/mn[1]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mn");
				t.is(1, model.getOffset());
				model.setData("+");
				t.is("/root/line[1]/math[1]/mo[2]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mo");
				t.is(1, model.getOffset());
				model.setData("1");
				t.is("/root/line[1]/math[1]/mn[3]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mn");
				t.is(1, model.getOffset());
				model.setData("=");
				t.is("/root/line[1]/math[1]/mo[4]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mo");
				t.is(1, model.getOffset());
				model.setData("2");
				t.is("/root/line[1]/math[1]/mn[5]", model.getPath());
				t.is(model.getFocusNode().nodeName, "mn");
				t.is(1, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		
		
		// TODO:测试删除，测试替换，其实删除是一种特殊的替换，是用空字符串替换掉选中的文本。
		// TODO:如果是数学用的表达式，则在弹出的框中给出提示，如“math”，“数”或用更好看的图片
		// 在model开始处添加值
		// 在model的中间行的开始处添加值
		// 在model的中间行的中间处添加值
		// 在model的中间行的结尾处添加值
		// 根据当前插入点获取节点信息
		// 根据当前插入点获取其余XXXXXX信息
		// 每种移动都有多种临界点，都需要测试
		{
			name: "在model中右移",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在model中左移",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在model中上移",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在model中下移",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				
			},
			tearDown: function(){
				
			}
		}
	]);
	
});