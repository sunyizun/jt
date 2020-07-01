package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//表示redis配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	@Value("${redis.nodes}")
	private String redisNodes;
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		String[] nodeArray = redisNodes.split(",");
        for (String node : nodeArray) { //node=host:port
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort = new HostAndPort(host, port);
            nodes.add(hostAndPort);
        }
		return new JedisCluster(nodes);
	}
	/**
	 * 
	@Value("${redis.sentinels}")
	private String jedisSentinelNodes;
	@Value("${redis.sentinel.masterName}")
	private String masterName;
	@Bean
	public JedisSentinelPool jedisSentinelPool() {
		Set<String> sentinels =new HashSet<String>();
		
		sentinels.add(jedisSentinelNodes);
		return new JedisSentinelPool(masterName,sentinels);
		
	}
	*/
	/**
	 * 
	 * @Value("${redis.nodes}")
	private String redisNodes;
	@Bean
	public ShardedJedis shardedJedis() {
		List<JedisShardInfo> shards =new ArrayList<JedisShardInfo>();
		String [] nodes =redisNodes.split(",");
		for (String node : nodes) {
			String host = node.split(":")[0];
			int port=Integer.parseInt(node.split(":")[1]);
			JedisShardInfo info=new JedisShardInfo(host,port);
			shards.add(info);
		}
		
		return new ShardedJedis(shards);
		
	}
	
	 */
	
/**
 * 
 
	@Value("${jedis.host}")
	private String host;
	@Value("${jedis.port}")
	private Integer port;
	@Bean
	public Jedis jedis() {
		
		return new Jedis (host,port);
		
	}
	*/
}
