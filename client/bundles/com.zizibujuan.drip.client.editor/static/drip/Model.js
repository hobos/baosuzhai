define([ "dojo/_base/declare",
         "dojo/_base/lang"], function(
        		 declare,
        		 lang) {

	return declare("drip.Model",null,{
		
		text : "",
		
		constructor: function(options){
			lang.mixin(this, options);
		},
		
		setData : function(data){
			this.text += data;
			this.onChange(data);
		},
		
		getData : function(){
			return this.text;
		},
		
		onChange : function(data){
			// 什么也不做，View在该方法执行完毕后，执行刷新操作
		}
	
	});
	
});