package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserTheadLocal;

import redis.clients.jedis.JedisCluster;
@Component
public class UserInterceptor implements HandlerInterceptor {
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 不登录不能操作购物车用拦截器来做
	 * 1.获取cookie数据
	 * 2.获取token(TICKET)
	 * 3判断redis缓存服务器中是否有数据
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获取cookie数据
		String token =null;
		Cookie[] cookies =request.getCookies();
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token=cookie.getValue();
				break;
			}
		}
		//2判断token是否为空
		if(!StringUtils.isEmpty(token)) {
			//3.判断redis是否有数据
			String userJSON =jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis中有用户数据放行
				//将userJSON转化为user对象
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//request.setAttribute("JT_USER", user);
				//用户每次请求都将数据保存到session中,切记用完关闭
				//request.getSession().setAttribute("JT_USER", user);
				UserTheadLocal .set(user);
				return true;
			}
		}
		//4重定向到用户登录页面
		response.sendRedirect("/user/login.html");
		return false;//标识拦截
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//关闭session
		//request.getSession().removeAttribute("JT_USER");
		UserTheadLocal.remove();
	}
}
