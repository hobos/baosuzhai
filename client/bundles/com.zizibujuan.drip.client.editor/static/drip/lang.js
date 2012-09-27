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
	}

});