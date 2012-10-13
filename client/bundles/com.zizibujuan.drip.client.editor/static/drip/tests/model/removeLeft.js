define([ "doh", "drip/Model" ], function(doh, Model) {
	doh.register("Model.removeLeft", [
	    {
			name: "当没有任何内容时，removeLeft不起任何作用",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				var removed = model.removeLeft();
				t.is("", removed);
				t.is("line",model.getFocusNode().nodeName);
				t.is(0, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当有一个text文本时，removeLeft后也删除text节点",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"a"});
				removed = model.removeLeft();
				t.is("a", removed);
				t.is("line", model.getFocusNode().nodeName);
				t.is(0, model.getOffset());
				
				// 同时从path也移除
				// 注意将删除节点和从path中移除聚焦的节点等信息都放在一起。
				t.is("/root/line[1]", model.getPath());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当text节点中有三个字符时，删除最后一个",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"你我他"});
				t.is("他", model.removeLeft());
				t.is("text", model.getFocusNode().nodeName);
				t.is(2, model.getOffset());
				model.clear();
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当text节点中有三个字符时，删除中间一个",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"你我他"});
				model.moveLeft();
				t.is("我", model.removeLeft());
				t.is("text", model.getFocusNode().nodeName);
				t.is("你他", model.getFocusNode().textContent);
				t.is(1, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当text节点中有三个字符时，删除最前一个",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"你我他"});
				model.moveLeft();
				model.moveLeft();
				t.is("你", model.removeLeft());
				t.is("text", model.getFocusNode().nodeName);
				t.is("我他", model.getFocusNode().textContent);
				t.is(0, model.getOffset());
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当在一个text节点后，有一个存储着1的math节点，在math节点后执行removeLeft后，text节点获取焦点",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"和"});
				model.setData({data:"1"});
				t.is("1", model.removeLeft());
				t.is("text", model.getFocusNode().nodeName);
				t.is("和", model.getFocusNode().textContent);
				t.is(1, model.getOffset());
				// 确保math节点被删除
				var line = model.getLineAt(0);
				t.is(1, line.childNodes.length);
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当一个text文本后，有math时，在text文本后执行removeLeft后，math获取焦点",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"和"});
				model.setData({data:"1"});
				model.moveLeft();
				t.is("和", model.removeLeft());
				t.is("mn", model.getFocusNode().nodeName);
				t.is("1", model.getFocusNode().textContent);
				t.is(0, model.getOffset());
				// 确保text节点被删除
				var line = model.getLineAt(0);
				t.is(1, line.childNodes.length);
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当有一个text节点，其后紧跟math节点，如果text节点中有两个字符，则text之后执行removeLeft后，就近text获取焦点。",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"你我"});
				model.setData({data:"1"});
				model.moveLeft();
				t.is("我", model.removeLeft());
				t.is("text", model.getFocusNode().nodeName);
				t.is("你", model.getFocusNode().textContent);
				t.is(1, model.getOffset());
				var line = model.getLineAt(0);
				t.is(2, line.childNodes.length);
			},
			tearDown: function(){
				
			}
		},
		{
			name: "当math节点中任何内容的token节点，只要其中没有内容，就删除节点，并让前一个节点获取焦点。",
			setUp: function(){
				this.model = new Model({});
			},
			runTest: function(t){
				var model = this.model;
				model.setData({data:"1"});
				model.setData({data:"+"});
				t.is("+",model.removeLeft());
				t.is("1", model.getFocusNode().textContent);
				t.is(1, model.getOffset());
				var line = model.getLineAt(0);
				t.is(1, line.firstChild.childNodes.length);
				// 测试，要同时修改path的值
				t.t(model.path.length == 4);
				t.t(model.path[3].nodeName=="mn");
			},
			tearDown: function(){
				
			}
		},
		
		// math节点与text节点类似，只要当前聚焦点前有大于等于两个字符的内容，就删除内容
		// 当math节点中如果只有一个token节点，执行删除后，math下面没有节点，则删除math节点
		// 当在第二行的起始位置时，removeLeft到第一行的最末尾
		//
		
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