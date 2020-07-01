package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.jt.pojo.Item;
import com.jt.pojo.User;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private HttpClientService httpClient;

	@Override
	public void saveUser(User user) {
		String url = "http://sso.jt.com/user/doRegister";
		String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		String userJSON = ObjectMapperUtil.toJSON(user);
		Map<String,String> params =new HashMap<>();
		params.put("userJSON", userJSON);
		
		//前台通过httpClient将数据进行远程传输.如果程序在后台执行错误!!
		String result =httpClient.doPost(url,params);
		System.out.println("2");
		try {
			
			//检测返回值结果是否正确
			SysResult sysResult = ObjectMapperUtil.toObject(result, SysResult.class);
			if(sysResult.getStatus() != 200){
				//表示程序有错
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

}
