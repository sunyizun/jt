package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;
//该类是dubbo的实现类
@Service(timeout=3000)
public class DubboUserServiceImpl implements DubboUserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster  jedisCluster;
	
	@Transactional
	@Override
	public void saveUser(User user) {
		//密码加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		 user.setEmail(user.getPhone()).setPassword(md5Pass).setCreated(new Date()).setUpdated(user.getCreated());
		 userMapper.insert(user);
		 
	}
	/**
	 * 1.校验用户名和密码
	 * 
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		//1.需要将密码进行加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
        User userDB = userMapper.selectOne(queryWrapper);
        if(userDB != null) {
        	token="JT_TICKET"+userDB.getUsername()+System.currentTimeMillis();
        	token=DigestUtils.md5DigestAsHex(token.getBytes());
        	//脱敏处理
        	userDB .setPassword("你猜猜");
        	String userJSON = ObjectMapperUtil.toJSON(userDB);
        	jedisCluster.setex(token, 7*24*60*60, userJSON);
        }
		
		return token;
	}
	
	
}
