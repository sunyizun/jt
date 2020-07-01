package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

//编辑工具类,实现对象与json的转换
public class ObjectMapperUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static String toJSON(Object target) {
		String json =null;
		try {
			json=MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return json;
		
	}
	public static <T> T toObject(String json,Class<T> targetClass) {
		 T target=null;
		try {
			target=MAPPER.readValue(json, targetClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return target;
		
	}
	
	
}
