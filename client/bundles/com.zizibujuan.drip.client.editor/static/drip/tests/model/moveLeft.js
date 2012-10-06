define([ "doh", "drip/Model" ], function(doh, Model) {
	doh.register("Model.moveLeft", [ {
		name:"model中没有任何内容时，什么也不做",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.moveLeft();
			t.is("line", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
		},
		tearDown: function(){
			
		}
	},{
		name: "在model中的text节点中往左移动",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.setData({data:"你我他"});
			
			var node1 = model.getFocusNode();
			var offset1 = model.getOffset();
			t.is(3, offset1);
			
			model.moveLeft();
			var node2 = model.getFocusNode();
			var offset2 = model.getOffset();
			t.is(2, offset2);
			t.is(node1, node2);
			
			model.moveLeft();
			var node3 = model.getFocusNode();
			var offset3 = model.getOffset();
			t.is(1, offset3);
			t.is(node1, node3);
			
			model.moveLeft();
			var node4 = model.getFocusNode();
			var offset4 = model.getOffset();
			t.is(0, offset4);
			t.is(node1, node4);
			
			model.moveLeft();
			var node5 = model.getFocusNode();
			var offset5 = model.getOffset();
			t.is(0, offset5);
			t.is(node1, node4);
		},
		tearDown: function(){
			
		}
	},{
		name: "mn节点向text节点移动",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.setData({data: "我"});
			model.setData({data: "1"});
			// TODO：在setData时，需要考虑所处的位置，是不是在text和math节点之间。
			model.moveLeft();
			// 在设置聚焦节点和聚焦位置遵循就近原则。
			t.is("mn", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
			model.moveLeft();
			t.is("text", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
		},
		tearDown: function(){
			
		}
	},{
		name: "text节点向mn节点移动",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.setData({data: "1"});
			model.setData({data: "我"});
			model.moveLeft();
			t.is("text", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
			model.moveLeft();
			t.is("mn", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
			model.moveLeft();
			t.is("mn", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
		},
		tearDown: function(){
			
		}
	},{
		name: "在math中的不同token节点之间移动",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.setData({data:"11"});
			model.setData({data:"+"});
			model.moveLeft();
			t.is("mo", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
			model.moveLeft();
			t.is("mn", model.getFocusNode().nodeName);
			t.is(1, model.getOffset());
			model.moveLeft();
			t.is("mn", model.getFocusNode().nodeName);
			t.is(0, model.getOffset());
		},
		tearDown: function(){
			
		}
	},{
		name: "从第二个空的line节点移动到第一个line节点的末尾",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.setData({data:"中"});
			model.setData({data:"\n"});
			model.moveLeft();
			t.is("text", model.getFocusNode().nodeName);
			t.is(1, model.getOffset());
			model.clear();
			
			model.setData({data:"1"});
			model.setData({data:"\n"});
			model.moveLeft();
			t.is("mn", model.getFocusNode().nodeName);
			t.is(1, model.getOffset());
		},
		tearDown: function(){
			
		}
	},{
		name: "从第二个非空的line节点的最前面移动到第一个line节点的末尾",
		setUp: function(){
			this.model = new Model({});
		},
		runTest: function(t){
			var model = this.model;
			model.setData({data:"中"});
			model.setData({data:"\n"});
			model.setData({data:"文"});
			
			model.moveLeft();
			model.moveLeft();
			t.is("中",model.getFocusNode().textContent);
			t.is("text",model.getFocusNode().nodeName);
			t.is(1, model.getOffset());
			model.clear();
			
			model.setData({data:"1"});
			model.setData({data:"\n"});
			model.setData({data:"文"});
			model.moveLeft();
			model.moveLeft();
			t.is("1",model.getFocusNode().textContent);
			t.is("mn",model.getFocusNode().nodeName);
			t.is(1, model.getOffset());
			
		},
		tearDown: function(){
			
		}
	} ]);
});

// 行之间的移动