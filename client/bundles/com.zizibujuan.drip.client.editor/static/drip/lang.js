define({

	isNumber: function(obj) {
		return !isNaN(parseFloat(obj)) && isFinite(obj);
	},
	
	isOperator: function(obj){
		if(obj == "+" || obj == "=" || obj == "-" || obj == "&#xD7;"/*乘*/ || obj == "&#xF7;"/*除*/){
			return true;
		}
		return false;
	},
	
	isNewLine: function(obj){
		return obj === "\n";
	},
	
	isTab: function(obj){
		return obj === "\t";
	},
	
	insertNodeAfter: function(newNode, existingNode){
		var parentNode = existingNode.parentNode;
		if(parentNode.lastChild == existingNode){
			parentNode.appendChild(newNode)
		}else{
			parentNode.insertBefore(newNode, existingNode.nextSibling);
		}
	},
	
	_fontStyles: {
			fontFamily : 1,
			fontSize : 1,
			fontWeight : 1,
			fontStyle : 1,
			lineHeight : 1
	},
		
	measureTextSize: function(elem ,text) {
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

});