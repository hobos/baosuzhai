define({
	
	splitData: function(data){
		// summary:
		//		将传入的数据拆分为最小单元的html符号。
		//		dataArray的每个元素都只能看作一个字符。
		
		var len = data.length;
		var result = [];
		var index = 0;
		var append = false;
		var cache = "";
		var span = 0; //&和;中字符的个数
		for(var i = 0; i < len; i++){
			var char = data.charAt(i);
			if(char == "&"){
				span = 0;
				append = true;
				cache = char;
			}else if(append && char == ";"){
				if(span == 0){
					result[index] = cache;
					index++;
					result[index] = char;
					index++;
				}else{
					cache += char;
					result[index] = cache;
					index++;
				}
				append = false;
				cache = "";
			}else{
				if(append){
					cache += char;
					span++;
				}else{
					result[index] = data.charAt(i);
					index++;
				}
			}
		}
		return result;
	},
	
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