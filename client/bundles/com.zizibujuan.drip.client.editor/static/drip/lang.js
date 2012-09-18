define({

	isNumber : function(obj) {
		return !isNaN(parseFloat(obj)) && isFinite(obj);
	}

});