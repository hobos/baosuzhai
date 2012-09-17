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
				t.is("/root/line[1]", model.getXPath());
				// 获取与该路径对应的节点
				var node = getNodeByXPath(model.getXPath(), model.doc);
				t.is(model.getFocusNode(), node);
				// 如果子节点是文本，则获取文本中的偏移量；
				// 如果子节点不是文本节点，则偏移量为0
				t.is(0, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "在model的末尾添加值",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				// 如果是中文，则放在text节点中
				model.setData("中文");
				
				// 因为line节点中包含
				t.is("/root/line[1]/*[1]", model.getXPath());
				var node = getNodeByXPath(model.getXPath(), model.doc);
				t.is(model.getFocusNode(), node);
				t.is(2, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
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