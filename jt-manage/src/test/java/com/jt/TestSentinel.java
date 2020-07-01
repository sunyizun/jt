package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {

	//测试哨兵get/set操作
	@Test
	public void test01() {
		Set<String> sentinels =new HashSet<String>();
		sentinels.add("192.168.6.129:26379");
		JedisSentinelPool sentinelPool =new JedisSentinelPool("mymaster",sentinels);
		Jedis jedis =sentinelPool.getResource();
		jedis.set("cc", "好好找工作");
		System.out.println(jedis.get("cc"));
		jedis.close();
 	}
	
	
	
}
