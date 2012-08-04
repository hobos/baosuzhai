package com.zizibujuan.drip.server.util;

import java.io.Serializable;

/**
 * restful请求中的range，用于存储查询记录数范围的信息。格式为items=0-24
 * @author jinzw
 * @since 0.0.1
 */
public class PageInfo implements Serializable{
	private static final long serialVersionUID = 6448266504259394345L;
	private String name;//名称，默认为items
	private int start;// 开始记录数
	private int end;// 结束记录数
	private int count;//总记录数
	
	public PageInfo(String rangeText){
		String[] keyValuePair = rangeText.split("=");
		assert keyValuePair.length == 2:"range文本被拆分为两个字符串";
		this.name = keyValuePair[0];
		String[] nums = keyValuePair[1].split("-");
		assert nums.length == 2:"拆分出起始和结束两个字符串";
		this.start = Integer.valueOf(nums[0]);
		this.end = Integer.valueOf(nums[1]);
	}
	
	//items 0-1/3
	@Override
	public String toString() {
		return String.format("items %d-%d/%d", start,end,count);
	}

	public void setCount(int count) {
		if(count == 0){
			this.start = this.end = this.count = 0;
		}else{
			this.count = count;
		}
	}

	public String getName() {
		return name;
	}
	public int getStart() {
		assert count >= 0:"开始索引应大于0";
		if(count==0)return start;
		if(start>=count)return count-1;
		return start;
	}
	public int getEnd() {
		assert count >= 0:"开始索引应大于0";
		if(count==0)return end;
		if(end>=count)return count-1;
		return end;
	}

	public int getCount() {
		return count;
	}
}