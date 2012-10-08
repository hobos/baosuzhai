define(["doh/main", "require"], function(doh, require){
	if(doh.isBrowser){
		doh.register("tests.View", require.toUrl("./View.html"), 30000);
	}
});
