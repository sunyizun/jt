package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	//@Autowired
	//private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	//导入dubbo的用户接口
	@Reference(timeout = 3000,check =false)
	private DubboUserService userService;

	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName) {
		return moduleName;

	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {

			userService.saveUser(user);
			System.out.println("1");
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SysResult.fail("新增用户失败");

	}
	//实现用户登录
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response) {
		try {
			//调用sso系统获取秘钥
			String token = userService.findUserByUP(user);
			//判断数据是否为空
			if(!StringUtils.isEmpty(token)) {
				//将用户信息写入cookie中.
				//cookie中的值固定为JT_TICKET
				Cookie cookie = new Cookie("JT_TICKET", token);
				cookie.setMaxAge(7*24*3600);  //设定cookie最大使用时间
				cookie.setDomain("jt.com");   //在指定的域名中设定cookie数据共享
				cookie.setPath("/");          //cookie的作用范围,从根目录开始.
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}


	/**
	 * 业务需求:实现用户登出操作
	 * url: http://www.jt.com/user/logout.html
	 * 参数: 没有参数
	 * 返回值: 重定向到系统首页.
	 * 删除用户登陆凭证:  删除redis信息, 删除cookie信息.
	 */

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		//1.首先获取cookie中的值
		Cookie[] cookies = request.getCookies();
		//2.从数组中动态获取JT_TICKET数据
		String ticket = null;
		if(cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("JT_TICKET")) {
					//3.动态获取cookie的值
					ticket = cookie.getValue();
					//4.同时删除cookie
					cookie.setMaxAge(0);
					cookie.setDomain("jt.com"); 
					cookie.setPath("/");    
					response.addCookie(cookie);
					break;
				}
			}

		}

		//5.判断redis中是否存在该数据,如果存在直接删除
		if(jedisCluster.exists(ticket)) {
			jedisCluster.del(ticket);
		}

		return "redirect:/";
	}

}
