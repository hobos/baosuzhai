define([ "doh", "drip/string" ], function(doh, dripString) {

	doh.register("string 工具类", [ {
		name : "insertAtOffset 插入字符",
		runTest : function(t) {
			t.is("1a",dripString.insertAtOffset("1",1,"a"));
			t.is("a1",dripString.insertAtOffset("1",0,"a"));
			t.is("1a2",dripString.insertAtOffset("12",1,"a"));
		},
	},{
		name : "insertAtOffset 替换字符",
		runTest : function(t) {
			t.is("",dripString.insertAtOffset("12",2,"",2));
			t.is("123",dripString.insertAtOffset("1a3",2,"2",1));
		},
	}]);

});