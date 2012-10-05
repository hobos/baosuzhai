define([ "doh", "drip/mathContentAssist" ], function(doh, mathContentAssist) {
	doh.register("mathContentAssist",[{
		name:"getProposals",
		runTest: function(t){
			mathContentAssist.keywords = [];
			var result = mathContentAssist.getProposals("a");
			t.t(result.length == 0);
			
			mathContentAssist.keywords = [{
				input:"abc",
			},{
				input:"a12"
			},{
				input:"/"
			}];
			var result = mathContentAssist.getProposals("a");
			t.t(result.length == 2);
		}
	}]);
});