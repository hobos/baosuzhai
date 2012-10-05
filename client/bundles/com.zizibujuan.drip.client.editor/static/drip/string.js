define({
	
	insertAtOffset : function(target, offset, source, removeCount){
		// summary:
		//		在给定字符串的指定偏移量处插入字符串。注意：在文本中直接使用\t表示一个制表符。
		// target: String
		//		目标字符串，会修改该字符串。
		// offset:
		//		偏移量，从0开始
		// source:
		//		需要插入到指定位置的字符串
		// removeCount:
		//		在offset指定的位置往前删除的字符个数
		// returns: String
		//		返回新的字符串
		
		var removeCount = removeCount || 0;
		
		var len = target.length;
		if(offset < 0 || len < offset) return target;
		var part1 = target.substring(0,offset-removeCount);
		var part2 = target.substring(offset)
		return part1 + source + part2;
	}
});