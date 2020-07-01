package com.jt;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.Jedis;

public class TestRedis2 {

	//测试hash/list事务控制
	@Test
	public void testHash(){
		Jedis jedis = new Jedis("192.168.6.129",6379); 
		jedis.hset("user", "id","200");
		
	}
	
	
	
	
	
	
	
	
	
}
