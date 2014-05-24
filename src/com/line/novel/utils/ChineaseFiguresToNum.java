package com.line.novel.utils;

import java.util.HashMap;
import java.util.Map;

public class ChineaseFiguresToNum {
	
	private static Map<Character,Long> table;
	
	static{
		table = new HashMap<Character,Long>();
		table.put('零',0l);
		table.put('一',1l);
		table.put('二',2l);
		table.put('三',3l);
		table.put('四',4l);
		table.put('五',5l);
		table.put('六',6l);
		table.put('七',7l);
		table.put('八',8l);
		table.put('九',9l);
		table.put('十',10l);
		table.put('百',100l);
		table.put('千',1000l);
		table.put('万',10000l);
		table.put('亿',100000000l);
	}
	
	public static long coverToNum(String str){
		
		if(!isChineaseNum(str)){
			return Long.parseLong(str);
		}
		
		char[] num = str.toCharArray();
		long result = 0;
		long maxUnit = 0;
		int i = num.length-1;
		if(table.get(num[i]) < 10){
			result = table.get(num[i--]);
		}
			
		while(i>0){
			
			if("零".equals(String.valueOf(num[i]))){
				i--;
				continue;
			}
			long unit = table.get(num[i--]);
			long value = table.get(num[i--]);
			
			if(maxUnit > unit){
				result += value * unit * maxUnit;
			}else{
				result += value * unit;
				maxUnit = unit;
			}
		}
		
//		System.out.println("数字 " + str + " ---> " + result);
		return result;
	}
	
	private static boolean isChineaseNum(String num){
		try{
			Long.parseLong(num);
			return false;
		}catch(NumberFormatException e){
			return true;
		}
	}
	
}
