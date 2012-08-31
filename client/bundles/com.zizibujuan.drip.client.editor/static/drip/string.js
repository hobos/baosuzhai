define({
	
	insertAtOffset : function(target, offset, source){
		// summary:
		//		在给定字符串的指定偏移量处插入字符串。
		// target: String
		//		目标字符串，会修改该字符串。
		// offset:
		//		偏移量，从0开始
		// source:
		//		需要插入到指定位置的字符串
		// returns: String
		//		返回新的字符串
		
		var len = target.length;
		if(offset < 0 || len < offset) return target;		
		return target.substring(0,offset)+source+target.substring(offset);
	}
});