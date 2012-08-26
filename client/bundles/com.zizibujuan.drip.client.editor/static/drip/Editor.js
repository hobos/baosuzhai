define(["dojo/_base/declare",
         "dijit/_WidgetBase"],function(
        		 declare,
        		 _WidgetBase){
	
	return declare("drip.Editor",[_WidgetBase],{
		
		postCreate : function(){
			this.domNode.innerHTML = "I'm editor";
		}
		
	});
	
});