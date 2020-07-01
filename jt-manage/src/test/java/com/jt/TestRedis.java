package com.jt;

import org.junit.jupiter.api.Test;


import redis.clients.jedis.Jedis;

public class TestRedis {
	//String类型操作
	@Test
	public void testString() {
		Jedis jedis = new Jedis("192.168.6.129",6379);
		jedis.set("1902", "1902班");
		System.out.println(jedis.get("1902"));
	}
	
	
	

}
