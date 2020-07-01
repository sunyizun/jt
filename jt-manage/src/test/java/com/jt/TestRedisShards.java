package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
 * 将多台redis当做一台yong
 * @author sunyizun
 *
 */
public class TestRedisShards {
	
	@Test
	public void testShards() {
		List<JedisShardInfo> shards =new ArrayList<JedisShardInfo>();
		JedisShardInfo info1 =new JedisShardInfo("192.168.6.129",6379);
		JedisShardInfo info2 =new JedisShardInfo("192.168.6.129",6380);
		JedisShardInfo info3 =new JedisShardInfo("192.168.6.129",6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		//操作分片的redis对象工具类
		ShardedJedis shardesJedis=new ShardedJedis(shards);
		shardesJedis.set("1902", "真棒");
		System.out.println(shardesJedis.get("1902"));
		
	}
}
