define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/request/xhr",
        "dijit/_WidgetBase",
        "dijit/form/DropDownButton", 
        "dijit/DropDownMenu", 
        "dijit/MenuItem",
        "dijit/_TemplatedMixin",
        "dojo/text!/templates/Header.html",
        ], function(
        		declare,
        		lang,
        		xhr,
        		_WidgetBase,
	       		DropDownButton,
	       		DropDownMenu,
	       		MenuItem,
        		_TemplatedMixin,
        		headerTemplate) {

	return declare("Header", [_WidgetBase,_TemplatedMixin], {
		templateString: headerTemplate,
		
		postCreate : function(){
			
			xhr("/login/",{method:"POST", handleAs:"json"}).then(lang.hitch(this,function(response){
				var menu = new DropDownMenu({ style: "display: none;"});
		        var menuSetting = new MenuItem({
		            label: "设置"
		        });
		        menu.addChild(menuSetting);

		        var menuLogout = new MenuItem({
		            label: "注销"
		        });
		        menu.addChild(menuLogout);

		        var button = new DropDownButton({
		            label: response.realName,
		            dropDown: menu
		        });
		        this.userMenu.appendChild(button.domNode);
		        
		        menuSetting.on("click", function(e){
		        	window.location = "/settings";
		        });
		        
		        menuLogout.on("click", function(e){
		        	xhr("/logout/",{method:"POST", handleAs:"json"}).then(function(data){
		        		// window.location = "/"
		        	},function(error){
		        		window.location = "/";
		        	});
		        });
				
			}),function(error){
				window.location = "/";
			});
		}
	});
});