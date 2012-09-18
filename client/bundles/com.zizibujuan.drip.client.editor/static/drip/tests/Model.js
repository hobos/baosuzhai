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
				model.setData("a");
				// 需要阻断
				// 因为line节点中包含
				t.is("/root/line[1]/text[1]", model.getPath());
				var node = getNodeByXPath(model.getPath(), model.doc);
				t.is(model.getFocusNode(), node);
				t.is(2, model.getOffset());
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
				debugger;
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