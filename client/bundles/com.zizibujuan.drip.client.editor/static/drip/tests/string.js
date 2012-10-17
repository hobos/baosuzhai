define([ "doh", "drip/string" ], function(doh, dripString) {

	doh.register("string 工具类", [ {
		name: "insertAtOffset 插入字符",
		runTest: function(t) {
			t.is("1a",dripString.insertAtOffset("1",1,"a"));
			t.is("a1",dripString.insertAtOffset("1",0,"a"));
			t.is("1a2",dripString.insertAtOffset("12",1,"a"));
		},
	},{
		name: "insertAtOffset 替换字符",
		runTest: function(t) {
			t.is("",dripString.insertAtOffset("12",2,"",2));
			t.is("123",dripString.insertAtOffset("1a3",2,"2",1));
		},
	},
	{
		name: "拆分字符",
		runTest: function(t){
			var dataArray = dripString.splitData("你好");
			t.is("你",dataArray[0]);
			t.is("好",dataArray[1]);
			
			dataArray = dripString.splitData("&1;");
			t.is("&1;",dataArray[0]);
			t.t(dataArray.length == 1);
			
			// 如果&和;之间不存在任何值，则不做unicode处理
			dataArray = dripString.splitData("&;");
			t.is("&",dataArray[0]);
			t.is(";",dataArray[1]);
			t.t(dataArray.length == 2);
			
			
			dataArray = dripString.splitData("1&#xD7;2");
			t.is("1",dataArray[0]);
			t.is("&#xD7;",dataArray[1]);
			t.is("2",dataArray[2]);
			t.t(dataArray.length == 3);
		}
	},
	{
		name: "insertAtOffset 删除unicode字符",
		runTest: function(t){
			// t.is("",dripString.insertAtOffset("&#1;",1,"",1));
			// 因为现在unicode码都是存在mo节点中，在removeLeft中已经处理了删除unicode的逻辑，所以在
			// insertAtOffset中暂不做处理
		}
	}]);

});