define([ "doh", "drip/lang" ], function(doh, dripLang) {

	doh.register("isNumber", [ {
		name : "校验输入的内容是不是数字",

		runTest : function(t) {
			t.t(dripLang.isNumber(0));
			t.t(dripLang.isNumber(9));
			t.t(dripLang.isNumber(09));
			t.t(dripLang.isNumber(90));
			t.t(dripLang.isNumber("1"));
			
			t.f(dripLang.isNumber(""));
			t.f(dripLang.isNumber(" "));
			t.f(dripLang.isNumber("a"));
			
		},
	} ]);

});