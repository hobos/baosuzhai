define(["dojo/_base/array"],function(array){

	var lang = {};
	
	lang.isNumber = function(obj) {
		return !isNaN(parseFloat(obj)) && isFinite(obj);
	},
	
	lang.isOperator = function(obj){
		if(obj == "+" || obj == "=" || obj == "-" || obj == "&#xD7;"/*乘*/ || obj == "&#xF7;"/*除*/){
			return true;
		}
		return false;
	},
	
	lang.isNewLine = function(obj){
		return obj === "\n";
	},
	
	lang.isTab = function(obj){
		return obj === "\t";
	},
	
	lang.insertNodeAfter = function(newNode, existingNode){
		var parentNode = existingNode.parentNode;
		if(parentNode.lastChild == existingNode){
			parentNode.appendChild(newNode)
		}else{
			parentNode.insertBefore(newNode, existingNode.nextSibling);
		}
	},
	
	lang._fontStyles = {
			fontFamily : 1,
			fontSize : 1,
			fontWeight : 1,
			fontStyle : 1,
			lineHeight : 1
	},
	
	lang.isMathTokenNode = function(node){
		var nodeName = node.nodeName;
		return this.isMathTokenName(nodeName);
	},
	
	lang.isMathTokenName = function(nodeName){
		var isTokenNode = false;
		
		var tokenNames = ["mi","mn","mo","mtext","mspace","ms"];
		array.forEach(tokenNames, function(name,index){
			if(nodeName == name){
				isTokenNode = true;
				return;
			}
		});
		return isTokenNode;
	},
		
	lang.measureTextSize = function(elem ,text) {
		if (!this.measureNode) {
			var _measureNode = document.createElement("div");
			var style = _measureNode.style;
			style.width = style.height = "auto";
			style.left = style.top = "-1000px";

			style.visibility = "hidden";
			style.position = "absolute";
			style.overflow = "visible";
			style.whiteSpace = "nowrap";
			document.body.appendChild(_measureNode);
			this.measureNode = _measureNode;
		}
		var measureNode = this.measureNode;
		
		measureNode.innerHTML = text;
		var style = measureNode.style;
		var computedStyle = window.getComputedStyle(elem, null);
		for ( var prop in this._fontStyles) {
			style[prop] = computedStyle[prop];
		}

		var size = {
			height : measureNode.offsetHeight,
			width : measureNode.offsetWidth
		};
		return size;
	}

	return lang;
});