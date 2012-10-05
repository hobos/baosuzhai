define(["doh/main", "require"], function(doh, require){
	if(doh.isBrowser){
		doh.register("tests.ContentAssist", require.toUrl("./ContentAssist.html"), 30000);
	}
});