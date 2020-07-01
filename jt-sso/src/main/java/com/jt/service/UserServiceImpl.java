package com.jt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 需要根据参数,查询数据库.
	 * type:1 username、2 phone、3 email
	 * 返回值:      true用户已存在，false用户不存在，
	 */
	@Override
	public Boolean checkUser(String param, Integer type) {
		//String column = type==1?"username":(type==2?"phone":"email");
		Map<Integer,String> map = new HashMap<Integer, String>();
		map.put(1, "username");
		map.put(2, "phone");
		map.put(3, "email");
		
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq(map.get(type), param);
		int count = userMapper.selectCount(queryWrapper);
		//返回值:true用户已存在，false用户不存在
		return count==0?false:true;
	}
	@Transactional
	@Override
	public void saveUser(User user) {
		user.setEmail(user.getPhone()).setCreated(user.getCreated()).setUpdated(new Date());
		System.out.println("3");
		int row=userMapper.insert(user);
		System.out.println("row="+row);
	}
	
}
