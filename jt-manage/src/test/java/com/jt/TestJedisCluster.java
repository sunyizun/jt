package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestJedisCluster {
	@Test
	public void test01() {
		Set<HostAndPort> sets = new HashSet<>();
		sets.add(new HostAndPort("192.168.6.129",7000));
		sets.add(new HostAndPort("192.168.6.129",7001));
		sets.add(new HostAndPort("192.168.6.129",7002));
		sets.add(new HostAndPort("192.168.6.129",7003));
		sets.add(new HostAndPort("192.168.6.129",7004));
		sets.add(new HostAndPort("192.168.6.129",7005));
		JedisCluster cluster = new JedisCluster(sets);
		cluster.set("1902", "集群完成");
		System.out.println(cluster.get("1902"));
	}
	
	

}
