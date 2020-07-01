package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndecController {
	/**
	 * //实现页面跳转    采用restFul结构接收参数
	 * 1.要求参数必须使用/分割
	 * 2.参数位置必须固定
	 * 3.接受参数时必须使用{}标识参数
	 * 使用特定的注解并且名称最好一致
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
		
	}
	
	
	
}
