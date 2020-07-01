package com.jt.util;

import com.jt.pojo.User;

//TheadLocal是线程安全的
public class UserTheadLocal {

	private static ThreadLocal<User> thread =new ThreadLocal<>();
	//新增数据
	public static void set(User user) {
		thread.set(user);
	}
	//获取数据
	public static User get() {
		return thread.get();
	}
	//使用threadLocal切记内存泄漏
	public static void remove () {
		thread.remove();
	}
}
