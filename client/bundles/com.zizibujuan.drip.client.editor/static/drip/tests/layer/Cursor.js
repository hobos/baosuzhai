define(["doh/main", "require"], function(doh, require){
	if(doh.isBrowser){
		doh.register("tests.Cursor", require.toUrl("./Cursor.html"), 30000);
	}
});
